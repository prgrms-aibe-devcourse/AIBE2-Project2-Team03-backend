package com.myshop.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:application-pay.properties")
@Component
@ConfigurationProperties(prefix = "kakaopay")
@Getter @Setter
public class KakaoPayProperties {
  
  private String cId;
  private String secretKey;
}
