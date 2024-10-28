package com.example.apparchilog.repositories;


import com.example.apparchilog.models.responses.Weather;
import com.example.apparchilog.repositories.inter_responses.IWeatherResponse;
import com.example.apparchilog.services.RetrofitWeatherClientInstance;
import com.example.apparchilog.services.inter.IWeatherService;
import com.google.android.gms.maps.model.LatLng;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;


public class WeatherRepository {

    public void getWeather(double lat, double lon, IWeatherResponse iWeatherResponse) {
        IWeatherService service = RetrofitWeatherClientInstance.getRetrofitInstance().create(IWeatherService.class);
        Call<Weather> call = service.getCurrentWeather(lat, lon, "1786c2ff4290a191b28bc12aa26ff959");
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(@NotNull Call<Weather> call, @NotNull Response<Weather> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LatLng> path = new ArrayList<>();
                    iWeatherResponse.onResponse(response.body());
                }else{
                    iWeatherResponse.onFailure(new Throwable("Failed to load météo"));
                }
            }
            @Override
            public void onFailure(@NotNull Call<Weather> call, @NotNull Throwable t) {
                iWeatherResponse.onFailure(t);
            }
        });

    }


}
