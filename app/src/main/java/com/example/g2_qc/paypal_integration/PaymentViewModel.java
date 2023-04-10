package com.example.g2_qc.paypal_integration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PaymentViewModel extends ViewModel {
    private MutableLiveData<Double> wage = new MutableLiveData<>();
    private MutableLiveData<Double> tipAmount = new MutableLiveData<>();
    private MutableLiveData<Double> totalAmount = new MutableLiveData<>();
    private MutableLiveData<String> paymentStatus = new MutableLiveData<>();
    private MutableLiveData<Boolean> paymentSuccess = new MutableLiveData<>();

    // Getters and setters for LiveData objects

    public LiveData<Double> getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage.setValue(wage);
    }

    public LiveData<Double> getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(double tipAmount) {
        this.tipAmount.setValue(tipAmount);
    }

    public LiveData<Double> getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount.setValue(totalAmount);
    }

    public LiveData<String> getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus.setValue(paymentStatus);
    }

    public LiveData<Boolean> isPaymentSuccess() {
        return paymentSuccess;
    }

    public void setPaymentSuccess(boolean paymentSuccess) {
        this.paymentSuccess.setValue(paymentSuccess);
    }
}
