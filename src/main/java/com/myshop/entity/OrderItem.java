package com.myshop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="t_orderitem")
@Getter
@Setter
@ToString
public class OrderItem extends BaseEntity {

    @Id
    @Column(name="order_item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order; // 주문 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item; // 주문한 상품

    private int orderPrice; // 주문한 상품 가격
    private int count; // 주문한 상품 수량

    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());
        item.removeStock(count); // 주문 수량만큼 재고 감소
        return orderItem;
    }

    public int getTotalPrice() {
        return this.orderPrice * this.count; // 총 주문 가격 계산
    }

    public void cancel() {
        this.getItem().addStock(this.count); // 주문 취소 시 재고 수량 증가
    }
}
