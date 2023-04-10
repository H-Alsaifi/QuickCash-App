package com.example.g2_qc.submitNewJob;

import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.g2_qc.R;
import com.example.g2_qc.location.LocationDetails;
import com.example.g2_qc.main_page.MainPageActivity;
import com.example.g2_qc.main_page.ui.Employee.EmployeeFragment;
import com.example.g2_qc.paypal_integration.ApplyActivity;
import com.example.g2_qc.user_profile.History.HistoryDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * This class is used for submitting a job as an employee to the Firebase Realtime Database.
 */
public class SubmitJobAsEmployee extends AppCompatActivity {

    // Notification channel constants
    private static final String CHANNEL_ID = "my_channel";
    private static final String CHANNEL_NAME = "My Channel";
    private static NotificationManager notificationManager;

    LocationDetails location;

    // UI elements
    private EditText jobName;
    private EditText jobDescription;
    private EditText jobPayment;
    private ImageView jobImage;
    private Button submitJobButton;
    private Spinner categoriesSpinner;
    private FirebaseAuth authProfile = FirebaseAuth.getInstance();
    private DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference();

    // Firebase database reference
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Users");
    private static final int REQUEST_CODE_OPEN_DOCUMENT = 1;

    /**
     * Initializes all UI elements and sets up the click listeners for buttons.
     * Also sets up the adapter for the categories spinner.
     * @param savedInstanceState The saved instance state of the app.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_job_as_employee);

        // Find UI elements by their IDs
        jobName = findViewById(R.id.job_name_employee);
        jobDescription = findViewById(R.id.job_description_employee);
        jobPayment = findViewById(R.id.job_payment_employee);
        categoriesSpinner = findViewById(R.id.categories_spinner_employee);
        jobImage = findViewById(R.id.jobImage_employee);
        submitJobButton = findViewById(R.id.submit_job_employee);

        // Create an adapter for the spinner and set it
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.JobsCategories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(adapter);

        jobImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        submitJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitJob();
            }
        });
    }

    /**
     * Opens an Intent to allow the user to select an image file from their device's storage.
     */
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_OPEN_DOCUMENT);
    }

    /**
     * Submits a job to the Firebase Realtime Database with the input values entered by the user
     * and the selected image URI. Displays an error message if any of the fields are empty or if the payment
     * amount is invalid.
     */
    private void submitJob() {
        // Get the input values entered by the user
        String name = jobName.getText().toString();
        String description = jobDescription.getText().toString();
        String paymentStr = jobPayment.getText().toString();
        String category = categoriesSpinner.getSelectedItem().toString();

        // Check if any of the fields are empty and display an error message if they are
        if (name.isEmpty()) {
            jobName.setError("Please enter a job name");
            jobName.requestFocus();
        }

        if (description.isEmpty()) {
            jobDescription.setError("Please enter a job description");
            jobDescription.requestFocus();
        }

        if (paymentStr.isEmpty()) {
            jobPayment.setError("Please enter a job payment");
            jobPayment.requestFocus();
        }


        // Check if payment amount is valid
        double paymentAmount = 0.0;
        try {
            paymentAmount = Double.parseDouble(paymentStr);
            if (paymentAmount <= 0) {
                jobPayment.setError("Please enter a valid job payment");
                jobPayment.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            jobPayment.setError("Please enter a valid job payment");
            jobPayment.requestFocus();
            return;
        }

        // Display a toast message prompting the user to select an image
        if (jobImage.getDrawable() == null) {
            Toast.makeText(SubmitJobAsEmployee.this, "Please Select Image", Toast.LENGTH_SHORT);
        }
    }

    /**
     * This method is called when the user has selected an image from their device's storage.
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is from selecting an image and if the result is OK
        if (requestCode == REQUEST_CODE_OPEN_DOCUMENT && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            // Get a reference to the Firebase Storage and create a reference to the images folder
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imagesRef = storageRef.child("images/"
                    + selectedImageUri);

            // Set the selected image as the job image
            jobImage.setImageURI(selectedImageUri);

            // Set a listener for when the submit job button is clicked
            submitJobButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the input values entered by the user
                    String name = jobName.getText().toString();
                    String description = jobDescription.getText().toString();
                    String paymentStr = jobPayment.getText().toString();
                    String category = categoriesSpinner.getSelectedItem().toString();
                    String timePosted = timePosted();

                    // Validate the input values
                    if (name.isEmpty()) {
                        jobName.setError("Please enter a job name");
                        jobName.requestFocus();
                        return;
                    }

                    else if (description.isEmpty()) {
                        jobDescription.setError("Please enter a job description");
                        jobDescription.requestFocus();
                        return;
                    }

                    else if (paymentStr.isEmpty()) {
                        jobPayment.setError("Please enter a job payment");
                        jobPayment.requestFocus();
                        return;
                    }

                    // Create a new Post object with the input values and the selected image URI
                    Post post = new Post(name, description, paymentStr, selectedImageUri.toString(), category, timePosted, location);

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    userRef.child("location coordinates").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                LocationDetails locationDetails = snapshot.getValue(LocationDetails.class);
                                if (locationDetails != null) {
                                    post.setLocation(locationDetails);
                                }
                            }

                            String postId = userRef.child("Employee").child("Posts").push().getKey();
                            userRef.child("Employee").child("Posts").child(postId).setValue(post).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    HistoryDetails historyDetails = new HistoryDetails();
                                    historyDetails.addPostToHistory( profileRef , authProfile,  "postsAsEmployee");
                                    Toast.makeText(SubmitJobAsEmployee.this, "Job Posted", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SubmitJobAsEmployee.this, ApplyActivity.class);
                                    intent.putExtra("wage",paymentStr);
                                    finish();
                                } else {
                                    Toast.makeText(SubmitJobAsEmployee.this, "Error posting job", Toast.LENGTH_SHORT).show();
                                }
                            });

                            UploadTask uploadTask = imagesRef.putFile(selectedImageUri);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(SubmitJobAsEmployee.this, "Error uploading location", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    /**
     * computes the current time in the Canada/Atlantic timezone as a formatted string.
     * @return a string representing the current time in the "yyyy-MM-dd HH:mm" format
     */
    public String timePosted() {
        long currentTime = System.currentTimeMillis();
        TimeZone timeZone = TimeZone.getTimeZone("Canada/Atlantic");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormat.setTimeZone(timeZone);
        String timeString = dateFormat.format(new Date(currentTime));
        return timeString;
    }
}