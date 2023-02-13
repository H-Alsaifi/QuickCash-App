package com.example.g2_qc.forgot_password;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.g2_qc.R;

public class reset_password_page extends AppCompatActivity {

    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_page);

        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnResetPassword = findViewById(R.id.btn_reset_password);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = etNewPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                if (newPassword.isEmpty()) {
                    etNewPassword.setError("New password is required");
                } else if (confirmPassword.isEmpty()) {
                    etConfirmPassword.setError("Confirm password is required");
                } else if (!newPassword.equals(confirmPassword)) {
                    etConfirmPassword.setError("Passwords do not match");
                } else {


                    // Add code here to reset the password and show a Toast message if the reset is successful



                }
            }
        });
        Button btnBackToLogin = findViewById(R.id.btn_back_to_login);
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(reset_password_page.this, demo_login_page.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
