package com.myshop.controller;

import com.myshop.dto.KakaoPayApproveDto;
import com.myshop.dto.KakaoPayReadyDto;
import com.myshop.notification.domain.message.PaymentSuccessMessage;
import com.myshop.notification.event.NotificationEvent;
import com.myshop.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class KakaoPayController {
  private final KakaoPayService kakaoPayService;
  private final ApplicationEventPublisher applicationEventPublisher;
  
  @GetMapping(value = "/kakaopay/success")
  public String acceptPay(@RequestParam("pg_token") String pgToken) {
    KakaoPayApproveDto result = kakaoPayService.acceptPay(pgToken);
    NotificationEvent notificationEvent = new NotificationEvent(
        result.getPartner_user_id(),
            new PaymentSuccessMessage(
                    result.getPartner_user_id(),
                    result.getTotalAmount(),
                    result.getPartner_order_id()
            )
    );
    applicationEventPublisher.publishEvent(notificationEvent);
    return "redirect:/";
  }
}
