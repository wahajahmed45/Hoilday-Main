package com.example.apparchilog.services.inter;


import com.example.apparchilog.models.requests.ActivityRequest;
import com.example.apparchilog.models.holiday.Activity;
import retrofit2.Call;
import retrofit2.http.*;

public interface IActivityServicePUT {

    @Headers("Content-Type: application/json")
    @PUT("vacations/{id_vacation}/activity/{id_activity}")
    Call<Activity> updateActivity(@Path("id_vacation") Long id_vacation, @Path("id_activity") Long id_activity, @Body ActivityRequest activityRequest, @Header("Authorization") String authHeader);
}
