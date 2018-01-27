package com.amc.akhil.myapplication.db;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by user on 24-11-2017.
 */

public class UserInfo {
    public int id;
    public String firstName = "";
    public String lastName = "";
    public int phoneNumber;
    public boolean isBVI;
    public String password = "";
    public String userName = "";

    /**
     * No need to do anything, fields are already set to default values above
     */
    public UserInfo(int id, String firstName, String lastName, int phoneNumber, boolean isBVI, String userName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isBVI = isBVI;
        this.userName = userName;
    }


}
