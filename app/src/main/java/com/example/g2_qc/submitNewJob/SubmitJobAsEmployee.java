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


public class SubmitJobAsEmployee extends AppCompatActivity {
    private static final String CHANNEL_ID = "my_channel";
    private static final String CHANNEL_NAME = "My Channel";
    private static NotificationManager notificationManager;

    private EditText jobName;
    private EditText jobDescription;
    private EditText jobPayment;
    private ImageView jobImage;
    private Button submitJobButton;
    private Spinner categoriesSpinner;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Users");
    private static final int REQUEST_CODE_OPEN_DOCUMENT = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_job_as_employee);

        jobName = findViewById(R.id.job_name_employee);
        jobDescription = findViewById(R.id.job_description_employee);
        jobPayment = findViewById(R.id.job_payment_employee);
        categoriesSpinner = findViewById(R.id.categories_spinner_employee);
        jobImage = findViewById(R.id.jobImage_employee);
        submitJobButton = findViewById(R.id.submit_job_employee);


        // Create the notification channel and manager
        createNotificationChannel();
        notificationManager = getSystemService(NotificationManager.class);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.JobsCategories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(adapter);
        jobImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_OPEN_DOCUMENT);
            }
        });

        submitJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = jobName.getText().toString();
                String description = jobDescription.getText().toString();
                String paymentStr = jobPayment.getText().toString();
                String category = categoriesSpinner.getSelectedItem().toString();

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

                Toast.makeText(SubmitJobAsEmployee.this, "Please Select Image", Toast.LENGTH_SHORT).show();
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

                    Post post = new Post(name, description, paymentStr, selectedImageUri.toString(), category);

                    String postID = root.push().getKey();

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("Employee").child("Posts").child(postID)
                            .setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SubmitJobAsEmployee.this, "Job Posted", Toast.LENGTH_SHORT).show();
                                        showNotification("New Post Added", "A new job has been posted by an employee.");
                                        Intent intent = new Intent(SubmitJobAsEmployee.this, MainPageActivity.class);
                                        intent.putExtra("fragment", "employee");
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(SubmitJobAsEmployee.this, "Error posting job", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    UploadTask uploadTask = imagesRef.putFile(selectedImageUri);
                }
            });
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("My Channel Description");
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void showNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Check if notifications are enabled
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            Intent intent = new Intent(this, MainPageActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            builder.setContentIntent(pendingIntent);

            int notificationId = (int) System.currentTimeMillis();
            notificationManager.notify(notificationId, builder.build());
        } else {
            // Notifications are disabled, show a toast message instead
            Toast.makeText(this, "Notifications are disabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void addPostListener() {
        DatabaseReference postsReference = FirebaseDatabase.getInstance().getReference("posts");
        postsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // Get the post details
                    String title = postSnapshot.child("title").getValue(String.class);
                    String message = postSnapshot.child("message").getValue(String.class);

                    // Show the notification
                    showNotification(title, message);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

}
