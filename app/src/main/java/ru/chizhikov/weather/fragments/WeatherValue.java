package ru.chizhikov.weather.fragments;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import ru.chizhikov.weather.DataClass;
import ru.chizhikov.weather.R;
import ru.chizhikov.weather.RecyclerViewAdapter;

public class WeatherValue extends Fragment {
    private RecyclerView recyclerView;
    private TextView tvTextViewWeather;
    private TextView tvTemperatureValue;
    private TextView tvRainfallValue;
    private TextView tvHumidity;
    private TextView tvAtmPressure;
    private TextView tvWindSpeed;
    private String[] arraysCities;
    private String[] arraysTemperature;
    private String[] arraysWindSpeed;
    private String[] arraysHumidity;
    private String[] arraysAtmPressure;
    private String[] arraysRainfall;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View weatherValue = inflater.inflate(R.layout.fragment_weather_value,
                container, false);
        initViews(weatherValue);
        initRecycler(weatherValue);
        initRecyclerView();
        return weatherValue;
    }
    private void initRecyclerView() {
        DataClass[] data = new DataClass[7];
        String[] arraysTemperatureFuture = getResources().getStringArray(R.array.temper_future);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar instance = Calendar.getInstance();
        int[] arraysPicture = getImageArray();
        for (int i=0; data.length > i; i++) {
            instance.add(Calendar.DAY_OF_MONTH, 1);// прибавляем 3 дня к установленной дате
            Date newDate = instance.getTime(); // получаем измененную дату
            String newDateString =  dateFormat.format(newDate);
            data[i] = new DataClass(arraysPicture[i], newDateString, arraysTemperatureFuture[i]+ "C" + (char) 176);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(data);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    private int[] getImageArray() {
        @SuppressLint("Recycle") TypedArray pictures = getResources().obtainTypedArray(R.array.pictures);
        int length = pictures.length();
        int[] answer = new int[length];
        for(int i = 0; i < length; i++){
            answer[i] = pictures.getResourceId(i, 0);
        }
        return answer;
    }
    private void initRecycler(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
    }
    private void initViews(View view){
        tvTextViewWeather = view.findViewById(R.id.textViewWeather);
        tvTemperatureValue = view.findViewById(R.id.temperatureValue);
        tvRainfallValue = view.findViewById(R.id.rainfallValue);
        tvHumidity = view.findViewById(R.id.humidity);
        tvAtmPressure = view.findViewById(R.id.atmPressure);
        tvWindSpeed = view.findViewById(R.id.windSpeed);
        arraysCities = getResources().getStringArray(R.array.cities);
        arraysTemperature = getResources().getStringArray(R.array.temperature);
        arraysWindSpeed = getResources().getStringArray(R.array.windSpeed);
        arraysHumidity = getResources().getStringArray(R.array.humidity);
        arraysAtmPressure = getResources().getStringArray(R.array.atmPressure);
        arraysRainfall = getResources().getStringArray(R.array.rainfall);
    }
    @SuppressLint("SetTextI18n")
    public void setWeatherValue(int numberPosition) {
        String nameCity = arraysCities[numberPosition];
        String temperature = arraysTemperature[numberPosition];
        String windSpeed = arraysWindSpeed[numberPosition];
        String humidity = arraysHumidity[numberPosition];
        String atmPressure = arraysAtmPressure[numberPosition];
        String rainfall = arraysRainfall[numberPosition];
        tvTextViewWeather.setText(getResources().getString(R.string.weatherIn) + nameCity);
        tvTextViewWeather.setVisibility(View.VISIBLE);
        tvTemperatureValue.setText(temperature + "C" + (char) 176);
        tvTemperatureValue.setVisibility(View.VISIBLE);
        tvRainfallValue.setText(rainfall + "");
        tvRainfallValue.setVisibility(View.VISIBLE);
        tvWindSpeed.setText(getResources().getString(R.string.windSpeed) + ": " +
                windSpeed + getResources().getString(R.string.windSpeedSymbol));
        tvWindSpeed.setVisibility(View.VISIBLE);
        tvHumidity.setText(getResources().getString(R.string.humidity) + ": " +
                humidity + getResources().getString(R.string.humiditySymbol));
        tvHumidity.setVisibility(View.VISIBLE);
        tvAtmPressure.setText(getResources().getString(R.string.atmPressure) + ": " +
                atmPressure + getResources().getString(R.string.atmPressureSymbol));
        tvAtmPressure.setVisibility(View.VISIBLE);
    }
}
