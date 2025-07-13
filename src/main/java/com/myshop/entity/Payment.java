package com.myshop.entity;

import com.myshop.constant.PaymentStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_payment")
@Getter
@Setter
@ToString
public class Payment extends BaseEntity {
    
    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false)
    private String tid; // 카카오페이 결제 고유번호

    @Column(nullable = false)
    private Long itemId; // 상품 ID 추가
    
    @Column(nullable = false)
    private String partnerOrderId; // 가맹점 주문번호
    
    @Column(nullable = false)
    private String partnerUserId; // 가맹점 회원 ID
    
    @Column(nullable = false)
    private String itemName; // 상품명
    
    @Column(nullable = false)
    private Integer quantity; // 상품 수량
    
    @Column(nullable = false)
    private Integer totalAmount; // 결제 금액
    
    @Column(nullable = false)
    private Integer taxFreeAmount; // 비과세 금액
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // 결제 상태
    
    private String aid; // 결제 승인 고유번호
    
    private LocalDateTime approvedAt; // 결제 승인 시간
    
    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private Order order; // 연결된 주문
}