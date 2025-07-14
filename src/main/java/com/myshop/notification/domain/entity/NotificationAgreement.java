package com.myshop.notification.domain.entity;

import com.myshop.entity.BaseEntity;
import com.myshop.notification.domain.constant.NotificationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="t_notificationagreement",
        indexes = {
                @Index(name = "idx_notification_member_id", columnList = "memberId")
        })
@Getter
@Setter
@ToString
public class NotificationAgreement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private boolean agreement;

    public static NotificationAgreement createNotificationAgreement(Long memberId, NotificationType type, boolean agreement) {
        NotificationAgreement notificationAgreement = new NotificationAgreement();
        notificationAgreement.setMemberId(memberId);
        notificationAgreement.setType(type);
        notificationAgreement.setAgreement(agreement);
        return notificationAgreement;
    }
}
