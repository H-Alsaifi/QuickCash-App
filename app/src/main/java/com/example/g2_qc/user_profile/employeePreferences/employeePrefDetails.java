package com.example.g2_qc.user_profile.employeePreferences;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.g2_qc.R;
import com.example.g2_qc.greeting_page.greetingPage;
import com.example.g2_qc.login_page.demo_login_page;
import com.example.g2_qc.main_page.MainPageActivity;
import com.example.g2_qc.user_profile.personal_information.userDetails;
import com.example.g2_qc.user_profile.personal_information.userProfile;

public class employeePrefDetails extends AppCompatActivity {

    private EditText yrsExp;
    private EditText fieldOfWork;
    private EditText primaryLocation;
    private Button complete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_preferences_page);

        yrsExp = findViewById(R.id.yearsOfExp);
        fieldOfWork = findViewById(R.id.fow);
        primaryLocation = findViewById(R.id.primLocation);
        complete = findViewById(R.id.doneBtn);


        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(employeePrefDetails.this, MainPageActivity.class);
                startActivity(intent);
            }
        });

    }
}

