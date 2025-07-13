package com.myshop.repository;

import com.myshop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTid(String tid);
    Optional<Payment> findByPartnerOrderId(String partnerOrderId);
}