package com.example.g2_qc;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.g2_qc.login_page.demo_login_page;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    static demo_login_page demoPage;

    @BeforeClass
    public static void setup() {
        demoPage = new demo_login_page();
    }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }
    @Test
    public void checkEmptyEmailField(){

    }
}