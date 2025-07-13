package com.myshop.controller;

import com.myshop.constant.PaymentStatus;
import com.myshop.dto.OrderDto;
import com.myshop.dto.OrderHistoryDto;
import com.myshop.dto.PaymentApprovalDto;
import com.myshop.dto.PaymentDto;
import com.myshop.dto.PaymentReadyDto;
import com.myshop.entity.Payment;
import com.myshop.service.OrderService;
import com.myshop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    @PostMapping("/order")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto,
                                              BindingResult bindingResult,
                                              Principal principal) {

        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                errors.append(fieldError.getDefaultMessage()).append(" ");
            }

            return new ResponseEntity<String>(errors.toString(),HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long orderId;

        try {
            orderId = orderService.order(orderDto, email);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHistory(@PathVariable("page") Optional<Integer>page, Principal principal, Model model) {

        Pageable pageable = PageRequest.of(page.orElse(0), 4);
        Page<OrderHistoryDto> orderHistoryDtoList = orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", orderHistoryDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHistory";
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {

        if (!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        orderService.cancelOrder(orderId);

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    // 결제 관련 코드
    @PostMapping("/order/payment/ready")
    public @ResponseBody ResponseEntity paymentReady(@RequestBody @Valid PaymentDto paymentDto,
                                                     BindingResult bindingResult,
                                                     Principal principal,
                                                     HttpSession session) {
        
        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            
            for (FieldError fieldError : fieldErrors) {
                errors.append(fieldError.getDefaultMessage()).append(" ");
            }
            
            return new ResponseEntity<String>(errors.toString(), HttpStatus.BAD_REQUEST);
        }
        
        try {
            PaymentReadyDto readyDto = paymentService.preparePayment(paymentDto, principal.getName(), session);
            return new ResponseEntity<PaymentReadyDto>(readyDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("결제 준비 실패: ", e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/order/payment/approval")
    public String paymentApproval(@RequestParam("pg_token") String pgToken,
                                  HttpSession session,
                                  Model model,
                                  Principal principal) {
        
        try {
            // 세션에서 tid 가져오기
            String tid = (String) session.getAttribute("paymentTid");
            if (tid == null) {
                throw new RuntimeException("결제 정보를 찾을 수 없습니다.");
            }
            
            // 결제 승인
            PaymentApprovalDto approvalDto = paymentService.approvePayment(pgToken, tid);
            
            // 결제 정보 조회
            Payment payment = paymentService.getPaymentByTid(tid);
            
            // 주문 생성
            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(payment.getItemId());
            orderDto.setCount(payment.getQuantity());
            
            Long orderId = orderService.order(orderDto, principal.getName());
            
            // 세션 정리
            session.removeAttribute("paymentTid");
            
            model.addAttribute("payment", approvalDto);
            model.addAttribute("orderId", orderId);
            
            return "payment/success";
            
        } catch (Exception e) {
            log.error("결제 승인 실패: ", e);
            model.addAttribute("errorMessage", e.getMessage());
            return "payment/fail";
        }
    }
    
    @GetMapping("/order/payment/cancel")
    public String paymentCancel(HttpSession session, Model model) {
        String tid = (String) session.getAttribute("paymentTid");
        if (tid != null) {
            paymentService.updatePaymentStatus(tid, PaymentStatus.CANCELLED);
            session.removeAttribute("paymentTid");
        }
        model.addAttribute("message", "결제가 취소되었습니다.");
        return "payment/cancel";
    }
    
    @GetMapping("/order/payment/fail")
    public String paymentFail(HttpSession session, Model model) {
        String tid = (String) session.getAttribute("paymentTid");
        if (tid != null) {
            paymentService.updatePaymentStatus(tid, PaymentStatus.FAILED);
            session.removeAttribute("paymentTid");
        }
        model.addAttribute("message", "결제가 실패했습니다.");
        return "payment/fail";
    }
}
