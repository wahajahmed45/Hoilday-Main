package com.example.apparchilog.services.inter;


import com.example.apparchilog.models.requests.RegisterRequest;
import com.example.apparchilog.models.responses.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IRegisterService {

    @Headers("Content-Type: application/json")
    @POST("auth/signup")
    Call<RegisterResponse> register(@Body RegisterRequest registerBody);
}
