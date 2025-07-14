package com.myshop.notification.event;

import com.myshop.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final NotificationService notificationService;

    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleNotificationEvent(NotificationEvent event) {
        log.info("Received notification event: {}", event);
        notificationService.notify(event.getEmail(), event.getMessage());
    }
}
