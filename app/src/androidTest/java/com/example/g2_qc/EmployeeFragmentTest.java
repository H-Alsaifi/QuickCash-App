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
//import com.example.g2_qc.main_page.MainPageActivity;
//import com.example.g2_qc.main_page.ui.Employee.EmployeeFragment;
//import com.example.g2_qc.submitNewJob.Post;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.FirebaseDatabase;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//
//@RunWith(AndroidJUnit4.class)
//public class EmployeeFragmentTest {
//
//    private Context context;
//
//    @Rule
//    public ActivityTestRule<MainPageActivity> activityTestRule = new ActivityTestRule<>(MainPageActivity.class);
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
//
//        String jobName2 = "Job 2";
//        String jobDescription2 = "This is job 2.";
//
//        String jobName3 = "Job 3";
//        String jobDescription3 = "This is job 3.";
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
//    public void testJobNameAndDescriptionDisplayedCorrectly() {
//        // Create a mock list of Post objects
//        List<Post> postList = new ArrayList<>();
//        postList.add(new Post("Job 1", "This is job 1.", "100", "", "Category 1"));
//        postList.add(new Post("Job 2", "This is job 2.", "200", "", "Category 2"));
//        postList.add(new Post("Job 3", "This is job 3.", "300", "", "Category 3"));
//
//        // Convert the mock list into a HashMap of DataSnapshot objects
//        HashMap<String, DataSnapshot> dataSnapshotHashMap = new HashMap<>();
//        for (int i = 0; i < postList.size(); i++) {
//            Post post = postList.get(i);
//            DataSnapshot dataSnapshot = Mockito.mock(DataSnapshot.class);
//            Mockito.when(dataSnapshot.child("jobName").getValue(String.class)).thenReturn(post.getJobName());
//            Mockito.when(dataSnapshot.child("jobDescription").getValue(String.class)).thenReturn(post.getJobDescription());
//            dataSnapshotHashMap.put(String.valueOf(i), dataSnapshot);
//        }
//
//        // Create a mock DataSnapshot object with the HashMap of Post objects
//        DataSnapshot dataSnapshot = Mockito.mock(DataSnapshot.class);
//        Mockito.when(dataSnapshot.getChildrenCount()).thenReturn((long) postList.size());
//        Mockito.when(dataSnapshot.getChildren()).thenReturn(dataSnapshotHashMap.values());
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
