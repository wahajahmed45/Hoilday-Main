package com.example.apparchilog.services.inter;


import com.example.apparchilog.models.responses.MessageResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface IVacationServiceDelete {

    @Headers("Content-Type: application/json")
    @DELETE("vacations/{id_vacation}/activity/{id_activity}")
    Call<MessageResponse> removeActivityFromVacation(@Path("id_vacation") Long id_vacation, @Path("id_activity") Long id_activity, @Header("Authorization") String token);
}
