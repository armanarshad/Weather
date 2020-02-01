package com.logiic.weather.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.logiic.weather.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SectionPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Integer> fragmentIconList = new ArrayList<>();
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    private Context context;

    public SectionPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    public void addFragment(Fragment fragment, String title, int tabIcon) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
        fragmentIconList.add(tabIcon);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tabTextView = view.findViewById(R.id.tab_text_view);
        tabTextView.setText(fragmentTitleList.get(position));
        ImageView tabImageView = view.findViewById(R.id.tab_image_view);
        tabImageView.setImageResource(fragmentIconList.get(position));
        return view;
    }

    public View getSelectedTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tabTextView = view.findViewById(R.id.tab_text_view);
        tabTextView.setText(fragmentTitleList.get(position));
        tabTextView.setTextColor(ContextCompat.getColor(context, R.color.tabIndicator));
        ImageView tabImageView = view.findViewById(R.id.tab_image_view);
        tabImageView.setImageResource(fragmentIconList.get(position));
        tabImageView.setColorFilter(ContextCompat.getColor(context, R.color.tabIndicator), PorterDuff.Mode.SRC_ATOP);
        return view;
    }
}
