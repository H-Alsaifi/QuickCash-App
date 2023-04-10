package com.example.g2_qc.signup_page;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.example.g2_qc.greeting_page.greetingPage;
import com.example.g2_qc.login_page.login_page;
import com.example.g2_qc.welcome_page.Welcome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.LoginActivity;

import java.util.regex.Pattern;

/**
 * This class represents the Signup Activity. Users can sign up by filling out the form provided.
 */
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
                displayInstructions();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToWelcomePage();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUserAccount();
            }
        });
    }

    /**
     * Creates a new account for the user and saves the data to the Firebase database.
     */
    private void createUserAccount() {
        // Get the input values
        String firstName = fName.getText().toString();
        String lastName = lName.getText().toString();
        String userAge = age.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        String userConfirmPassword = confirmPassword.getText().toString();
        String userExp = experience.getText().toString();

        if (TextUtils.isEmpty(firstName)) {
            fName.setError("Please enter your first name");
            fName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(lastName)) {
            lName.setError("Please enter your last name");
            lName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(userAge)) {
            age.setError("Please enter your age");
            age.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(userEmail)) {
            email.setError("Please enter your email address");
            email.requestFocus();
            return;
        }

        if (!isValidEmail(userEmail)) {
            email.setError("Please enter a valid email address");
            email.requestFocus();
            return;
        }

        if (!isValidPassword(userPassword)) {
            password.setError("Please enter a password");
            password.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(userConfirmPassword)) {
            confirmPassword.setError("Please confirm your password");
            confirmPassword.requestFocus();
            return;
        }

        if (!userPassword.equals(userConfirmPassword)) {
            confirmPassword.setError("Passwords do not match");
            confirmPassword.requestFocus();
            return;
        }

        //Check if the User Experience is valid.
        if (!isValidExperience(userExp,userAge)) {
            Toast.makeText(signup.this, "Please enter a valid user experience.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user object with the input values
        User user = new User(firstName, lastName, userEmail, userAge, userExp);

        // upload the data to Firebase
        uploadUserData(user, userPassword);

        // Navigate back to the login activity
        Intent intent = new Intent(signup.this, login_page.class);
        startActivity(intent);
        finish();
    }

    /**
     This method is used to upload the user data to Firebase after validating the user's input values.
     The input parameters are the User object and the user's password.
     The method first creates a new account using the user's email address and password.
     If the account creation is successful, the user's data is then added to the Realtime Database.
     If both tasks are successful, a success message is displayed to the user using a Toast.
     If either task fails, an error message is displayed to the user using a Toast.
     @param user The User object containing the user's data.
     @param userPassword The user's password.
     */
    private void uploadUserData(User user, String userPassword) {
        // All input values are valid, create new account and go to login page
        // add data to firebase (email and password to auth database And the other data to realtime database)
        mAuth.createUserWithEmailAndPassword(user.emailAddress, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                // Show a success message to the user
                                Toast.makeText(signup.this, "Account created successfully!", Toast.LENGTH_LONG).show();
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
    }

    /**
     * Displays the instructions on how to sign up to the user.
     */
    private void displayInstructions() {
        new AlertDialog.Builder(this)
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

    /**
     * Navigates to the Welcome page.
     */
    private void navigateToWelcomePage() {
        Intent backToWelcomePage = new Intent(this, Welcome.class);
        startActivity(backToWelcomePage);
    }

    /**
     * Checks if the first name is actually a valid one.
     * @param firstName user's firstName
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
