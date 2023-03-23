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
import com.example.g2_qc.greeting_page.greetingPage;
import com.example.g2_qc.forgot_password.forgot_password_page;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class demo_login_page extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword;
    private TextView tvCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_login_page);

        etEmail = findViewById(R.id.et_email_address);
        etPassword = findViewById(R.id.et_password);

        // for debug mode
//        etEmail.setText("uhruvb@gmail.com");
//        etPassword.setText("Adjita97!");


        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvForgotPassword.setPaintFlags(tvForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvCreateAccount = findViewById(R.id.tv_create_account);
        tvCreateAccount.setPaintFlags(tvCreateAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Initialize Firebase authentication
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (emailAddress.isEmpty()) {
                    etEmail.setError("Email Address is required");
                }
                else if (password.isEmpty()) {
                    etPassword.setError("Password is required");
                }
                else {
                    // Authenticate the user with Firebase
                    mAuth.signInWithEmailAndPassword(emailAddress, password)
                            .addOnCompleteListener(demo_login_page.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, show a Toast message and go to the next activity
                                    Toast.makeText(demo_login_page.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(demo_login_page.this, greetingPage.class);
                                    startActivity(intent);
                                }
                                else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(demo_login_page.this, "Incorrect email address or password.\nPlease try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            }
        });

        //This is the forgot password option that should be added to the main login page
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPasswordIntent = new Intent(demo_login_page.this, forgot_password_page.class);
                startActivity(forgotPasswordIntent);
            }
        });

        //To switch to SignUp page
        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPasswordIntent = new Intent(demo_login_page.this, com.example.g2_qc.signup_page.signup.class);
                startActivity(forgotPasswordIntent);
            }
        });
    }
}

