package com.example.g2_qc.user_profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.g2_qc.R;
import com.example.g2_qc.forgot_password.forgot_password_page;
import com.example.g2_qc.login_page.demo_login_page;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userProfile extends AppCompatActivity{

    private TextView textViewWelcome, textViewFirstName, textViewLastName, textViewEmail, textViewAge;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private String firstName, lastName, email, age;
    private TextView job, category, description, time, payment;
    String jobName, jobCategory, jobDescription, jobPayment, timePosted;
    private Button logout, my_jobs, my_personal_p, update;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        logout= findViewById(R.id.signout);
        my_jobs = findViewById(R.id.jobs);
        my_personal_p = findViewById(R.id.personal_p);
        update = findViewById(R.id.update);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userProfile.this, demo_login_page.class);
                startActivity(intent);
            }
        });

        my_jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userProfile.this, jobDetails.class);
                startActivity(intent);
            }
        });
        job = findViewById(R.id.textView_show_job_name);
        category = findViewById(R.id.textView_show_job_category);
        description = findViewById(R.id.textView_show_job_desc);
        payment = findViewById(R.id.textView_show_job_payment);
        time = findViewById(R.id.textView_show_time_posted);

        my_personal_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userProfile.this, userDetails.class);
                startActivity(intent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userProfile.this, userDetails.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setTitle("Homepage");

        textViewWelcome = findViewById(R.id.textView_show_welcome);
        textViewFirstName = findViewById(R.id.textView_show_first_name);
        textViewLastName = findViewById(R.id.textView_show_last_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewAge = findViewById(R.id.textView_show_age);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser user = authProfile.getCurrentUser();
        showProfile(user);

        if(user == null) {
            Toast.makeText(userProfile.this, "user profile details not found", Toast.LENGTH_LONG).show();
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            showProfile(user);
        }
    }

    private void showProfile(FirebaseUser firebaseUser) {
        String ID = firebaseUser.getUid();
        DatabaseReference profile_ref = FirebaseDatabase.getInstance().getReference("Users");
        profile_ref.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails userDetails = snapshot.getValue(userDetails.class);
                if (userDetails != null) {
                    firstName = userDetails.firstName;
                    lastName = userDetails.lastname;
                    email = userDetails.emailAddress;
                    age = userDetails.agePerson;

                    textViewWelcome.setText("Welcome " + firstName + "!");
                    textViewFirstName.setText(firstName);
                    textViewLastName.setText(lastName);
                    textViewAge.setText(age);
                    textViewEmail.setText(email);

                    progressBar.setVisibility(View.INVISIBLE);
                }

                jobDetails details = snapshot.getValue(jobDetails.class);
                if (details != null) {
                    jobName = details.jobName;
                    jobCategory = details.jobCategory;
                    jobDescription = details.jobDescription;
                    jobPayment = details.jobPayment;
                    timePosted = details.timePosted;

                    job.setText(jobName);
                    category.setText(jobCategory);
                    description.setText(jobDescription);
                    payment.setText(jobPayment);
                    time.setText(timePosted);
                }
            }

            public void update(View view) {
                if(first_name_changed() || last_name_changed() || age_changed()) {
                    Toast.makeText(userProfile.this, "Your data has been updated", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(userProfile.this, "Your data is already up to date", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(userProfile.this, "Error: we could not complete your request", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean age_changed() {
        if(!age.equals(textViewAge.getText().toString())) {
            DatabaseReference profile_ref = FirebaseDatabase.getInstance().getReference("Users");
            profile_ref.child("agePerson").setValue(textViewAge.getText().toString());
            return true;
        }
        else {
            return false;
        }
    }

    private boolean first_name_changed() {
        if(!firstName.equals(textViewFirstName.getText().toString())) {
            DatabaseReference profile_ref = FirebaseDatabase.getInstance().getReference("Users");
           profile_ref.child("firstName").setValue(textViewFirstName.getText().toString());
            return true;
        }
        else {
            return false;
        }
    }

    private boolean last_name_changed() {
        if(!lastName.equals(textViewLastName.getText().toString())) {
            DatabaseReference profile_ref = FirebaseDatabase.getInstance().getReference("Users");
            profile_ref.child("lastName").setValue(textViewLastName.getText().toString());
            return true;
        }
        else {
            return false;
        }
    }

}