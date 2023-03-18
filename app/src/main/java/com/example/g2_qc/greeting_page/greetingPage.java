package com.example.g2_qc.greeting_page;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.g2_qc.R;
import com.example.g2_qc.main_page.MainPageActivity;

import com.example.g2_qc.user_profile.userDetails;
import com.example.g2_qc.user_profile.userProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class greetingPage extends AppCompatActivity {

    private TextView welcomeUserText;
    private TextView greetText;
    private Button getStarted;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greeting_page_layout);

        greetText = findViewById(R.id.greetingText);
        welcomeUserText = findViewById(R.id.welcomeUser);
        getStarted = findViewById(R.id.getStartedButtom);
        root = FirebaseDatabase.getInstance().getReference("Users");

        root.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails userDetails = snapshot.getValue(userDetails.class);
                if(userDetails != null){
                    String fName = userDetails.firstName;
                    welcomeUserText.setText("Welcome, " + fName + "!");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(greetingPage.this, "You are welcome whomever you are!", Toast.LENGTH_LONG).show();
            }
        });


        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(greetingPage.this, MainPageActivity.class);
                startActivity(intent);
            }
        });

    }
}



