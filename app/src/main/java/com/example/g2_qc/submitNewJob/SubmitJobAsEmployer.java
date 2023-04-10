package com.example.g2_qc.submitNewJob;

import static android.content.ContentValues.TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.g2_qc.R;
import com.example.g2_qc.location.LocationDetails;
import com.example.g2_qc.main_page.MainPageActivity;
import com.example.g2_qc.main_page.ui.Employee.EmployeeFragment;
import com.example.g2_qc.main_page.ui.Employer.EmployerFragment;
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
import java.util.Date;
import java.util.TimeZone;

/**
 *  SubmitJobAsEmployer is an activity that allows employers to submit a new job.
 *  It consists of a form where the employer can enter the job details, select a job category, and upload an image.
 *  The activity also validates the input values entered by the user and displays error messages if necessary.
 *  When the employer submits the job, the activity sends a notification to all employees.
 */
public class SubmitJobAsEmployer extends AppCompatActivity {

    // Notification channel ID and name for sending notifications
    private static final String CHANNEL_ID = "my_channel";
    private static final String CHANNEL_NAME = "My Channel";
    private static NotificationManager notificationManager;

    LocationDetails location;

    // Declare views
    private EditText jobName;
    private EditText jobDescription;
    private EditText jobPayment;
    private ImageView jobImage;
    private Button submitJobButton;
    private Spinner categoriesSpinner;
    private FirebaseAuth authProfile = FirebaseAuth.getInstance();
    private DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference();

    // Declare database reference
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Users");

    // Constant for image selection intent
    private static final int REQUEST_CODE_OPEN_DOCUMENT = 1;

    /**
     * This method creates the SubmitJobAsEmployer activity and sets up the views and listeners.
     * It creates and sets up the spinner for job categories, sets up onClickListener for selecting job image
     * and onClickListener for submitting job.
     * @param savedInstanceState A Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_job_as_employer);

        // Find views by ID
        jobName = findViewById(R.id.job_name);
        jobDescription = findViewById(R.id.job_description);
        jobPayment = findViewById(R.id.job_payment);
        categoriesSpinner = findViewById(R.id.categories_spinner);
        jobImage = findViewById(R.id.jobImage);
        submitJobButton = findViewById(R.id.submit_job);

        // Create and set up the spinner for job categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.JobsCategories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(adapter);

        // Set up onClickListener for selecting job image
        jobImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        // Set up onClickListener for submitting job
        submitJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitJob();
            }
        });
    }

    /**
     * This method opens a file picker to select an image.
     *  It sets the type of the intent to image/* to only show image files.
     *  The selected image is then displayed in the ImageView on the screen.
     */
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_OPEN_DOCUMENT);
    }

    /**
     * This method is called when the submit job button is clicked.
     * It retrieves the input values entered by the user, validates them, and displays error messages if necessary.
     * If all fields are filled in, it creates a new Post object with the job information and adds it to the employer's post list in the Firebase database.
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

        // Display a toast message prompting the user to select an image
        if (jobImage.getDrawable() == null) {
            Toast.makeText(SubmitJobAsEmployer.this, "Please Select Image", Toast.LENGTH_SHORT);
        }
    }

    /**
     * Called when an activity launched for result returns, with the requestCode,
     * resultCode, and Intent data of the activity. This method handles the result of
     * opening a document and uploading an image to FirebaseStorage. It also creates
     * a new post object and adds it to the employer's posts in the Firebase Database.
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

        if (requestCode == REQUEST_CODE_OPEN_DOCUMENT && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imagesRef = storageRef.child("images/"
                    + selectedImageUri);

            jobImage.setImageURI(selectedImageUri);

            submitJobButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = jobName.getText().toString();
                    String description = jobDescription.getText().toString();
                    String paymentStr = jobPayment.getText().toString();
                    String category = categoriesSpinner.getSelectedItem().toString();
                    String timePosted = timePosted();

                    // Validate user input and display error messages if necessary
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

                    // Create a new Post object with the job information
                    Post post = new Post(name, description, paymentStr, selectedImageUri.toString(), category, timePosted, location);

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    userRef.child("location coordinates").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                LocationDetails locationDetails = snapshot.getValue(LocationDetails.class);
                                if (locationDetails != null) {
                                    post.setLocation(locationDetails);
                                }
                            }

                            String postId = userRef.child("Employer").child("Posts").push().getKey();
                            userRef.child("Employer").child("Posts").child(postId).setValue(post).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    HistoryDetails historyDetails = new HistoryDetails();
                                    historyDetails.addPostToHistory( profileRef , authProfile,  "postsAsEmployer");
                                    Toast.makeText(SubmitJobAsEmployer.this, "Job Posted", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SubmitJobAsEmployer.this, MainPageActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SubmitJobAsEmployer.this, "Error posting job", Toast.LENGTH_SHORT).show();
                                }
                            });

                            UploadTask uploadTask = imagesRef.putFile(selectedImageUri);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(SubmitJobAsEmployer.this, "Error uploading location", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    /**
     * This method retrieves the current time and sets it to the Atlantic Canada time zone.
     * It then formats the time to display as a string in the format "yyyy-MM-dd HH:mm".
     * @return a string representing the current time in the Atlantic Canada time zone in the format "yyyy-MM-dd HH:mm"
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

