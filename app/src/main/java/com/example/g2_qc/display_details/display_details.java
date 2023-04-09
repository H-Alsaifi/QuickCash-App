package com.example.g2_qc.display_details;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.g2_qc.R;
import com.example.g2_qc.main_page.MainPageActivity;
import com.example.g2_qc.paypal_integration.ApplyActivity;
import com.example.g2_qc.signup_page.User;
import com.example.g2_qc.submitNewJob.SubmitJobAsEmployer;
import com.example.g2_qc.user_profile.History.HistoryDetails;
import com.example.g2_qc.user_profile.History.MyHistoryActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class display_details extends AppCompatActivity {

    // Declare private variables to hold UI elements
    public TextView jobNameTextView;
    private TextView timePostedTextView;
    private TextView jobCategoryTextView;
    public TextView jobDescriptionTextView;
    private TextView jobPaymentTextView;
    private ImageView jobImageView;
    private String userId;
    public String firstName;
    public String lastName;
    public String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_details);

        // Initialize the TextViews
        jobNameTextView = findViewById(R.id.job_name);
        timePostedTextView = findViewById(R.id.time_posted);
        jobCategoryTextView = findViewById(R.id.job_category);
        jobDescriptionTextView = findViewById(R.id.job_description);
        jobPaymentTextView = findViewById(R.id.job_payment);

        // Initialize the ImageView
        jobImageView = findViewById(R.id.job_image);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get the post ID from the previous activity
        Intent intent = getIntent();
        String postKey = intent.getStringExtra("postId");
        String className = intent.getStringExtra("class");

        // Extract post information from Firebase database
        extractInfo(postKey);

        // Set click listener for the "Back" button
        Button btn = findViewById(R.id.backButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                startActivity(intent);
            }
        });

        // Info Button click listener
        Button infoButton = findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showJobInfo();
            }
        });


        Button applyButton = findViewById(R.id.applyButton);
        if (className != null && className.equals("EmployeeFragment")){
            applyButton.setText("HIRE");
        }
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (className != null && className.equals("EmployerFragment")) {
                    // Create a new AlertDialog builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(display_details.this);

                    // Set the message to display in the dialog
                    builder.setMessage("Please contact the employer directly if you are interested in this job.\nWe will directly transfer the money from our account to you once you have completed the task and the employer has approved it. ");

                    // Set up the "OK" button for the dialog
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Close the dialog when the button is clicked
                            dialog.dismiss();
                        }
                    });

                    // Create and show the dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // Get the wage from the TextView
                    String wage = jobPaymentTextView.getText().toString();

                    // Create a new Intent to navigate to ApplyActivity
                    Intent intent = new Intent(getApplicationContext(), ApplyActivity.class);

                    // Add the wage as an extra to the Intent
                    intent.putExtra("wage", wage);
                    intent.putExtra("userId", userId);
                    intent.putExtra("postId", postKey);
                    intent.putExtra("email", email);

                    // Start the ApplyActivity with the Intent
                    startActivity(intent);

                }
            }
        });

    }

    /**
     This method extracts the job post information from the Firebase database and populates the
     UI elements with the appropriate values.
     @param postId the ID of the job post to display
     */
    public void extractInfo(String postId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Loop through all users in the database
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Get references to the "Posts" nodes under "Employee" and "Employer" nodes
                    DatabaseReference postsReferenceEmployee = userSnapshot.child("Employee").child("Posts").getRef();
                    DatabaseReference postsReferenceEmployer = userSnapshot.child("Employer").child("Posts").getRef();

                    // Extract post information from both "Employee" and "Employer" nodes
                    postsReferenceEmployee.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            extractPostInfo(dataSnapshot, postId);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(display_details.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    postsReferenceEmployer.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            extractPostInfo(dataSnapshot, postId);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(display_details.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(display_details.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     This method is used to extract information about a post with a given ID from a DataSnapshot.
     It loops through all posts in the DataSnapshot, checks if the current post has the given ID,
     and if it does, extracts information about the post such as job name, time posted, job category,
     job description, payment, and image. It then sets the UI elements with this information and
     loads the image from Firebase storage and displays it in the ImageView.
     @param dataSnapshot A DataSnapshot containing all posts.
     @param postId The ID of the post to extract information about.
     */
    public void extractPostInfo(DataSnapshot dataSnapshot, String postId) {
        // Loop through all posts under the given DataSnapshot
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            // Check if the current post has the given ID
            String snapshotKey = postSnapshot.getKey();
            if (snapshotKey != null && snapshotKey.equals(postId)) {
                String currUserId = postSnapshot.getRef().getParent().getParent().getParent().toString().replace("https://quickcash-group2-1d321-default-rtdb.firebaseio.com/Users/" ,"");
                userId = currUserId;

                ExtractUserInfo(currUserId);
                // Extract post information
                String jobName = postSnapshot.child("jobName").getValue(String.class);
                String timePosted = "Time posted: " + postSnapshot.child("timePosted").getValue(String.class);
                String jobCategory = postSnapshot.child("jobCategory").getValue(String.class);
                String description = "Description:\n\n" + postSnapshot.child("jobDescription").getValue(String.class);
                String payment = "Wage: " + postSnapshot.child("jobPayment").getValue(String.class) + "$";
                String imageUrl = postSnapshot.child("image").getValue(String.class);
                imageUrl.replace("content://com.android.providers.downloads.documents/document/", "");

                // Set UI elements with post information
                jobNameTextView.setText(jobName);
                timePostedTextView.setText(timePosted);
                jobCategoryTextView.setText(jobCategory);
                jobDescriptionTextView.setText(description);
                jobPaymentTextView.setText(payment);

                // Load image from Firebase storage and display it in the ImageView
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference().child("images").child(imageUrl);
                storageRef.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        jobImageView.setImageBitmap(bitmap);
                    }
                });
            }
        }
    }

    /**
     Extracts user information from a Firebase database based on their ID.
     @param Id The ID of the user whose information is to be extracted.
     */
    private void ExtractUserInfo( String Id) {
        // Get the user's ID and a reference to their profile info in the database
        DatabaseReference profile_ref = FirebaseDatabase.getInstance().getReference("Users");

        // Add a listener to the profile info reference to get the user's email, location, and category
        profile_ref.child(Id).addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * This method is called when the database returns data.
             * @param snapshot The data snapshot returned by the database.
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userDetails = snapshot.getValue(User.class);
                if (userDetails != null) {
                    firstName = userDetails.firstName;
                    lastName = userDetails.lastname;
                    email = userDetails.emailAddress;

                }
            }
            /**
             * This method is called if the database read operation is cancelled.
             * @param error The error that caused the cancellation.
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Display an error message if the database read was cancelled
                Toast.makeText(display_details.this, "Error: we could not complete your request", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     This method extracts the job name and description from their respective TextViews and
     shows a dialog displaying the user's name and email.
     */
    public void showJobInfo() {

        // Extract the job name and description
        String jobName = jobNameTextView.getText().toString();
        String jobDescription = jobDescriptionTextView.getText().toString();

        // Build the alert message
        StringBuilder alertMessage = new StringBuilder();
        alertMessage.append("Name: ").append(firstName +" "+lastName).append("\n\n");
        alertMessage.append("Email: ").append(email);

        // Show the alert
        AlertDialog.Builder builder = new AlertDialog.Builder(display_details.this);
        builder.setMessage(alertMessage.toString())
                .setTitle("Contact Info")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}