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
import com.example.g2_qc.main_page.MainPageActivity;
import com.example.g2_qc.main_page.ui.Employee.EmployeeFragment;
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


public class SubmitJobAsEmployee extends AppCompatActivity {

    // Notification channel constants
    private static final String CHANNEL_ID = "my_channel";
    private static final String CHANNEL_NAME = "My Channel";
    private static NotificationManager notificationManager;

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
                // Create an intent to open a file picker for images
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_OPEN_DOCUMENT);
            }
        });

        submitJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                Toast.makeText(SubmitJobAsEmployee.this, "Please Select Image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // This method is called when the user has selected an image from their device's storage
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
                    Post post = new Post(name, description, paymentStr, selectedImageUri.toString(), category, timePosted);

                    // Generate a unique ID for the post and save it to the database
                    String postID = root.push().getKey();

                    // Set a listener for when the post has been saved to the database
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("Employee").child("Posts").child(postID)
                            .setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        HistoryDetails historyDetails = new HistoryDetails();
                                        historyDetails.addPostToHistory( profileRef , authProfile,  "postsAsEmployee");
                                        Toast.makeText(SubmitJobAsEmployee.this, "Job Posted", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SubmitJobAsEmployee.this, MainPageActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(SubmitJobAsEmployee.this, "Error posting job", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    // Upload the selected image to Firebase Storage
                    UploadTask uploadTask = imagesRef.putFile(selectedImageUri);
                }
            });
        }
    }

    //add the time that the post is posted at
    public String timePosted() {
        long currentTime = System.currentTimeMillis();
        TimeZone timeZone = TimeZone.getTimeZone("Canada/Atlantic");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormat.setTimeZone(timeZone);
        String timeString = dateFormat.format(new Date(currentTime));
        return timeString;
    }
}