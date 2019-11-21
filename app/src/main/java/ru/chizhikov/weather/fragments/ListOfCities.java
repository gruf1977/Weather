package ru.chizhikov.weather.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.chizhikov.weather.rest.CityRepo;
import ru.chizhikov.weather.rest.entities.CityRequestRestModel;
import ru.chizhikov.weather.MainActivity;
import ru.chizhikov.weather.OnSelectedPositionCity;
import ru.chizhikov.weather.R;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class ListOfCities extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 10;
    private static final String TAG = MainActivity.class.getName();
    private LocationManager locationManager;
    private int numberPosition;
    private ListView listView;
    private TextView emptyTextView;
    private boolean isLandscapeOrientation;
    private OnSelectedPositionCity listener;
    private Boolean flag = false;
    private String[] arrayCities;
    private ArrayAdapter adapter;

    public void setNumberPosition(int numberPosition) {
        this.numberPosition = numberPosition;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewListOfCities = inflater
                .inflate(R.layout.fragment_list_cities, container, false);

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        } else {
            requestLocationPermissions();
        }

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
        adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
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
        arrayCities =  nameCityStr.split(", ");
        return arrayCities;
    }



    @SuppressLint("MissingPermission")
    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 10000, 10,
                    new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        String lat = String.valueOf(location.getLatitude());
                        String lon = String.valueOf(location.getLongitude());
                        readCityFromInternet(lat, lon);
                    }
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
                @Override
                public void onProviderEnabled(String provider) {
                }
                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                Objects.requireNonNull(getActivity()),
                Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void readCityFromInternet(String lat, String lon){
        CityRepo.getSingleton().getAPI().loadWeather(lat, lon,
                "f3f2763fe63803beef4851d6365c83bc")
                .enqueue(new Callback<CityRequestRestModel>() {
            @Override
            public void onResponse(@NonNull Call<CityRequestRestModel> call,
                                   @NonNull Response<CityRequestRestModel> response) {
                if (response.body() != null && response.isSuccessful()) {
                    renderWeather(response.body());
                }
            }
            @Override
            public void onFailure(Call<CityRequestRestModel> call, Throwable t) {
                return;
            }
        });
    }

    private void renderWeather(CityRequestRestModel body) {
        String city = body.city;
        if (isAdded()) {
            ArrayList namesCities = new ArrayList(Arrays.asList(arrayCities));
            if (!namesCities.contains(city)) {
                addCity(city, namesCities);
            }
        }
    }

    private void addCity(final String city, final ArrayList nameCity) {
        Snackbar.make(Objects.requireNonNull(getView()),
                getResources().getString(R.string.you_in) + " " + city + " "
                        + getResources().getString(R.string.add_this_place), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.yes), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nameCity.add(city);
                        SharedPreferences listCities = Objects.requireNonNull(getActivity())
                                .getSharedPreferences("listCities", MODE_PRIVATE);
                        SharedPreferences.Editor ed = listCities.edit();
                        ed.putString("LIST_CITIES", String.valueOf(nameCity));
                        ed.apply();
                        createListView();
                        }
                }).show();
    }
}