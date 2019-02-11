package com.ajayh.rainstats.database;

import android.provider.BaseColumns;

public class RainDatabaseContract {


    public static final String DB_NAME = "rain_db_stats";
    public static final int DB_VERSION = 1;

    public static abstract class RainStats implements BaseColumns {

        public static final String TABLE_NAME = "rain_stats";

        public static final String RECORD_ID = "record_id";
        public static final String RAIN_LOCATION = "location";
        public static final String RAIN_IN_MM = "rain_in_mm";
        public static final String MIN_RAIN_IN_MM = "min_rain_in_mm";
        public static final String MAX_RAIN_IN_MM = "max_rain_in_mm";
        public static final String RAIN_MONTH = "rain_month";
        public static final String RAIN_YEAR = "rain_year";
    }


}
