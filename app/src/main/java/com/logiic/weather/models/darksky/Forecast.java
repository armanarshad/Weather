package com.logiic.weather.models.darksky;


import com.google.gson.annotations.SerializedName;

public class Forecast {

    @SerializedName("currently")
    private Currently currently;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("timezone")
    private String timeZone;

    public Forecast(Currently currently, double longitude, double latitude, String timeZone) {
        this.currently = currently;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timeZone = timeZone;
    }

    public Currently getCurrently() {
        return currently;
    }

    public void setCurrently(Currently currently) {
        this.currently = currently;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
