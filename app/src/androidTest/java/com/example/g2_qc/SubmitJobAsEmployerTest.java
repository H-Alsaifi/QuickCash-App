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


import com.example.g2_qc.submitNewJob.SubmitJobAsEmployer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class SubmitJobAsEmployerTest {
    @Rule
    public ActivityScenarioRule<SubmitJobAsEmployer> activityRule = new ActivityScenarioRule<>(SubmitJobAsEmployer.class);

    @Test
    public void testSubmitJobButtonEnabledWhenFieldsFilled() {
        onView(withId(R.id.job_name)).perform(typeText("Test Job"));
        onView(withId(R.id.job_description)).perform(typeText("This is a test job"));
        onView(withId(R.id.job_payment)).perform(typeText("50"));
        onView(withId(R.id.submit_job)).check(matches(isEnabled()));
    }


    @Test
    public void testJobNameFieldRequired() {
        onView(withId(R.id.job_name)).perform(typeText(""));
        onView(withId(R.id.submit_job)).perform(click());
        onView(withId(R.id.job_name)).check(matches(hasErrorText("Please enter a job name")));
    }


    @Test
    public void testJobDescriptionFieldRequired() {
        onView(withId(R.id.job_description)).perform(typeText(""));
        onView(withId(R.id.submit_job)).perform(click());
        onView(withId(R.id.job_description)).check(matches(hasErrorText("Please enter a job description")));
    }

    @Test
    public void testJobPaymentFieldRequired() {
        onView(withId(R.id.job_payment)).perform(typeText(""));
        onView(withId(R.id.submit_job)).perform(click());
        onView(withId(R.id.job_payment)).check(matches(hasErrorText("Please enter a job payment")));
    }
}