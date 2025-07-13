package com.myshop.notification.service;

import com.myshop.notification.domain.constant.NotificationType;
import com.myshop.notification.domain.message.NotificationMessage;
import com.myshop.notification.dto.SlackMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SlackNotificationSender implements NotificationSender {

    private final SlackClient slackClient;

    @Override
    public boolean support(NotificationType type) {
        return type == NotificationType.SLACK;
    }

    @Override
    public void sendNotification(NotificationMessage message) {
        SlackMessageDto slackMessage = SlackMessageDto.of(message.getFormattedContent());
        slackClient.sendSlackMessage(slackMessage);
    }
}
