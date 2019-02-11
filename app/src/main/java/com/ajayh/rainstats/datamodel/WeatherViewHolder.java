package com.ajayh.rainstats.datamodel;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajayh.rainstats.R;

public class WeatherViewHolder extends RecyclerView.ViewHolder {

    public ImageView mRainDp;
    public TextView mRainInMM;
    public TextView mMinRain;
    public TextView mMaxRain;
    public TextView mDate;

    public WeatherViewHolder(@NonNull View itemView) {
        super(itemView);
        mRainDp = itemView.findViewById(R.id.rain_forecast_icon);
        mRainInMM = itemView.findViewById(R.id.rain_in_mm_value);
        mMinRain = itemView.findViewById(R.id.min_rain_in_mm_value);
        mMaxRain = itemView.findViewById(R.id.max_rain_in_mm_value);
        mDate = itemView.findViewById(R.id.stats_date);
    }

}
