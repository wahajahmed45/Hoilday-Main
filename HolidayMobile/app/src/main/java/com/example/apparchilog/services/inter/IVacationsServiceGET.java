package com.example.apparchilog.services.inter;


import com.example.apparchilog.models.holiday.Vacation;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

import java.util.List;

public interface IVacationsServiceGET {

    @Headers("Content-Type: application/json")
    @GET("vacations")
    Call<List<Vacation>> getAllVacations(@Header("Authorization") String authHeader);
}
