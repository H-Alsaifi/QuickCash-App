package com.example.g2_qc.main_page;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.g2_qc.R;
import com.example.g2_qc.databinding.ActivityMainBinding;
import com.example.g2_qc.display_details.display_details;
import com.example.g2_qc.location.LocationActivity;
import com.example.g2_qc.location.LocationDetails;
import com.example.g2_qc.login_page.login_page;
import com.example.g2_qc.signup_page.User;
import com.example.g2_qc.user_profile.History.HistoryDetails;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.ArrayList;

/**
 This class represents the main page of the application.
 It sets up the UI, navigation, and functionality for the main page.
 */
public class MainPageActivity extends AppCompatActivity {

    // Declare required variables
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private Button locationButton;
    private FirebaseAuth authProfile;
    private String email;
    private String location;
    private String category;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> list2 = new ArrayList<String>();

    /**
     Called when the activity is created. Sets up the UI, navigation, and functionality.
     @param savedInstanceState saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set the app bar as the action bar
        setSupportActionBar(binding.appBarMain.toolbar);

        // Add an onClickListener for the notification icon
        binding.appBarMain.notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call methods to show the notification and an alert dialog
                showNotification();
                showNotificationAlert();
            }
        });

        // Get a reference to the user's history node in the database
        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("History");

        // Check if the "History" node is null
        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) { // If the node doesn't exist, initialize it
                    HistoryDetails history = new HistoryDetails();
                    historyRef.setValue(history);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
            }
        });

        // Get references to the drawer and navigation view
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Build the app bar configuration with top level destinations
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_employee, R.id.nav_employer, R.id.nav_profile)
                .setOpenableLayout(drawer)
                .build();

        // Get the nav controller and set up the action bar and navigation view with it
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Get a reference to the location button and set its onClickListener
        locationButton = findViewById(R.id.location_button);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLocationButtonClick(view);
            }
        });

        // Get a reference to the FirebaseAuth object and the current user
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser user = authProfile.getCurrentUser();

        // Call a method to extract user info and enable notifications
        ExtractInfo(user);
        enableNotification();
    }

    /**
     * Called when the options menu is created.
     * Inflates the menu with the options menu layout.
     * Gets a reference to the logout menu item and sets its onClickListener.
     * @param menu The menu to be displayed.
     * @return true to display the options menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu with the options menu layout
        getMenuInflater().inflate(R.menu.main, menu);

        // Get a reference to the logout menu item and set its onClickListener
        MenuItem logoutItem = menu.findItem(R.id.action_logout);
        logoutItem.setOnMenuItemClickListener(item -> onOptionsItemSelected(item));

        // Return true to display the options menu
        return true;
    }

    /**
     * Delegates up navigation to the nav controller.
     * @return true if the navigation was handled by the nav controller, false otherwise.
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Extracts user info from the Firebase database.
     * Gets the user's ID and a reference to their profile info in the database.
     * Adds a listener to the profile info reference to get the user's email, location, and category.
     * Sets the email as the subtitle of the navigation view header and sets the text of the location button based on the user's location.
     * @param firebaseUser The FirebaseUser object representing the current user.
     */
    private void ExtractInfo(FirebaseUser firebaseUser) {
        if (firebaseUser == null) {
            // Handle the case where the FirebaseUser object is null
            return;
        }

        // Get the user's ID and a reference to their profile info in the database
        String myId = firebaseUser.getUid();
        DatabaseReference profile_ref = FirebaseDatabase.getInstance().getReference("Users");

        // Add a listener to the profile info reference to get the user's email, location, and category
        profile_ref.child(myId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userDetails = snapshot.getValue(User.class);
                if (userDetails != null) {
                    email = userDetails.emailAddress;
                    location = userDetails.location;
                    //category = userDetails.category;

                    // Set the email as the subtitle of the navigation view header
                    NavigationView navigationView = findViewById(R.id.nav_view);
                    View headerView = navigationView.getHeaderView(0);
                    TextView subtitleTextView = headerView.findViewById(R.id.nav_header_subtitle);
                    subtitleTextView.setText(email);

                    // Set the text of the location button based on the user's location
                    if (location.isEmpty()) {
                        locationButton.setText("Choose location");
                    } else {
                        locationButton.setText(" "+location);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Display an error message if the database read was cancelled
                Toast.makeText(MainPageActivity.this, "Error: we could not complete your request", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     This method handles options menu item selection.
     @param item The selected menu item.
     @return boolean Returns true if the event has been handled, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            // If the logout menu item was selected, sign the user out of Firebase
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();

            // Navigate back to the login activity
            Intent intent = new Intent(this, login_page.class);
            startActivity(intent);
            finish();

            // Return true to indicate that the event has been handled
            return true;
        } else if (id == R.id.action_Refresh) {
            // Perform the refresh operation here
            // For example, you can reload the current activity/fragment by recreating it
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            Toast.makeText(this, "Refresh successful", Toast.LENGTH_SHORT).show();

            // Return true to indicate that the event has been handled
            return true;
        }

        // Return the result of the super class method
        return super.onOptionsItemSelected(item);
    }

    /**
     This method launches the location activity.
     @param view The view that was clicked.
     */
    public void onLocationButtonClick(View view) {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }

    /**
     This method enables notifications for the app.
     */
    public void enableNotification() {
        // Get a reference to the notification manager and create a notification channel if the device is running Android O or higher
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null) {
            NotificationChannel channel = new NotificationChannel("my_channel", "My Channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);

            // Display an alert dialog if the channel's importance level is set to none
            if (notificationManager.getNotificationChannel(channel.getId()).getImportance() == NotificationManager.IMPORTANCE_NONE) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Notifications Disabled");
                builder.setMessage("Please enable notifications to receive job updates");
                builder.setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Launch the notification settings activity to allow the user to enable notifications for the app
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");

                        // Add extra data to the intent to specify the app package and UID
                        intent.putExtra("app_package", getPackageName());
                        intent.putExtra("app_uid", getApplicationInfo().uid);

                        // Add extra data to the intent for devices running Android O and above
                        intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());

                        // Start the notification settings activity
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // Show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }



    /**
     Displays a notification alert for new job postings by fetching data from Firebase Realtime Database and
     checking for matching job categories.
     */
    public void showNotification(){
    // showNotification method - displays a notification alert for new job postings
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        // Get a reference to the Users node in the database and add a listener to get all users
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * Callback method triggered when data is changed in the database.
             * Loops through all users in the database and fetches their job postings.
             * @param dataSnapshot the data snapshot of the Users node
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Loop through all users in the database
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();

                    // Get a reference to the user's Employee Posts and add a listener to get all job postings
                    DatabaseReference postsReferenceEmployee = userSnapshot.child("Employee").child("Posts").getRef();
                    postsReferenceEmployee.addListenerForSingleValueEvent(new ValueEventListener() {
                        /**
                         * Callback method triggered when data is changed in the database.
                         * Checks each job posting for a matching job category and adds it to the list of jobs if it matches.
                         * @param dataSnapshot the data snapshot of the Employee Posts node
                         */
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Check each job posting for a matching job category and add it to the list of jobs if it matches
                            notificationCheck(dataSnapshot, userSnapshot.child("location coordinates").getValue(LocationDetails.class));

                            // Dismiss the progress dialog when all job postings have been checked
                            progressDialog.dismiss();
                        }

                        /**
                         * Callback method triggered when database read is cancelled.
                         * Displays an error message if the database read was cancelled.
                         * @param databaseError the database error that caused the read to be cancelled
                         */
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Display an error message if the database read was cancelled
                            progressDialog.dismiss();
                            Toast.makeText(MainPageActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Get a reference to the user's Employer Posts and add a listener to get all job postings
                    DatabaseReference postsReferenceEmployer = userSnapshot.child("Employer").child("Posts").getRef();
                    postsReferenceEmployer.addListenerForSingleValueEvent(new ValueEventListener() {
                        /**
                         * Callback method triggered when data is changed in the database.
                         * Checks each job posting for a matching job category and adds it to the list of jobs if it matches.
                         * @param dataSnapshot the data snapshot of the Employer Posts node
                         */
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Check each job posting for a matching job category and add it to the list of jobs if it matches
                            notificationCheck(dataSnapshot, userSnapshot.child("location coordinates").getValue(LocationDetails.class));

                            // Dismiss the progress dialog when all job postings have been checked
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Display an error message if the database read was cancelled
                            progressDialog.dismiss();
                            Toast.makeText(MainPageActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Display an error message if the database read was cancelled
                progressDialog.dismiss();
                Toast.makeText(MainPageActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     This method checks if a job posting matches the user's job category and adds it to the list of jobs if it does.
     It also calculates the distance between the job's location and the user's location, and adds the job to the
     list if it's within 10KM of the user's location. If there are no new job postings in the user's job category,
     it displays an alert dialog indicating so.
     @param dataSnapshot - a snapshot of the job postings data in the database
     @param userLocation - the user's current location
     */
    public void notificationCheck(DataSnapshot dataSnapshot, LocationDetails userLocation) {

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

            String jobName = postSnapshot.child("jobName").getValue(String.class);
            String timePosted = postSnapshot.child("timePosted").getValue(String.class);
            String jobCategory = postSnapshot.child("jobCategory").getValue(String.class);

            Double jobLatitude = postSnapshot.child("location").child("latitude").getValue(Double.class);
            Double jobLongitude = postSnapshot.child("location").child("longitude").getValue(Double.class);

            if (jobLatitude != null && jobLongitude != null){

                // Calculate the distance between the job's location and the user's location
                float[] results = new float[1];
                Location.distanceBetween(jobLatitude, jobLongitude, userLocation.getLatitude(), userLocation.getLongitude(), results);
                float distanceInMeters = results[0];

                // Add the job to the list if it's within 10KM of the user's location
                if (distanceInMeters <= 10000) {
                    String currJob = jobName + " (near you)\n " + timePosted + "\n";
                    list.add(currJob);
                    String postId = postSnapshot.getKey();
                    list2.add(postId);
                }

            } else {
                String currJob = jobName + "\n " + timePosted + "\n";
                list.add(currJob);
                String postId = postSnapshot.getKey();
                list2.add(postId);
            }

        }
    }

    /**
     This method displays an alert dialog containing a list of new job postings in the user's job category.
     If there are no new job postings in the user's job category, it displays an alert dialog indicating so.
     */
    public void showNotificationAlert() {
        // Create an alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(MainPageActivity.this);
        builder.setTitle("Jobs Notification");

        if (list.isEmpty()) {
            // Display a message indicating that there are no new job postings in the user's job category
            builder.setMessage("No new job postings in your category. \n Please try again later.");
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();

            // Close the dialog automatically after a 1 second delay
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }, 2000);
        } else {
            // Convert the list of jobs to an array and display them in the alert dialog
            String[] jobs = list.toArray(new String[0]);
            builder.setItems(jobs, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // When a job is clicked, launch the display_details activity to display the job details
                    String postId = list2.get(which);
                    Intent intent = new Intent(MainPageActivity.this, display_details.class);
                    intent.putExtra("postId", postId);
                    startActivity(intent);
                }
            });

            // Add a "OK" button to the alert dialog to allow the user to dismiss it
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            // Display the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();

            // Clear the lists of jobs and job IDs
            list.clear();
            list2.clear();
        }
    }
}