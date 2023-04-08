package com.example.g2_qc;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.example.g2_qc.display_details.display_details;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class jobdetailsTest2 {

    @Rule
    public ActivityScenarioRule<display_details> activityScenarioRule
            = new ActivityScenarioRule<>(display_details.class);

    @Test
    public void testJobDetailsDisplayed() {
        // Check if the job details are displayed correctly in the TextView
        Espresso.onView(ViewMatchers.withId(R.id.display))
                .check(ViewAssertions.matches(ViewMatchers.withText("Job Name: Software Developer\nJob category: IT\nJob description: We are looking for a software developer with experience in Java and Android development.\nPayment: $500\nTime Posted: 2022-10-10 09:00:00")));
    }

    @Test
    public void testBackPressed() {
        // Check if pressing the back button finishes the activity
        Espresso.onView(ViewMatchers.withId(android.R.id.content))
                .perform(ViewActions.pressBack());
        Espresso.onView(ViewMatchers.withId(R.id.display))
                .check(ViewAssertions.doesNotExist());
    }

}