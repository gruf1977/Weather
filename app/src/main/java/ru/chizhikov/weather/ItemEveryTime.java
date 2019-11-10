package ru.chizhikov.weather;

public class ItemEveryTime {
    String time;
    float speedWind;
    int pressure;
    int humidity;
    String description;
    String temperature;
    int picture;
    private String cityCountry;

    public ItemEveryTime(String time,
                         float speedWind,
                         int pressure,
                         int humidity,
                         String description,
                         String temperature,
                         int picture,
                         String cityCountry) {
        this.time = time;
        this.speedWind = speedWind;
        this.pressure = pressure;
        this.humidity = humidity;
        this.description = description;
        this.temperature = temperature;
        this.picture = picture;
        this.cityCountry = cityCountry;
    }

    public String getCityCountry() {
        return cityCountry;
    }

    public String getTime() {
        return time;
    }

    public float getSpeedWind() {
        return speedWind;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public String getDescription() {
        return description;
    }

    public String getTemperature() {
        return temperature;
    }

    public int getPicture() {
        return picture;
    }
}
