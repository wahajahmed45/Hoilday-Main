package com.example.apparchilog.repositories.inter_responses;

import com.example.apparchilog.models.holiday.Event;

import java.util.List;

public interface IEvent {
    void onResponse(List<Event> events);
    void onResponse(Event event);
    void onFailure(Throwable throwable);
}
