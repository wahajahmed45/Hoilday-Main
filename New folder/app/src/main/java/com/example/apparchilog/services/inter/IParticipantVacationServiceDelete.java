package com.example.apparchilog.services.inter;


import com.example.apparchilog.models.responses.MessageResponse;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface IParticipantVacationServiceDelete {

    @Headers("Content-Type: application/json")
    @DELETE("vacations/{id_vacation}/participant/{id_participant}")
    Call<MessageResponse> removeParticipantFromVacation(@Path("id_vacation") Long id_vacation, @Path("id_participant") Long id_participant,
                                                        @Header("Authorization") String token);
}

