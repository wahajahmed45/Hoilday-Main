package com.example.apparchilog.services.inter;

import com.example.apparchilog.models.responses.Weather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IWeatherService {
   /* @GET("maps/api/directions/json")
    Call<GoogleResponse> getDirections(@Query("origin") String origin,
                                       @Query("destination") String destination,
                                       @Query("key") String apiKey
    );*/
    @GET("weather")
    Call<Weather> getCurrentWeather(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String apiKey);
}
