package com.example.apparchilog.repositories.inter_responses;

import com.example.apparchilog.models.responses.Weather;

public interface IWeatherResponse {

    void onResponse(Weather weather);

    void onFailure(Throwable t);
}

