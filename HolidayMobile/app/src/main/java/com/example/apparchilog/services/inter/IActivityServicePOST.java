package com.example.apparchilog.services.inter;


import com.example.apparchilog.models.requests.ActivityRequest;
import com.example.apparchilog.models.holiday.Activity;
import retrofit2.Call;
import retrofit2.http.*;

public interface IActivityServicePOST {

    @Headers("Content-Type: application/json")
    @POST("vacations/{id_vacation}/activity")
    Call<Activity> createActivity(@Path("id_vacation") Long id_vacation, @Body ActivityRequest activityRequest , @Header("Authorization") String token);
}
