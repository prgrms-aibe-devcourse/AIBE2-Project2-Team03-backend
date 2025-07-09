package com.myshop.dto;

import com.myshop.constant.ItemSellStatus;
import com.myshop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private Long id; // 상품 코드

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemName; // 상품명
    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price; // 가격
    @NotBlank(message = "상품 상세 설명은 필수 입력 값입니다.")
    private String itemDetail; // 상품 상세 설명
    @NotNull(message = "재고 수량은 필수 입력 값입니다.")
    private Integer stockNumber; // 재고 수량

    private ItemSellStatus itemSellStatus; // 상품 판매 상태
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
    private List<Long> itemImgIds = new ArrayList<>(); // 이미지 아이디 목록

    private static ModelMapper mapper = new ModelMapper();

    public static ItemFormDto of(Item item) {
        return mapper.map(item, ItemFormDto.class);
    }
    public Item createItem() {
        return mapper.map(this, Item.class);
    }
}
