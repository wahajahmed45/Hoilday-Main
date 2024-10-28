package com.example.apparchilog.services.inter;


import com.example.apparchilog.models.holiday.Activity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface IActivityServiceGET {

    @Headers("Content-Type: application/json")
    @GET("vacations/activity/{id_activity}")
    Call<Activity> getActivity(@Path("id_activity") Long id_activity, @Header("Authorization") String authHeader);
}
