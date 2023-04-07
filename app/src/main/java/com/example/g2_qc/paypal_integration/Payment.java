package com.example.g2_qc.paypal_integration;

public class Payment {
    private String userId;
    private String paymentId;
    private String state;
    private double amount;

    public Payment() {}

    public Payment(String userId, String paymentId, String state, double amount) {
        this.userId = userId;
        this.paymentId = paymentId;
        this.state = state;
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
