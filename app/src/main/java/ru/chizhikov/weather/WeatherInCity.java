package ru.chizhikov.weather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;

public class WeatherInCity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_in_city);
        showWeatherInCity();
        getWeather();
    }
    private void showWeatherInCity() {
        String weather_in_city = getResources().getString(R.string.weatherIn) + getIntent().getStringExtra("nameCity");
        TextView textViewWeather = findViewById(R.id.textViewWeather);
        textViewWeather.setText(weather_in_city);
    }
    private void getWeather() {
        Weather weather = (Weather) Objects.requireNonNull(getIntent()
                .getExtras())
                .getSerializable("weather");
        if (weather != null) {
            showWeatherParam(weather);
            showAtmPressure(weather);
            showHumidity(weather);
            showWindSpeed(weather);
        }
    }
    @SuppressLint("SetTextI18n")
    private void showWeatherParam(Weather weather) {
        TextView temperatureValue = findViewById(R.id.temperatureValue);
        temperatureValue.setText(weather.getTemperature() + "C" + (char) 176);
        TextView rainfallValue = findViewById(R.id.rainfallValue);
        rainfallValue.setText(weather.getRainfall() + "");
     }
    @SuppressLint("SetTextI18n")
    private void showWindSpeed(Weather weather) {
        if (getIntent().getBooleanExtra("checkWindSpeed", false)) {
            TextView windSpeed = findViewById(R.id.windSpeed);
            windSpeed.setText(getResources().getString(R.string.windSpeed) + ": " +
                    weather.getHumidity() +
                    getResources().getString(R.string.windSpeedSymbol));
            windSpeed.setVisibility(View.VISIBLE);
        }
    }
    @SuppressLint("SetTextI18n")
    private void showHumidity(Weather weather) {
        if (getIntent().getBooleanExtra("checkHumidity", false)) {
            TextView humidity = findViewById(R.id.humidity);
            humidity.setText(getResources().getString(R.string.humidity) + ": " +
                    weather.getHumidity() +
                    getResources().getString(R.string.humiditySymbol));
            humidity.setVisibility(View.VISIBLE);
        }
    }
    @SuppressLint("SetTextI18n")
    private void showAtmPressure(Weather weather) {
        if (getIntent().getBooleanExtra("checkAtmPressure", false)) {
            TextView atmPressure = findViewById(R.id.atmPressure);
            atmPressure.setText(getResources().getString(R.string.atmPressure) + ": " +
                    weather.getAtm_pressure() +
                    getResources().getString(R.string.atmPressureSymbol));
            atmPressure.setVisibility(View.VISIBLE);
        }
    }
}

