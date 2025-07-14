package com.myshop.notification.domain.message;

public class PaymentSuccessMessage extends NotificationMessage {

    private final String username;
    private final int amount;
    private final String orderNumber;
    private final String formattedContent;

    public PaymentSuccessMessage(String username, int amount, String orderNumber) {
        this.username = username;
        this.amount = amount;
        this.orderNumber = orderNumber;
        this.formattedContent = buildDefaultMessage();
    }

    private String buildDefaultMessage() {
        return String.format("[✔ 결제 완료]  %s님이 %d원 결제 완료 (주문번호: %s)", username, amount, orderNumber);
    }

    @Override
    public String getFormattedContent() {
        return formattedContent;
    }
}
