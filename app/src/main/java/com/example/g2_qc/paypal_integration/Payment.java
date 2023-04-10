package com.example.g2_qc.paypal_integration;

/**
 * This class represents a payment made through PayPal integration.
 */
public class Payment {
    private String userId, postUserId;
    private String paymentId;
    private String state;
    private double amount;

    /**
     * Default constructor for Payment class.
     */
    public Payment() {}

    /**
     * Constructor for Payment class.
     * @param userId The ID of the user making the payment.
     * @param paymentId The ID of the payment.
     * @param state The state of the payment (e.g. "approved", "completed").
     * @param amount The amount of the payment.
     * @param postUserId The ID of the user who posted the job.
     */
    public Payment(String userId, String paymentId, String state, double amount, String postUserId) {
        this.userId = userId;
        this.paymentId = paymentId;
        this.state = state;
        this.amount = amount;
        this.postUserId = postUserId;
    }

    /**
     * Get the user ID of the payment.
     * @return The user ID of the payment.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Set the user ID of the payment.
     * @param userId The user ID of the payment.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Get the user ID of the post.
     * @return The user ID of the post.
     */
    public String getPostUserId() {
        return postUserId;
    }

    /**
     * Set the user ID of the post.
     * @param userId The user ID of the post.
     */
    public void setPostUserId(String userId) {
        this.postUserId = postUserId;
    }

    /**
     * Get the payment ID.
     * @return The payment ID.
     */
    public String getPaymentId() {
        return paymentId;
    }

    /**
     * Set the payment ID.
     * @param paymentId The payment ID.
     */
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    /**
     * Get the state of the payment.
     * @return The state of the payment.
     */
    public String getState() {
        return state;
    }

    /**
     * Set the state of the payment.
     * @param state The state of the payment.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Get the amount of the payment.
     * @return The amount of the payment.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Set the amount of the payment.
     * @param amount The amount of the payment.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
