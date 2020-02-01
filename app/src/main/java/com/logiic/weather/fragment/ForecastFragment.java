package com.logiic.weather.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.logiic.weather.BuildConfig;
import com.logiic.weather.adapter.DailyAdapter;
import com.logiic.weather.adapter.HourlyAdapter;
import com.logiic.weather.R;
import com.logiic.weather.activities.WeatherActivity;
import com.logiic.weather.api.RetrofitClient;
import com.logiic.weather.models.darksky.Forecast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastFragment extends Fragment {

    private static final String TAG = ForecastFragment.class.getSimpleName();
    private static final String API_KEY = BuildConfig.DARKSKY_API_KEY;

    private RecyclerView dailyView;
    private RecyclerView hourlyView;
    private TextView date;
    double latitude;
    double longitude;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        dailyView = (RecyclerView) view.findViewById(R.id.daily_view);
        hourlyView = (RecyclerView) view.findViewById(R.id.hourly_view);
        date = (TextView) view.findViewById(R.id.date);

        long timestamp = System.currentTimeMillis() / 1000L;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000L);
        String monthAndDay = DateFormat.format("MMMM dd", cal).toString();

        date.setText(monthAndDay);

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
                addHourlyData(response.body());
                addDailyData(response.body());
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                //
            }
        });

        return view;
    }

    private void addHourlyData(Forecast body) {
        HourlyAdapter adapter = new HourlyAdapter(getContext(), body);
        hourlyView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        hourlyView.setAdapter(adapter);
    }

    private void addDailyData(Forecast body) {
        DailyAdapter adapter = new DailyAdapter(getContext(), body);
        dailyView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dailyView.setAdapter(adapter);
    }
}
