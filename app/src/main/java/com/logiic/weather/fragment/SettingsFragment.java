package com.logiic.weather.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.logiic.weather.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    private TextView measure;
    private Switch notificationSwitch;
    private SharedPreferences sharedPreferences;
    private Switch unitsOfMeasure;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences("com.logiic.weather.preference", Context.MODE_PRIVATE);


        measure = (TextView) view.findViewById(R.id.settings_measure_text);
        notificationSwitch = (Switch) view.findViewById(R.id.settings_notifications);
        unitsOfMeasure = (Switch) view.findViewById(R.id.settings_measure);

        if (sharedPreferences.getBoolean("notificationSwitch", true)) {
            notificationSwitch.setChecked(true);
        } else {
            notificationSwitch.setChecked(false);
        }

        if (sharedPreferences.getBoolean("unitsOfMeasure", false)) {
            measure.setText("C\u00B0/MPH");
            unitsOfMeasure.setChecked(true);
        } else {
            measure.setText("\u2109/KPH");
            unitsOfMeasure.setChecked(false);
        }

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("notificationSwitch", true);
                    editor.apply();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("notificationSwitch", false);
                    editor.apply();
                }
            }
        });

        unitsOfMeasure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    measure.setText("C\u00B0/MPH");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("unitsOfMeasure", true);
                    editor.apply();
                } else {
                    measure.setText("\u2109/KPH");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("unitsOfMeasure", false);
                    editor.apply();
                }
            }
        });


        return view;
    }

}
