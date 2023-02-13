package com.example.g2_qc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // button to go to demo login page (can be deleted later)
        Button btnGoToForgotPassword = findViewById(R.id.btn_go_to_demo_login_page);
        btnGoToForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.g2_qc.forgot_password.demo_login_page.class);
                startActivity(intent);
            }
        });
    }
}
