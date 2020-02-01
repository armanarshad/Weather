package com.logiic.weather.models.darksky;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Daily {

    @SerializedName("data")
    private List<Data> data;

    public Daily(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() { return data; }

    public void setData(List<Data> data) { this.data = data; }
}
