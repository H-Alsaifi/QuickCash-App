package com.example.g2_qc;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.g2_qc.signup_page.signup;

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
public class signUpTest {

    @Rule
    public ActivityScenarioRule<signup> signUp = new ActivityScenarioRule<signup>(signup.class);

    private String text = "";
    private String text2 = "abc";

    private String xEmail = "abc@gmail.com";
    private String xInvEmail = "abcmail.com";

    private String xPassword = "123@abc123";

    private String xInvPass = "a12";

    private String xAge = "20";

    private String xInvAge = "-4";

    @Before
    public void setup(){

    }

    @Test
    public void checkEmptyFieldErrorMessage(){
        Espresso.onView(withId(R.id.firstName)).perform(typeText(text2));
        //empty last name field, should trigger error message
        Espresso.onView(withId(R.id.lastName)).perform(typeText(text));
        Espresso.onView(withId(R.id.age)).perform(typeText(xAge));
        Espresso.onView(withId(R.id.email_address)).perform(typeText(xEmail));
        Espresso.onView(withId(R.id.setPassword)).perform(typeText(xPassword));
        //closing keyboard
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.confirmPassword)).perform(typeText(xPassword));

        //closing keyboard
        Espresso.closeSoftKeyboard();

        //clicking button
        Espresso.onView(withId(R.id.signup_button)).perform(click());

        Espresso.onView(withText("Please fill all fields.")).check(doesNotExist());

    }

    @Test
    public void checkIfAgeInvalid(){
        Espresso.onView(withId(R.id.firstName)).perform(typeText(text2));
        Espresso.onView(withId(R.id.lastName)).perform(typeText(text2));
        //incorrect age, should trigger error message.
        Espresso.onView(withId(R.id.age)).perform(typeText(xInvAge));
        Espresso.onView(withId(R.id.email_address)).perform(typeText(xEmail));
        Espresso.onView(withId(R.id.setPassword)).perform(typeText(xPassword));
        //closing keyboard
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.confirmPassword)).perform(typeText(xPassword));

        //closing keyboard
        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.signup_button)).perform(click());

        Espresso.onView(withText("Please enter a valid age.")).check(doesNotExist());
    }

    @Test
    public void checkIfEmailInvalid(){
        //all field are being input with correct format, except email.
        Espresso.onView(withId(R.id.firstName)).perform(typeText(text2));
        Espresso.onView(withId(R.id.lastName)).perform(typeText(text2));
        Espresso.onView(withId(R.id.age)).perform(typeText(xAge));
        //invalid email format being input, should trigger error message.
        Espresso.onView(withId(R.id.email_address)).perform(typeText(xInvEmail));
        Espresso.onView(withId(R.id.setPassword)).perform(typeText(xPassword));
        //closing keyboard
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.confirmPassword)).perform(typeText(xPassword));

        //closing keyboard
        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.signup_button)).perform(click());

        Espresso.onView(withText("Please enter a valid email address.")).check(doesNotExist());

    }

    @Test
    public void checkIfPasswordInvalid(){
        //all field are being input with correct format, except password.
        Espresso.onView(withId(R.id.firstName)).perform(typeText(text2));
        Espresso.onView(withId(R.id.lastName)).perform(typeText(text2));
        Espresso.onView(withId(R.id.age)).perform(typeText(xAge));
        Espresso.onView(withId(R.id.email_address)).perform(typeText(xEmail));
        //invalid password being input, should trigger error message.
        Espresso.onView(withId(R.id.setPassword)).perform(typeText(xInvPass));
        //closing keyboard
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.confirmPassword)).perform(typeText(xInvPass));

        //closing keyboard
        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.signup_button)).perform(click());

        Espresso.onView(withText("Invalid password!")).check(doesNotExist());
    }

    @Test
    public void checkPasswordMatch(){
        //all field are being input with correct format, except confirm password.
        Espresso.onView(withId(R.id.firstName)).perform(typeText(text2));
        Espresso.onView(withId(R.id.lastName)).perform(typeText(text2));
        Espresso.onView(withId(R.id.age)).perform(typeText(xAge));
        Espresso.onView(withId(R.id.email_address)).perform(typeText(xEmail));
        //valid password being input
        Espresso.onView(withId(R.id.setPassword)).perform(typeText(xPassword));
        //closing keyboard
        Espresso.closeSoftKeyboard();
        //non-matching password, should trigger error message.
        Espresso.onView(withId(R.id.confirmPassword)).perform(typeText(xInvPass));

        //closing keyboard
        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.signup_button)).perform(click());

        Espresso.onView(withText("Passwords don't match.")).check(doesNotExist());
    }



}