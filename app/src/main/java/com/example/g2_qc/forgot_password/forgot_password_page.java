package com.example.g2_qc.forgot_password;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.g2_qc.R;

public class forgot_password_page extends AppCompatActivity {

    private EditText etEmail;
    private Button btnSubmit;
    private EditText etCode;
    private Button btnResetPassword;
    private int codeSentCounter = 0;
    private long lastCodeSentTime = 0;
    private final int CODE_SEND_LIMIT = 3;
    private final int CODE_SEND_INTERVAL = 60000; // in milliseconds (1 minute)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_page);

        etEmail = findViewById(R.id.et_email);
        btnSubmit = findViewById(R.id.btn_submit);
        etCode = findViewById(R.id.et_code);
        btnResetPassword = findViewById(R.id.btn_reset_password);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                if (email.isEmpty()) {
                    etEmail.setError("Email is required");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Enter a valid email address");
                }else {
                    // Add code here to send a password reset email to the user
                    // Generate the 6-digit code
                    int code = (int)(Math.random() * 1000000);
                    // Format the code as a 6-digit string
                    String formattedCode = String.format("%06d", code);

                    // Add code here to send an email to the user containing the 6-digit code

                    Toast.makeText(forgot_password_page.this, "Code sent to " + email, Toast.LENGTH_LONG).show();

                    // Show the code EditText and reset password button
                    etCode.setVisibility(View.VISIBLE);
                    btnResetPassword.setVisibility(View.VISIBLE);
                }
            }
        });
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = etCode.getText().toString();
                if (code.isEmpty()) {
                    etCode.setError("Code is required");
                } else {
                    // Check if the user has reached the code send limit within the interval
                    if (codeSentCounter >= CODE_SEND_LIMIT && System.currentTimeMillis() - lastCodeSentTime < CODE_SEND_INTERVAL) {
                        Toast.makeText(forgot_password_page.this, "You have reached the code send limit. Please try again later.", Toast.LENGTH_LONG).show();
                    } else {
                        Intent forgotPasswordIntent = new Intent(forgot_password_page.this, reset_password_page.class);
                        startActivity(forgotPasswordIntent);

                        // Increment the code sent counter and update the last code sent time
                        codeSentCounter++;
                        lastCodeSentTime = System.currentTimeMillis();
                    }
                }
            }
        });
        Button btnBackToLogin = findViewById(R.id.btn_back_to_login);
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(forgot_password_page.this, demo_login_page.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
