package com.logiic.weather;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;
import com.logiic.weather.activities.MainActivity;
import com.logiic.weather.api.RetrofitClient;
import com.logiic.weather.models.darksky.Forecast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.core.app.NotificationCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String API_KEY = BuildConfig.DARKSKY_API_KEY;
    private static final String TAG = NotificationReceiver.class.getSimpleName();

    public static final String NOTIFICATION_CHANNEL_ID = "channel_id";

    private Context context;
    double latitude;
    double longitude;
    private SharedPreferences sharedPreferences;
    private String summary;
    private String temperature;
    private String units;


    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("com.logiic.weather.preference", Context.MODE_PRIVATE);
        getWeatherData();

        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeatingIntent = new Intent(context, MainActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (sharedPreferences.getBoolean("unitsOfMeasure", false)) {
            units = "\u00B0";
        } else {
            units = "\u2109";
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setContentTitle("Weather")
                    .setContentText("Today is " + summary + " and today's temperature will be " + temperature + units)
                    .setSmallIcon(R.drawable.heavy_rain)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                notificationManager.notify(100, builder.build());
            }
        }, 20000);
    }

    private void getWeatherData() {
        String location1 = sharedPreferences.getString("location", null);

        if (Geocoder.isPresent()) {
            try {
                String myLocaction = location1;
                Geocoder gc = new Geocoder(context);
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
                summary = response.body().getCurrently().getSummary();

                if (sharedPreferences.getBoolean("unitsOfMeasure", false) == true) {
                    temperature = response.body().getCurrently().getCelsius();
                } else {
                    temperature = response.body().getCurrently().getFahrenheit();
                }

            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                //
            }
        });
    }
}
