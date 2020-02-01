package com.logiic.weather.fragment;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherFragment extends Fragment {

    private static final String TAG = WeatherFragment.class.getSimpleName();
    private static final String API_KEY = BuildConfig.DARKSKY_API_KEY;

    private double latitude;
    private double longitude;

    private TextView bottomSummary;
    private TextView summary;
    private TextView temperature;
    private TextView wind;
    private TextView rain;

    ProgressDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        bottomSummary = (TextView) view.findViewById(R.id.bottom_text);
        summary = (TextView) view.findViewById(R.id.top_text);
        temperature = (TextView) view.findViewById(R.id.temperature);
        wind = (TextView) view.findViewById(R.id.wind);
        rain = (TextView) view.findViewById(R.id.rain);

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
                temperature.setText(response.body().getCurrently().getTemperature().toLowerCase() + "\u00B0");
                summary.setText("Today is " + response.body().getCurrently().getSummary());
                wind.setText(String.valueOf(Math.round(response.body().getCurrently().getWindSpeed())));
                rain.setText(String.valueOf(Math.round(response.body().getCurrently().getRain())));
                bottomSummary.setText("Mostly " + response.body().getCurrently().getSummary().toLowerCase() + " today");

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
