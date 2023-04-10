package com.example.g2_qc.user_profile.History;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HistoryDetails {

    private long totalPosts;
    private long postsAsEmployer;
    private long postsAsEmployee;
    private long totalIncome;
    private long appliedJobs;
    private static HistoryDetails instance = null;

    public HistoryDetails() {
    }

    // Static method to get the singleton instance
    public static HistoryDetails getInstance() {
        if (instance == null) {
            instance = new HistoryDetails();
        }
        return instance;
    }

    public HistoryDetails(long totalPosts, long postsAsEmployer, long postsAsEmployee, long totalIncome, long appliedJobs) {
        this.totalPosts = totalPosts;
        this.postsAsEmployer = postsAsEmployer;
        this.postsAsEmployee = postsAsEmployee;
        this.totalIncome = totalIncome;
        this.appliedJobs = appliedJobs;
    }

    public void retrieveHistoryDetails(TextView totalPostsTextView, TextView PostsAsEmployerTextView, TextView PostsAsEmployeeTextView, TextView totalIncomeTextView, TextView appliedJobsTextView, FirebaseAuth authProfile) {
        FirebaseUser user = authProfile.getCurrentUser();
        if (user != null) {
            String ID = user.getUid();

            DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("Users");
            profileRef.child(ID).child("History").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        HistoryDetails details = snapshot.getValue(HistoryDetails.class);
                        if (details != null) {
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
                    } else {
                        // "History" node does not exist in database
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    public long getAppliedJobs() {
        return appliedJobs;
    }

    public void setAppliedJobs(long appliedJobs) {
        this.appliedJobs = appliedJobs;
    }

    public long getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(long totalIncome) {
        this.totalIncome = totalIncome;
    }

    public long getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(long totalPosts) {
        this.totalPosts = totalPosts;
    }

    public long getPostsAsEmployer() {
        return postsAsEmployer;
    }

    public void setPostsAsEmployer(long postsAsEmployer) {
        this.postsAsEmployer = postsAsEmployer;
    }

    public long getPostsAsEmployee() {
        return postsAsEmployee;
    }

    public void setPostsAsEmployee(long postsAsEmployee) {
        this.postsAsEmployee = postsAsEmployee;
    }

    public void addPostToHistory(DatabaseReference profileRef, FirebaseAuth authProfile, String type) {
        FirebaseUser user = authProfile.getCurrentUser();
        if (user != null) {
            String ID = user.getUid();

            DatabaseReference historyRef = profileRef.child("Users").child(ID).child("History");
            historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HistoryDetails details = snapshot.getValue(HistoryDetails.class);

                    // If details is null, initialize all fields to 0
                    long totalPosts = 0;
                    long postsAsEmployer = 0;
                    long postsAsEmployee = 0;
                    long totalIncome = 0;
                    long appliedJobs = 0;

                    if (details != null) {
                        // If details exist, retrieve values from the snapshot
                        totalPosts = details.getTotalPosts();
                        postsAsEmployer = details.getPostsAsEmployer();
                        postsAsEmployee = details.getPostsAsEmployee();
                        totalIncome = details.getTotalIncome();
                        appliedJobs = details.getAppliedJobs();
                    } else {
                        // If the History node does not exist, initialize it with default values
                        historyRef.child("totalPosts").setValue(totalPosts);
                        historyRef.child("postsAsEmployer").setValue(postsAsEmployer);
                        historyRef.child("postsAsEmployee").setValue(postsAsEmployee);
                        historyRef.child("totalIncome").setValue(totalIncome);
                        historyRef.child("appliedJobs").setValue(appliedJobs);
                    }

                    if (type.equals("postsAsEmployer")) {
                        postsAsEmployer++;
                    } else if (type.equals("postsAsEmployee")) {
                        postsAsEmployee++;
                    }

                    totalPosts++;

                    // Update the values in the database
                    historyRef.child("totalPosts").setValue(totalPosts);
                    historyRef.child("").setValue(postsAsEmployer);
                    historyRef.child("postpostsAsEmployersAsEmployee").setValue(postsAsEmployee);
                    historyRef.child("totalIncome").setValue(totalIncome);
                    historyRef.child("appliedJobs").setValue(appliedJobs);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }
}