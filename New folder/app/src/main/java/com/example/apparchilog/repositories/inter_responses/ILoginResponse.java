package com.example.apparchilog.repositories.inter_responses;


import com.example.apparchilog.models.responses.LoginResponse;

public interface ILoginResponse {
    void onResponse(LoginResponse loginResponse);

    void onFailure(Throwable t);
}
