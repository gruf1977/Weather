package ru.chizhikov.weather;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import java.util.Objects;
import ru.chizhikov.weather.fragments.WeatherValue;

public class WeatherInCity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_in_city);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        Intent intent = getIntent();
        int numberPosition = intent.getIntExtra("numberPosition", 0);
            FragmentManager fragmentManager = getSupportFragmentManager();
            WeatherValue weatherValue = (WeatherValue)fragmentManager
                    .findFragmentById(R.id.weather_value);
            Objects.requireNonNull(weatherValue)
                    .setWeatherValue(numberPosition);
    }
}

