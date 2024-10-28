package com.example.apparchilog.repositories.inter_responses;

import com.example.apparchilog.models.holiday.Vacation;

import java.util.List;

public interface IVacation {
    void onResponse(List<Vacation> vacation);

    void onFailure(Throwable t);

    void onResponse(Vacation vacation);
}

