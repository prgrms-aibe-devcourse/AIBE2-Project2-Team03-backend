package com.myshop.notification.repository;

import com.myshop.notification.domain.constant.NotificationType;
import com.myshop.notification.domain.entity.NotificationAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationAgreementRepository extends JpaRepository<NotificationAgreement, Long> {

    @Query("SELECT a.type FROM NotificationAgreement a WHERE a.memberId = :memberId AND a.agreement = true")
    List<NotificationType> findAgreedNotificationTypesByMemberId(@Param("memberId") Long memberId);
}
