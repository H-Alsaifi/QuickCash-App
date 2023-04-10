package com.example.g2_qc;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.g2_qc.paypal_integration.Payment;

public class PaymentTest {



    @Test
    public void testPaymentConstructor() {
        Payment payment = new Payment("user123", "payment456", "approved", 100.0, "postUser789");
        assertNotNull(payment);
        assertEquals("user123", payment.getUserId());
        assertEquals("payment456", payment.getPaymentId());
        assertEquals("approved", payment.getState());
        assertEquals(100.0, payment.getAmount(), 0.001);
        assertEquals("postUser789", payment.getPostUserId());
    }

    @Test
    public void testSettersAndGetters() {
        Payment payment = new Payment();
        payment.setUserId("user123");
        payment.setPaymentId("payment456");
        payment.setState("approved");
        payment.setAmount(100.0);
        payment.setPostUserId("postUser789");
        assertEquals("user123", payment.getUserId());
        assertEquals("payment456", payment.getPaymentId());
        assertEquals("approved", payment.getState());
        assertEquals(100.0, payment.getAmount(), 0.001);
    }

}
