package com.ajayh.rainstats.datamodel;

public class WeatherResponse {

    private double value;

    private int year;

    private int month;

    private String minRain;

    private String maxRain;

    private int location;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getMinRain() {
        return minRain;
    }

    public void setMinRain(String minRain) {
        this.minRain = minRain;
    }

    public String getMaxRain() {
        return maxRain;
    }

    public void setMaxRain(String maxRain) {
        this.maxRain = maxRain;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }
}
