package com.example.g2_qc;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.g2_qc.forgot_password.forgot_password_page;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)

public class forgotPassTest {

    @Rule
    public ActivityScenarioRule<forgot_password_page> forgotPassword= new ActivityScenarioRule<forgot_password_page>(forgot_password_page.class);

    private String text = "";

    private String xInvEmail = "abcmail.com";

    private String authEmail = "hm985160@dal.ca";

    private String xEmail = "abc@gmail.com";

    @Before
    public void setUp(){

    }
    @Test
    public void noEmailWarning(){
        //filling the email field with empty text
        Espresso.onView(withId(R.id.et_email_address)).perform(typeText(text));

        //closing keyboard
        Espresso.closeSoftKeyboard();

        //clicking button
        Espresso.onView(withId(R.id.btn_submit)).perform(click());

        //the error message should show up since no email was included.
        Espresso.onView(withId(R.id.et_email_address)).check(matches(hasErrorText("Email is required")));
    }

    @Test
    public void invalidEmailWarning(){
        //filling the email field with empty text
        Espresso.onView(withId(R.id.et_email_address)).perform(typeText(xInvEmail));

        //closing keyboard
        Espresso.closeSoftKeyboard();

        //clicking button
        Espresso.onView(withId(R.id.btn_submit)).perform(click());

        //the error message should show up since no email was included.
        Espresso.onView(withId(R.id.et_email_address)).check(matches(hasErrorText("Enter a valid email address")));
    }


    @Test
    public void EmailNotInDBWarning(){
        //inputting an email that is not saved in the database, this should trigger an error.
        Espresso.onView(withId(R.id.et_email_address)).perform(typeText(xEmail));

        //closing keyboard
        Espresso.closeSoftKeyboard();

        //clicking button
        Espresso.onView(withId(R.id.btn_submit)).perform(click());

        //the error message should show up since no email was included.
        Espresso.onView(withText("Try again! Wrong email was entered!")).check(doesNotExist());
    }

    @Test
    public void emailSentMessage(){
        //correct email being input, should alert the user that an email has been sent.
        Espresso.onView(withId(R.id.et_email_address)).perform(typeText(authEmail));

        //closing keyboard
        Espresso.closeSoftKeyboard();

        //clicking button
        Espresso.onView(withId(R.id.btn_submit)).perform(click());

        Espresso.onView(withText("Code has been sent to " + authEmail + " successfully")).check(doesNotExist());
    }
}
