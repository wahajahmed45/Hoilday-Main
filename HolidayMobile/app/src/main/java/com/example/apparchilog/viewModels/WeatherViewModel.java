package com.example.apparchilog.viewModels;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.apparchilog.R;
import com.example.apparchilog.fragments.WeatherFragment;
import com.example.apparchilog.models.responses.Weather;
import com.example.apparchilog.repositories.WeatherRepository;
import com.example.apparchilog.repositories.inter_responses.IWeatherResponse;
import com.example.apparchilog.views.HomeActivity;
import com.example.apparchilog.views.NavigationActivity;

public class WeatherViewModel extends ViewModel {

    private final WeatherRepository weatherRepository;
    private final MutableLiveData<String> weatherMessageLiveData = new MutableLiveData<>();
    private final MutableLiveData<Weather> weatherMutableLiveData;

    private final MutableLiveData<Boolean> isLoading;

    public WeatherViewModel() {
        weatherRepository = new WeatherRepository();
        isLoading = new MutableLiveData<>();
        weatherMutableLiveData = new MutableLiveData<>();
    }
public void fetchWeather(double latitude, double longitude) {
        isLoading.setValue(true);
        weatherRepository.getWeather(latitude, longitude, new IWeatherResponse() {
            @Override
            public void onResponse(Weather weather) {
                if (weather != null) {
                    isLoading.setValue(false);
                    weatherMutableLiveData.setValue(weather);
                    weatherMessageLiveData.setValue("Loading success");
                    Log.d("TAG_WEATHER", weather.toString());
                }
            }
            @Override
            public void onFailure(Throwable t) {
                isLoading.setValue(false);
                weatherMessageLiveData.setValue("Loading failed");
            }
        });

    }
    private double roundToOneDecimal(double value) {
        double roundedValue = Math.round(value * 10.0) / 10.0;
        return (roundedValue < 0) ? -1.0 : roundedValue;
    }
    public MutableLiveData<Weather> getWeatherMutableLiveData() {return weatherMutableLiveData;}
    public MutableLiveData<String> getWeatherMessageLiveData() {
        return weatherMessageLiveData;
    }
    public MutableLiveData<Boolean> getIsLoading() {return isLoading;}


}

