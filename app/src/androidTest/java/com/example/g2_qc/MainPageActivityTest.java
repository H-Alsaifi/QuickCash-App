package com.example.g2_qc;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.g2_qc.R;
import com.example.g2_qc.main_page.MainPageActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainPageActivityTest {

    @Rule
    public ActivityScenarioRule<MainPageActivity> activityRule = new ActivityScenarioRule<>(MainPageActivity.class);

    @Test
    public void checkLogout() {
        // Open the overflow menu
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        // Click the "Logout" menu item
        onView(withText(R.string.action_logout))
                .perform(click());

    }
}
