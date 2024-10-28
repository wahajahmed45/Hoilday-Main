package com.example.apparchilog.services.inter;


import com.example.apparchilog.models.requests.ParticipantRequest;
import com.example.apparchilog.models.holiday.Participant;
import retrofit2.Call;
import retrofit2.http.*;

public interface IParticipantActivityServicePOST {

    @Headers("Content-Type: application/json")
    @POST("vacations/activity/{id_activity}/participant")
    Call<Participant> createParticipantFromActivity(@Path("id_activity") Long id_activity, @Body ParticipantRequest participantRequest , @Header("Authorization") String token);
}
