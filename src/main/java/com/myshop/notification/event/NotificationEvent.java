package com.myshop.notification.event;

import com.myshop.notification.domain.message.NotificationMessage;
import lombok.Getter;

@Getter
public class NotificationEvent {

    private final String email;
    private final NotificationMessage message;

    public NotificationEvent(String email, NotificationMessage message) {
        this.email = email;
        this.message = message;
    }
}
