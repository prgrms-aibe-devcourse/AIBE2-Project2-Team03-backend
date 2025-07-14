package com.myshop.dto;

import lombok.Data;

@Data
public class KakaoPayApproveDto {
  private String aid;
  private String tid;
  private String partner_order_id;
  private String partner_user_id;
  private String item_name;
  private int quantity;
  private Amount amount;
  
  @Data
  private class Amount {
    private int total;
    private int tax_free;
    private int vat;
  }
  
}
