package com.example.g2_qc;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.g2_qc.user_profile.personal_information.RatingExperience;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RatingTest {

    @Rule
    public ActivityScenarioRule<RatingExperience> activityScenarioRule = new ActivityScenarioRule<>(RatingExperience.class);

    @Test
    public void testRoleSpinnerDisplayed() {
        Espresso.onView(withId(R.id.role_spinner))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testRatingInputDisplayed() {
        Espresso.onView(withId(R.id.rating_input))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testAddRatingButtonDisplayed() {
        Espresso.onView(withId(R.id.previous_ratings_button))
                .check(matches(isDisplayed()));
    }
}
