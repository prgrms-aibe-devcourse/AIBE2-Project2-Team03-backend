package com.myshop.entity;

import com.myshop.constant.ItemSellStatus;
import javax.persistence.*;

import com.myshop.dto.ItemFormDto;
import com.myshop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="t_item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;            //상품코드

    @Column(nullable = false, length = 50)
    private String itemName;    //상품명
    @Column(name="price", nullable = false)
    private int price;          //가격
    @Column(nullable = false)
    private int stockNumber;    //재고수량
    @Lob
    @Column(nullable = false)
    private String itemDetail;  //상품상세설명
    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;  //상품판매상태

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemName = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber;
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }

    public void addStock(int stockNumber) {
        this.stockNumber += stockNumber;
    }
}