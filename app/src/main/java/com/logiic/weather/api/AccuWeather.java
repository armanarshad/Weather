package com.logiic.weather.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class AccuWeather {

    private static final String TAG = "Database";

    private JSONArray jsonArray;
    private Thread thread;

    private JSONArray parseJson(final String userInput) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
            try {
                URL url = new URL("https://www.accuweather.com/web-api/autocomplete?query=" + userInput + "&language=en-us");
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();

                String line = "";

                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                String json = stringBuffer.toString();
                jsonArray = new JSONArray(json);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    public ArrayList<String> getSuggestions(String userInput) {
        parseJson(userInput);

        ArrayList<String> suggestions = new ArrayList<String>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String city = jsonObject.getString("localizedName");
                String county = jsonObject.getJSONObject("administrativeArea").getString("localizedName");
                String country = jsonObject.getJSONObject("country").getString("localizedName");
                suggestions.add(city + ",  " + county + ",  " + country);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return suggestions;
    }
}
