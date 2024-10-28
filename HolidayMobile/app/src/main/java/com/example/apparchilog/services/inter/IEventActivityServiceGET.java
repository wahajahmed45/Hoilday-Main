package com.example.apparchilog.services.inter;

import com.example.apparchilog.models.holiday.Event;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface IEventActivityServiceGET {
    @Headers("Content-Type: application/json")
    @GET("vacations/schedule/activity/{id_activity}")
    Call<Event> addEventToCalendar(@Path("id_activity") Long id_activity, @Header("Authorization") String authHeader);
}
