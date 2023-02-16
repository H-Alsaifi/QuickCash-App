package com.example.g2_qc.signup_page;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.g2_qc.R;
import com.example.g2_qc.forgot_password.forgot_password_page;
import com.example.g2_qc.login_page.demo_login_page;
import com.example.g2_qc.welcome_page.Welcome;

public class signup extends AppCompatActivity {
    private EditText fName;
    private EditText lName;
    private EditText age;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button signUp;
    private TextView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
        Button instructionsButton = findViewById(R.id.instructionsButton);
        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email_address);
        password = findViewById(R.id.setPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        signUp = findViewById(R.id.signup_button);
        back = findViewById(R.id.backToWelcomePage);
        instructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(signup.this, "How to Join!" +
                        "Add Your First and Last name" +
                        "Please add a valid email address. For example: abc123@gmail.com"
                        + "Your password must include: 8 characters" +
                        "An uppercase and lowercase letter" +
                        "At least 1 number" +
                        "At least 1 special character(!,@,#,$,%,^,&,*" +
                        "Enter your Age", Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(signup.this, Welcome.class);
                startActivity(back);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the input values
                String firstName = fName.getText().toString();
                String lastName = lName.getText().toString();
                String userAge = age.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                String userConfirmPassword = confirmPassword.getText().toString();

                // Check if any fields are empty
                if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
                        TextUtils.isEmpty(userAge) || TextUtils.isEmpty(userEmail) ||
                        TextUtils.isEmpty(userPassword) || TextUtils.isEmpty(userConfirmPassword)) {
                    Toast.makeText(signup.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if password and confirm password fields match
                if (!userPassword.equals(userConfirmPassword)) {
                    Toast.makeText(signup.this, "Passwords don't match.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if age is a valid number
                try {
                    int ageInt = Integer.parseInt(userAge);
                    if (ageInt <= 0) {
                        Toast.makeText(signup.this, "Please enter a valid age.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(signup.this, "Please enter a valid age.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if email is valid
                if (!isValidEmail(userEmail)) {
                    Toast.makeText(signup.this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                    return;
                }


                // All input values are valid, create new account and go to login page
                // Code to create new account goes here


                //add data to firebase (email and password to auth database And the other data to realtime database)








                //go back to to login data base
                Intent loginIntent = new Intent(signup.this, demo_login_page.class);
                startActivity(loginIntent);
                finish(); // Remove the sign-up activity from the back stack
            }
        });
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
}
