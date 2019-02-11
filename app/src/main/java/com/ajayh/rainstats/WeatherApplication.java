package com.ajayh.rainstats;

import android.app.Application;
import android.content.Context;

import com.ajayh.rainstats.network.APIHelper;
import com.ajayh.rainstats.network.APIService;

public class WeatherApplication extends Application {

    private static WeatherApplication mInstance;
    private static Context mContext;
    private APIHelper mApiHelper;

    public static WeatherApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mInstance = this;
        mApiHelper = APIHelper.getInstance(this);

        doInit();
    }

    public static Context getAppContext() {
        return mContext;
    }

    private void doInit() {
        mApiHelper = APIHelper.getInstance(this);
    }

    public synchronized APIService getApiService() {
        return mApiHelper.getApiService();
    }
}
