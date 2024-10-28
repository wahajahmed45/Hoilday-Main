package com.example.apparchilog.services.inter;

import com.example.apparchilog.models.requests.LoginRequest;
import com.example.apparchilog.models.responses.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ILoginService {

    @Headers("Content-Type: application/json")
    @POST("auth/signing")
    Call<LoginResponse> login(@Body LoginRequest loginBody);
}
