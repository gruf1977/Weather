package ru.chizhikov.weather.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CityRepo {

    private static CityRepo singleton = null;
    private IOpenCity API;
    private CityRepo() {
        API = createAdapter();
    }
    public static CityRepo getSingleton() {
        if(singleton == null) {
            singleton = new CityRepo();
        }
        return singleton;
    }
    public IOpenCity getAPI() {
        return API;
    }

    private IOpenCity createAdapter() {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return adapter.create(IOpenCity.class);
    }
}
