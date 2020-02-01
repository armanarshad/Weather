package com.logiic.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.logiic.weather.R;
import com.logiic.weather.models.darksky.Forecast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.viewHolder>{

    private static final String TAG = HourlyAdapter.class.getSimpleName();

    private Context context;
    Forecast forecast;

    public HourlyAdapter(Context context, Forecast forecast) {
        this.context = context;
        this.forecast = forecast;
    }

    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.data_hourly, parent, false);
        viewHolder holder = new viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {
        if (forecast.getHourly().getData().get(position).getIcon() != null) {
            switch (forecast.getHourly().getData().get(position).getIcon()) {
                case "rain":
                    holder.hourlyIcon.setImageResource(R.drawable.light_rain);
                    break;
                case "snow":
                    holder.hourlyIcon.setImageResource(R.drawable.snow);
                    break;
                case "wind":
                    holder.hourlyIcon.setImageResource(R.drawable.windy);
                    break;
                case "partly-cloudy-day":
                    holder.hourlyIcon.setImageResource(R.drawable.cloud);
                    break;
                case "cloudy":
                    holder.hourlyIcon.setImageResource(R.drawable.cloud);
                    break;
                case "clear-day":
                    holder.hourlyIcon.setImageResource(R.drawable.clear);
                    break;
                case "sleet":
                    holder.hourlyIcon.setImageResource(R.drawable.sleet);
                    break;
                case "fog":
                    holder.hourlyIcon.setImageResource(R.drawable.fog);
                    break;
            }
        }
        holder.temperature.setText(forecast.getHourly().getData().get(position).getTemperature().toLowerCase() + "\u00B0");
        holder.time.setText(forecast.getHourly().getData().get(position).getTimes());
    }

    @Override
    public int getItemCount() { return 24; }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView temperature;
        ImageView hourlyIcon;
        TextView time;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            temperature = (TextView) itemView.findViewById(R.id.hourly_temperature);
            hourlyIcon = (ImageView) itemView.findViewById(R.id.hourly_icon);
            time = (TextView) itemView.findViewById(R.id.hourly_time);
        }
    }
}
