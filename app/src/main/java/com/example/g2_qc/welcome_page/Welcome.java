package com.example.g2_qc.welcome_page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.g2_qc.R;
import com.example.g2_qc.login_page.login_page;
import com.example.g2_qc.signup_page.signup;

public class Welcome extends AppCompatActivity {
    private Button signInButton;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        TextView welcomeMessage = findViewById(R.id.welcome_message);
        welcomeMessage.setText("Join our community, which includes many jobs and qualified workers in various fields");

        // To move from welcome page to the sign-in page
        signInButton = (Button) findViewById(R.id.button1);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, login_page.class);
                startActivity(intent);
            }
        });

        // To move from welcome page to the sign-up page
        createAccountButton = (Button) findViewById(R.id.button2);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, signup.class);
                startActivity(intent);
            }
        });
    }
}