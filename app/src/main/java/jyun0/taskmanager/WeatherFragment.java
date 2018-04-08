package jyun0.taskmanager;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyun0.taskmanager.R;


public class WeatherFragment extends Fragment {

    private TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField;
    private Typeface weatherFont;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        findWeather();
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        weatherFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/weathericons-regular-webfont.ttf");

        cityField = (TextView)view.findViewById(R.id.city_field);
        updatedField = (TextView)view.findViewById(R.id.updated_field);
        detailsField = (TextView)view.findViewById(R.id.details_field);
        currentTemperatureField = (TextView)view.findViewById(R.id.current_temperature_field);
        humidity_field = (TextView)view.findViewById(R.id.humidity_field);
        pressure_field = (TextView)view.findViewById(R.id.pressure_field);
        weatherIcon = (TextView)view.findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);

        Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                cityField.setText(weather_city);
                updatedField.setText(weather_updatedOn);
                detailsField.setText(weather_description);
                currentTemperatureField.setText(weather_temperature);
                humidity_field.setText("Humidity: "+weather_humidity);
                pressure_field.setText("Pressure: "+weather_pressure);
                weatherIcon.setText(Html.fromHtml(weather_iconText));

            }
        });

        asyncTask.execute("49.286793", "-123.112845"); //  asyncTask.execute("Latitude", "Longitude")

        return view;
    }

}
