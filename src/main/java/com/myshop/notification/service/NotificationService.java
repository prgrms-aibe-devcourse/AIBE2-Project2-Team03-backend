package com.myshop.notification.service;

import com.myshop.notification.domain.constant.NotificationType;
import com.myshop.notification.domain.message.NotificationMessage;
import com.myshop.notification.repository.NotificationAgreementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationAgreementRepository notificationAgreementRepository;

    private final List<NotificationSender> notificationSenders;

    public void notify(Long memberId, NotificationMessage message) {

        List<NotificationType> agreedNotificationTypes = notificationAgreementRepository.findAgreedNotificationTypesByMemberId(memberId);

        log.info("Agreed notification types: {}", agreedNotificationTypes);
        for (NotificationType notificationType : agreedNotificationTypes) {
            notificationSenders.stream()
                    .filter(sender -> sender.support(notificationType))
                    .forEach(sender -> sender.sendNotification(message));
        }
    }
}
