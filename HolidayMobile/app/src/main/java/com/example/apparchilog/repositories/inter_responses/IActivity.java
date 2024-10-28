package com.example.apparchilog.repositories.inter_responses;

import com.example.apparchilog.models.holiday.Activity;

import java.util.List;

public interface IActivity {
    void onResponse(List<Activity> activities);

    void onFailure(Throwable t);

    void onResponse(Activity activity);
}

