package com.example.g2_qc.user_profile.History;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.g2_qc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A class representing the user's history activity screen that displays their post and income history.
 */
public class MyHistoryActivity extends AppCompatActivity {

    private HistoryDetails historyDetails;
    private FirebaseAuth authProfile;

    /**
     * Initializes the MyHistoryActivity and sets the layout to be displayed.
     * Also sets a click listener on the back button to go back to previous screen.
     * Retrieves and displays the user's history details.
     * @param savedInstanceState savedInstanceState Bundle containing the activity's previously
     *                           saved state, or null if none exists
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_page);

        // Set up back button to return to previous screen
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Instantiate HistoryDetails object and retrieve history details from Firebase database
        historyDetails = new HistoryDetails();
        retrieveHistoryDetails();
    }

    /**
     * Retrieves the user's history details and displays them in the UI.
     */
    public void retrieveHistoryDetails() {
        // Get the current authenticated user
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser user = authProfile.getCurrentUser();
        if (user != null) {
            String ID = user.getUid();

            // Set up database references
            DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("Users");
            DatabaseReference historyRef = profileRef.child(ID).child("History");

            // Retrieve history details using a ValueEventListener
            historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        HistoryDetails details = snapshot.getValue(HistoryDetails.class);
                        if (details != null) {
                            displayHistoryDetails(details);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    // Method to display history details on the screen
    private void displayHistoryDetails(HistoryDetails details) {
        // Get the necessary TextView objects from the layout file
        TextView totalPostsTextView = findViewById(R.id.total_posts);
        TextView PostsAsEmployerTextView = findViewById(R.id.employer_posts);
        TextView PostsAsEmployeeTextView = findViewById(R.id.employee_posts);
        TextView totalIncomeTextView = findViewById(R.id.total_income);
        TextView appliedJobsTextView = findViewById(R.id.applied_jobs);

        // Retrieve the necessary history details and set the text for the corresponding TextView objects
        long totalPosts = details.getTotalPosts();
        String CurrPostsAsEmployer = String.valueOf(details.getPostsAsEmployer());
        String CurrPostsAsEmployee = String.valueOf(details.getPostsAsEmployee());
        long totalIncome = details.getTotalIncome();
        long appliedJobs = details.getAppliedJobs();

        totalPostsTextView.setText(String.valueOf(totalPosts));
        PostsAsEmployerTextView.setText(CurrPostsAsEmployer);
        PostsAsEmployeeTextView.setText(CurrPostsAsEmployee);
        totalIncomeTextView.setText("$" + String.valueOf(totalIncome)); // Add dollar symbol here
        appliedJobsTextView.setText(String.valueOf(appliedJobs));
    }
}

