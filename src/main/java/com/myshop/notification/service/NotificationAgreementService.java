package com.myshop.notification.service;

import com.myshop.notification.domain.constant.NotificationType;
import com.myshop.notification.domain.entity.NotificationAgreement;
import com.myshop.notification.repository.NotificationAgreementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationAgreementService {

    private final NotificationAgreementRepository notificationAgreementRepository;

    public void createNotificationAgreement(Long memberId) {
        List<NotificationAgreement> defaultAgreements = List.of(
                NotificationAgreement.createNotificationAgreement(memberId, NotificationType.SLACK, true)
        );

        notificationAgreementRepository.saveAll(defaultAgreements);
    }
}
