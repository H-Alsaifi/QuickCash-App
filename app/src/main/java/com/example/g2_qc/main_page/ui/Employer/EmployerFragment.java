package com.example.g2_qc.main_page.ui.Employer;

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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EmployerFragment extends Fragment {
    private SearchView searchView;
    private FragmentEmployerBinding binding;
    private boolean isDescendingOrder;


    /**
     Inflate the fragment's layout and return the inflated view.
     @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     @return The View for the fragment's UI, or null.
     */
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

        // Set up the filter icon and add a listener for it
        ImageView filterIcon = getView().findViewById(R.id.filter_icon_employer);
        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
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
                    String className = "EmployerFragment";
                    intent.putExtra("class", className);
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

                if (minCost > maxCost) {
                    Toast.makeText(getContext(), "Minimum cost cannot be greater than maximum cost", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isDescendingOrder) {
                    filterPostsByCost(minCost, maxCost);
                    filterPostsByCostDescending();

                } else {
                    filterPostsByCost(minCost, maxCost);
                    filterPostsByCostAscending();

                }

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

        Switch costSwitch = filterView.findViewById(R.id.filter_by_cost_switch);
        isDescendingOrder = costSwitch.isChecked(); // save initial state of switch
        costSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDescendingOrder = isChecked; // save current state of switch
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void filterPostsByCost(double minCost, double maxCost) {
        LinearLayout linearLayout = getView().findViewById(R.id.linear_layout);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View childView = linearLayout.getChildAt(i);
            TextView costTextView = childView.findViewById(R.id.box_wage);
            String costString = costTextView.getText().toString().replaceAll("\\$", "");
            double cost = Double.parseDouble(costString);

            if (cost < minCost || cost > maxCost) {
                childView.setVisibility(View.GONE);
            }else if (1000 == maxCost || cost < minCost){
                childView.setVisibility(View.GONE);

            }
            else {
                childView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void filterPostsByCostAscending() {
        LinearLayout linearLayout = getView().findViewById(R.id.linear_layout);
        ArrayList<View> childViews = new ArrayList<>();

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View childView = linearLayout.getChildAt(i);
            childViews.add(childView);
        }

        Collections.sort(childViews, new Comparator<View>() {
            @Override
            public int compare(View o1, View o2) {
                TextView costTextView1 = o1.findViewById(R.id.box_wage);
                String costString1 = costTextView1.getText().toString();
                costString1 = costString1.replace("$","");

                TextView costTextView2 = o2.findViewById(R.id.box_wage);
                String costString2 = costTextView2.getText().toString();
                costString2 = costString2.replace("$","");
                // Check if the cost strings are valid number format before parsing
                double cost1 = Double.parseDouble(costString1);
                double cost2 = Double.parseDouble(costString2);
                return Double.compare(cost1, cost2);

            }
        });

        for (View childView : childViews) {
            linearLayout.removeView(childView);
        }

        for (View childView : childViews) {
            linearLayout.addView(childView);
        }
    }
    private void filterPostsByCostDescending() {
        LinearLayout linearLayout = getView().findViewById(R.id.linear_layout);
        ArrayList<View> childViews = new ArrayList<>();

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View childView = linearLayout.getChildAt(i);
            childViews.add(childView);
        }

        Collections.sort(childViews, new Comparator<View>() {
            @Override
            public int compare(View o1, View o2) {
                TextView costTextView1 = o1.findViewById(R.id.box_wage);
                String costString1 = costTextView1.getText().toString();
                costString1 = costString1.replace("$","");
                double cost1 = Double.parseDouble(costString1);

                TextView costTextView2 = o2.findViewById(R.id.box_wage);
                String costString2 = costTextView2.getText().toString();
                costString2 = costString2.replace("$","");
                double cost2 = Double.parseDouble(costString2);

                return Double.compare(cost2, cost1);
            }
        });

        for (View childView : childViews) {
            linearLayout.removeView(childView);
        }

        for (View childView : childViews) {
            linearLayout.addView(childView);
        }
    }

}
