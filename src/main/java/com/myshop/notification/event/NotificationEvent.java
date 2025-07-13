package com.myshop.notification.event;

import com.myshop.notification.domain.message.NotificationMessage;
import lombok.Getter;

@Getter
public class NotificationEvent {

    private final Long memberId;
    private final NotificationMessage message;

    public NotificationEvent(Long memberId, NotificationMessage message) {
        this.memberId = memberId;
        this.message = message;
    }
}
