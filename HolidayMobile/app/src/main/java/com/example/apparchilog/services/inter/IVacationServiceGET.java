package com.example.apparchilog.services.inter;


import com.example.apparchilog.models.holiday.Vacation;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface IVacationServiceGET {

    @Headers("Content-Type: application/json")
    @GET("vacations/{id}")
    Call<Vacation> getVacation(@Path("id") long id, @Header("Authorization") String authHeader);
}
