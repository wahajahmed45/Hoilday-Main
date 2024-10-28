package com.example.apparchilog.repositories.inter_responses;


import com.example.apparchilog.models.responses.RegisterResponse;

public interface IRegisterResponse {
    void onResponse(RegisterResponse registerResponse);

    void onFailure(Throwable t);
}
