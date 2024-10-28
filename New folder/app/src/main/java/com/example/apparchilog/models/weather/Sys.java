package com.example.apparchilog.models.weather;

import java.io.Serializable;

public class Sys implements Serializable {
    private double type;
    private int id;
    private String country;
    private long sunrise;
    private long sunset;

    public double getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }
}