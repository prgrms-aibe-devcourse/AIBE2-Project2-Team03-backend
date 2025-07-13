package com.myshop.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentReadyDto {
    private String tid;
    private String nextRedirectAppUrl;
    private String nextRedirectMobileUrl;
    private String nextRedirectPcUrl;
    private String androidAppScheme;
    private String iosAppScheme;
    private String createdAt;
}