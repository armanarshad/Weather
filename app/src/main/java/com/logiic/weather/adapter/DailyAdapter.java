package com.logiic.weather.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.logiic.weather.R;
import com.logiic.weather.models.darksky.Forecast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.viewHolder> {

    private static final String TAG = DailyAdapter.class.getSimpleName();

    private Context context;
    private Forecast forecast;
    private SharedPreferences sharedPreferences;

    public DailyAdapter(Context context, Forecast forecast) {
        this.context = context;
        this.forecast = forecast;
    }

    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.data_weekly, parent, false);
        viewHolder holder = new viewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {
        sharedPreferences = context.getSharedPreferences("com.logiic.weather.preference", Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean("unitsOfMeasure", false)) {
            holder.highestTemperature.setText(forecast.getDaily().getData().get(position).getHighestCelsius().toLowerCase() + "\u00B0");
            holder.lowestTemperature.setText(forecast.getDaily().getData().get(position).getLowestCelsius().toLowerCase() + "\u00B0");
        } else {
            holder.highestTemperature.setText(forecast.getDaily().getData().get(position).getHighestFahrenheit().toLowerCase() + "\u2109");
            holder.lowestTemperature.setText(forecast.getDaily().getData().get(position).getLowestFahrenheit().toLowerCase() + "\u2109");
        }
        if (forecast.getDaily().getData().get(position).getIcon() != null) {
            switch (forecast.getDaily().getData().get(position).getIcon()) {
                case "rain":
                    holder.icons.setImageResource(R.drawable.light_rain);
                    break;
                case "snow":
                    holder.icons.setImageResource(R.drawable.snow);
                    break;
                case "wind":
                    holder.icons.setImageResource(R.drawable.windy);
                    break;
                case "partly-cloudy-day":
                    holder.icons.setImageResource(R.drawable.cloud);
                    break;
                case "cloudy":
                    holder.icons.setImageResource(R.drawable.cloud);
                    break;
                case "clear-day":
                    holder.icons.setImageResource(R.drawable.clear);
                    break;
                case "sleet":
                    holder.icons.setImageResource(R.drawable.sleet);
                    break;
                case "fog":
                    holder.icons.setImageResource(R.drawable.fog);
                    break;
            }
        }
        holder.icons.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_ATOP);
        holder.condition.setText(forecast.getDaily().getData().get(position).getCondition());
        holder.days.setText(forecast.getDaily().getData().get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    class viewHolder extends RecyclerView.ViewHolder {
        TextView days;
        ImageView icons;
        TextView condition;
        TextView highestTemperature;
        TextView lowestTemperature;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            days = (TextView) itemView.findViewById(R.id.day_names);
            icons = (ImageView) itemView.findViewById(R.id.condition_icon);
            condition = (TextView) itemView.findViewById(R.id.condition);
            highestTemperature = (TextView) itemView.findViewById(R.id.highest_temperature);
            lowestTemperature = (TextView) itemView.findViewById(R.id.lowest_temperature);
        }
    }
}
