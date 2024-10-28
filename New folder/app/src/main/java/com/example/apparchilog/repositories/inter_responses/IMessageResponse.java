package com.example.apparchilog.repositories.inter_responses;

import com.example.apparchilog.models.responses.MessageResponse;

public interface IMessageResponse {

    void onResponse(MessageResponse messageResponse);

    void onFailure(Throwable t);
}

