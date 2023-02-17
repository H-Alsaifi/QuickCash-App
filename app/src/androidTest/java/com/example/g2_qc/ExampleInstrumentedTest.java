package com.example.g2_qc;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



import static org.junit.Assert.*;

import com.example.g2_qc.login_page.demo_login_page;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityScenarioRule<demo_login_page> activityTest = new ActivityScenarioRule<demo_login_page>(demo_login_page.class);

    private String text = "";
    private String text2 = "abc";
    @Before
    public void setUp() throws Exception{

    }
    @Test
    public void checkEmptyEmailField(){
        //filling the email field with empty text
        Espresso.onView(withId(R.id.et_email_address)).perform(typeText(text));
        //filling password with some text
        Espresso.onView(withId(R.id.et_password)).perform(typeText(text2));

        //closing keyboard
        Espresso.closeSoftKeyboard();

        //clicking button
        Espresso.onView(withId(R.id.btn_login)).perform(click());

        //the error message should show up since no email was included.
        Espresso.onView(withId(R.id.et_email_address)).check(matches(hasErrorText("Email Address is required")));
    }

    @Test
    public void checkEmptyPasswordField(){
        //filling the password field with empty text
        Espresso.onView(withId(R.id.et_password)).perform(typeText(text));
        //filling password with some text
        Espresso.onView(withId(R.id.et_email_address)).perform(typeText(text2));

        //closing keyboard
        Espresso.closeSoftKeyboard();

        //clicking button
        Espresso.onView(withId(R.id.btn_login)).perform(click());

        //the error message should show up since no email was included.
        Espresso.onView(withId(R.id.et_password)).check(matches(hasErrorText("Password is required")));
    }









    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.g2_qc", appContext.getPackageName());
    }
}