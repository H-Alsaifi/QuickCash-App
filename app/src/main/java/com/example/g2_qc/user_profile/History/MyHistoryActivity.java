package com.example.g2_qc.user_profile.History;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.g2_qc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A class representing the user's history activity screen that displays their post and income history.
 */
public class MyHistoryActivity extends AppCompatActivity {

    private TextView totalPostsTextView, PostsAsEmployerTextView , PostsAsEmployeeTextView, totalIncomeTextView, appliedJobsTextView;

    private HistoryDetails historyDetails;
    private FirebaseAuth authProfile;

    /**
     * Initializes the MyHistoryActivity and sets the layout to be displayed.
     * Also sets a click listener on the back button to go back to previous screen.
     * Retrieves and displays the user's history details.
     * @param savedInstanceState savedInstanceState Bundle containing the activity's previously
     *                           saved state, or null if none exists
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_page);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        totalPostsTextView = findViewById(R.id.total_posts);
        PostsAsEmployerTextView = findViewById(R.id.employer_posts);
        PostsAsEmployeeTextView = findViewById(R.id.employee_posts);
        totalIncomeTextView = findViewById(R.id.total_income);
        appliedJobsTextView = findViewById(R.id.applied_jobs);

        authProfile = FirebaseAuth.getInstance();

        historyDetails = new HistoryDetails();
        retrieveHistoryDetails();
    }

    /**
     * Retrieves the user's history details and displays them in the UI.
     */
    public void retrieveHistoryDetails() {
        historyDetails.retrieveHistoryDetails(totalPostsTextView, PostsAsEmployerTextView , PostsAsEmployeeTextView, totalIncomeTextView, appliedJobsTextView, authProfile);
    }

    /**
     * Sets the HistoryDetails instance.
     * @param historyDetails The new HistoryDetails instance to set
     */
    public void setHistoryDetails(HistoryDetails historyDetails) {
        this.historyDetails = historyDetails;
    }

    /**
     * Sets the Firebase authentication instance.
     *      @param authProfile The new Firebase authentication instance to set
     */
    public void setAuthProfile(FirebaseAuth authProfile) {
        this.authProfile = authProfile;
    }

    /**
     * Returns the totalPostsTextView.
     * @return The totalPostsTextView
     */
    public TextView getTotalPostsTextView() {
        return totalPostsTextView;
    }

    /**
     * Sets the totalPostsTextView.
     *      @param totalPostsTextView The new totalPostsTextView to set
     */
    public void setTotalPostsTextView(TextView totalPostsTextView) {
        this.totalPostsTextView = totalPostsTextView;
    }

    /**
     * Returns the PostsAsEmployerTextView.
     *      @return The PostsAsEmployerTextView
     */
    public TextView getPostsAsEmployerTextView() {
        return PostsAsEmployerTextView;
    }

    /**
     * Sets the PostsAsEmployerTextView.
     * @param PostsAsEmployerTextView The new PostsAsEmployerTextView to set
     */
    public void setPostsAsEmployerTextView(TextView PostsAsEmployerTextView) {
        this.PostsAsEmployerTextView = PostsAsEmployerTextView;
    }

    /**
     * Returns the TextView representing the posts made by an employee
     * @return the TextView representing the posts made by an employee
     */
    public TextView getPostsAsEmployeeTextView() {
        return PostsAsEmployeeTextView;
    }


    public void setPostsAsEmployeeTextView(TextView PostsAsEmployeeTextView) {
        this.PostsAsEmployeeTextView = PostsAsEmployeeTextView;
    }

    public TextView getTotalIncomeTextView() {
        return totalIncomeTextView;
    }

    public void setTotalIncomeTextView(TextView totalIncomeTextView) {
        this.totalIncomeTextView = totalIncomeTextView;
    }

    public TextView getAppliedJobsTextView() {
        return appliedJobsTextView;
    }

    public void setAppliedJobsTextView(TextView appliedJobsTextView) {
        this.appliedJobsTextView = appliedJobsTextView;
    }
}

