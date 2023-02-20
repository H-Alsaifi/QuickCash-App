package com.example.g2_qc;

import static org.junit.Assert.assertTrue;

import com.example.g2_qc.signup_page.signup;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class signupTest {
    static com.example.g2_qc.signup_page.signup signup;

    @BeforeClass
    public static void setUp() {
        signup = new signup();
    }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }

    @Test
    public void isValidFirstName1(){
        String firstName = "John";
        assertTrue(signup.isValidPassword(firstName));
    }

    @Test
    public void isValidFirstName2(){
        String firstName = "John xx";
        assertTrue(signup.isValidPassword(firstName));
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