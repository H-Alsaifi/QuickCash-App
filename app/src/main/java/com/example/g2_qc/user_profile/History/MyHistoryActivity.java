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

public class MyHistoryActivity extends AppCompatActivity {

    private TextView totalPostsTextView, PostsAsEmployerTextView , PostsAsEmployeeTextView, totalIncomeTextView, appliedJobsTextView;

    private HistoryDetails historyDetails;
    private FirebaseAuth authProfile;

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

    public void retrieveHistoryDetails() {
        historyDetails.retrieveHistoryDetails(totalPostsTextView, PostsAsEmployerTextView , PostsAsEmployeeTextView, totalIncomeTextView, appliedJobsTextView, authProfile);
    }

    public void setHistoryDetails(HistoryDetails historyDetails) {
        this.historyDetails = historyDetails;
    }

    public void setAuthProfile(FirebaseAuth authProfile) {
        this.authProfile = authProfile;
    }

    public TextView getTotalPostsTextView() {
        return totalPostsTextView;
    }

    public void setTotalPostsTextView(TextView totalPostsTextView) {
        this.totalPostsTextView = totalPostsTextView;
    }

    public TextView getPostsAsEmployerTextView() {
        return PostsAsEmployerTextView;
    }

    public void setPostsAsEmployerTextView(TextView PostsAsEmployerTextView) {
        this.PostsAsEmployerTextView = PostsAsEmployerTextView;
    }

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

