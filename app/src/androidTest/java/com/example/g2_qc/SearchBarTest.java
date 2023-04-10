package com.example.g2_qc;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

import android.view.KeyEvent;

@RunWith(AndroidJUnit4.class)
public class SearchBarTest {

    @Rule
    public ActivityScenarioRule<com.example.g2_qc.main_page.MainPageActivity> activityRule =
            new ActivityScenarioRule<>(com.example.g2_qc.main_page.MainPageActivity.class);

    @Test
    public void searchTest() {
        // Find the search view by its id
        onView(withId(R.id.search_view))
                // Perform a click on the search view
                .perform(click())
                // Type "coffee" into the search view
                .perform(typeText("coffee"))
                // Press the enter key
                .perform(pressKey(KeyEvent.KEYCODE_ENTER))
                // Verify that the search results are displayed
                .check(matches(isDisplayed()));
    }

}
