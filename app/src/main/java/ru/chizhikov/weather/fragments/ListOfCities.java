package ru.chizhikov.weather.fragments;

import android.content.SharedPreferences;
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
import ru.chizhikov.weather.MainActivity;
import ru.chizhikov.weather.OnSelectedPositionCity;
import ru.chizhikov.weather.R;
import static android.content.Context.MODE_PRIVATE;

public class ListOfCities extends Fragment {
    private int numberPosition;
    private ListView listView;
    private TextView emptyTextView;
    private boolean isLandscapeOrientation;
    private OnSelectedPositionCity listener;
    private Boolean flag = false;

    public void setNumberPosition(int numberPosition) {
        this.numberPosition = numberPosition;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewListOfCities = inflater
                .inflate(R.layout.fragment_list_cities, container, false);
        initViews(viewListOfCities);
        numberPosition = MainActivity.getNumberPositionTemp();
        return viewListOfCities;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLandscapeOrientation){
            listener.onPositionSelected(numberPosition);
        } else if (flag){
            listener.onPositionSelected(numberPosition);
            flag = false;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isLandscapeOrientation = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        listener = (OnSelectedPositionCity) getActivity();
        checkPosition();
    }

    private void initViews(View view) {
        listView = view.findViewById(R.id.cities_list_view);
        emptyTextView = view.findViewById(R.id.cities_list_empty_view);
        createListView();
    }

    private void createListView() {
        ArrayAdapter adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_list_item_activated_1,  setArrayCities());
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
        MainActivity.setNumberPositionTemp(numberPosition);
        super.onSaveInstanceState(outState);
    }

    private void getWeatherParameters() {
        checkPosition();
        listener.onPositionSelected(numberPosition);
    }

    private void checkPosition(){
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setItemChecked(numberPosition, true);
    }

    private String[] setArrayCities() {
        SharedPreferences listCities = Objects.requireNonNull(getActivity())
                .getSharedPreferences("listCities",MODE_PRIVATE);
        String nameCityStr = listCities.getString("LIST_CITIES", "Moscow");
        nameCityStr = nameCityStr.replace("[", "");
        nameCityStr = nameCityStr.replace("]", "");
        return nameCityStr.split(", ");
    }
}