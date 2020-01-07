package com.logiic.weather.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.logiic.weather.R;
import com.logiic.weather.api.AccuWeather;
import com.logiic.weather.services.LocationService;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements TextWatcher {

    private static final String TAG = "MainActivity";

    private AccuWeather accuWeather = new AccuWeather();
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView autoComplete;
    private List<String> suggestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            setContentView(R.layout.activity_main);

            autoComplete = findViewById(R.id.auto_complete_country);
            autoComplete.addTextChangedListener(this);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION);
        } else {
            //
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
}
