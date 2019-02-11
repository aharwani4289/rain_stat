package com.ajayh.rainstats.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ajayh.rainstats.R;
import com.ajayh.rainstats.WeatherApplication;
import com.ajayh.rainstats.adapter.WeatherAdapter;
import com.ajayh.rainstats.database.RainDatabaseDbQueryManager;
import com.ajayh.rainstats.datamodel.WeatherResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final int UK = 1;
    private static final int ENGLAND = 2;
    private static final int WALES = 3;
    private static final int SCOTLAND = 4;

    private RecyclerView mWeatherRecycler;
    private WeatherAdapter mWeatherAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;
    private Toolbar mToolbar;
    private int mCurrentLocation = UK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initComponents();
        executeWeatherApi(UK);
        getSupportActionBar().setTitle(getString(R.string.uk));
    }

    private void initComponents() {
        mWeatherRecycler = findViewById(R.id.weather_recycler);
        mProgressBar = findViewById(R.id.progress_bar);
    }

    private void executeWeatherApi(int loc) {
        mCurrentLocation = loc;
        handleProgressBarVisibility(View.VISIBLE);
        if (!RainDatabaseDbQueryManager.getInstance().isRainStatsEmptyForLocation(loc)) {
            ArrayList<WeatherResponse> data = RainDatabaseDbQueryManager.getInstance().getRainStatsByLocation(loc);
            handleProgressBarVisibility(View.GONE);
            updateUI(data);
        } else {
            WeatherApplication application = WeatherApplication.getInstance();
            switch (loc) {
                case UK:
                    getSupportActionBar().setTitle(getString(R.string.uk));
                    application.getApiService().getWeatherForUK().enqueue(mCallback);
                    break;
                case ENGLAND:
                    getSupportActionBar().setTitle(getString(R.string.england));
                    application.getApiService().getWeatherForEngland().enqueue(mCallback);
                    break;
                case WALES:
                    getSupportActionBar().setTitle(getString(R.string.wales));
                    application.getApiService().getWeatherForWales().enqueue(mCallback);
                    break;
                case SCOTLAND:
                    getSupportActionBar().setTitle(getString(R.string.scotland));
                    application.getApiService().getWeatherForScotland().enqueue(mCallback);
                    break;

            }
        }
    }

    private void handleProgressBarVisibility(int visibility) {
        mProgressBar.setVisibility(visibility);
    }

    private Callback mCallback = new Callback<ArrayList<WeatherResponse>>() {
        @Override
        public void onResponse(Call<ArrayList<WeatherResponse>> call, Response<ArrayList<WeatherResponse>> response) {
            handleProgressBarVisibility(View.GONE);
            Log.i(TAG, "Network call success");
            if (response.isSuccessful() && response.body() != null) {

                Gson gson = new Gson();
                ArrayList<WeatherResponse> rainData = gson.fromJson(gson.toJson(response.body())
                        , new TypeToken<List<WeatherResponse>>() {
                        }.getType());

                updateDbAsynchronously(rainData);
                updateUI(rainData);
            }
        }

        @Override
        public void onFailure(Call<ArrayList<WeatherResponse>> call, Throwable t) {
            handleProgressBarVisibility(View.GONE);
            // TODO : Handle erro scenario
        }
    };

    private void updateDbAsynchronously(ArrayList<WeatherResponse> weatherResponses) {
        new AsyncTask<Object, Void, Void>() {

            @Override
            protected Void doInBackground(Object... objects) {
                RainDatabaseDbQueryManager.getInstance().saveRainStatsByLocation((int) objects[1], (ArrayList<WeatherResponse>) objects[0]);
                return null;
            }

        }.execute(weatherResponses, mCurrentLocation);
    }

    private void updateUI(ArrayList<WeatherResponse> rainData) {
        if (mWeatherAdapter == null) {
            mWeatherAdapter = new WeatherAdapter(MainActivity.this, rainData);
            mLinearLayoutManager = new LinearLayoutManager(MainActivity.this,
                    LinearLayoutManager.VERTICAL, false);
            mWeatherRecycler.setLayoutManager(mLinearLayoutManager);
            mWeatherRecycler.setAdapter(mWeatherAdapter);
        } else {
            mWeatherAdapter.refreshRainData(rainData);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_uk) {
            mToolbar.setTitle(getString(R.string.uk));
            executeWeatherApi(UK);
        } else if (id == R.id.nav_england) {
            mToolbar.setTitle(getString(R.string.england));
            executeWeatherApi(ENGLAND);
        } else if (id == R.id.nav_wales) {
            mToolbar.setTitle(getString(R.string.wales));
            executeWeatherApi(WALES);
        } else if (id == R.id.nav_scotland) {
            mToolbar.setTitle(getString(R.string.scotland));
            executeWeatherApi(SCOTLAND);
        } else if (id == R.id.nav_share) {
            Toast.makeText(MainActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_contact) {
            Toast.makeText(MainActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
