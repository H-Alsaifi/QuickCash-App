package com.example.g2_qc.main_page;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.g2_qc.R;
import com.example.g2_qc.databinding.ActivityMainBinding;
import com.example.g2_qc.display_details.display_details;
import com.example.g2_qc.signup_page.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private Button locationButton;

    private FirebaseAuth authProfile;
    private String email;
    private String location;
    private String category;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> list2 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotification();
                showNotificationAlert();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_employee, R.id.nav_employer, R.id.nav_profile)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        locationButton = findViewById(R.id.location_button);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser user = authProfile.getCurrentUser();
        ExtractInfo(user);
        enableNotification();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem logoutItem = menu.findItem(R.id.action_logout);
        logoutItem.setOnMenuItemClickListener(item -> onOptionsItemSelected(item));

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void ExtractInfo(FirebaseUser firebaseUser) {
        if (firebaseUser == null) {
            // Handle the case where the FirebaseUser object is null
            return;
        }

        String myId = firebaseUser.getUid(); //user ID
        DatabaseReference profile_ref = FirebaseDatabase.getInstance().getReference("Users");
        profile_ref.child(myId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userDetails = snapshot.getValue(User.class);
                if (userDetails != null) {

                    email = userDetails.emailAddress;
                    location = userDetails.location;
                    category = userDetails.category;

                    NavigationView navigationView = findViewById(R.id.nav_view);
                    View headerView = navigationView.getHeaderView(0);
                    TextView subtitleTextView = headerView.findViewById(R.id.nav_header_subtitle);
                    subtitleTextView.setText(email);

                    if (location.isEmpty()) {
                        locationButton.setText("Choose location");
                    } else {
                        locationButton.setText(location);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainPageActivity.this, "Error: we could not complete your request", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();

            // navigate back to login activity
            Intent intent = new Intent(this, com.example.g2_qc.login_page.demo_login_page.class);
            startActivity(intent);
            finish();

            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    public void onLocationButtonClick(View view) {
        // Launch the location activity
        Intent intent = new Intent(this, location.class);
        startActivity(intent);
    }


    public void enableNotification() {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null) {
            NotificationChannel channel = new NotificationChannel("my_channel", "My Channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);

            if (notificationManager.getNotificationChannel(channel.getId()).getImportance() == NotificationManager.IMPORTANCE_NONE) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Notifications Disabled");
                builder.setMessage("Please enable notifications to receive job updates");
                builder.setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");

                        // For Android 5 and above
                        intent.putExtra("app_package", getPackageName());
                        intent.putExtra("app_uid", getApplicationInfo().uid);

                        // For Android 8 and above
                        intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());

                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    public void showNotification() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

//        String myId = FirebaseAuth.getInstance().getCurrentUser().getUid(); //user ID

        // Call populateScrollView to populate the scroll view with existing posts
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();


                    DatabaseReference postsReferenceEmployee = userSnapshot.child("Employee").child("Posts").getRef();
                    postsReferenceEmployee.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            notificationCheck(dataSnapshot);
                            progressDialog.dismiss(); // dismiss the progress dialog here
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressDialog.dismiss();
                            Toast.makeText(MainPageActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                    DatabaseReference postsReferenceEmployer = userSnapshot.child("Employer").child("Posts").getRef();
                    postsReferenceEmployer.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            notificationCheck(dataSnapshot);
                            progressDialog.dismiss(); // dismiss the progress dialog here
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressDialog.dismiss();
                            Toast.makeText(MainPageActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(MainPageActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void notificationCheck(DataSnapshot dataSnapshot) {
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            String jobName = postSnapshot.child("jobName").getValue(String.class);
            String timePosted = postSnapshot.child("timePosted").getValue(String.class);
            String jobCategory = postSnapshot.child("jobCategory").getValue(String.class);

            if (jobCategory.equals(category)) {
                String currJob = jobName + "\n " + timePosted + "\n";
                list.add(currJob);
                String postId = postSnapshot.getKey();
                list2.add(postId);
            }
        }
    }

    public void showNotificationAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainPageActivity.this);
        builder.setTitle("Jobs Notification of Your Category");
        if (list.isEmpty()) {
            builder.setMessage("No new job postings in your category. \n Please try again later.");
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }, 1000); // 1 second delay before auto-closing the dialog
        } else {
            String[] jobs = list.toArray(new String[0]);
            builder.setItems(jobs, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String postId = list2.get(which);
                    Intent intent = new Intent(MainPageActivity.this, display_details.class);
                    intent.putExtra("postId", postId);
                    startActivity(intent);
                }
            });
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            list.clear();
            list2.clear();
        }
    }


}