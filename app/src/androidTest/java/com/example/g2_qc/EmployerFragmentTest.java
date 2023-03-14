//package com.example.g2_qc;
//
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.core.app.ActivityScenario.ActivityAction;
//import androidx.test.espresso.UiController;
//import androidx.test.espresso.ViewAction;
//import androidx.test.espresso.matcher.ViewMatchers;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import org.hamcrest.Matcher;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//
//@RunWith(AndroidJUnit4.class)
//public class EmployerFragmentTest {
//
//    @Rule
//    public ActivityScenarioRule<MainActivity> activityRule =
//            new ActivityScenarioRule<>(MainActivity.class);
//
//    @Test
//    public void testScrollViewPopulated() {
//
//        // Find the scroll view in the fragment
//        onView(withId(R.id.scroll_view))
//                .check(matches(isDisplayed()));
//
//        // Wait for the scroll view to be populated with posts
//        onView(withId(R.id.linear_layout))
//                .perform(waitFor(5000));
//
//        // Check that the scroll view contains at least one post
//        onView(withId(R.id.linear_layout))
//                .check(matches(hasMinimumChildCount(1)));
//    }
//
//    public static ViewAction waitFor(long millis) {
//        return new ViewAction() {
//            @Override
//            public Matcher<View> getConstraints() {
//                return ViewMatchers.isRoot();
//            }
//
//            @Override
//            public String getDescription() {
//                return "wait for " + millis + " milliseconds";
//            }
//
//            @Override
//            public void perform(UiController uiController, View view) {
//                uiController.loopMainThreadForAtLeast(millis);
//            }
//        };
//    }
//}
