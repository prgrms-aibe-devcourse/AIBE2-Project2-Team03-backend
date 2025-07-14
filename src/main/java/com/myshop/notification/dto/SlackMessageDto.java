package com.myshop.notification.dto;

import lombok.Getter;

@Getter
public class SlackMessageDto {
    private String text;

    public SlackMessageDto(String text) {
        this.text = text;
    }

    public static SlackMessageDto of(String content) {
        return new SlackMessageDto(content);
    }
}
