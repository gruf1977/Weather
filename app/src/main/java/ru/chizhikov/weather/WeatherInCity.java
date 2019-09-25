package ru.chizhikov.weather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;

public class WeatherInCity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "WeatherInCity: onCreate()");
        setContentView(R.layout.activity_weather_in_city);
        String weather_in_city = getResources().getString(R.string.weatherIn) + " " + getIntent().getStringExtra("nameCity");
        TextView textViewWeather = findViewById(R.id.textViewWeather);
        textViewWeather.setText(weather_in_city);
        Weather weather = (Weather) Objects.requireNonNull(getIntent().getExtras()).getSerializable("weather");

        if (weather != null) {
            TextView temperatureValue = findViewById(R.id.temperatureValue);
            temperatureValue.setText(weather.getTemperature() + "C" + (char) 176);
            TextView rainfallValue = findViewById(R.id.rainfallValue);
            rainfallValue.setText(weather.getRainfall() + "");

            if (getIntent().getBooleanExtra("checkAtmPressure", false)) {
                TableRow tblRowAtmPressure = findViewById(R.id.tblRowAtmPressure);
                tblRowAtmPressure.setVisibility(View.VISIBLE);
                TextView atmPressureValue = findViewById(R.id.atmPressureValue);
                atmPressureValue.setText(": " + weather.getAtm_pressure() + getResources().getString(R.string.atmPressureSymbol));
            }

            if (getIntent().getBooleanExtra("checkHumidity", false)) {
                TableRow tblRowHumidity = findViewById(R.id.tblRowHumidity);
                tblRowHumidity.setVisibility(View.VISIBLE);
                TextView humidityValue = findViewById(R.id.humidityValue);
                humidityValue.setText(": " + weather.getHumidity() + getResources().getString(R.string.humiditySymbol));
            }

            if (getIntent().getBooleanExtra("checkWindSpeed", false)) {
                TableRow tblRowWindSpeed = findViewById(R.id.tblRowWindSpeed);
                tblRowWindSpeed.setVisibility(View.VISIBLE);
                TextView windSpeedValue = findViewById(R.id.windSpeedValue);
                windSpeedValue.setText(": " + weather.getWind_speed() + getResources().getString(R.string.windSpeedSymbol));
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "WeatherInCity: onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "WeatherInCity: onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "WeatherInCity: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "WeatherInCity: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "WeatherInCity: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "WeatherInCity: onDestroy()");
    }
}

