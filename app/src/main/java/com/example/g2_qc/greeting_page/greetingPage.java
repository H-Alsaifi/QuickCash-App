package com.example.g2_qc.greeting_page;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.g2_qc.R;
import com.example.g2_qc.main_page.MainPageActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class greetingPage extends AppCompatActivity {

    private TextView welcomeUserText;
    private TextView greetText;
    private Button getStarted;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greeting_page_layout);

        greetText = findViewById(R.id.greetingText);
        welcomeUserText = findViewById(R.id.welcomeUser);
        getStarted = findViewById(R.id.getStartedButtom);
        firebaseAuth = FirebaseAuth.getInstance();


        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(greetingPage.this, MainPageActivity.class);
                startActivity(intent);
            }
        });
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String firstName = firebaseAuth.getCurrentUser().getDisplayName();
            welcomeUserText.setText("Welcome, " + firstName + "!");
        }
    }
}



