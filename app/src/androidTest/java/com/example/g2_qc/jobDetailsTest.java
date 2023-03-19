package com.example.g2_qc;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.g2_qc.display_details.display_details;

import org.junit.Rule;
import org.junit.Test;

public class jobDetailsTest {
    @Rule
    public ActivityScenarioRule<display_details> activityScenarioRule =
            new ActivityScenarioRule<>(display_details.class);

    @Test
    public void testDisplayDetails() {
        // Check if the job name is displayed
        Espresso.onView(ViewMatchers.withId(R.id.job_name))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Check if the time posted is displayed
        Espresso.onView(ViewMatchers.withId(R.id.time_posted))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Check if the job category is displayed
        Espresso.onView(ViewMatchers.withId(R.id.job_category))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Check if the job description is displayed
        Espresso.onView(ViewMatchers.withId(R.id.job_description))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Check if the job payment is displayed
        Espresso.onView(ViewMatchers.withId(R.id.job_payment))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Check if the job image is displayed
        Espresso.onView(ViewMatchers.withId(R.id.job_image))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Click on the back button
        Espresso.onView(ViewMatchers.withId(R.id.backButton))
                .perform(ViewActions.click());
    }
}
