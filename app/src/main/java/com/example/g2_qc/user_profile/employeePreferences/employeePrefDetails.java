package com.example.g2_qc.user_profile.employeePreferences;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.g2_qc.R;
import com.example.g2_qc.main_page.MainPageActivity;

/**
 * The employeePrefDetails class represents the activity that allows employees to enter their work preferences
 *  including years of experience, field of work and primary location. It also includes a button to complete the
 *  input and redirect to the main page activity.
 */
public class employeePrefDetails extends AppCompatActivity {

    private EditText yrsExp;
    private EditText fieldOfWork;
    private EditText primaryLocation;
    private Button complete;

    /**
     * This method is called when the activity is created. It initializes the UI components
     * and sets the listener for the "complete" button click event.
     * @param savedInstanceState the saved instance state
     */
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

