package com.myshop.notification.service;

import com.myshop.notification.domain.constant.NotificationType;
import com.myshop.notification.domain.message.NotificationMessage;

public interface NotificationSender {
    boolean support(NotificationType type);
    void sendNotification(NotificationMessage message);
}
