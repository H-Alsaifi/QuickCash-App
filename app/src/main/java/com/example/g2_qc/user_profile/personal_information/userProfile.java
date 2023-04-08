package com.example.g2_qc.user_profile.personal_information;

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
import com.example.g2_qc.employeePreferences.employeePrefDetails;
import com.example.g2_qc.login_page.demo_login_page;
import com.example.g2_qc.user_profile.job_details.jobDetails;
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
    private Button my_jobs, my_personal_p, update;
    private FirebaseAuth authProfile;
    private String firstName, lastName, email, age;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        my_jobs = findViewById(R.id.jobs);
        my_personal_p = findViewById(R.id.personal_p);
        update = findViewById(R.id.update);

        my_jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userProfile.this, jobDetails.class);
                startActivity(intent);
            }
        });


        my_personal_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(userProfile.this, "Error: we could not complete your request", Toast.LENGTH_LONG).show();
            }
        });
    }
}