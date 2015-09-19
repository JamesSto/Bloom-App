package cornell.hacks.bloom;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

/**
 * @author Han Li
 *         Date: 9/19/2015.
 */
public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    // Flag for GPS status
    boolean isGPSEnabled = false;

    // Flag for network status
    boolean isNetworkEnabled = false;

    // Flag for GPS status
    boolean canGetLocation = false;

    Location location; // Location
    double latitude; // Latitude
    double longitude; // Longitude
    final int EARTH_RADIUS = 6371000; //meters
    final int MAX_CUTOFF = 300; //meters

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // Getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // No network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // If GPS enabled, get latitude/longitude using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app.
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }


    /**
     *
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }


    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/Wi-Fi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to get the distance in meters between two location
     * @param loc1 -first location
     * @param loc2 - second location
     * @return distance in meters between two locations
     */
    public double getDistanceBetween(Location loc1, Location loc2){
        double loc1Lat = loc1.getLatitude();
        double loc1Long = loc1.getLongitude();
        double loc2Lat = loc2.getLatitude();
        double loc2Long = loc2.getLongitude();

        double dLat = Math.toRadians(loc2Lat - loc1Lat);
        double dLong = Math.toRadians(loc2Long - loc1Long);
        double loc1LatRadians = Math.toRadians(loc1Lat);
        double loc2LatRadians = Math.toRadians(loc2Lat);

        double a = Math.sin(dLat/2.0) * Math.sin(dLat/2.0) +
                Math.sin(dLong/2.0) * Math.sin(dLong/2.0) * Math.cos(loc1LatRadians) * Math.cos(loc2LatRadians);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0-a));

        double distance = EARTH_RADIUS * c;

        return Math.abs(distance);
    }

    /**
     * Function that returns
     * @param dist - distance between two points in meters
     * @return intensity - value bewtween 1 and 100.
     */
    public int calcIntensity(double dist){
        double distance = dist;
        if(distance < 1.0){
            distance = 1.0;
        }
        if(distance > MAX_CUTOFF){
            distance = MAX_CUTOFF;
        }

        double scalingfactor = 100.0/Math.log(MAX_CUTOFF);
        double intensityDouble = 100 - scalingfactor * Math.log(distance);

        if (intensityDouble < 0.0){
            intensityDouble = 0.0;
        }

        int intensity = (int)Math.round(intensityDouble);
        return intensity;
    }

    @Override
    public void onLocationChanged(Location location) {
    }


    @Override
    public void onProviderDisabled(String provider) {
    }


    @Override
    public void onProviderEnabled(String provider) {
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}