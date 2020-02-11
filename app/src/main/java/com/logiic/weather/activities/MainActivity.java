package com.logiic.weather.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.logiic.weather.NotificationReceiver;
import com.logiic.weather.R;
import com.logiic.weather.api.AccuWeather;
import com.logiic.weather.services.LocationService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements TextWatcher {

    private static final String TAG = MainActivity.class.getSimpleName();

    private AccuWeather accuWeather = new AccuWeather();
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView autoComplete;
    private List<String> suggestions = new ArrayList<>();
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = getSharedPreferences("com.logiic.weather.preference", Context.MODE_PRIVATE);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            setContentView(R.layout.activity_main);

            autoComplete = findViewById(R.id.auto_complete_country);
            autoComplete.addTextChangedListener(this);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION);
        } else {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //
    }

    @Override
    public void afterTextChanged(Editable s) {
        retrieveData(s);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
        autoComplete.setAdapter(adapter);
    }

    private void retrieveData(Editable s) {
        suggestions = accuWeather.getSuggestions(String.valueOf(s));
    }

    public void Confirm(View view) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("location", autoComplete.getEditableText().toString());
        editor.commit();
        Intent intent = new Intent(this, WeatherActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String location = sharedPref.getString("location", null);

        if (sharedPref.getBoolean("notificationSwitch", true)) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 6);
            calendar.set(Calendar.MINUTE, 0);

            Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
            PendingIntent penddingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, penddingIntent);
        } else {
            //
        }

        if (location != null) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
        }
    }

}
