package com.example.apparchilog.models.responses;

import com.example.apparchilog.models.weather.*;

import java.io.Serializable;
import java.util.List;

public class Weather implements Serializable {

    private Coord coord;
    private List<Weathers> weather;
    private String base;
    private Main main;
    private double visibility;
    private Wind wind;
    private Clouds clouds;
    private double dt;
    private Sys sys;
    private long timezone;
    private long id;
    private String name;
    private long cod;

    public Coord getCoord() {
        return coord;
    }

    public List<Weathers> getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return main;
    }

    public double getVisibility() {
        return visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public double getDt() {
        return dt;
    }

    public Sys getSys() {
        return sys;
    }

    public long getTimezone() {
        return timezone;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getCod() {
        return cod;
    }
}
