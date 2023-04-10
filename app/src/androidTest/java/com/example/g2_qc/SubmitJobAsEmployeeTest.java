package com.example.g2_qc;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.g2_qc.submitNewJob.SubmitJobAsEmployee;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class SubmitJobAsEmployeeTest {
    @Rule
    public ActivityScenarioRule<SubmitJobAsEmployee> activityRule = new ActivityScenarioRule<>(SubmitJobAsEmployee.class);


    @Test
    public void testSubmitJobButtonEnabledWhenFieldsFilled() {
        onView(withId(R.id.job_name_employee)).perform(typeText("Test Job"));
        onView(withId(R.id.job_description_employee)).perform(typeText("This is a test job"));
        onView(withId(R.id.job_payment_employee)).perform(typeText("50"));
        onView(withId(R.id.submit_job_employee)).check(matches(isEnabled()));
    }


    @Test
    public void testJobNameFieldRequired() {
        onView(withId(R.id.job_name_employee)).perform(typeText(""));
        onView(withId(R.id.submit_job_employee)).perform(click());
        onView(withId(R.id.job_name_employee)).check(matches(hasErrorText("Please enter a job name")));
    }


    @Test
    public void testJobDescriptionFieldRequired() {
        onView(withId(R.id.job_description_employee)).perform(typeText(""));
        onView(withId(R.id.submit_job_employee)).perform(click());
        onView(withId(R.id.job_description_employee)).check(matches(hasErrorText("Please enter a job description")));
    }

    @Test
    public void testJobPaymentFieldRequired() {
        onView(withId(R.id.job_payment_employee)).perform(typeText(""));
        onView(withId(R.id.submit_job_employee)).perform(click());
        onView(withId(R.id.job_payment_employee)).check(matches(hasErrorText("Please enter a job payment")));
    }


}