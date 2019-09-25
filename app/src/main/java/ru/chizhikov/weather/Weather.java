package ru.chizhikov.weather;

import java.io.Serializable;

public class Weather implements Serializable {
    private String rainfall; // дождь, облачно, солнечно, снег, метель
    private int temperature;
    private int atmPressure;
    private int humidity;
    private int windSpeed;


    Weather(String rainfall, int temperature, int atmPressure, int humidity, int windSpeed) {
        this.rainfall = rainfall;
        this.temperature = temperature;
        this.atmPressure = atmPressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    String getRainfall() {
        return rainfall;
    }

    public void setRainfall(String rainfall) {
        this.rainfall = rainfall;
    }

    int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    int getAtm_pressure() {
        return atmPressure;
    }

    public void setAtm_pressure(int atm_pressure) {
        this.atmPressure = atm_pressure;
    }

    int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    int getWind_speed() {
        return windSpeed;
    }

    public void setWind_speed(int wind_speed) {
        this.windSpeed = wind_speed;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "rainfall='" + rainfall + '\'' +
                ", temperature=" + temperature +
                ", atmPressure=" + atmPressure +
                ", humidity=" + humidity +
                ", windSpeed=" + windSpeed +
                '}';
    }
}