//package com.example.g2_qc;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import com.example.g2_qc.main_page.LocationActivity;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//public class LocationTest {
//
//    @Test
//    public void testMapLoaded() {
//        onView(withId(R.id.location_button)).check(matches(isDisplayed()));
//    }
//    @Test
//    public void checkLocationButtonDisplayed() {
//        ActivityScenario.launch(LocationActivity.class);
//        onView(withId(R.id.location_button)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void checkMapDisplayedOnClick() {
//        ActivityScenario.launch(LocationActivity.class);
//        onView(withId(R.id.location_button)).perform(click());
//        onView(withId(R.id.map_fragment)).check(matches(isDisplayed()));
//    }
//
//}
