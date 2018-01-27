package com.amc.akhil.myapplication.db;

/**
 * Created by user on 24-11-2017.
 */
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;

import com.amc.akhil.myapplication.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A class representation of a row in table "Person".
 */
public class GPSData {

    private double previousLatitude;
    private double previousLongitude;
    private long previousTime;
    private long currentTime;
    private double currentLatitude;
    private double currentLongitude;
    private boolean isBVICoordinates;
    private long speed;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    public GPSData(double latitude, double longitude, long time, String userId, boolean isBVICoordinates){
        this.previousLatitude = MainActivity.currentLocation.getLatitude();
        this.previousLongitude = MainActivity.currentLocation.getLongitude();
        this.previousTime = MainActivity.currentLocation.getTime();
        this.currentTime = time;
        this.currentLatitude = latitude;
        this.currentLongitude = longitude;
        this.userId = userId;
        this.isBVICoordinates = isBVICoordinates;
        setSpeed();
    }

    public GPSData(Map object) {
        this.currentLatitude = (double) object.get("latitude");
        this.currentLongitude = (double) object.get("longitude");
        this.userId = (String) object.get("user");
        this.isBVICoordinates = (boolean) object.get("isBVICoordinates");
        this.speed = (long) object.get("speed");
    }
    /**
     * No need to do anything, fields are already set to default values above
     */
    public GPSData() {
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("latitude", currentLatitude);
        result.put("longitude", currentLongitude);
        result.put("user", userId);
        result.put("isBVICoordinates",isBVICoordinates);
        result.put("speed",speed);
        return result;
    }

    public boolean isBVICoordinates() {
        return isBVICoordinates;
    }

    public void setSpeed() {
        float timeDifference = (currentTime - previousTime)/1000; //time in seconds
        Location pLocation = new Location(LocationManager.GPS_PROVIDER);
        Location cLocation = new Location(LocationManager.GPS_PROVIDER);
        pLocation.setLongitude(previousLongitude);
        pLocation.setLatitude(previousLatitude);
        cLocation.setLongitude(currentLongitude);
        cLocation.setLatitude(currentLatitude);
        float distanceInMeters = cLocation.distanceTo(pLocation);
        this.speed = (long) (distanceInMeters/timeDifference);
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public long getSpeed() {
        return speed;
    }

}
