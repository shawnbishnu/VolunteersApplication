package com.amc.akhil.myapplication.db;

/**
 * Created by user on 24-11-2017.
 */
import android.content.ContentValues;
import android.database.Cursor;

/**
 * A class representation of a row in table "Person".
 */
public class GPSInfo {

    // SQL convention says Table name should be "singular", so not Persons
    public static final String TABLE_NAME = "GPS_INFO";
    // Naming the id column with an underscore is good to be consistent
    // with other Android things. This is ALWAYS needed
    public static final String COL_ID = "_id";
    // These fields can be anything you want.
    public static final String COL_LATITUDE = "latitude";
    public static final String COL_LONGITUDE = "longitude";
    public static final String COL_USER_ID = "user_id";

    // For database projection so order is consistent
    public static final String[] FIELDS = { COL_ID, COL_LATITUDE, COL_LONGITUDE};

    /*
     * The SQL code that creates a Table for storing Persons in.
     * Note that the last row does NOT end in a comma like the others.
     * This is a common source of error.
     */
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_LATITUDE + " INTEGER,"
                    + COL_LONGITUDE + " INTEGER"
                    //+ COL_USER_ID + "INTEGER, FOREIGN KEY (USER_ID) REFERENCES USER(ID)"
                    + ")";

    public GPSInfo(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public long getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Fields corresponding to database columns
    public long id = -1;
    public double latitude;
    public double longitude;
    //public  User user;

    /**
     * No need to do anything, fields are already set to default values above
     */
    public GPSInfo() {
    }

    /**
     * Convert information from the database into a Person object.
     */
    public GPSInfo(final Cursor cursor) {
        // Indices expected to match order in FIELDS!
        this.id = cursor.getLong(0);
        this.latitude = cursor.getDouble(1);
        this.longitude = cursor.getDouble(2);
        //this.user = cursor.getS
    }

    /**
     * Return the fields in a ContentValues object, suitable for insertion
     * into the database.
     */
    /*public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        // Note that ID is NOT included here
        values.put(COL_LATITUDE, latitude);
        values.put(COL_LONGITUDE, longitude);

        return values;
    }*/
}
