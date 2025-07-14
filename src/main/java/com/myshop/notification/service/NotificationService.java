package com.myshop.notification.service;

import com.myshop.entity.Member;
import com.myshop.notification.domain.constant.NotificationType;
import com.myshop.notification.domain.message.NotificationMessage;
import com.myshop.notification.repository.NotificationAgreementRepository;
import com.myshop.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    public void notify(String email, NotificationMessage message) {

        Member member = memberRepository.findByEmail(email);
        List<NotificationType> agreedNotificationTypes = notificationAgreementRepository.findAgreedNotificationTypesByMemberId(member.getId());

        log.info("Agreed notification types: {}", agreedNotificationTypes);
        for (NotificationType notificationType : agreedNotificationTypes) {
            notificationSenders.stream()
                    .filter(sender -> sender.support(notificationType))
                    .forEach(sender -> sender.sendNotification(message));
        }
    }
}
