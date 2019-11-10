package ru.chizhikov.weather.rest.entities;

import com.google.gson.annotations.SerializedName;

class CoordRestModel {
    @SerializedName("lat") public float lat;
    @SerializedName("lon") public float lon;
}
