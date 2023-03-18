package com.example.g2_qc.display_details;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class display_details extends AppCompatActivity {

    private TextView jobNameTextView, timePostedTextView, jobCategoryTextView, jobDescriptionTextView, jobPaymentTextView;
    private ImageView jobImageView;

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

        Intent intent = getIntent();
        String postKey = intent.getStringExtra("postId");
        extractInfo(postKey);


        Button btn = findViewById(R.id.backButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainPageActivity.class);
                startActivity(intent);
            }
        });





    }
    public void extractInfo(String postId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {


                    DatabaseReference postsReferenceEmployee = userSnapshot.child("Employee").child("Posts").getRef();
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


                    DatabaseReference postsReferenceEmployer = userSnapshot.child("Employer").child("Posts").getRef();
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


    public void extractPostInfo(DataSnapshot dataSnapshot, String postId) {
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            String snapshotKey = postSnapshot.getKey();
            if (snapshotKey != null && snapshotKey.equals(postId)) {
                String jobName = postSnapshot.child("jobName").getValue(String.class);
                String timePosted = "Time posted: " + postSnapshot.child("timePosted").getValue(String.class);
                String jobCategory = postSnapshot.child("jobCategory").getValue(String.class);
                String description = "Description:\n\n" +postSnapshot.child("jobDescription").getValue(String.class);
                String payment = "Wage: "+ postSnapshot.child("jobPayment").getValue(String.class) + "$";
                String imageUrl = postSnapshot.child("image").getValue(String.class);
                imageUrl.replace("content://com.android.providers.downloads.documents/document/", "");

                jobNameTextView.setText(jobName);
                timePostedTextView.setText(timePosted);
                jobCategoryTextView.setText(jobCategory);
                jobDescriptionTextView.setText(description);
                jobPaymentTextView.setText(payment);

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

}