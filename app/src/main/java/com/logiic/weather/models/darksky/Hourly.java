package com.logiic.weather.models.darksky;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Hourly {

    @SerializedName("icon")
    private String icon;
    @SerializedName("data")
    private List<Data> data;

    public Hourly(String icon, List<Data> data) {
        this.icon = icon;
        this.data = data;
    }

    public String getIcon() { return icon; }

    public void setIcon(String icon) { this.icon = icon; }

    public List<Data> getData() { return data; }

    public void setData(List<Data> data) { this.data = data; }
}
