package ru.chizhikov.weather;

public class ItemEveryTime {
    String city;
    String time;
    float speedWind;
    int pressure;
    int humidity;
    String description;
    String temperature;
    int picture;
    private String cityCountry;
    private long timeStamp;

    public ItemEveryTime(String city,
                         String time,
                         float speedWind,
                         int pressure,
                         int humidity,
                         String description,
                         String temperature,
                         int picture,
                         String cityCountry,
                         long timeStamp) {
        this.city = city;
        this.time = time;
        this.speedWind = speedWind;
        this.pressure = pressure;
        this.humidity = humidity;
        this.description = description;
        this.temperature = temperature;
        this.picture = picture;
        this.cityCountry = cityCountry;
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getCity() {
        return city;
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
