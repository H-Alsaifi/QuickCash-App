package com.example.g2_qc.main_page.ui.Employer;

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
import com.example.g2_qc.databinding.FragmentEmployerBinding;
import com.example.g2_qc.display_details.display_details;
import com.example.g2_qc.submitNewJob.Post;
import com.example.g2_qc.submitNewJob.SubmitJobAsEmployer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class EmployerFragment extends Fragment {
    private SearchView searchView;

    private FragmentEmployerBinding binding;

    // Inflate the fragment's layout
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEmployerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;

    }

    // Clear the binding when the view is destroyed
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Initialize the fragment's UI components and add event listeners
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
                    DatabaseReference postsReference = userSnapshot.child("Employer").child("Posts").getRef();
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

        // Add a listener for the search view
        searchView = getView().findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // Filter the posts in the scroll view based on the search query
            @Override
            public boolean onQueryTextChange(String newText) {
                filterPosts(newText.trim());
                return true;
            }
        });

        // Add a listener for the "Add New Post" button
        addNewPostEmployer(view);
    }

    // Populate the scroll view with posts retrieved
    public void populateScrollView(DataSnapshot dataSnapshot) {
        LinearLayout linearLayout = getView().findViewById(R.id.linear_layout);

        // Iterate over the posts and create a box view for each one
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

            // Load the image for the post and set it as the background of the box view
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

    // Add a listener for the "Add New Post" button
    public void addNewPostEmployer(View view) {
        Button addNewPostButton = view.findViewById(R.id.add_new_post);
        addNewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubmitJobAsEmployer.class);
                startActivity(intent);
            }
        });
    }

    // Filter the posts in the scroll view based on a search query
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
