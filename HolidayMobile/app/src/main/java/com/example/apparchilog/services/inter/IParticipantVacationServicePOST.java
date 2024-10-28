package com.example.apparchilog.services.inter;


import com.example.apparchilog.models.requests.ParticipantRequest;
import com.example.apparchilog.models.holiday.Participant;
import retrofit2.Call;
import retrofit2.http.*;

public interface IParticipantVacationServicePOST {

    @Headers("Content-Type: application/json")
    @POST("vacations/{id_vacation}/participant")
    Call<Participant> createParticipantFromVacation(@Path("id_vacation") Long id_vacation, @Body ParticipantRequest participantRequest , @Header("Authorization") String token);
}

