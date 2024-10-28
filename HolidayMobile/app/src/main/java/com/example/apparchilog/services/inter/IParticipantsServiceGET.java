package com.example.apparchilog.services.inter;


import com.example.apparchilog.models.holiday.Participant;
import com.example.apparchilog.models.requests.ParticipantRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface IParticipantsServiceGET {

    @Headers("Content-Type: application/json")
    @GET("vacations/participants")
    Call<List< Participant>> findAllParticipants(@Header("Authorization") String token);
}
