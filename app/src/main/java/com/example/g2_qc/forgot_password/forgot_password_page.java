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
import com.example.g2_qc.login_page.demo_login_page;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class forgot_password_page extends AppCompatActivity {

    private EditText etEmail;
    private Button btnSubmit;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_page);

        etEmail = findViewById(R.id.et_email);
        btnSubmit = findViewById(R.id.btn_submit);

        mAuth = FirebaseAuth.getInstance();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    etEmail.setError("Email is required");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Enter a valid email address");
                }else {
                    // Add code here to send a password reset email to the user

                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                    Toast.makeText(forgot_password_page.this, "Code has been sent to " + email + " successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(forgot_password_page.this, demo_login_page.class);
                            startActivity(intent);
                            finish();
                            }else{
                                Toast.makeText(forgot_password_page.this, "Try again! Wrong email was entered!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

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
