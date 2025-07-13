package com.myshop.service;

import com.myshop.constant.PaymentStatus;
import com.myshop.dto.PaymentApprovalDto;
import com.myshop.dto.PaymentDto;
import com.myshop.dto.PaymentReadyDto;
import com.myshop.entity.Item;
import com.myshop.entity.Member;
import com.myshop.entity.Payment;
import com.myshop.repository.ItemRepository;
import com.myshop.repository.MemberRepository;
import com.myshop.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    
    @Value("${payment.kakao.cid}")
    private String cid;
    
    @Value("${payment.kakao.secret-key}")
    private String secretKey;
    
    @Value("${payment.kakao.ready-url}")
    private String readyUrl;
    
    @Value("${payment.kakao.approve-url}")
    private String approveUrl;
    
    @Value("${server.domain}")
    private String domain;
    
    public PaymentReadyDto preparePayment(PaymentDto paymentDto, String email, HttpSession session) {
        Item item = itemRepository.findById(paymentDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        
        Member member = memberRepository.findByEmail(email);
        
        String partnerOrderId = UUID.randomUUID().toString();
        String partnerUserId = member.getId().toString();
        
        // 결제 준비 요청 데이터
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("cid", cid);
        requestData.put("partner_order_id", partnerOrderId);
        requestData.put("partner_user_id", partnerUserId);
        requestData.put("item_name", item.getItemName());
        requestData.put("quantity", paymentDto.getCount());
        requestData.put("total_amount", item.getPrice() * paymentDto.getCount());
        requestData.put("tax_free_amount", 0);
        requestData.put("approval_url", domain + "/payment/approval");
        requestData.put("cancel_url", domain + "/payment/cancel");
        requestData.put("fail_url", domain + "/payment/fail");
        
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + secretKey);
        headers.set("Content-Type", "application/json");
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestData, headers);
        
        // 카카오페이 결제 준비 API 호출
        ResponseEntity<PaymentReadyDto> response = restTemplate.exchange(
            readyUrl, HttpMethod.POST, entity, PaymentReadyDto.class);
        
        PaymentReadyDto readyDto = response.getBody();
        
        // Payment 엔티티 저장
        Payment payment = new Payment();
        payment.setTid(readyDto.getTid());
        payment.setItemId(paymentDto.getItemId()); // 추가
        payment.setPartnerOrderId(partnerOrderId);
        payment.setPartnerUserId(partnerUserId);
        payment.setItemName(item.getItemName());
        payment.setQuantity(paymentDto.getCount());
        payment.setTotalAmount(item.getPrice() * paymentDto.getCount());
        payment.setTaxFreeAmount(0);
        payment.setPaymentStatus(PaymentStatus.READY);
        
        paymentRepository.save(payment);
        
        // ⭐ 세션에 tid 저장 (중요!)
        session.setAttribute("paymentTid", readyDto.getTid());
        
        return readyDto;
    }
    
    public PaymentApprovalDto approvePayment(String pgToken, String tid) {
        Payment payment = paymentRepository.findByTid(tid)
                .orElseThrow(() -> new EntityNotFoundException("결제 정보를 찾을 수 없습니다."));
        
        // 결제 승인 요청 데이터
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("cid", cid);
        requestData.put("tid", tid);
        requestData.put("partner_order_id", payment.getPartnerOrderId());
        requestData.put("partner_user_id", payment.getPartnerUserId());
        requestData.put("pg_token", pgToken);
        
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + secretKey);
        headers.set("Content-Type", "application/json");
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestData, headers);
        
        // 카카오페이 결제 승인 API 호출
        ResponseEntity<PaymentApprovalDto> response = restTemplate.exchange(
            approveUrl, HttpMethod.POST, entity, PaymentApprovalDto.class);
        
        PaymentApprovalDto approvalDto = response.getBody();
        
        // Payment 상태 업데이트
        payment.setAid(approvalDto.getAid());
        payment.setPaymentStatus(PaymentStatus.APPROVED);
        payment.setApprovedAt(LocalDateTime.now());
        
        return approvalDto;
    }
    
    public Payment getPaymentByTid(String tid) {
        return paymentRepository.findByTid(tid)
                .orElseThrow(() -> new EntityNotFoundException("결제 정보를 찾을 수 없습니다."));
    }
    
    public void updatePaymentStatus(String tid, PaymentStatus status) {
        Payment payment = getPaymentByTid(tid);
        payment.setPaymentStatus(status);
    }
}