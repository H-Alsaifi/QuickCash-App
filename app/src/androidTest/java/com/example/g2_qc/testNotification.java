package com.example.g2_qc;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.g2_qc.main_page.MainPageActivity;

import org.junit.Rule;
import org.junit.Test;

public class testNotification {
    @Rule
    public ActivityScenarioRule<MainPageActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainPageActivity.class);

    @Test
    public void testNotification() {
        // Click on the floating action button to trigger showNotification
        onView(withId(R.id.notification)).perform(click());

        // Wait for the notification alert dialog to appear
        onView(withText("Jobs Notification of Your Category")).inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

}
