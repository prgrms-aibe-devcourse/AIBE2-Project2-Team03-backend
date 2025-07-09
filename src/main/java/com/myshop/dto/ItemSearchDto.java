package com.myshop.dto;

import com.myshop.contant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

    private String searchDateType;
    private ItemSellStatus itemSellStatus;
    private String searchBy;
    private String searchQuery;
}
