package com.amc.akhil.myapplication.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Srinivas on 24-11-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static DatabaseHandler singleton;

    public static DatabaseHandler getInstance(final Context context) {
        if (singleton == null) {
            singleton = new DatabaseHandler(context);
        }
        return singleton;
    }

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AssistMe";

    private final Context context;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // Good idea to have the context that doesn't die with the window
        this.context = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GPSInfo.CREATE_TABLE);
        GPSInfo info = new GPSInfo();
        //test co-ordinates
        info.setLatitude(22.11);
        info.setLatitude(32.11);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public GPSInfo getGPSInfo(final long id) {
        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.query(GPSInfo.TABLE_NAME, GPSInfo.FIELDS,
                GPSInfo.COL_ID + " IS ?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor == null || cursor.isAfterLast()) {
            return null;
        }

        GPSInfo item = null;
        if (cursor.moveToFirst()) {
            item = new GPSInfo(cursor);
        }
        cursor.close();
        return item;
    }

    /*public boolean putGPSInfo(final GPSInfo gpsInfo) {
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = this.getWritableDatabase();
        if (gpsInfo.id > -1) {
            result += db.update(GPSInfo.TABLE_NAME, gpsInfo.getContent(),
                    GPSInfo.COL_ID + " IS ?",
                    new String[]{String.valueOf(gpsInfo.id)});
        } else {
            result += db.insert(GPSInfo.TABLE_NAME, null, gpsInfo.getContent());
        }
        if (result > 0) {
            success = true;
        } else {
            // Update failed or wasn't possible, insert instead
            final long id = db.insert(GPSInfo.TABLE_NAME, null,
                    gpsInfo.getContent());

            if (id > -1) {
                gpsInfo.id = id;
                success = true;
            }
        }
        if (success) {
            //notifyProviderOnPersonChange();
        }
        return success;
    }*/

    public List<GPSInfo> getAllGPSInfo() {
        final SQLiteDatabase db = this.getReadableDatabase();
        List<GPSInfo> gpsInfoList = new ArrayList<>();
        final Cursor cursor = db.rawQuery("Select * from " + GPSInfo.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                gpsInfoList.add(new GPSInfo(cursor.getDouble(1), cursor.getDouble(2)));
                cursor.moveToNext();
            }
        }
        return gpsInfoList;
    }
}