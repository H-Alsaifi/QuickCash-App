package com.example.g2_qc.paypal_integration;

public class Payment {
    private String userId, postUserId;
    private String paymentId;
    private String state;
    private double amount;

    public Payment() {}

    public Payment(String userId, String paymentId, String state, double amount, String postUserId) {
        this.userId = userId;
        this.paymentId = paymentId;
        this.state = state;
        this.amount = amount;
        this.postUserId = postUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(String userId) {
        this.postUserId = postUserId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
