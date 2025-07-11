package com.myshop.controller;

import com.myshop.dto.KakaoPayReadyDto;
import com.myshop.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class KakaoPayController {
  private final KakaoPayService kakaoPayService;
  
  @GetMapping(value = "/kakaopay/success")
  public String acceptPay(@RequestParam("pg_token") String pgToken) {
    kakaoPayService.acceptPay(pgToken);
    return "redirect:/";
  }
}
