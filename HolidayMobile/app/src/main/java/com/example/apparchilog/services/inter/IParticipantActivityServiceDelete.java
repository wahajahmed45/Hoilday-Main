package com.example.apparchilog.services.inter;


import com.example.apparchilog.models.responses.MessageResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface IParticipantActivityServiceDelete {

    @Headers("Content-Type: application/json")
    @DELETE("vacations/activity/{id_activity}/participant/{id_participant}")
    Call<MessageResponse> removeParticipantFromActivity(@Path("id_activity") Long id_activity, @Path("id_participant") Long id_participant,
                                                        @Header("Authorization") String token);
}
