package ru.chizhikov.weather.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Objects;

import ru.chizhikov.weather.OnSelectedPositionCity;
import ru.chizhikov.weather.R;
import ru.chizhikov.weather.WeatherInCity;

public class ListOfCities extends Fragment {
    private int numberPosition;
    private ListView listView;
    private TextView emptyTextView;
    private boolean isLandscapeOrientation;
    private OnSelectedPositionCity listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewListOfCities = inflater
                .inflate(R.layout.fragment_list_cities, container, false);
        initViews(viewListOfCities);
        if (savedInstanceState == null){
            numberPosition = 0;
        } else {
            numberPosition = savedInstanceState.getInt("CurrentCity", 0);
        }
        checkPosition();
        return viewListOfCities;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isLandscapeOrientation = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        listener = (OnSelectedPositionCity) getActivity();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (isLandscapeOrientation){
            Objects.requireNonNull(listener).onPositionSelected(numberPosition);
        }
    }
    private void initViews(View view) {
        listView = view.findViewById(R.id.cities_list_view);
        emptyTextView = view.findViewById(R.id.cities_list_empty_view);
        createListView();
    }
    private void createListView() {
        ArrayAdapter adapter =
                ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()),
                        R.array.cities, android.R.layout.simple_list_item_activated_1);
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyTextView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                numberPosition = position;
                getWeatherParameters();
            }
        });
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("CurrentCity", numberPosition);
        super.onSaveInstanceState(outState);
    }
    private void checkPosition(){
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setItemChecked(numberPosition, true);
    }
    private void getWeatherParameters() {
        checkPosition();
        createInfoWeatherInCity();
    }
    private void createInfoWeatherInCity() {
        if (isLandscapeOrientation) {
            Objects.requireNonNull(listener).onPositionSelected(numberPosition);
        } else {
            Intent intent = new Intent(getActivity(), WeatherInCity.class);
            intent.putExtra("numberPosition", numberPosition);
            startActivity(intent);
        }
    }
}

