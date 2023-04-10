package com.example.g2_qc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.g2_qc.signup_page.signup;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


public class signupTest {

    static signup user1;
    static signup user2;

    @BeforeClass
    public static void setUp() {
        String vName = "John";
        user1 = Mockito.mock(signup.class);
        Mockito.when(user1.isValidFirstName(vName)).thenReturn(true);
        Mockito.when(user1.isValidAge("21")).thenReturn(true);
        Mockito.when(user1.isValidLastName("McCain")).thenReturn(true);
        Mockito.when(user1.isValidExperience("5","21")).thenReturn(true);


        String invName = "Troy12121";
        user2 = Mockito.mock(signup.class);
        Mockito.when(user2.isValidFirstName(invName)).thenReturn(false);
        Mockito.when(user2.isValidAge("-21")).thenReturn(false);
        Mockito.when(user2.isValidLastName("2999")).thenReturn(false);
        Mockito.when(user1.isValidExperience("22","21")).thenReturn(false);


    }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }


    @Test
    public void checkValidFirstName(){
        String firstName = "John";
        assertTrue(user1.isValidFirstName(firstName));
    }

    @Test
    public void checkInvalidName(){
        String invName1 = "Troy12121";
        assertFalse(user2.isValidPassword(invName1));
    }

    @Test
    public void checkValidAge(){
        String age = "21";
        assertTrue(user1.isValidAge(age));

    }

    @Test
    public void checkInvalidAge(){
        String age = "-21";
        assertFalse(user2.isValidAge(age));
    }

    @Test
    public void checkValidLN(){
        String ln = "McCain";
        assertTrue(user1.isValidLastName(ln));
    }

    @Test
    public void checkInvalidLN(){
        String ln = "2999";
        assertFalse(user2.isValidLastName(ln));
    }

    @Test
    public void checkInvalidExp(){
        String ln = "22";
        String ln2 = "21";
        assertFalse(user2.isValidExperience(ln,ln2));
    }

    @Test
    public void checkValidExp(){
        String ln = "5";
        String ln2 = "21";
        assertTrue(user1.isValidExperience(ln,ln2));
    }


}