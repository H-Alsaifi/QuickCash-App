package com.example.g2_qc.employeePreferences;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.g2_qc.R;

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



    }
}
