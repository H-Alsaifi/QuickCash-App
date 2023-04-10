package com.example.g2_qc.location;

/**
 * This class represents the latitude and longitude of a location.
 */
public class LocationDetails {
    private double latitude; // The latitude of the location.
    private double longitude; // The longitude of the location.

    /**
     * Default constructor.
     */
    public LocationDetails() {}

    /**
     * Constructor that sets the latitude and longitude of the location.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     */
    public LocationDetails(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the latitude of the location.
     *
     * @return The latitude of the location.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the location.
     *
     * @param latitude The latitude of the location.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the longitude of the location.
     *
     * @return The longitude of the location.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the location.
     *
     * @param longitude The longitude of the location.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
