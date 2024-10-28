package com.example.apparchilog.models.weather;

import java.io.Serializable;

public class Coord implements Serializable {
    private double lat;
    private double lon;

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}