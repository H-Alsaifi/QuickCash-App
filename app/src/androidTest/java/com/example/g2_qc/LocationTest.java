//package com.example.g2_qc;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//
//import androidx.test.espresso.Espresso;
//import androidx.test.espresso.action.ViewActions;
//import androidx.test.espresso.assertion.ViewAssertions;
//import androidx.test.espresso.matcher.ViewMatchers;
//import androidx.test.rule.ActivityTestRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import com.example.g2_qc.R;
//import com.example.g2_qc.main_page.location;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//public class LocationTest {
//
//    @Rule
//    public ActivityTestRule<location> activityRule = new ActivityTestRule<>(location.class);
//
//    @Before
//    public void grantPermission() {
//        // Grant location permissions
//        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//        activityRule.getActivity().requestPermissions(permissions, 0);
//    }
//
//    @Test
//    public void testMapLoaded() {
//        onView(withId(R.id.location_button)).check(matches(isDisplayed()));
//    }
//
//
//    @Test
//    public void testMapDisplayed() {
//        // Verify that the map is displayed
//        onView(withId(R.id.location_button)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void testMapClick() {
//        // Perform a map click and verify that a marker is added
//        onView(withId(R.id.location_button)).perform(ViewActions.click());
//        onView(ViewMatchers.withText("Clicked location")).check(matches(isDisplayed()));
//    }
//}
