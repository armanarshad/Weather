package com.logiic.weather.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.logiic.weather.BuildConfig;
import com.logiic.weather.R;
import com.logiic.weather.activities.WeatherActivity;
import com.logiic.weather.api.RetrofitClient;
import com.logiic.weather.models.darksky.Forecast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherFragment extends Fragment {

    private static final String TAG = WeatherFragment.class.getSimpleName();
    private static final String API_KEY = BuildConfig.DARKSKY_API_KEY;

    private TextView bottomSummary;
    private double latitude;
    private ProgressDialog loadingDialog;
    private double longitude;
    private TextView rain;
    private ImageView rainIcon;
    private SharedPreferences sharedPreferences;
    private TextView summary;
    private TextView temperature;
    private TextView unicode;
    private ConstraintLayout weather;
    private TextView wind;
    private  ImageView windIcon;
    private TextView windSpeed;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        sharedPreferences = getContext().getSharedPreferences("com.logiic.weather.preference", Context.MODE_PRIVATE);

        bottomSummary = (TextView) view.findViewById(R.id.bottom_text);
        windSpeed = (TextView) view.findViewById(R.id.wind_speed);
        summary = (TextView) view.findViewById(R.id.top_text);
        temperature = (TextView) view.findViewById(R.id.temperature);
        wind = (TextView) view.findViewById(R.id.wind);
        windIcon = (ImageView) view.findViewById(R.id.wind_icon);
        rain = (TextView) view.findViewById(R.id.rain);
        rainIcon = (ImageView) view.findViewById(R.id.rain_icon);
        unicode = (TextView) view.findViewById(R.id.unicode);
        weather = (ConstraintLayout) view.findViewById(R.id.weather);


        loadingDialog = new ProgressDialog(getContext());
        loadingDialog.setMessage("Loading..");
        loadingDialog.setTitle("Loading Data");
        loadingDialog.setIndeterminate(false);
        loadingDialog.setCancelable(true);
        loadingDialog.show();

        WeatherActivity activity = (WeatherActivity) getActivity();
        String location1 = activity.getData();

        if (Geocoder.isPresent()) {
            try {
                String myLocaction = location1;
                Geocoder gc = new Geocoder(getContext());
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

                if (sharedPreferences.getBoolean("unitsOfMeasure", false) == true) {
                    temperature.setText(response.body().getCurrently().getCelsius().toLowerCase());
                    unicode.setText("\u2103");
                    windSpeed.setText("MPH");
                    wind.setText(String.valueOf(Math.round(response.body().getCurrently().getWindMPH())));
                } else {
                    temperature.setText(response.body().getCurrently().getFahrenheit().toLowerCase());
                    unicode.setText("\u2109");
                    windSpeed.setText("KPH");
                    wind.setText(String.valueOf(Math.round(response.body().getCurrently().getWindKPH())));
                }

                if (response.body().getCurrently().getSummary() != null) {
                    switch (response.body().getCurrently().getSummary().toLowerCase()) {
                        case "rain":
                            weather.setBackgroundResource(R.drawable.rain_background);
                            break;
                        case "snow":
                        case "sleet":
                            weather.setBackgroundResource(R.drawable.snow_background);
                            break;
                        case "wind":
                            weather.setBackgroundResource(R.drawable.wind_background);
                            break;
                        case "partly-cloudy-day":
                        case "cloudy":
                        case "overcast":
                            weather.setBackgroundResource(R.drawable.overcast_background);
                            break;
                        case "partly-cloudy-night":
                        case "clear-night":
                            weather.setBackgroundResource(R.drawable.night_background);
                            break;
                        case "clear-day":
                            weather.setBackgroundResource(R.drawable.sunny_background);
                            break;
                        case "clear":
                            weather.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.sunny_background));
                            break;
                        case "fog":
                            weather.setBackgroundResource(R.drawable.fog_background);
                            break;
                        default:
                            break;
                    }
                }
                summary.setText("Today is " + response.body().getCurrently().getSummary());
                rain.setText(String.valueOf(Math.round(response.body().getCurrently().getRain())));
                bottomSummary.setText("Mostly " + response.body().getCurrently().getSummary().toLowerCase() + " today");
                windIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
                windIcon.setImageResource(R.drawable.windy);
                rainIcon.setImageResource(R.drawable.light_rain);
                rainIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);

                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                //
            }
        });

        return view;
    }

}
