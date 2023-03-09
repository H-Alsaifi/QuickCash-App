package com.example.g2_qc;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class location extends MainActivity{
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;

    public FusedLocationProviderClient getFusedLocationProviderClient() {
        return fusedLocationProviderClient;
    }

    public LocationCallback getLocationCallback() {
        return locationCallback;
    }

    public void setFusedLocationProviderClient(FusedLocationProviderClient fusedLocationProviderClient) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
    }

    public void setLocationCallback(LocationCallback locationCallback) {
        this.locationCallback = locationCallback;
    }

    public location(Context context) {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Log.d(TAG, "Latitude: " + latitude + "Longitude: " + longitude);
                }
            }
        };
    }
    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000); // Update location every 10 seconds
        locationRequest.setFastestInterval(5000); // Fastest update interval is 5 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Use high accuracy mode
        return locationRequest;
    }
    public void startLocationUpdates(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
        else{
            fusedLocationProviderClient.requestLocationUpdates(getLocationRequest(),locationCallback,null);
        }
    }
    public void stopLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

}
