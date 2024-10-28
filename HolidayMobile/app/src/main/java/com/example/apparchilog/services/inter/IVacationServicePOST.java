package com.example.apparchilog.services.inter;


import com.example.apparchilog.models.requests.VacationRequest;
import com.example.apparchilog.models.holiday.Vacation;
import retrofit2.Call;
import retrofit2.http.*;

public interface IVacationServicePOST {

    @Headers("Content-Type: application/json")
    @POST("vacations")
    Call<Vacation> createVacation(@Body VacationRequest vacation , @Header("Authorization") String token);
}
