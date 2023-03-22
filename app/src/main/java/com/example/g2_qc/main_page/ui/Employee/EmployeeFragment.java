package com.example.g2_qc.main_page.ui.Employee;

import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

    // Inflate the fragment's layout
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEmployeeBinding.inflate(inflater, container, false);
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

        // Add a listener for the search view
        searchView = getView().findViewById(R.id.search_view);
        ImageView filterIcon = getView().findViewById(R.id.filter_icon);

        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // Filter the posts in the scroll view based on the search query
            @Override
            public boolean onQueryTextChange(String newText) {
                String query = newText.trim().toLowerCase();
                filterPosts(query);
                return true;
            }
        });

        // Add a listener for the "Add New Post" button
        addNewPostEmployee(view);
    }

    // Populate the scroll view with posts retrieved
    public void populateScrollView(DataSnapshot dataSnapshot) {
        LinearLayout linearLayout = getView().findViewById(R.id.linear_layout);

        // Iterate over the posts and create a box view for each one
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            String jobName = postSnapshot.child("jobName").getValue(String.class);
            String jobDescription = postSnapshot.child("jobDescription").getValue(String.class);
            String wage = postSnapshot.child("jobPayment").getValue(String.class);
            String imageUrl = postSnapshot.child("image").getValue(String.class);
            imageUrl.replace("content://com.android.providers.downloads.documents/document/", "");

            // Create a new box view
            View boxView = LayoutInflater.from(getContext()).inflate(R.layout.box_layout, null);

            // Set the job name and description as the text of the box view
            TextView textViewName = boxView.findViewById(R.id.box_title);
            TextView textViewDescription = boxView.findViewById(R.id.box_content);
            TextView textViewWage = boxView.findViewById(R.id.box_wage);

            textViewName.setText(TextUtils.ellipsize(jobName, (TextPaint) textViewName.getPaint(), 400, TextUtils.TruncateAt.END));
            textViewDescription.setText(TextUtils.ellipsize(jobDescription, (TextPaint) textViewDescription.getPaint(), 1000, TextUtils.TruncateAt.END));
            textViewWage.setText(wage+"$");

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

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.filter_dialog_title);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View filterView = inflater.inflate(R.layout.filter_dialog, null);
        builder.setView(filterView);

        TextView minCostTextView = filterView.findViewById(R.id.min_cost_number);
        SeekBar minCostSeekBar = filterView.findViewById(R.id.min_cost_seekbar);
        minCostSeekBar.setMax(1000);

        // Set the initial text value of the minimum cost TextView
        double initialMinCost = (double) minCostSeekBar.getProgress();
        minCostTextView.setText(String.valueOf(initialMinCost));

        // Add a listener to the minimum cost SeekBar to update the TextView in real-time
        minCostSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double minCost = (double) progress;
                minCostTextView.setText(String.valueOf(minCost));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        TextView maxCostTextView = filterView.findViewById(R.id.max_cost_number);
        SeekBar maxCostSeekBar = filterView.findViewById(R.id.max_cost_seekbar);
        maxCostSeekBar.setMax(1000);

        // Set the initial text value of the maximum cost TextView
        double initialMaxCost = (double) maxCostSeekBar.getProgress();
        maxCostTextView.setText(String.valueOf(initialMaxCost));

        // Add a listener to the maximum cost SeekBar to update the TextView in real-time
        maxCostSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double maxCost = (double) progress;
                maxCostTextView.setText(String.valueOf(maxCost));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        builder.setPositiveButton(R.string.filter_dialog_apply_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                double minCost = (double) minCostSeekBar.getProgress();
                double maxCost = (double) maxCostSeekBar.getProgress();
                filterPostsByCost(minCost, maxCost);
                dialog.dismiss();
            }
        });

        builder.setNeutralButton(R.string.filter_dialog_clear_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Make all box views visible when the "Clear Filter" button is clicked
                LinearLayout linearLayout = getView().findViewById(R.id.linear_layout);
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    View childView = linearLayout.getChildAt(i);
                    childView.setVisibility(View.VISIBLE);
                }
                dialog.dismiss();
            }
        });

        AlertDialog filterDialog = builder.create();
        filterDialog.show();
    }




    public void filterPostsByCost(double minCost, double maxCost) {
        LinearLayout linearLayout = getView().findViewById(R.id.linear_layout);

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View childView = linearLayout.getChildAt(i);

            if (childView instanceof View) {

                View boxView = (View) childView;
                TextView textViewName = boxView.findViewById(R.id.box_title);
                TextView textViewCost = boxView.findViewById(R.id.box_wage);

                if (Double.compare(minCost, 0) == 0 && Double.compare(maxCost, 100) == 0) {
                    // No filtering required
                    boxView.setVisibility(View.VISIBLE);
                } else {
                    String costString = textViewCost.getText().toString().trim();
                    costString = costString.replace("$", "");
                    double cost = 0.0;
                    try {
                        cost = Double.parseDouble(costString);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        continue; // Skip this box view if costString is not a valid number
                    }

                    if (cost >= minCost && cost <= maxCost) {
                        boxView.setVisibility(View.VISIBLE);
                    } else {
                        boxView.setVisibility(View.GONE);
                    }
                }
            }
        }
    }
}
