package com.myshop.notification.service;

import com.myshop.notification.domain.constant.NotificationType;
import com.myshop.notification.domain.message.NotificationMessage;
import com.myshop.notification.dto.SlackMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackNotificationSender implements NotificationSender {

    private final SlackClient slackClient;

    @Override
    public boolean support(NotificationType type) {
        log.info("Checking support for notification type: {}", type);
        return type == NotificationType.SLACK;
    }

    @Override
    public void sendNotification(NotificationMessage message) {
        SlackMessageDto slackMessage = SlackMessageDto.of(message.getFormattedContent());
        slackClient.sendSlackMessage(slackMessage);
        log.info("Sent Slack notification: {}", slackMessage);
    }
}
