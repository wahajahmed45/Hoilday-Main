package com.example.apparchilog.services.inter;

import com.example.apparchilog.models.holiday.Event;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

import java.util.List;

public interface IEventVacationServiceGET {
    @Headers("Content-Type: application/json")
    @GET("vacations/schedule/vacation/{id_vacation}")
    Call<List<Event>> addEventToCalendar(@Path("id_vacation") Long id_vacation, @Header("Authorization") String authHeader);
}
