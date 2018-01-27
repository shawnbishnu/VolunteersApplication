package com.amc.akhil.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amc.akhil.myapplication.db.DatabaseHandler;
import com.amc.akhil.myapplication.db.GPSData;
import com.amc.akhil.myapplication.db.GPSInfo;
import com.amc.akhil.myapplication.db.User;
import com.amc.akhil.myapplication.db.UserInfo;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    private Button logoutButton;
    protected LocationManager locationManager;
    protected Context context;
    private DatabaseReference dbReference;
    private FirebaseAuth firebaseAuth;
    public static Location currentLocation;

    public List<GPSData> getGpsDataList() {
        return gpsDataList;
    }

    public void setGpsDataList(List<GPSData> gpsDataList) {
        this.gpsDataList = gpsDataList;
    }

    private List<GPSData> gpsDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page_volunteers);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new NotificationHandler())
                .init();
        initializeRefereces();
        checkLogin();
        //speakOut("You are on Main screen");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void checkLogin() {
        if(firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        //send tag to onesignal for push notification
        if(firebaseAuth.getCurrentUser() != null){
            OneSignal.sendTag("User_id",firebaseAuth.getCurrentUser().getUid());
            startLocationInfoRetrieval();
        }
    }

    @Override
    public void onClick(View view) {

        if(view == logoutButton){
            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }

    void startLocationInfoRetrieval() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onLocationChanged(Location location) {
        writeGPSData(location);
        currentLocation = location;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    public void writeGPSData(Location location){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            GPSData gpsData = new GPSData(location.getLatitude(), location.getLongitude(),
                    location.getTime(), firebaseUser.getUid(), false);
            Map<String, Object> map = gpsData.toMap();
            dbReference.child("gps"+firebaseUser.getUid()).updateChildren(map);
        }
    }

    public void initializeRefereces() {
        firebaseAuth = FirebaseAuth.getInstance();
        logoutButton = (Button) findViewById(R.id.logout);
        logoutButton.setOnClickListener(this);
        gpsDataList = new ArrayList<>();
        dbReference = FirebaseDatabase.getInstance().getReference("GPSInfo");
    }

    public class NotificationHandler implements OneSignal.NotificationOpenedHandler{

        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            intent.putExtra("requestor_latitude", currentLocation.getLatitude());
            intent.putExtra("requestor_longitude", currentLocation.getLongitude());
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

}
