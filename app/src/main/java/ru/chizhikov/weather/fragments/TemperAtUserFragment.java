package ru.chizhikov.weather.fragments;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Objects;
import ru.chizhikov.weather.R;
import static android.content.Context.SENSOR_SERVICE;

public class TemperAtUserFragment extends Fragment {
    private TextView textTemperature;
    private TextView textHumidity;
    private SensorManager sensorManager;
    private Sensor sensorTemperature;
    private Sensor sensorHumidity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewTemperAtUserFragment = inflater
                .inflate(R.layout.fragment_temper_at_user, container, false);
        initViews(viewTemperAtUserFragment);
        getSensors();
        return viewTemperAtUserFragment;
    }

    private void initViews(View view) {
        textTemperature =  view.findViewById(R.id.text_temperature_sensor);
        textHumidity =  view.findViewById(R.id.text_humidity_sensor);
    }

    private void getSensors() {
        sensorManager = (SensorManager) Objects.requireNonNull(getContext())
                .getSystemService(SENSOR_SERVICE);
        sensorTemperature = Objects.requireNonNull(sensorManager)
                .getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (sensorTemperature == null){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n")
                    .append(getResources().getString(R.string.temperature))
                    .append("\n")
                    .append(getResources().getString(R.string.sensor_is_not_detected));
            textTemperature.setText(stringBuilder);
        }
        sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (sensorHumidity == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n")
                    .append(getResources().getString(R.string.humidity))
                    .append("\n")
                    .append(getResources().getString(R.string.sensor_is_not_detected))
                    .append("\n");
            textHumidity.setText(stringBuilder);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(listenerTemperature, sensorTemperature,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listenerHumidity, sensorHumidity,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listenerTemperature, sensorTemperature);
        sensorManager.unregisterListener(listenerHumidity, sensorHumidity);
    }

    private void showTemperatureSensors(SensorEvent event){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n")
                .append(getResources().getString(R.string.temperature))
                .append(":").append("\n")
                .append(event.values[0])
                .append("C")
                .append((char) 176);
        textTemperature.setText(stringBuilder);
    }

    private void showHumiditySensors(SensorEvent event){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n")
                .append(getResources().getString(R.string.humidity))
                .append("\n")
                .append(event.values[0])
                .append(getResources().getString(R.string.humiditySymbol));
        textHumidity.setText(stringBuilder);
    }

    private SensorEventListener listenerTemperature = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        @Override
        public void onSensorChanged(SensorEvent event) {
            showTemperatureSensors(event);
        }
    };

    private SensorEventListener listenerHumidity = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        @Override
        public void onSensorChanged(SensorEvent event) {
            showHumiditySensors(event);
        }
    };
}

