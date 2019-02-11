package com.ajayh.rainstats.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ajayh.rainstats.WeatherApplication;

public class RainDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "RainDatabaseHelper";

    public RainDatabaseHelper(Context context) {
        super(context, RainDatabaseContract.DB_NAME, null, RainDatabaseContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String CREATE_DB_TABLE = "CREATE TABLE IF NOT EXISTS "
                    + RainDatabaseContract.RainStats.TABLE_NAME + " (" + RainDatabaseContract.RainStats.RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + RainDatabaseContract.RainStats.RAIN_LOCATION + " INTEGER, "
                    + RainDatabaseContract.RainStats.RAIN_IN_MM + " TEXT, "
                    + RainDatabaseContract.RainStats.MIN_RAIN_IN_MM + " TEXT, "
                    + RainDatabaseContract.RainStats.MAX_RAIN_IN_MM + " TEXT, "
                    + RainDatabaseContract.RainStats.RAIN_MONTH + " INTEGER, "
                    + RainDatabaseContract.RainStats.RAIN_YEAR + " INTEGER);";

            db.execSQL(CREATE_DB_TABLE);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
