package com.example.apparchilog.services.inter;


import com.example.apparchilog.models.holiday.Activity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

import java.util.List;

public interface IActivitiesServiceGET {

    @Headers("Content-Type: application/json")
    @GET("vacations/activities")
    Call<List<Activity>> getAllActivities(@Header("Authorization") String authHeader);
}
