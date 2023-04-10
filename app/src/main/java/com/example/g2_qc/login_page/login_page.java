package com.example.g2_qc.login_page;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.g2_qc.R;
import com.example.g2_qc.forgot_password.forgot_password_page;
import com.example.g2_qc.greeting_page.greetingPage;
import com.example.g2_qc.signup_page.signup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 Represents a demo login page activity.
 */
public class login_page extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword;
    private TextView tvCreateAccount;
    FirebaseAuth mAuth;

    /**
     Called when the activity is starting.
     @param savedInstanceState The saved instance state Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_layout);

        // Initialize views
        initializeViews();

        // Initialize Firebase authentication
        mAuth = FirebaseAuth.getInstance();

        // Set click listener for login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                // Validate inputs
                validateInputs(emailAddress, password);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToForgotPass();
            }
        });

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSignUp();
            }
        });
    }

    /**
     Initialize views.
     */
    private void initializeViews() {
        etEmail = findViewById(R.id.et_email_address);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvForgotPassword.setPaintFlags(tvForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvCreateAccount = findViewById(R.id.tv_create_account);
        tvCreateAccount.setPaintFlags(tvCreateAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    /**
     Validate inputs.
     @param emailAddress The email address
     @param password The password
     */
    private void validateInputs(String emailAddress, String password) {
        if (emailAddress.isEmpty()) {
            etEmail.setError("Email Address is required");
        }
        else if (password.isEmpty()) {
            etPassword.setError("Password is required");
        }
        else {
            authenticateUser(emailAddress, password);
        }
    }

    /**
     Authenticate the user with Firebase.
     @param emailAddress The email address
     @param password The password
     */
    private void authenticateUser(String emailAddress, String password) {
        mAuth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(login_page.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, show a Toast message and go to the next activity
                            Toast.makeText(login_page.this, "Login successful", Toast.LENGTH_SHORT).show();
                            goToGreetingPage();
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(login_page.this, "Incorrect email address or password.\nPlease try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     Go to the greeting page activity.
     */
    private void goToGreetingPage() {
        Intent intent = new Intent(login_page.this, greetingPage.class);
        startActivity(intent);
    }

    /**
     * Go to Forgot Pass page.
     */
    private void goToForgotPass() {
        Intent intent = new Intent(login_page.this, forgot_password_page.class);
        startActivity(intent);
    }

    /**
     * Go to sign up page.
     */
    private void goToSignUp() {
        Intent intent = new Intent(login_page.this, signup.class);
        startActivity(intent);
    }
}


