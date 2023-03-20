package com.example.g2_qc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.g2_qc.signup_page.signup;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;


public class signupTest {

    static signup validName;
    static signup invalidName;

    @BeforeClass
    public static void setUp() {
        String vName = "John";
        validName = Mockito.mock(signup.class);
        Mockito.when(validName.isValidFirstName(vName)).thenReturn(true);

        String invName = "Troy12121";
        invalidName = Mockito.mock(signup.class);
        Mockito.when(invalidName.isValidFirstName(invName)).thenReturn(false);

    }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }


    @Test
    public void checkValidFirstName(){
        String firstName = "John";
        assertTrue(validName.isValidFirstName(firstName));
    }

    @Test
    public void checkInvalidName(){
        String invName1 = "Troy12121";
        assertFalse(signup.isValidPassword(invName1));
    }

    @Test
    public void isValidFirstName3(){
        String firstName = "";
        assertTrue(signup.isValidPassword(firstName));
    }

    @Test
    public void isValidFirstName4(){
        String firstName = "123";
        assertTrue(signup.isValidPassword(firstName));
    }

    @Test
    public void isValidFirstName5(){
        String firstName = "&@";
        assertTrue(signup.isValidPassword(firstName));
    }
}