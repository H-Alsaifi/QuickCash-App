package com.example.g2_qc.display_details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.g2_qc.R;
import com.example.g2_qc.main_page.MainPageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class display_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_details);

        //String myId = FirebaseAuth.getInstance().getCurrentUser().getUid(); //user ID
        extractInfo("-NQckYWwbc3Hg_vjdtQm", "wFZqWpbGHQMoCWNxJala3ibWs523");
//        FirebaseDatabase db = FirebaseDatabase.getInstance("https://quickcash-group2-default-rtdb.firebaseio.com/");
//        DatabaseReference ref = db.getReference().child("users");

        Button btn = findViewById(R.id.backButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainPageActivity.class);
                startActivity(intent);
            }
        });





    }
    public void extractInfo(String postId, String UserUID) {
        DatabaseReference databaseReferenceEmployee = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(UserUID).child("Employee").child("Posts").getRef();
        databaseReferenceEmployee.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                extractPostInfo(dataSnapshot, postId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(display_details.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        DatabaseReference databaseReferenceEmployer = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(UserUID).child("Employee").child("Posts").getRef();
        databaseReferenceEmployer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                extractPostInfo(dataSnapshot, postId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(display_details.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void extractPostInfo(DataSnapshot dataSnapshot, String postId) {
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            if (postId.equals(postSnapshot.getKey())) {
                String jobName = postSnapshot.child("jobName").getValue(String.class);
                String timePosted = postSnapshot.child("timePosted").getValue(String.class);
                String jobCategory = postSnapshot.child("jobCategory").getValue(String.class);
                String description = postSnapshot.child("jobDescription").getValue(String.class);
                String payment = postSnapshot.child("jobPayment").getValue(String.class);
                //any other info

                TextView view = findViewById(R.id.display);
                view.setText("Job Name: " + jobName + "\nJob category: " + jobCategory + "\nJob description: " + description + "\nPayment: " + payment + "\nTime Posted: " + timePosted);


            }

        }

    }

}