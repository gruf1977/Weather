package ru.chizhikov.weather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final String LOG_TAG = "myLogs";
    String nameCity;
    private long backPressedTime;
    private Toast backToast;
    Button btnOk;
    CheckBox checkBoxAtmPressure;
    CheckBox checkBoxHumidity;
    CheckBox checkBoxWindSpeed;
    String[] cities;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "MainActivity: onCreate()");
        setContentView(R.layout.activity_main);
        initGreetingText();
        initViews();
        createSpinner();
    }
    private void initViews() {
        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
        checkBoxAtmPressure = findViewById(R.id.checkBoxAtmPressure);
        checkBoxHumidity = findViewById(R.id.checkBoxHumidity);
        checkBoxWindSpeed = findViewById(R.id.checkBoxWindSpeed);
        cities = getResources().getStringArray(R.array.cities);
    }
    private void createSpinner() {
        Spinner spinner = findViewById(R.id.cities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView
                .OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nameCity = (String)parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }
    private void initGreetingText() {
        TextView greetingTextView = findViewById(R.id.greetingTextView);
        String text = new GreetingsBuilder().getGreetings(getApplicationContext());
        greetingTextView.setText(text); }
    @Override
    public void onBackPressed(){
        //подтверждение выхода из приложения
        if (backPressedTime +2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            String msgForToast = getResources().getString(R.string.clickForExit);
            backToast = Toast.makeText(getBaseContext(), msgForToast, Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
    @Override
    public void onClick(View v) {
        Log.d(LOG_TAG, "MainActivity: onClick()");
        Weather weather;
        weather = new Weather(
                getResources().getString(R.string.rainfallValue),
                Integer.parseInt(getResources().getString(R.string.temperatureValue)),
                Integer.parseInt(getResources().getString(R.string.atmPressureValue)),
                Integer.parseInt(getResources().getString(R.string.humidityValue)),
                Integer.parseInt(getResources().getString(R.string.windSpeedValue)));
        createActivityWeatherInCity(weather);
    }
    private void createActivityWeatherInCity(Weather weather) {
        Intent intent = new Intent(this, WeatherInCity.class);
        intent.putExtra("nameCity", nameCity);
        intent.putExtra("weather", weather);
        intent.putExtra("checkAtmPressure", checkBoxAtmPressure.isChecked());
        intent.putExtra("checkHumidity", checkBoxHumidity.isChecked());
        intent.putExtra("checkWindSpeed", checkBoxWindSpeed.isChecked());
        startActivity(intent);
    }
}




