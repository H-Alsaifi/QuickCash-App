package com.example.g2_qc.user_profile.personal_information;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.g2_qc.R;
import com.example.g2_qc.databinding.FragmentProfileBinding;
import com.example.g2_qc.user_profile.History.MyHistoryActivity;
import com.example.g2_qc.user_profile.employeePreferences.employeePrefDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private TextView textViewWelcome, textViewEmail ;
    private EditText textViewFirstName, textViewLastName, textViewAge;
    private ProgressBar progressBar;
    private Button my_history, my_personal_p, update, rateYourExperienceButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        my_history = root.findViewById(R.id.jobs);
        my_personal_p = root.findViewById(R.id.personal_p);
        update = root.findViewById(R.id.update);
        rateYourExperienceButton = root.findViewById(R.id.rate);
        my_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyHistoryActivity.class);
                startActivity(intent);
            }
        });
        rateYourExperienceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RatingExperience.class);
                startActivity(intent);
            }
        });

        my_personal_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), employeePrefDetails.class);
                startActivity(intent);
            }
        });

        textViewWelcome = root.findViewById(R.id.textView_show_welcome);
        textViewFirstName = root.findViewById(R.id.textView_show_first_name);
        textViewLastName = root.findViewById(R.id.textView_show_last_name);
        textViewEmail = root.findViewById(R.id.textView_show_email);
        textViewAge = root.findViewById(R.id.textView_show_age);
        progressBar = root.findViewById(R.id.progressBar);

        FirebaseAuth authProfile = FirebaseAuth.getInstance();
        FirebaseUser user = authProfile.getCurrentUser();

        if(user == null) {
            Toast.makeText(getContext(), "user profile details not found", Toast.LENGTH_LONG).show();
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            showProfile(user);
        }

        update = root.findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = user.getUid();
                DatabaseReference profile_ref = FirebaseDatabase.getInstance().getReference("Users");

                profile_ref.child(ID).child("agePerson").setValue(textViewAge.getText().toString());
                profile_ref.child(ID).child("firstName").setValue(textViewFirstName.getText().toString());
                profile_ref.child(ID).child("lastname").setValue(textViewLastName.getText().toString());

                // Show a toast message to confirm that the update was successful
                Toast.makeText(getContext(), "Profile information updated successfully!", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showProfile(FirebaseUser firebaseUser) {
        String ID = firebaseUser.getUid();
        DatabaseReference profile_ref = FirebaseDatabase.getInstance().getReference("Users");
        profile_ref.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails userDetails = snapshot.getValue(userDetails.class);
                if (userDetails != null) {
                    String firstName = userDetails.firstName;
                    String lastName = userDetails.lastname;
                    String email = userDetails.emailAddress;
                    String age = userDetails.agePerson;

                    textViewWelcome.setText("Welcome " + firstName + "!");
                    textViewFirstName.setText(firstName);
                    textViewLastName.setText(lastName);
                    textViewAge.setText(age);
                    textViewEmail.setText(email);

                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: we could not complete your request", Toast.LENGTH_LONG).show();
            }
        });
    }
}
