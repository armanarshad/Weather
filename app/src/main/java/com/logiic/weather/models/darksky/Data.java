package com.logiic.weather.models.darksky;

import android.text.format.DateFormat;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Locale;

public class Data {

    private static final String TAG = Data.class.getSimpleName();

    @SerializedName("icon")
    private String icon;
    @SerializedName("precipType")
    private String condition;
    @SerializedName("temperatureHigh")
    private String highestTemperature;
    @SerializedName("temperatureLow")
    private String lowestTemperature;
    @SerializedName("time")
    private long time;
    @SerializedName("temperature")
    private String temperature;

    public Data(String icon, String condition, String highestTemperature, String lowestTemperature, long time) {
        this.condition = condition;
        this.highestTemperature = highestTemperature;
        this.lowestTemperature = lowestTemperature;
        this.time = time;
        this.icon = icon;
    }

    public Data(String icon, long time, String temperature) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
    }

    public String getIcon() { return icon; }

    public void setIcon(String icon) { this.icon = icon; }

    public String getCondition() { return condition; }

    public void setCondition(String condition) { this.condition = condition; }

    public String getHighestTemperature() {
        double temp = Double.parseDouble(highestTemperature);
        return String.valueOf(Math.round(((temp - 32) * 5) / 9));}

    public void setHighestTemperature(String highestTemperature) { this.highestTemperature = highestTemperature; }

    public String getLowestTemperature() {
        double temp = Double.parseDouble(lowestTemperature);
        return String.valueOf(Math.round(((temp - 32) * 5) / 9));
    }

    public void setLowestTemperature(String lowestTemperature) { this.lowestTemperature = lowestTemperature; }

    public String getTime() {
        long timestamp = time;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000L);
        String date = DateFormat.format("EEEE", cal).toString();
        return date;
    }

    public String getTimes() {
        long timestamp = time;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000L);
        String date = DateFormat.format("hh:mm a", cal).toString();
        return date;
    }

    public void setTime(long time) { this.time = time; }

    public String getTemperature() {
        double temp = Double.parseDouble(temperature);
        return String.valueOf(Math.round(((temp - 32) * 5) / 9));
    }

    public void setTemperature(String temperature) { this.temperature = temperature; }
}
