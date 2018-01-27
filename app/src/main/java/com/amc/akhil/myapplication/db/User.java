package com.amc.akhil.myapplication.db;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by user on 24-11-2017.
 */

public class User {
    // SQL convention says Table name should be "singular", so not Persons
    public static final String TABLE_NAME = "USER";
    // Naming the id column with an underscore is good to be consistent
    // with other Android things. This is ALWAYS needed
    public static final String COL_ID = "_id";
    // These fields can be anything you want.
    public static final String COL_FIRSTNAME = "firstName";
    public static final String COL_LASTNAME = "lastName";
    public static final String COL_PHONENUMBER = "phoneNumber";
    public static final String COL_PASSWORD = "password";
    public static final String COL_IS_BVI = "isBVI";
    public static final String COL_USER_NAME = "userName";

    // For database projection so order is consistent
    public static final String[] FIELDS = { COL_ID, COL_FIRSTNAME, COL_LASTNAME, COL_PHONENUMBER,
                                            COL_PASSWORD, COL_IS_BVI, COL_USER_NAME};

    /*
     * The SQL code that creates a Table for storing Persons in.
     * Note that the last row does NOT end in a comma like the others.
     * This is a common source of error.
     */
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COL_ID + " INTEGER PRIMARY KEY,"
                    + COL_FIRSTNAME + " TEXT NOT NULL DEFAULT '',"
                    + COL_LASTNAME + " TEXT NOT NULL DEFAULT '',"
                    + COL_PHONENUMBER + " INT NOT NULL DEFAULT '',"
                    + COL_IS_BVI + " INT NOT NULL DEFAULT '',"
                    + COL_PASSWORD + " TEXT NOT NULL DEFAULT '',"
                    + COL_USER_NAME + " TEXT NOT NULL DEFAULT '',"
                    + ")";

    // Fields corresponding to database columns
    public long id = -1;
    public String firstName = "";
    public String lastName = "";
    public int phoneNumber;
    public boolean isBVI;
    public String password = "";
    public String userName = "";

    /**
     * No need to do anything, fields are already set to default values above
     */
    public User() {
    }

    /**
     * Convert information from the database into a User object.
     */
    public User(final Cursor cursor) {
        // Indices expected to match order in FIELDS!
        this.id = cursor.getLong(0);
        this.firstName = cursor.getString(1);
        this.lastName = cursor.getString(2);
        this.phoneNumber = cursor.getInt(3);
        this.password = cursor.getString(4);
        this.isBVI = (cursor.getInt(5) == 1)? true: false;
        this.userName = cursor.getString(6);
    }

    /**
     * Return the fields in a ContentValues object, suitable for insertion
     * into the database.
     */
    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        // Note that ID is NOT included here
        values.put(COL_FIRSTNAME, firstName);
        values.put(COL_LASTNAME, lastName);
        values.put(COL_PHONENUMBER, phoneNumber);
        values.put(COL_IS_BVI, (isBVI)? 1: 0);
        values.put(COL_PASSWORD, password);
        values.put(COL_USER_NAME, userName);
        return values;
    }
}
