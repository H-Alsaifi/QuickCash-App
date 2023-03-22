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
import com.example.g2_qc.main_page.MainPageActivity;
import com.example.g2_qc.main_page.ui.Employee.EmployeeFragment;
import com.example.g2_qc.main_page.ui.Employer.EmployerFragment;
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


public class SubmitJobAsEmployer extends AppCompatActivity {

    // Notification channel ID and name for sending notifications
    private static final String CHANNEL_ID = "my_channel";
    private static final String CHANNEL_NAME = "My Channel";
    private static NotificationManager notificationManager;

    // Declare views
    private EditText jobName;
    private EditText jobDescription;
    private EditText jobPayment;
    private ImageView jobImage;
    private Button submitJobButton;
    private Spinner categoriesSpinner;

    // Declare database reference
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Users");

    // Constant for image selection intent
    private static final int REQUEST_CODE_OPEN_DOCUMENT = 1;

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
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_OPEN_DOCUMENT);
            }
        });

        // Set up onClickListener for submitting job
        submitJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input from EditText fields
                String name = jobName.getText().toString();
                String description = jobDescription.getText().toString();
                String paymentStr = jobPayment.getText().toString();
                String category = categoriesSpinner.getSelectedItem().toString();


                // Check if job name, description, and payment have been entered
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

                // Show toast message prompting user to select image
                Toast.makeText(SubmitJobAsEmployer.this, "Please Select Image", Toast.LENGTH_SHORT).show();

            }
        });
    }

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
                    Post post = new Post(name, description, paymentStr, selectedImageUri.toString(), category, timePosted);

                    // Push the Post object to the database under the current user's Employer Posts
                    String postID = root.push().getKey();

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("Employer").child("Posts").child(postID)
                            .setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // If successful, display a notification and direct the user back to the main page
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SubmitJobAsEmployer.this, "Job Posted", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SubmitJobAsEmployer.this, MainPageActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If unsuccessful, display an error message
                                        Toast.makeText(SubmitJobAsEmployer.this, "Error posting job", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    // Upload the selected image to Firebase Storage
                    UploadTask uploadTask = imagesRef.putFile(selectedImageUri);
                }
            });
        }
    }

    // This method gets the current time and sets it to the Atlantic Canada time zone
    // It then formats the time to display as a string in the format "yyyy-MM-dd HH:mm"
    public String timePosted() {
        long currentTime = System.currentTimeMillis();
        TimeZone timeZone = TimeZone.getTimeZone("Canada/Atlantic");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormat.setTimeZone(timeZone);
        String timeString = dateFormat.format(new Date(currentTime));
        return timeString;
    }

}

