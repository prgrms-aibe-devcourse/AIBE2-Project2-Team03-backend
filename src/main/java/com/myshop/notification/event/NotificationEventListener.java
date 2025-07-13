package com.myshop.notification.event;

import com.myshop.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;

    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleNotificationEvent(NotificationEvent event) {
        notificationService.notify(event.getMemberId(), event.getMessage());
    }
}
