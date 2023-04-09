package com.example.g2_qc;

import org.junit.Test;

        import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.example.g2_qc.paypal_integration.Payment;

public class ApplyActivityTest {

    @Test
    public void testPaymentConstructor() {
        Payment payment = new Payment("user123", "pay456", "completed", 10.0, "user321");
        assertEquals("user123", payment.getUserId());
        assertEquals("pay456", payment.getPaymentId());
        assertEquals("completed", payment.getState());
        assertEquals(10.0, payment.getAmount(), 0.001);
    }

    @Test
    public void testPaymentSetters() {
        Payment payment = new Payment();
        payment.setUserId("user123");
        payment.setPaymentId("pay456");
        payment.setState("completed");
        payment.setAmount(10.0);
        assertEquals("user123", payment.getUserId());
        assertEquals("pay456", payment.getPaymentId());
        assertEquals("completed", payment.getState());
        assertEquals(10.0, payment.getAmount(), 0.001);
    }

    @Test
    public void testPaymentDefaultConstructor() {
        Payment payment = new Payment();
        assertNull(payment.getUserId());
        assertNull(payment.getPaymentId());
        assertNull(payment.getState());
        assertEquals(0.0, payment.getAmount(), 0.001);
    }

    @Test
    public void testPaymentSetUserId() {
        Payment payment = new Payment();
        payment.setUserId("user123");
        assertEquals("user123", payment.getUserId());
    }

    @Test
    public void testPaymentSetPaymentId() {
        Payment payment = new Payment();
        payment.setPaymentId("pay456");
        assertEquals("pay456", payment.getPaymentId());
    }

    @Test
    public void testPaymentSetState() {
        Payment payment = new Payment();
        payment.setState("completed");
        assertEquals("completed", payment.getState());
    }

    @Test
    public void testPaymentSetAmount() {
        Payment payment = new Payment();
        payment.setAmount(10.0);
        assertEquals(10.0, payment.getAmount(), 0.001);
    }
}
