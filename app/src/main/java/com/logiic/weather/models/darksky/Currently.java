package com.logiic.weather.models.darksky;

import com.google.gson.annotations.SerializedName;

public class Currently {

    @SerializedName("cloudCover")
    private String cloudCover;
    @SerializedName("humidity")
    private String humidity;
    @SerializedName("icon")
    private String icon;
    @SerializedName("precipProbability")
    private double rain;
    @SerializedName("summary")
    private String summary;
    @SerializedName("temperature")
    private String temperature;
    @SerializedName("visibility")
    private String visibility;
    @SerializedName("windSpeed")
    private double windSpeed;
    @SerializedName("windBearing")
    private String windDirection;

    public Currently(String summary, String icon, String temperature, String humidity, double windSpeed, String cloudCover, String visibility, double rain, String windDirection) {
        this.summary = summary;
        this.icon = icon;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.cloudCover = cloudCover;
        this.visibility = visibility;
        this.rain = rain;
        this.windDirection = windDirection;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCelsius() {
        double temp = Double.parseDouble(temperature);
        return String.valueOf(Math.round(((temp - 32) * 5) / 9));
    }

    public String getFahrenheit() {
        return String.valueOf(Math.round(Float.parseFloat(temperature)));
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public double getWindMPH() {
        return windSpeed;
    }

    public double getWindKPH() {
        double temp = windSpeed * 1.61;
        return temp;
    }



    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(String cloudCover) {
        this.cloudCover = cloudCover;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public double getRain() { return rain; }

    public void setRain(double rain) { this.rain = rain; }

    public String getWindDirection() { return windDirection; }

    public void setWindDirection(String windDirection) { this.windDirection = windDirection; }
}


