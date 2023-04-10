package com.example.g2_qc.user_profile.job_details;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.g2_qc.R;
import com.example.g2_qc.user_profile.personal_information.userDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyJobActivity extends AppCompatActivity {

    private TextView job, category, description, time, payment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_page);


        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        job = findViewById(R.id.textView_show_job_name);
        category = findViewById(R.id.textView_show_job_category);
        description = findViewById(R.id.textView_show_job_desc);
        payment = findViewById(R.id.textView_show_job_payment);
        time = findViewById(R.id.textView_show_time_posted);


        FirebaseAuth authProfile = FirebaseAuth.getInstance();
        FirebaseUser user = authProfile.getCurrentUser();

        String ID = user.getUid();

        DatabaseReference profile_ref = FirebaseDatabase.getInstance().getReference("Users");
        profile_ref.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails details = snapshot.getValue(userDetails.class);
                if (details != null) {
                    String jobName = details.jobName;
                    String jobCategory = details.jobCategory;
                    String jobDescription = details.jobDescription;
                    String jobPayment = details.jobPayment;
                    String timePosted = details.timePosted;

                    job.setText(jobName);
                    category.setText(jobCategory);
                    description.setText(jobDescription);
                    payment.setText(jobPayment);
                    time.setText(timePosted);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: we could not complete your request", Toast.LENGTH_LONG).show();
            }
        });
    }
}
