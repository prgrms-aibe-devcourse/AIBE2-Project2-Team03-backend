package com.myshop.notification.service;

import com.myshop.notification.dto.SlackMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "slackClient", url = "${slack.webhook.url}")
public class SlackClient {

    @PostMapping
    void sendSlackMessage(@RequestBody SlackMessageDto slackMessageDto) {}
}
