package com.example.g2_qc.submitNewJob;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class SubmitJobAsEmployee extends AppCompatActivity {

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
                                    Toast.makeText(SubmitJobAsEmployee.this, "Job Posted", Toast.LENGTH_SHORT).show();
                                }
                            });
                    UploadTask uploadTask = imagesRef.putFile(selectedImageUri);
                }
            });
        }
    }

}
