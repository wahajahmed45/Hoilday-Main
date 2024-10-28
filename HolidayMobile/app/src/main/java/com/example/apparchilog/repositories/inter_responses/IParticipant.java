package com.example.apparchilog.repositories.inter_responses;

import com.example.apparchilog.models.holiday.Participant;

import java.util.List;

public interface IParticipant {
    void onResponse(List<Participant> participants);
    void onResponse(Participant participant);

    void onFailure(Throwable t);
}

