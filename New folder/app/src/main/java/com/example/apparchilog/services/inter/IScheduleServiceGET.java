package com.example.apparchilog.services.inter;


import com.example.apparchilog.models.holiday.Event;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

import java.util.List;

public interface IScheduleServiceGET {

    @Headers("Content-Type: application/json")
    @GET("vacations/schedule/event")
    Call<List<Event>> getAllSchedules(@Header("Authorization") String authHeader);
}
