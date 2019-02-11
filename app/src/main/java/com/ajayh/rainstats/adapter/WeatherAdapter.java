package com.ajayh.rainstats.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajayh.rainstats.R;
import com.ajayh.rainstats.WeatherApplication;
import com.ajayh.rainstats.datamodel.WeatherResponse;
import com.ajayh.rainstats.datamodel.WeatherViewHolder;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> {

    private Context mContext;

    private ArrayList<WeatherResponse> mRainData;
    private LayoutInflater mLayourtInflator;

    public WeatherAdapter(Context context, ArrayList<WeatherResponse> rainData) {
        mContext = context;
        mRainData = rainData;
        mLayourtInflator = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = mLayourtInflator.inflate(R.layout.rain_item, null);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        WeatherResponse rain = mRainData.get(position);

        if (rain.getValue() <= 30.0D) {
            Drawable res = mContext.getResources().getDrawable(R.drawable.sunny);
            holder.mRainDp.setImageDrawable(res);
        } else if (rain.getValue() <= 70) {
            Drawable res = mContext.getResources().getDrawable(R.drawable.heavy_rain);
            holder.mRainDp.setImageDrawable(res);
        } else {
            Drawable res = mContext.getResources().getDrawable(R.drawable.stormy_rain);
            holder.mRainDp.setImageDrawable(res);
        }

        holder.mRainInMM.setText(String.valueOf(rain.getValue()) + " mm");

        String minRain = !TextUtils.isEmpty(rain.getMinRain()) ? rain.getMinRain() : "NA";
        Drawable drawable = ContextCompat.getDrawable(WeatherApplication.getAppContext()
                , R.drawable.min_temp);
        drawable.setBounds(0, 0, 120, 120);
        holder.mMinRain.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
        holder.mMinRain.setText(minRain);

        String maxRain = !TextUtils.isEmpty(rain.getMaxRain()) ? rain.getMinRain() : "NA";
        Drawable maxRainDrawable = ContextCompat.getDrawable(WeatherApplication.getAppContext()
                , R.drawable.max_temp);
        maxRainDrawable.setBounds(0, 0, 120, 120);
        holder.mMaxRain.setCompoundDrawablesRelativeWithIntrinsicBounds(maxRainDrawable, null, null, null);
        holder.mMaxRain.setText(maxRain);

        Date date = new Date();
        date.setMonth(rain.getMonth());

        holder.mDate.setText("Rain Duration: " +
                new SimpleDateFormat("MMMM").format(date) + ", " + rain.getYear());
    }

    @Override
    public int getItemCount() {
        return mRainData.size();
    }

    public void refreshRainData(ArrayList<WeatherResponse> rainData) {
        this.mRainData.clear();
        this.mRainData.addAll(rainData);
        notifyDataSetChanged();
    }
}
