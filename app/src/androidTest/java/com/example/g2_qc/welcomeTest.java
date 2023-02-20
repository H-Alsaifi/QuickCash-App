package com.example.g2_qc;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.g2_qc.login_page.demo_login_page;
import com.example.g2_qc.signup_page.signup;
import com.example.g2_qc.welcome_page.Welcome;

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

public class welcomeTest {

    @Rule
    public ActivityScenarioRule<Welcome> welcome = new ActivityScenarioRule<Welcome>(Welcome.class);

    @Before
    public void setUp(){

    }

    @Test
    public void checkWelcomeMessageDisplay(){
        //checking to see if the welcome message is correctly presented.
        Espresso.onView(withId(R.id.welcome_message)).check(matches(withText("Join our community, which includes many jobs and qualified workers in various fields")));
    }

    @Test
    public void checkIfSwitchedToLogin(){
        //clicking the sign in button
        Espresso.onView(withId(R.id.button1)).perform(click());

        //checking it switched to the sign in page.
        intended(hasComponent(demo_login_page.class.getName()));
    }

    @Test
    public void checkIfSwitchedToSignUp(){
        //clicking the sign up button
        Espresso.onView(withId(R.id.button2)).perform(click());

        //checking it switched to the sign up page.
        intended(hasComponent(signup.class.getName()));
    }
}
