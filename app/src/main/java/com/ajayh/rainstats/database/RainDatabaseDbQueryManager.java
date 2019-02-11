package com.ajayh.rainstats.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import com.ajayh.rainstats.WeatherApplication;
import com.ajayh.rainstats.datamodel.WeatherResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RainDatabaseDbQueryManager {

    private static final String TAG = "RainDbDbQueryManager";
    private static RainDatabaseHelper rainDatabaseHelper;
    private static SQLiteDatabase db;
    private static RainDatabaseDbQueryManager dbManager;

    private static RainDatabaseDbQueryManager mInstance;

    private RainDatabaseDbQueryManager() {

    }

    public static RainDatabaseDbQueryManager getInstance() {
        if (mInstance == null) {
            mInstance = new RainDatabaseDbQueryManager();
        }
        return mInstance;
    }

    private static SQLiteDatabase getDatabase() {
        if (dbManager == null) {
            dbManager = new RainDatabaseDbQueryManager();
            rainDatabaseHelper = new RainDatabaseHelper(WeatherApplication.getAppContext());
            db = rainDatabaseHelper.getWritableDatabase();
            db.enableWriteAheadLogging();
        }
        return db;
    }

    /**
     * Returns variable to determine rain stats for location is avalable in db or not
     *
     * @param location
     * @return
     */
    public boolean isRainStatsEmptyForLocation(int location) {
        Cursor cursor = null;
        boolean isTableEmpty = false;
        try {
            cursor = getDatabase().query(RainDatabaseContract.RainStats.TABLE_NAME, null,
                    RainDatabaseContract.RainStats.RAIN_LOCATION + " =? ",
                    new String[]{String.valueOf(location)}, null, null, null);
            if (cursor != null && cursor.getCount() < 1) {
                isTableEmpty = true;
            }
        } catch (Exception e) {
            Log.e(TAG, "EXCEPTION OCCURRED " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isTableEmpty;
    }

    /**
     * Save rain stats by location
     *
     * @param location
     * @param weatherResponses
     */
    public void saveRainStatsByLocation(int location, List<WeatherResponse> weatherResponses) {

        String insertQuery = "INSERT INTO " + RainDatabaseContract.RainStats.TABLE_NAME + " ("
                + RainDatabaseContract.RainStats.RAIN_LOCATION + ","
                + RainDatabaseContract.RainStats.MIN_RAIN_IN_MM + ","
                + RainDatabaseContract.RainStats.MAX_RAIN_IN_MM + ","
                + RainDatabaseContract.RainStats.RAIN_IN_MM + ","
                + RainDatabaseContract.RainStats.RAIN_MONTH + ","
                + RainDatabaseContract.RainStats.RAIN_YEAR + ")" + " VALUES (?,?,?,?,?,?);";
        SQLiteStatement sqlInsertStatement = null;

        getDatabase().beginTransaction();
        try {
            sqlInsertStatement = getDatabase().compileStatement(insertQuery);
            for (WeatherResponse response : weatherResponses) {
                sqlInsertStatement.clearBindings();
                sqlInsertStatement.bindLong(1, location);
                sqlInsertStatement.bindString(2, !TextUtils.isEmpty(response.getMinRain()) ? response.getMinRain() : "NA");
                sqlInsertStatement.bindString(3, !TextUtils.isEmpty(response.getMaxRain()) ? response.getMaxRain() : "NA");
                sqlInsertStatement.bindString(4, String.valueOf(response.getValue()));
                sqlInsertStatement.bindLong(5, response.getMonth());
                sqlInsertStatement.bindLong(6, response.getYear());
                sqlInsertStatement.execute();
            }
            getDatabase().setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e(TAG, "EXCEPTION WHILE INSERTING INTO DB" + ex.getMessage());
        } finally {
            if (sqlInsertStatement != null) {
                sqlInsertStatement.close();
            }
            getDatabase().endTransaction();
        }
    }

    /**
     * Get Rain data by location
     *
     * @param location
     * @return
     */
    public ArrayList<WeatherResponse> getRainStatsByLocation(int location) {
        Cursor c = commonQuery(null,
                RainDatabaseContract.RainStats.RAIN_LOCATION + " =? ",
                new String[]{String.valueOf(location)},
                null);
        return returnCursorToList(c);
    }

    private Cursor commonQuery(String[] projection, String selection,
                               String[] selectionArgs, String sortOrder) {
        return getDatabase().query(
                RainDatabaseContract.RainStats.TABLE_NAME,
                projection,
                getAccountIdSelection(selection),
                getAccountIdArgs(selectionArgs), null, null, null);
    }

    private ArrayList<WeatherResponse> returnCursorToList(Cursor c) {
        ArrayList<WeatherResponse> weatherList = new ArrayList<>();
        if (c != null) {
            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {
                    WeatherResponse weatherResponse = new WeatherResponse();
                    setToDoTask(c, weatherResponse);
                    weatherList.add(weatherResponse);
                    c.moveToNext();
                }
            }
            c.close();
        }
        return weatherList;
    }

    private void setToDoTask(Cursor c, WeatherResponse weatherResponse) {
        weatherResponse.setMaxRain(c.getString(c.getColumnIndex(RainDatabaseContract.RainStats.MAX_RAIN_IN_MM)));
        weatherResponse.setMinRain(c.getString(c.getColumnIndex(RainDatabaseContract.RainStats.MIN_RAIN_IN_MM)));
        weatherResponse.setValue(c.getInt(c.getColumnIndex(RainDatabaseContract.RainStats.RAIN_IN_MM)));
        weatherResponse.setMonth(c.getInt(c.getColumnIndex(RainDatabaseContract.RainStats.RAIN_MONTH)));
        weatherResponse.setYear(c.getInt(c.getColumnIndex(RainDatabaseContract.RainStats.RAIN_YEAR)));
        weatherResponse.setLocation(c.getInt(c.getColumnIndex(RainDatabaseContract.RainStats.RAIN_LOCATION)));
    }

    private ContentValues returnModelToContentValue(WeatherResponse weatherResponse) {
        ContentValues values = new ContentValues();
        values.put(RainDatabaseContract.RainStats.MIN_RAIN_IN_MM, weatherResponse.getMinRain());
        values.put(RainDatabaseContract.RainStats.MAX_RAIN_IN_MM, weatherResponse.getMaxRain());
        values.put(RainDatabaseContract.RainStats.RAIN_IN_MM, weatherResponse.getValue());
        values.put(RainDatabaseContract.RainStats.RAIN_MONTH, weatherResponse.getMonth());
        values.put(RainDatabaseContract.RainStats.RAIN_YEAR, weatherResponse.getYear());
        values.put(RainDatabaseContract.RainStats.RAIN_LOCATION, weatherResponse.getLocation());
        return values;
    }

    private String getAccountIdSelection(String selection) {
        if (TextUtils.isEmpty(selection)) {
            return null;
        }
        return selection;
    }

    private String[] getAccountIdArgs(String[] selectionArgs) {
        ArrayList<String> arguments = new ArrayList<>();
        if (selectionArgs != null) {
            arguments.addAll(Arrays.asList(selectionArgs));
        }
        return arguments.toArray(new String[arguments.size()]);
    }
}
