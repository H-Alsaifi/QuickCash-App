package com.example.g2_qc.user_profile.personal_information;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.g2_qc.R;
import java.util.ArrayList;

public class RatingExperience extends AppCompatActivity {

    private Spinner roleSpinner;
    private ListView previousRatingsList;
    private Button addRating;
    private EditText ratingInput;
    private TextView emptyRatingsText;
    private ArrayList<String> ratings;
    private ArrayAdapter<String> ratingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_layout);

        roleSpinner = findViewById(R.id.role_spinner);
        previousRatingsList = findViewById(R.id.previous_ratings_list);
        addRating = findViewById(R.id.previous_ratings_button);
        ratingInput = findViewById(R.id.rating_input);
        emptyRatingsText = findViewById(R.id.empty_ratings_text);

        ratings = new ArrayList<>();
        ratingsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ratings);
        previousRatingsList.setAdapter(ratingsAdapter);

        addRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRating();
            }
        });

        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateHintForInput();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loadPreviousRatings();
    }

    private void loadPreviousRatings() {
        // Added some sample ratings
        ratings.add("Software Developer - 7");
        ratings.add("Project Manager - 9");
        ratingsAdapter.notifyDataSetChanged();
        updateListVisibility();
    }

    private void addNewRating() {
        String newRating = ratingInput.getText().toString().trim();

        if (!newRating.isEmpty()) {
            ratings.add(newRating);
            ratingsAdapter.notifyDataSetChanged();
            ratingInput.setText("");
            Toast.makeText(this, "New rating added successfully!", Toast.LENGTH_SHORT).show();
            updateListVisibility();
        } else {
            Toast.makeText(this, "Please enter the required details and rating.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateListVisibility() {
        if (ratings.isEmpty()) {
            previousRatingsList.setVisibility(View.GONE);
            emptyRatingsText.setVisibility(View.VISIBLE);
        } else {
            previousRatingsList.setVisibility(View.VISIBLE);
            emptyRatingsText.setVisibility(View.GONE);
        }
    }

    private void updateHintForInput() {
        if (roleSpinner.getSelectedItemPosition() == 0) {
            ratingInput.setHint("Job description - Rating (1-10)");
        } else {
            ratingInput.setHint("Employee name - Rating (1-10)");
        }
    }
}

