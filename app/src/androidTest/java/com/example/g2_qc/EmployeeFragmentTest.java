//package com.example.g2_qc;
//
//import static org.junit.Assert.assertEquals;
//
//import android.content.Context;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.espresso.Espresso;
//import androidx.test.espresso.action.ViewActions;
//import androidx.test.espresso.assertion.ViewAssertions;
//import androidx.test.espresso.matcher.ViewMatchers;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.rule.ActivityTestRule;
//import androidx.test.rule.GrantPermissionRule;
//
//import com.example.g2_qc.main_page.ui.Employee.EmployeeFragment;
//import com.google.firebase.database.FirebaseDatabase;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//public class EmployeeFragmentTest {
//
//    private Context context;
//
//    @Rule
//    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);
//
//    @Rule
//    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//    @Before
//    public void setUp() {
//        context = ApplicationProvider.getApplicationContext();
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//    }
//
//    @Test
//    public void testPopulateScrollView() {
//        // Given
//        String jobName1 = "Job 1";
//        String jobDescription1 = "This is job 1.";
//        String imageUrl1 = "job1.png";
//
//        String jobName2 = "Job 2";
//        String jobDescription2 = "This is job 2.";
//        String imageUrl2 = "job2.png";
//
//        String jobName3 = "Job 3";
//        String jobDescription3 = "This is job 3.";
//        String imageUrl3 = "job3.png";
//
//        // When
//        Espresso.onView(ViewMatchers.withId(R.id.linear_layout)).check(ViewAssertions.matches(ViewMatchers.withChild(ViewMatchers.withText(jobName1))));
//        Espresso.onView(ViewMatchers.withId(R.id.linear_layout)).check(ViewAssertions.matches(ViewMatchers.withChild(ViewMatchers.withText(jobDescription1))));
//
//        // Then
//        Espresso.onView(ViewMatchers.withId(R.id.linear_layout)).check(ViewAssertions.matches(ViewMatchers.withChild(ViewMatchers.withText(jobName2))));
//        Espresso.onView(ViewMatchers.withId(R.id.linear_layout)).check(ViewAssertions.matches(ViewMatchers.withChild(ViewMatchers.withText(jobDescription2))));
//
//        // Then
//        Espresso.onView(ViewMatchers.withId(R.id.linear_layout)).check(ViewAssertions.matches(ViewMatchers.withChild(ViewMatchers.withText(jobName3))));
//        Espresso.onView(ViewMatchers.withId(R.id.linear_layout)).check(ViewAssertions.matches(ViewMatchers.withChild(ViewMatchers.withText(jobDescription3))));
//    }
//
//    @Test
//    public void testImageView() {
//        // Given
//        String imageUrl1 = "job1.png";
//        int expectedWidth = (int) context.getResources().getDimension(R.dimen.image_width);
//        int expectedHeight = (int) context.getResources().getDimension(R.dimen.image_height);
//
//        // When
//        Espresso.onView(ViewMatchers.withId(R.id.linear_layout)).perform(ViewActions.swipeUp());
//        Espresso.onView(ViewMatchers.withId(R.id.box_image)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
//        Espresso.onView(ViewMatchers.withId(R.id.box_image)).check(ViewAssertions.matches(ViewMatchers.withTagValue(ViewMatchers.equalTo(imageUrl1))));
//        Espresso.onView(ViewMatchers.withId(R.id.box_image)).check(ViewAssertions.matches(new ImageSizeMatcher(expectedWidth, expectedHeight)));
//    }
//
//    @Test
//    public void testJobNameAndDescriptionDisplayedCorrectly() {
//        // Mock the data snapshot
//        DataSnapshot dataSnapshot = Mockito.mock(DataSnapshot.class);
//        DataSnapshot postSnapshot1 = Mockito.mock(DataSnapshot.class);
//        DataSnapshot postSnapshot2 = Mockito.mock(DataSnapshot.class);
//        DataSnapshot postSnapshot3 = Mockito.mock(DataSnapshot.class);
//        Mockito.when(postSnapshot1.child("jobName").getValue(String.class)).thenReturn("Job 1");
//        Mockito.when(postSnapshot1.child("jobDescription").getValue(String.class)).thenReturn("This is job 1.");
//        Mockito.when(postSnapshot2.child("jobName").getValue(String.class)).thenReturn("Job 2");
//        Mockito.when(postSnapshot2.child("jobDescription").getValue(String.class)).thenReturn("This is job 2.");
//        Mockito.when(postSnapshot3.child("jobName").getValue(String.class)).thenReturn("Job 3");
//        Mockito.when(postSnapshot3.child("jobDescription").getValue(String.class)).thenReturn("This is job 3.");
//        Mockito.when(dataSnapshot.getChildren()).thenReturn(Arrays.asList(postSnapshot1, postSnapshot2, postSnapshot3));
//
//        // Inflate the layout
//        LayoutInflater inflater = LayoutInflater.from(ApplicationProvider.getApplicationContext());
//        LinearLayout linearLayout = new LinearLayout(ApplicationProvider.getApplicationContext());
//        View view = inflater.inflate(R.layout.box_layout, linearLayout, false);
//        TextView textViewName = view.findViewById(R.id.box_title);
//        TextView textViewDescription = view.findViewById(R.id.box_content);
//
//        // Call populateScrollView
//        EmployeeFragment fragment = new EmployeeFragment();
//        fragment.populateScrollView(dataSnapshot);
//
//        // Check that the job name and description are displayed correctly in each box view
//        assertEquals("Job 1", textViewName.getText().toString());
//        assertEquals("This is job 1.", textViewDescription.getText().toString());
//
//        view = inflater.inflate(R.layout.box_layout, linearLayout, false);
//        textViewName = view.findViewById(R.id.box_title);
//        textViewDescription = view.findViewById(R.id.box_content);
//        assertEquals("Job 2", textViewName.getText().toString());
//        assertEquals("This is job 2.", textViewDescription.getText().toString());
//
//        view = inflater.inflate(R.layout.box_layout, linearLayout, false);
//        textViewName = view.findViewById(R.id.box_title);
//        textViewDescription = view.findViewById(R.id.box_content);
//        assertEquals("Job 3", textViewName.getText().toString());
//        assertEquals("This is job 3.", textViewDescription.getText().toString());
//    }
//}
