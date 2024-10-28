package com.example.apparchilog.models.weather;

import java.io.Serializable;

public class Weathers implements Serializable {
    private String main;
    private String icon;
    private String description;

    public String getMain() {
        return main;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }
}