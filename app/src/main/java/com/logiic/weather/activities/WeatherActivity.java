package com.logiic.weather.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.logiic.weather.BuildConfig;
import com.logiic.weather.R;
import com.logiic.weather.api.RetrofitClient;
import com.logiic.weather.models.darksky.Forecast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = WeatherActivity.class.getSimpleName();
    private static final String API_KEY = BuildConfig.DARKSKY_API_KEY;

    private double latitude;
    private double longitude;
    private TextView bottomSummary;
    private TextView summary;
    private TextView temp;
    private TextView wind;
    private TextView rain;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        dialog = new ProgressDialog(WeatherActivity.this);
        dialog.setMessage("Loading..");
        dialog.setTitle("Loading Data");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();

        temp = findViewById(R.id.temperature);
        summary = findViewById(R.id.top_text);
        wind = findViewById(R.id.wind);
        bottomSummary = findViewById(R.id.bottom_text);
        rain = findViewById(R.id.rain);

        Intent intent = getIntent();
        String[] location = intent.getStringExtra("location").split(",");
        CharSequence title = location[0] + ", " + location[2];
        getSupportActionBar().setTitle(title);

        if (Geocoder.isPresent()) {
            try {
                String myLocaction = (String) title;
                Geocoder gc = new Geocoder(this);
                List<Address> addresses = gc.getFromLocationName(myLocaction, 5);

                List<LatLng> ll = new ArrayList<LatLng>(addresses.size());
                for (Address a : addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                        longitude = a.getLongitude();
                        latitude = a.getLatitude();
                    }
                }
            } catch (IOException e) {
                //
            }
        }

        Call<Forecast> currentWeather = RetrofitClient
            .getInstance()
            .getApi()
            .getCurrentWeather(API_KEY, latitude, longitude);

        currentWeather.enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                temp.setText(response.body().getCurrently().getTemperature().toLowerCase() + "\u00B0");
                summary.setText("Today is " + response.body().getCurrently().getSummary());
                wind.setText(String.valueOf(Math.round(response.body().getCurrently().getWindSpeed())));
                rain.setText(String.valueOf(Math.round(response.body().getCurrently().getRain())));
                bottomSummary.setText("Mostly " + response.body().getCurrently().getSummary().toLowerCase() + " today");

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                //
            }
        });
    }
}
