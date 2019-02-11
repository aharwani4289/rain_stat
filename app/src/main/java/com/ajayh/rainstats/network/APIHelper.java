package com.ajayh.rainstats.network;

import com.ajayh.rainstats.R;
import com.ajayh.rainstats.WeatherApplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ajay.Harwani
 */

public class APIHelper {

    private String TAG = this.getClass().getSimpleName();
    private static APIHelper instance;
    private APIService apiService;
    private WeatherApplication application;

    private APIResponse.Error getServerError() {
        return new APIResponse.Error(-1, "We apologize for the inconvenience. Please try again later.");
    }

    public static synchronized APIHelper getInstance(WeatherApplication application) {
        if (null == instance) {
            instance = new APIHelper();
            instance.setApplication(application);
            instance.initAPIService();
        }
        return instance;
    }

    public void setApplication(WeatherApplication application) {
        this.application = application;
    }

    private void initAPIService() {

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(application.getString(R.string.server_url))
                .build();

        apiService = retrofit.create(APIService.class);
    }

    public APIService getApiService() {
        return apiService;
    }
}