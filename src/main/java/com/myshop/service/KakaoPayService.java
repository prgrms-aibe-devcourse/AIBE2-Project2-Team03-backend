package com.myshop.service;

import com.myshop.dto.KakaoPayReadyDto;
import com.myshop.entity.Order;
import com.myshop.entity.OrderItem;
import com.myshop.properties.KakaoPayProperties;
import com.myshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class KakaoPayService {
  
  private final RestTemplate restTemplate;
  private final KakaoPayProperties kakaoPayProperties;
  private final OrderRepository orderRepository;
  
  private KakaoPayReadyDto kakaoPayReady;
  private Order order;
  private String email;
  
  private HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, kakaoPayProperties.getSecretKey());
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    return headers;
  }
  
  public String readyPay(Long orderId, String email) {
    this.order = orderRepository.findById(orderId)
            .orElseThrow(EntityNotFoundException::new);
    this.email = email;
    
    int size = this.order.getOrderItems().size();
    OrderItem firstItem = this.order.getOrderItems().get(0);
    
    
    Map<String, String> params = new HashMap<>();
    params.put("cid", kakaoPayProperties.getCId());
    params.put("partner_order_id", orderId.toString());
    params.put("partner_user_id", email);
    params.put("item_name", size > 1 ?
            firstItem.getItem().getItemName() + " 외 " + (size - 1) +  " 개"
            : firstItem.getItem().getItemName());
    params.put("quantity", String.valueOf(firstItem.getCount()));
    params.put("total_amount", String.valueOf(this.order.getTotalPrice()));
    params.put("vat_amount", "200");
    params.put("tax_free_amount", "0");
    params.put("approval_url", "http://localhost:8080/kakaopay/success");
    params.put("fail_url", "http://localhost:8080/kakaopay/fail");
    params.put("cancel_url", "http://localhost:8080/kakaopay/cancel");
    
    HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, this.getHeaders());
    
    kakaoPayReady = restTemplate.postForObject("https://open-api.kakaopay.com/online/v1/payment/ready", requestEntity, KakaoPayReadyDto.class);
     return kakaoPayReady.getNext_redirect_pc_url();
  }
  
  
  public void acceptPay(String pgToken) {
    Map<String, String> params = new HashMap<>();
    params.put("cid", kakaoPayProperties.getCId());
    params.put("tid", kakaoPayReady.getTid());
    params.put("partner_order_id", order.getId().toString());
    params.put("partner_user_id", email);
    params.put("pg_token", pgToken);
    
    HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, this.getHeaders());
    
    Map result = restTemplate.postForObject("https://open-api.kakaopay.com/online/v1/payment/approve", requestEntity, Map.class);
    
    System.out.println();
    System.out.println(result);
    System.out.println();
  }
}
