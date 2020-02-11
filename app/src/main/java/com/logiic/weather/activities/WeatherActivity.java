package com.logiic.weather.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.logiic.weather.BuildConfig;
import com.logiic.weather.R;
import com.logiic.weather.adapter.SectionPagerAdapter;
import com.logiic.weather.fragment.ForecastFragment;
import com.logiic.weather.fragment.SettingsFragment;
import com.logiic.weather.fragment.WeatherFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class WeatherActivity extends AppCompatActivity {

    private static final String API_KEY = BuildConfig.DARKSKY_API_KEY;
    private static final String TAG = WeatherActivity.class.getSimpleName();

    private SectionPagerAdapter sectionPagerAdapter;
    TabLayout tabLayout;
    private CharSequence title;
    private ViewPager viewPager;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        sharedPreferences = getSharedPreferences("com.logiic.weather.preference", Context.MODE_PRIVATE);

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), this);

        String[] location = sharedPreferences.getString("location", null).split(",");
        title = location[0] + ", " + location[2];

        setTitle(title);

        sectionPagerAdapter.addFragment(new WeatherFragment(), "Weather", tabIcons[0]);
        sectionPagerAdapter.addFragment(new ForecastFragment(), "Forecast", tabIcons[1]);
        sectionPagerAdapter.addFragment(new SettingsFragment(), "Settings", tabIcons[2]);
        viewPager.setAdapter(sectionPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        highLightCurrentTab(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(sectionPagerAdapter.getTabView(i));
        }

        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(sectionPagerAdapter.getSelectedTabView(position));
    }


    public String getData() {
        return (String) title;
    }

    private int[] tabIcons = {
        R.drawable.weather,
        R.drawable.forecast,
        R.drawable.settings
    };

}
