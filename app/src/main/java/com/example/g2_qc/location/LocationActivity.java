package com.example.g2_qc.location;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import com.example.g2_qc.R;
import com.example.g2_qc.databinding.ActivityLocationBinding;
import com.example.g2_qc.main_page.MainPageActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;

/**
 This activity displays a Google Map and allows the user to save a selected location
 with a custom name and coordinates.
 */
public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ActivityLocationBinding binding;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng selectedLocation;


    /**
     Sets up the activity layout and click listener for the save location button.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the save location button and its click listener
        Button saveLocationButton = findViewById(R.id.save_location_button);
        saveLocationButton.setOnClickListener(view -> {
            // Show a dialog box to prompt the user for an address or location name
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter a location name");
            EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String locationName = input.getText().toString();
                    saveLocation(locationName);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Check for location permissions, request if necessary
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            initMap();
        }
    }

    /**
     Initializes the map when the location button is clicked and shows the map fragment.
     */
    private void initMap() {
        if (mapFragment != null) {
            // Get a reference to the GoogleMap object and asynchronously load the map.
            mapFragment.getMapAsync(this);
        } else {
            // Log an error if the map fragment cannot be initialized.
            Log.e("Location", "Error initializing map fragment.");
        }
    }

    /**
     Sets up the GoogleMap when it's ready.
     Enables "My Location" button and gets the user's last known location.
     Adds a click listener to the map to allow the user to select a location.
     Saves the selected location to the database by calling the saveLocation() method.
     @param googleMap the GoogleMap instance
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Check for location permissions before enabling the "My Location" button and getting the user's last known location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(latLng -> {
            selectedLocation = latLng;
            mMap.clear();
            try {
                saveLocation(mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"))); // Call saveLocation() method to save the location to the database
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Move the camera to the user's last known location
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            } else {
                Toast.makeText(LocationActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     Called when the result of a location permission request is received.
     @param requestCode The code that was used to request the permission.
     @param permissions The permissions that were requested.
     @param grantResults The results of the permission request.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initMap();
            } else {
                Toast.makeText(this, "Location permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     Saves the selected location to the database.
     @param locationAddress The address of the location to save.
     */
    private void saveLocation(String locationAddress) {
        Geocoder geocoder = new Geocoder(this);
        try {
            // Get the address from the location name.
            List<Address> addresses = geocoder.getFromLocationName(locationAddress, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();

                // Save the location coordinates to the database.
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("location");
                LocationDetails locationDetails = new LocationDetails(latitude, longitude);
                FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("location coordinates").setValue(locationDetails);

                databaseReference.setValue(address.getAddressLine(0)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(LocationActivity.this, "Location saved successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LocationActivity.this, MainPageActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LocationActivity.this, "Error saving location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(LocationActivity.this, "No location found for the address", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(LocationActivity.this, "Error saving location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     Saves the selected location to the Firebase database and displays a success/failure message.
     @param map the Marker representing the selected location on the map
     @throws IOException if the Geocoder cannot retrieve the location information
     */
    private void saveLocation(Marker map) throws IOException {
        if (selectedLocation != null) {

            // Get the latitude and longitude coordinates of the selected location
            double latitude = map.getPosition().latitude;
            double longitude = map.getPosition().longitude;

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("location");


            // Save the location coordinates to the database
            saveLocationCoordinatesToDatabase(uid, latitude, longitude);

            // Get the location information using the Geocoder
            Geocoder geocoder = new Geocoder(this);
            List<Address> location = geocoder.getFromLocation(latitude, longitude, 1);
            String strLocation = location.get(0).getAddressLine(0);

            // Save the location string to the database and display a success/failure message
            saveLocationStringToDatabase(databaseReference, strLocation);
        } else {
            Toast.makeText(LocationActivity.this, "Please select a location", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     Saves the location coordinates to the Firebase database.
     @param uid the user ID
     @param latitude the latitude coordinate of the selected location
     @param longitude the longitude coordinate of the selected location
     */
    private void saveLocationCoordinatesToDatabase(String uid, double latitude, double longitude) {
        LocationDetails locationDetails = new LocationDetails(latitude, longitude);
        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("location coordinates").setValue(locationDetails);
    }

    /**
     Saves the location string to the Firebase database and displays a success/failure message.
     @param databaseReference the reference to the database location where the location string will be saved
     @param strLocation the location string to be saved
     */
    private void saveLocationStringToDatabase(DatabaseReference databaseReference, String strLocation) {
        databaseReference.setValue(strLocation).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(LocationActivity.this, "Location saved successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LocationActivity.this, MainPageActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LocationActivity.this, "Error saving location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}