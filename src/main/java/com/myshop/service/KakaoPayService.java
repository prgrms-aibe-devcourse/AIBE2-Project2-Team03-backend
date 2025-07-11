package com.myshop.service;

import com.myshop.dto.KakaoPayReadyDto;
import com.myshop.properties.KakaoPayProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class KakaoPayService {
  
  private final RestTemplate restTemplate;
  private final KakaoPayProperties kakaoPayProperties;
  
  private KakaoPayReadyDto kakaoPayReady;
  
  private HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, kakaoPayProperties.getSecretKey());
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    return headers;
  }
  
  public KakaoPayReadyDto readyPay() {
    Map<String, String> params = new HashMap<>();
    params.put("cid", kakaoPayProperties.getCId());
    params.put("partner_order_id", "ORDER_ID");
    params.put("partner_user_id", "USER_ID");
    params.put("item_name", "choco pie");
    params.put("quantity", "1");
    params.put("total_amount", "2200");
    params.put("vat_amount", "200");
    params.put("tax_free_amount", "0");
    params.put("approval_url", "http://localhost:8080/kakaopay/success");
    params.put("fail_url", "http://localhost:8080/kakaopay/fail");
    params.put("cancel_url", "http://localhost:8080/kakaopay/cancel");
    
    HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, this.getHeaders());
    
    kakaoPayReady = restTemplate.postForObject("https://open-api.kakaopay.com/online/v1/payment/ready", requestEntity, KakaoPayReadyDto.class);
    // return kakaoPayReady.getNext_redirect_pc_url();
    return kakaoPayReady;
  }
  
  
  public void acceptPay(String pgToken) {
    Map<String, String> params = new HashMap<>();
    params.put("cid", kakaoPayProperties.getCId());
    params.put("tid", kakaoPayReady.getTid());
    params.put("partner_order_id", "ORDER_ID");
    params.put("partner_user_id", "USER_ID");
    params.put("pg_token", pgToken);
    
    HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, this.getHeaders());
    
    Map result = restTemplate.postForObject("https://open-api.kakaopay.com/online/v1/payment/approve", requestEntity, Map.class);
    
    System.out.println();
    System.out.println(result);
    System.out.println();
  }
}
