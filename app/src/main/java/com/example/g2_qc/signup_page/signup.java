package com.example.g2_qc.signup_page;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.g2_qc.R;
import com.example.g2_qc.login_page.demo_login_page;
import com.example.g2_qc.welcome_page.Welcome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class signup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText fName;
    private EditText lName;
    private EditText age;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;

    private EditText experience;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        mAuth = FirebaseAuth.getInstance();

        Button instructionsButton = findViewById(R.id.instructionsButton);
        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email_address);
        experience = findViewById(R.id.experience);
        password = findViewById(R.id.setPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        Button signUpBtn = findViewById(R.id.signup_button);
        TextView back = findViewById(R.id.backToWelcomePage);

        instructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(signup.this)
                        .setTitle("How to Join!")
                        .setMessage("1- Enter your first and last name(just letters). \n\n" +
                                "2- Enter your Age.\n\n" +
                                "3- Please enter a valid email address.\n" +
                                "\t\t\t\t-ex: abc123@gmail.com\n\n" +
                                "4- Your password must include: \n" +
                                "\t\t* At least 1 number\n" +
                                "\t\t* At least 1 special character:\n" +
                                "\t\t\t\t-ex: (!,@,#,$,%,^,&,*,.)\n" +
                                "\t\t* 8 characters with at least one:\n"+
                                "\t\t\t\tA- uppercase letter.\n"+
                                "\t\t\t\tB- lowercase letter.\n")
                        .setPositiveButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToWelcomePage = new Intent(signup.this, Welcome.class);
                startActivity(backToWelcomePage);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the input values
                String firstName = fName.getText().toString();
                String lastName = lName.getText().toString();
                String userAge = age.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                String userConfirmPassword = confirmPassword.getText().toString();
                String userExp = experience.getText().toString();

                // Check if any fields are empty
                if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
                        TextUtils.isEmpty(userAge) || TextUtils.isEmpty(userEmail) ||TextUtils.isEmpty(userExp)||
                        TextUtils.isEmpty(userPassword) || TextUtils.isEmpty(userConfirmPassword)) {
                    Toast.makeText(signup.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //check if the first name contains any symbols
                if (!isValidFirstName(firstName)) {
                    Toast.makeText(signup.this, "Please enter a valid first name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //check if the last name contains any symbols
                if (!isValidLastName(lastName)) {
                    Toast.makeText(signup.this, "Please enter a valid last name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if Age is valid
                if (!isValidAge(userAge)) {
                    Toast.makeText(signup.this, "Please enter a valid Age.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if email is valid
                if (!isValidEmail(userEmail)) {
                    Toast.makeText(signup.this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check if the User Experience is valid.
                if (!isValidExperience(userExp,userAge)) {
                    Toast.makeText(signup.this, "Please enter a valid user experience.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if password is valid
                if (!isValidPassword(userPassword)) {
                    Toast.makeText(signup.this, "Invalid password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if password and confirm password fields match
                if (!userPassword.equals(userConfirmPassword)) {
                    Toast.makeText(signup.this, "Passwords don't match.", Toast.LENGTH_SHORT).show();
                    return;
                }


                // All input values are valid, create new account and go to login page
                // add data to firebase (email and password to auth database And the other data to realtime database)
                mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(firstName, lastName, userEmail, userAge, userExp);
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(signup.this, "Success", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(signup.this, "Failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(signup.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                //go back to to login data base
                Intent loginIntent = new Intent(signup.this, demo_login_page.class);
                startActivity(loginIntent);
                finish(); // Remove the sign-up activity from the back stack
            }
        });
    }

    /**
     * Checks if the first name is actually a valid one.
     * @param firstName user's firstNamw
     * @return returns a boolean after validating.
     */
    public boolean isValidFirstName(String firstName){
        String firstNameRegex = "^[A-Za-z][A-Za-z]+$";
        return firstName.matches(firstNameRegex);
    }

    /**
     * Checks if the last name is actually a valid one.
     * @param lastName user's lastName
     * @return returns a boolean after validating.
     */
    public boolean isValidLastName(String lastName){
        String lastNameRegex = "^[A-Za-z][A-Za-z]+( ?[A-Za-z][A-Za-z]+)?$";
        return lastName.matches(lastNameRegex);
    }

    /**
     * Checks if the user age is actually a valid one.
     * @param userAge user's age
     * @return returns a boolean after validating.
     */
    public boolean isValidAge(String userAge) {
        try {
            int ageInt = Integer.parseInt(userAge);
            if (ageInt <= 0) {
                return false;
            }
        }
        catch (NumberFormatException e) {
            Toast.makeText(signup.this, "Please enter a valid age.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Checks if the email is actually a valid one.
     * @param email user's email
     * @return returns a boolean after validating.
     */
    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    public boolean isValidExperience(String experience, String age) {
        try {
            int exp = Integer.parseInt(experience);
            int userAge = Integer.parseInt(age);


            if (exp > userAge ) {
                return false;
            }
        }
        catch (NumberFormatException e) {
            Toast.makeText(signup.this, "Please enter a valid experience.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    /**
     * Checks if the  password is actually a valid one.
     * @param password user's password
     * @return returns a boolean after validating.
     */
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }

        // Check if password is at least 8 characters long
        if (password.length() < 8) {
            return false;
        }

        // Check if password contains at least 1 digit
        if (!Pattern.compile(".*\\d.*").matcher(password).matches()) {
            return false;
        }

        // Check if password contains at least 1 special character
        if (!Pattern.compile(".*[!@#$%^&*\\.].*").matcher(password).matches()) {
            return false;
        }

        //at least an lowercase letter
        if (!password.matches("^(?=.*[a-z]).+$")) {
            return false;
        }

        //at least an uppercase letter
        if (!password.matches("^(?=.*[A-Z]).+$")) {
            return false;
        }
        return true;
    }

}
