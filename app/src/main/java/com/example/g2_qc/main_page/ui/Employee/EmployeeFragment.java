package com.example.g2_qc.main_page.ui.Employee;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.g2_qc.R;
import com.example.g2_qc.databinding.FragmentEmployeeBinding;
import com.example.g2_qc.display_details.display_details;
import com.example.g2_qc.submitNewJob.Post;
import com.example.g2_qc.submitNewJob.SubmitJobAsEmployee;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class EmployeeFragment extends Fragment {
    private SearchView searchView;

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

        searchView = getView().findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPosts(newText.trim());
                return true;
            }
        });

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

            // Set the job name and description as the text of the box view
            TextView textViewName = boxView.findViewById(R.id.box_title);
            TextView textViewDescription = boxView.findViewById(R.id.box_content);
            textViewName.setText(TextUtils.ellipsize(jobName, (TextPaint) textViewName.getPaint(), 400, TextUtils.TruncateAt.END));
            textViewDescription.setText(TextUtils.ellipsize(jobDescription, (TextPaint) textViewDescription.getPaint(), 1000, TextUtils.TruncateAt.END));

            // Add an OnClickListener to the whole box view
            boxView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Retrieve the necessary information from the box view
                    String postId = postSnapshot.getKey();

                    // Pass the information to the display_details activity
                    Intent intent = new Intent(getActivity(), display_details.class);
                    intent.putExtra("postId", postId);
                    startActivity(intent);
                }
            });

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
    public void filterPosts(String query) {
        LinearLayout linearLayout = getView().findViewById(R.id.linear_layout);

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View childView = linearLayout.getChildAt(i);

            if (childView instanceof View) {

                View boxView = (View) childView;
                TextView textViewName = boxView.findViewById(R.id.box_title);

                if (textViewName.getText().toString().toLowerCase().contains(query.toLowerCase())) {
                    boxView.setVisibility(View.VISIBLE);

                } else {
                    boxView.setVisibility(View.GONE);
                }
            }
        }
    }

}