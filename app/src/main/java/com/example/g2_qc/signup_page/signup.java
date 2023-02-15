package com.example.g2_qc.signup_page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


import com.example.g2_qc.R;
import com.example.g2_qc.forgot_password.forgot_password_page;
import com.example.g2_qc.login_page.demo_login_page;
import com.example.g2_qc.welcome_page.Welcome;

import androidx.annotation.Nullable;

public class signup extends AppCompatActivity {
    private EditText fName;
    private EditText LName;
    private EditText age;
    private EditText password;
    private EditText confirmPassword;
    private Button signUp;
    private TextView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        fName = findViewById(R.id.firstName);
        LName = findViewById(R.id.lastName);
        age = findViewById(R.id.age);
        password = findViewById(R.id.setPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        signUp = findViewById(R.id.signup_button);
        back = findViewById(R.id.backToWelcomePage);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(signup.this, Welcome.class);
                startActivity(back);
            }
        });
    }
}
