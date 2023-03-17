package com.example.g2_qc.main_page;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.g2_qc.R;

public class AnotherClass extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);

        Intent intent = getIntent();
        String jobName = intent.getStringExtra("dataSnapshot");
        String jobDescription = intent.getStringExtra("postSnapshot");
    }
}
