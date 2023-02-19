package com.example.g2_qc.user_profile;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class userProfile extends AppCompatActivity{

    private TextView textViewWelcome, textViewName, textViewEmail, textViewGender, textViewPhoneNumber, textViewAge;
    private ProgressBar progressBar;
    private ImageView imageView;
    private TextView textView = null;
    private FirebaseAuth authProfile;
    private String Name, email, age, gender, phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_page);

        getSupportActionBar().setTitle("Homepage");

        textViewWelcome = findViewById(R.id.textView_show_welcome);
        textViewName = findViewById(R.id.textView_show_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewGender = findViewById(R.id.textView_show_gender);
        textViewPhoneNumber = findViewById(R.id.textView_show_mobile);
        textViewAge = findViewById(R.id.textView_show_age);
        //progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser user = authProfile.getCurrentUser();

        showProfile(user);

//        if(user ==  null) {
//            Toast.makeText(userProfile.this, "user profile details not found", Toast.LENGTH_LONG).show();
//        }
//        else{
//            progressBar.setVisibility(View.VISIBLE);
//            showProfile(user);
//        }
    }

    private void showProfile(FirebaseUser firebaseUser) {
        String ID = firebaseUser.getUid();
        DatabaseReference profile_ref = FirebaseDatabase.getInstance().getReference("users registered");
        profile_ref.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails userDetails = snapshot.getValue(userDetails.class);
                if(userDetails != null) {
                    Name = firebaseUser.getDisplayName();
                    email = firebaseUser.getEmail();
                    gender = userDetails.gender;
                    age = userDetails.age;
                    phoneNumber = userDetails.phoneNumber;

                    textViewWelcome.setText("Welcome " + Name);
                    textViewName.setText(Name);
                    textViewAge.setText(age);
                    textViewEmail.setText(email);
                    textViewPhoneNumber.setText(phoneNumber);
                    textViewGender.setText(gender);
                }
//                progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(userProfile.this, "Error: we could not complete your request", Toast.LENGTH_LONG).show();
//                progressBar.setVisibility(View.GONE);
            }
        });
    }
}