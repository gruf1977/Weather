package ru.chizhikov.weather.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.chizhikov.weather.DBHelper;
import ru.chizhikov.weather.ItemEveryTime;
import ru.chizhikov.weather.ItemViewAdapter;
import ru.chizhikov.weather.R;
import ru.chizhikov.weather.RecyclerViewAdapter;
import ru.chizhikov.weather.rest.OpenWeatherRepo;
import ru.chizhikov.weather.rest.entities.WeatherRequestRestModel;
import static android.content.Context.MODE_PRIVATE;
import static ru.chizhikov.weather.MainActivity.fab;

public class WeatherValue extends Fragment {
    private long timeCash = 3600000;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewBottom;
    private TextView tvTextViewWeather;
    private TextView tvTemperatureValue;
    private TextView tvRainfallValue;
    private TextView tvHumidity;
    private TextView tvAtmPressure;
    private TextView tvWindSpeed;
    private TextView tvTextData;
    private ImageView tvImageWeather;
    private String[] arraysCities;
    private ProgressBar progressBar;
    private int numPosition;
    private int dayPosition;
    private int[] arraysPicture;
    private ArrayList<ItemEveryTime> data;
    private ArrayList<ArrayList> listEveryTimeOnDate;
    private ArrayList<ItemEveryTime> listOfObject;
    private boolean dataFromdb = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View weatherValue = inflater.inflate(R.layout.fragment_weather_value,
                container, false);
        initViews(weatherValue);
        return weatherValue;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("dayPosition", dayPosition);
    }

    public void setNumPosition(int numPosition) {
        this.numPosition = numPosition;
    }

    private void initViews(View view){
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerViewBottom = view.findViewById(R.id.recyclerViewBottom);
        progressBar = view.findViewById(R.id.progressBar);
        arraysPicture = getImageArray();
        tvImageWeather = view.findViewById(R.id.imageWeather);
        tvTextData = view.findViewById(R.id.textData);
        tvTextViewWeather = view.findViewById(R.id.textViewWeather);
        tvTemperatureValue = view.findViewById(R.id.temperatureValue);
        tvRainfallValue = view.findViewById(R.id.rainfallValue);
        tvHumidity = view.findViewById(R.id.humidity);
        tvAtmPressure = view.findViewById(R.id.atmPressure);
        tvWindSpeed = view.findViewById(R.id.windSpeed);
        arraysCities = setArrayCities();
    }

    @SuppressLint("SetTextI18n")
    private void setWeatherValueOnDate(int dayPosition) {
        try {
            ArrayList listItemsEveryTimeForMenu = listEveryTimeOnDate.get(dayPosition);
            int nowPos = 0;
            if (dayPosition > 0) {
                nowPos = 5;
            }
            ItemEveryTime itemEveryTimeForMenu =
                    (ItemEveryTime) listItemsEveryTimeForMenu.get(nowPos);
            tvImageWeather.setImageResource(itemEveryTimeForMenu.getPicture());
            tvTextData.setText(itemEveryTimeForMenu.getTime());
            tvTextViewWeather.setText(getResources().getString(R.string.weatherIn)
                    + itemEveryTimeForMenu.getCityCountry());
            tvTemperatureValue.setText(itemEveryTimeForMenu.getTemperature());
            tvRainfallValue.setText(itemEveryTimeForMenu.getDescription() + "");
            tvWindSpeed.setText(getResources().getString(R.string.windSpeed)
                    + itemEveryTimeForMenu.getSpeedWind()
                    + getResources().getString(R.string.windSpeedSymbol));
            tvHumidity.setText(getResources().getString(R.string.humidity)
                    + itemEveryTimeForMenu.getHumidity()
                    + getResources().getString(R.string.humiditySymbol));
            tvAtmPressure.setText(getResources().getString(R.string.atmPressure)
                    + itemEveryTimeForMenu.getPressure() + getResources()
                    .getString(R.string.atmPressureSymbol));
            setViewsVisible();
            initRecyclerViewBottom(dayPosition);
        } catch (Exception e){
            Toast.makeText(getContext(), R.string.Err, Toast.LENGTH_SHORT).show();
        }
    }

    private void setViewsVisible(){
        tvImageWeather.setVisibility(View.VISIBLE);
        tvTextData.setVisibility(View.VISIBLE);
        tvTextViewWeather.setVisibility(View.VISIBLE);
        tvTemperatureValue.setVisibility(View.VISIBLE);
        tvRainfallValue.setVisibility(View.VISIBLE);
        tvWindSpeed.setVisibility(View.VISIBLE);
        tvHumidity.setVisibility(View.VISIBLE);
        tvAtmPressure.setVisibility(View.VISIBLE);
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
            recyclerViewBottom.setVisibility(View.VISIBLE);
        else recyclerViewBottom.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            dayPosition = savedInstanceState.getInt("dayPosition", 0);
        }
        updateWeatherData();
    }

    private void updateWeatherData() {
        final String uriLang = getResources().getString(R.string.uri);
        final String city = arraysCities[numPosition];
        DBHelper dbHelper = new DBHelper(getContext());
        ArrayList<ItemEveryTime> listFromDB = dbHelper.readWeatherInCityByCity(city);
        if (listFromDB.size()>0) {
            if (listFromDB.get(0).getTimeStamp() > System.currentTimeMillis()) {
                dataFromdb = true;
                printWeatherFromBD(listFromDB);
                return;
            } else {
                dataFromdb = false;
                dbHelper.deleteWeatherInCityByCity(city);
            }
        }
        dbHelper.close();
        readWeatherFromInternet(uriLang, city);
    }

    @SuppressLint("SetTextI18n")
    private void readWeatherFromInternet(String uriLang, String city) {
        progressBar.setVisibility(View.VISIBLE);
        tvTextViewWeather.setText(getResources().getString(R.string.find_place)
                + " " + arraysCities[numPosition]);
        tvTextViewWeather.setVisibility(View.VISIBLE);

        OpenWeatherRepo.getSingleton().getAPI().loadWeather(city, uriLang,
                "metric", "f3f2763fe63803beef4851d6365c83bc")
                .enqueue(new Callback<WeatherRequestRestModel>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequestRestModel> call,
                                           @NonNull Response<WeatherRequestRestModel> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            if(isAdded()) {
                                renderWeather(response.body());
                            }
                        } else {
                            if(isAdded()) {
                                tvTextViewWeather.setText(getResources()
                                        .getString(R.string.find_not_place)
                                        + " " + arraysCities[numPosition]);
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequestRestModel> call, Throwable t) {
                        if(isAdded()) {
                            tvTextViewWeather.setText(getResources().getString(R.string.no_internet));
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void printWeatherFromBD(ArrayList<ItemEveryTime> listFromDB) {
        tvTextData.setVisibility(View.VISIBLE);
        tvTextData.setText(listFromDB.get(0).getCityCountry());
        listEveryTimeOnDate = new ArrayList<>();
        data =  new ArrayList<>();
        int j = -1;
        String dateStrOld="";
        for (int i= 0 ; i<listFromDB.size(); i++){
            String[] dateStrTemp = listFromDB.get(i).getTime().split(" ");
            if (!dateStrOld.equals(dateStrTemp[0])) {
                if (j!=-1){
                    listEveryTimeOnDate.add(listOfObject);
                }
                listOfObject = new ArrayList<>();
                j++;
                dateStrOld = dateStrTemp[0];
            }
            ItemEveryTime itemEveryTime = listFromDB.get(i);
            listOfObject.add(itemEveryTime);
            String strTimeWeather = "15:00:00";
            if (i==0 && j==0){
                data.add(itemEveryTime);
            } else if (dateStrTemp[1].equals(strTimeWeather) && j!=0) {
                data.add(itemEveryTime);
            }
        }
        progressBar.setVisibility(View.GONE);
        initRecyclerView();
        initRecyclerViewBottom(dayPosition);
        setWeatherValueOnDate(dayPosition);
    }

    private void renderWeather(WeatherRequestRestModel model) {
        ArrayList<ItemEveryTime> listWeatherForBD = new ArrayList();
        tvTextData.setVisibility(View.VISIBLE);
        tvTextData.setText(String.format("%s %s", model.city.nameCity, model.city.country));
        listEveryTimeOnDate = new ArrayList<>();
        data =  new ArrayList<>();
        int j = -1;
        String dateStrOld="";
        for (int i= 0 ; i<model.listRestModels.length; i++){
            String[] dateStrTemp = model.listRestModels[i].dataTime.split(" ");
            if (!dateStrOld.equals(dateStrTemp[0])) {
                if (j!=-1){
                    listEveryTimeOnDate.add(listOfObject);
                }
                listOfObject = new ArrayList<>();
                j++;
                dateStrOld = dateStrTemp[0];
            }
            String strTemp;
            if (model.listRestModels[i].main.temp>0){
                strTemp = "+" + model.listRestModels[i].main.temp + "C" + (char) 176;
            } else {
                strTemp = model.listRestModels[i].main.temp + "C" + (char) 176;
            }
            ItemEveryTime itemEveryTime = new ItemEveryTime(model.city.nameCity,
                    model.listRestModels[i].dataTime,
                    model.listRestModels[i].wind.speed,
                    model.listRestModels[i].main.pressure,
                    model.listRestModels[i].main.humidity,
                    model.listRestModels[i].weather[0].description,
                    strTemp,
                    setWeatherIcon(model.listRestModels[i].weather[0].id),
                    model.city.nameCity + ", " + model.city.country,
                    System.currentTimeMillis()+ timeCash);
            listWeatherForBD.add(itemEveryTime);
            listOfObject.add(itemEveryTime);
            String strTimeWeather = "15:00:00";
            if (i==0 && j==0){
                data.add(itemEveryTime);
            } else if (dateStrTemp[1].equals(strTimeWeather) && j!=0) {
                data.add(itemEveryTime);
            }
        }
        DBHelper dbHelper = new DBHelper(getContext());
        dbHelper.writeListWeatherForBD(listWeatherForBD);
        dbHelper.close();
        progressBar.setVisibility(View.GONE);
        initRecyclerView();
        initRecyclerViewBottom(dayPosition);
        setWeatherValueOnDate(dayPosition);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL, false);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(data);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                dayPosition = position;
                setWeatherValueOnDate(dayPosition);
            }
        });
    }

    private void initRecyclerViewBottom(int position) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ItemViewAdapter adapter = new ItemViewAdapter(listEveryTimeOnDate.get(position));
        recyclerViewBottom.setLayoutManager(layoutManager);
        recyclerViewBottom.setAdapter(adapter);
        recyclerViewBottom.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });
    }

    private int[] getImageArray() {
        @SuppressLint("Recycle") TypedArray pictures = getResources()
                .obtainTypedArray(R.array.pictures);
        int length = pictures.length();
        int[] answer = new int[length];
        for(int i = 0; i < length; i++){
            answer[i] = pictures.getResourceId(i, 0);
        }
        return answer;
    }

    private int setWeatherIcon(int actualId) {
        int id = actualId / 100;
        int picture = 0;
        if(actualId == 800) {
            picture = arraysPicture[1];
        } else if (actualId == 803) {
            picture = arraysPicture[0];
        } else if (actualId == 801) {
            picture = arraysPicture[3];
        } else if (actualId == 502) {
            picture = arraysPicture[4];
        }else if (actualId == 602) {
            picture = arraysPicture[5];
        }else if (actualId == 500) {
            picture = arraysPicture[6];
        }else if (actualId == 600) {
            picture = arraysPicture[7];
        }else if (actualId == 501) {
            picture = arraysPicture[8];
        }else if (actualId == 804) {
            picture = arraysPicture[9];
        }else if (actualId == 521) {
            picture = arraysPicture[10];
        }else if (actualId == 200) {
            picture = arraysPicture[14];
        }else if (actualId == 503) {
            picture = arraysPicture[15];
        }else if (actualId == 771) {
            picture = arraysPicture[16];
        }else if (actualId == 802) {
            picture = arraysPicture[16];
        }else if (actualId == 611) {
            picture = arraysPicture[11];
        } else {
            switch (id) {
                case 2: {
                    picture = arraysPicture[14];
                    break;
                }
                case 3: {
                    picture = arraysPicture[2];
                    break;
                }
                case 5: {
                    picture = arraysPicture[17];
                    break;
                }
                case 6: {
                    picture = arraysPicture[13];
                    break;
                }
                case 7: {
                    picture = arraysPicture[12];
                    break;
                }
                case 8: {
                    picture = arraysPicture[0];
                    break;
                }
            }
        }
        return picture;
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