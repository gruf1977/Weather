package ru.chizhikov.weather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final String LOG_TAG = "myLogs";

    private long backPressedTime;
    private Toast backToast;
    Button btnOk;
    EditText textCity;
    CheckBox checkBoxAtmPressure;
    CheckBox checkBoxHumidity;
    CheckBox checkBoxWindSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "MainActivity: onCreate()");
        setContentView(R.layout.activity_main);
        initGreetingText();
        textCity = findViewById(R.id.editTextCity);
        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);

        checkBoxAtmPressure = findViewById(R.id.checkBoxAtmPressure);
        checkBoxHumidity = findViewById(R.id.checkBoxHumidity);
        checkBoxWindSpeed = findViewById(R.id.checkBoxWindSpeed);
    }

    private void initGreetingText() {
        TextView greetingTextView = findViewById(R.id.greetingTextView);
        String text = new GreetingsBuilder().getGreetings(getApplicationContext());
        greetingTextView.setText(text);
    }

    //подтверждение выхода из приложения
    @Override
    public void onBackPressed(){
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
        String nameCity = textCity.getText().toString();
        Weather weather;
        weather = new Weather(
                getResources().getString(R.string.rainfallValue),
                Integer.parseInt(getResources().getString(R.string.temperatureValue)),
                Integer.parseInt(getResources().getString(R.string.atmPressureValue)),
                Integer.parseInt(getResources().getString(R.string.humidityValue)),
                Integer.parseInt(getResources().getString(R.string.windSpeedValue)));

        if (nameCity.isEmpty()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.InputCity), Toast.LENGTH_SHORT).show();
        return;
        } else {
            Intent intent = new Intent(this, WeatherInCity.class);
            intent.putExtra("nameCity", nameCity);
            intent.putExtra("weather", weather);
            intent.putExtra("checkAtmPressure", checkBoxAtmPressure.isChecked());
            intent.putExtra("checkHumidity", checkBoxHumidity.isChecked());
            intent.putExtra("checkWindSpeed", checkBoxWindSpeed.isChecked());

            Log.d(LOG_TAG, "MainActivity intent: " + "\n" +
                    "nameCity: " + nameCity + "\n" +
                    "weather: " + weather.toString() + "\n" +
                    "checkAtmPressure: " + checkBoxAtmPressure.isChecked() + ", " +
                    "checkHumidity: " + checkBoxHumidity.isChecked() + ", " +
                    "checkWindSpeed: " + checkBoxWindSpeed.isChecked());

            startActivity(intent);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "MainActivity: onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "MainActivity: onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "MainActivity: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "MainActivity: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "MainActivity: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "MainActivity: onDestroy()");
    }

}
