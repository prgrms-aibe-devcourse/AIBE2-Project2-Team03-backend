package com.myshop.notification.domain.message;

import lombok.Getter;

@Getter
public abstract class NotificationMessage {

    public abstract String getFormattedContent();
}
