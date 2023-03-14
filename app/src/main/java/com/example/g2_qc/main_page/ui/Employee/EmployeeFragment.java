package com.example.g2_qc.main_page.ui.Employee;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.g2_qc.R;
import com.example.g2_qc.databinding.FragmentEmployeeBinding;
import com.example.g2_qc.submitNewJob.SubmitJobAsEmployee;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EmployeeFragment extends Fragment {

    private FragmentEmployeeBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEmployeeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Call populateScrollView to populate the scroll view with existing posts
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    DatabaseReference postsReference = userSnapshot.child("Employee").child("Posts").getRef();
                    postsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            populateScrollView(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        addNewPostEmployee(view);
    }

    public void populateScrollView(DataSnapshot dataSnapshot) {
        LinearLayout linearLayout = getView().findViewById(R.id.linear_layout);

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            String jobName = postSnapshot.child("jobName").getValue(String.class);
            String jobDescription = postSnapshot.child("jobDescription").getValue(String.class);
            String imageUrl = postSnapshot.child("image").getValue(String.class);
            imageUrl.replace("content://com.android.providers.downloads.documents/document/", "");

            // Create a new box view
            View boxView = LayoutInflater.from(getContext()).inflate(R.layout.box_layout, null);

            // Set the job name as the text of the box view
            TextView textViewName = boxView.findViewById(R.id.box_title);
            TextView textViewDescription = boxView.findViewById(R.id.box_content);
            ImageView imageView = boxView.findViewById(R.id.box_image);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("images").child(imageUrl);

            storageRef.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(bitmap);
                }
            });

            textViewName.setText(TextUtils.ellipsize(jobName, (TextPaint) textViewName.getPaint(), 400, TextUtils.TruncateAt.END));
            textViewDescription.setText(TextUtils.ellipsize(jobDescription, (TextPaint) textViewDescription.getPaint(), 1000, TextUtils.TruncateAt.END));

            // Add the box view to the linear layout inside the scroll view
            linearLayout.addView(boxView);

        }

        // Scroll to the bottom of the scroll view
        ScrollView scrollView = getView().findViewById(R.id.scroll_view);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    public void addNewPostEmployee(View view) {
        Button addNewPostButton = view.findViewById(R.id.add_new_post);
        addNewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubmitJobAsEmployee.class);
                startActivity(intent);
            }
        });
    }
}