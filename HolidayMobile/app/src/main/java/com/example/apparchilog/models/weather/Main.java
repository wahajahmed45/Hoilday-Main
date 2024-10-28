package com.example.apparchilog.models.weather;

import java.io.Serializable;

public class Main implements Serializable {
    private double temp;
    private double feels_like;
    private double temp_min;
    private double temp_max;
    private int pressure;
    private int humidity;
    private int sea_level;
    private double grnd_level;

    public double getTemp() {
        return temp;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public int getPressure() {
        return pressure;
    }

    public long getHumidity() {
        return humidity;
    }

    public int getSea_level() {
        return sea_level;
    }

    public double getGrnd_level() {
        return grnd_level;
    }
}