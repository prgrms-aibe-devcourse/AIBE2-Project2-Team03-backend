package com.myshop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="t_orderitem")
@Getter
@Setter
@ToString
public class OrderItem {

    @Id
    @Column(name="order_item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order; // 주문 정보
    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item; // 주문한 상품

    private int orderPrice; // 주문한 상품 가격
    private int count; // 주문한 상품 수량

    private LocalDateTime regTime; // 주문 아이템 등록 시간
    private LocalDateTime updateTime; // 주문 아이템 수정 시간
}
