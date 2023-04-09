package com.example.g2_qc.forgot_password;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.g2_qc.R;
import com.example.g2_qc.login_page.login_page;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 Represents a page that allows users to reset their password if they have forgotten it.
 */
public class forgot_password_page extends AppCompatActivity {
    private EditText etEmail;
    private Button btnSubmit;
    private FirebaseAuth mAuth;

    /**
     Initializes the forgot password page, sets up event listeners for the submit and back buttons,
     and retrieves the email edit text and submit button from the layout.
     @param savedInstanceState Saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_page);

        // Get references to the email and submit button views
        etEmail = findViewById(R.id.et_email_address);
        btnSubmit = findViewById(R.id.btn_submit);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Set up event listener for the submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePasswordReset();
            }
        });

        // Set up event listener for the back button
        Button btnBackToLogin = findViewById(R.id.btn_back_to_login);
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToLoginPage();
            }
        });
    }

    /**
     Handles the password reset process when the submit button is clicked.
     */
    private void handlePasswordReset() {
        String email = etEmail.getText().toString().trim();

        // Validate email address
        if (email.isEmpty()) {
            etEmail.setError("Email is required");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
        } else {
            // Send password reset email to the user
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        showSuccessMessage(email);
                    } else {
                        showErrorMessage();
                    }
                }
            });
        }
    }

    /**
     Navigates the user back to the login page.
     */
    private void navigateToLoginPage() {
        Intent intent = new Intent(this, login_page.class);
        startActivity(intent);
        finish();
    }

    /**
     Displays a success message to the user after a password reset email has been sent successfully.
     @param email The email address that the password reset email was sent to.
     */
    private void showSuccessMessage(String email) {
        Toast.makeText(this, "Code has been sent to " + email + " successfully", Toast.LENGTH_LONG).show();
        navigateToLoginPage();
    }

    /**
     Displays an error message to the user if there was an error sending the password reset email.
     */
    private void showErrorMessage() {
        Toast.makeText(this, "Try again! Wrong email was entered!", Toast.LENGTH_LONG).show();
    }
}