package com.myshop.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CartItemDto {

    @NotNull(message = "상품 아이디는 필수입니다.")
    private Long itemId; // 상품 아이디
    @Min(value = 1, message = "최소 1개 이상 담아주세요.")
    private int count; // 장바구니에 담긴 상품 수량
}
