//package com.example.g2_qc;
//
//import androidx.test.espresso.Espresso;
//import androidx.test.espresso.IdlingRegistry;
//import androidx.test.espresso.IdlingResource;
//import androidx.test.espresso.action.ViewActions;
//import androidx.test.espresso.assertion.ViewAssertions;
//import androidx.test.espresso.matcher.ViewMatchers;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//
//import com.example.g2_qc.main_page.MainPageActivity;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//
//public class MainPageActivityTest {
//
//    @Rule
//    public ActivityScenarioRule<MainPageActivity> activityScenarioRule =
//            new ActivityScenarioRule<>(MainPageActivity.class);
//
//    private IdlingResource idlingResource;
//
//    @Before
//    public void registerIdlingResource() {
//        idlingResource = activityScenarioRule.getScenario().getIdlingRegistry().get("FIREBASE_USER_EXTRACTION");
//        IdlingRegistry.getInstance().register(idlingResource);
//    }
//
//    @After
//    public void unregisterIdlingResource() {
//        if (idlingResource != null) {
//            IdlingRegistry.getInstance().unregister(idlingResource);
//        }
//    }
//
//    @Test
//    public void testEmailIsDisplayed() {
//        Espresso.onView(ViewMatchers.withId(R.id.nav_header_subtitle))
//                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
//    }
//
//    @Test
//    public void testLocationButtonShowsText() {
//        Espresso.onView(ViewMatchers.withId(R.id.location_button))
//                .check(ViewAssertions.matches(ViewMatchers.withText("Choose location")));
//    }
//
//    @Test
//    public void testLocationButtonCanClick() {
//        Espresso.onView(ViewMatchers.withId(R.id.location_button))
//                .perform(ViewActions.click());
//        Espresso.onView(ViewMatchers.withId(R.id.map))
//                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
//    }
//
//    @Test
//    public void testLogoutMenuClick() {
//        Espresso.openActionBarOverflowOrOptionsMenu(activityScenarioRule.getActivity());
//        Espresso.onView(ViewMatchers.withText("Logout"))
//                .perform(ViewActions.click());
//        Espresso.onView(ViewMatchers.withText("Logged out"))
//                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
//    }
//}
