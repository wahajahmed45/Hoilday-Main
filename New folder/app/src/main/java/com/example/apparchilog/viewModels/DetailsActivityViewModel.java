package com.example.apparchilog.viewModels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.apparchilog.models.holiday.Event;
import com.example.apparchilog.models.holiday.Participant;
import com.example.apparchilog.models.holiday.Activity;
import com.example.apparchilog.repositories.ActivityRepository;
import com.example.apparchilog.repositories.inter_responses.IActivity;
import com.example.apparchilog.repositories.inter_responses.IEvent;

import java.util.List;
import java.util.Objects;

public class DetailsActivityViewModel extends ViewModel {
    private final MutableLiveData<List<Participant>> participants;
    private final MutableLiveData<Activity> activityLiveData;
    private final ActivityRepository mActivityRepository;
    private final MutableLiveData<String> messageLiveData;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<Event> eventMutableLiveData;

    public DetailsActivityViewModel() {
        activityLiveData = new MutableLiveData<>();
        mActivityRepository = new ActivityRepository();
        messageLiveData = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        participants = new MutableLiveData<>();
        eventMutableLiveData = new MutableLiveData<>();
    }

    public void loadActivity(Long activityId) {
        isLoading.setValue(true);
        mActivityRepository.findActivity(activityId, new IActivity() {
            @Override
            public void onResponse(List<Activity> vacation) {
            }

            @Override
            public void onFailure(Throwable t) {
                isLoading.setValue(true);
                Log.d("Failure To load Vacation", Objects.requireNonNull(t.getLocalizedMessage()));
                messageLiveData.setValue("Failed to load Vacation: " + t.getMessage());
            }

            @Override
            public void onResponse(Activity activity) {
                isLoading.setValue(false);
                activityLiveData.setValue(activity);
                participants.setValue(activity.getParticipants());
                Log.d("Success loading Vacation", activity.toString());
            }
        });
    }


    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getMessageLiveDate() {
        return messageLiveData;
    }

    public LiveData<List<Participant>> getParticipants() {
        return participants;
    }


    public LiveData<Activity> getActivityLiveData() {
        return activityLiveData;
    }

    public void addEvenToCalendar(Long activityId) {
        isLoading.setValue(true);
        mActivityRepository.addEvent(activityId, new IEvent() {
            @Override
            public void onResponse(List<Event> events) {

            }

            @Override
            public void onResponse(Event event) {
                if(event.getId() != null){
                    messageLiveData.setValue("success");
                    isLoading.setValue(false);
                    eventMutableLiveData.setValue(event);
                    Log.d("Success adding Event", event.toString());
                }else{
                    messageLiveData.setValue("Déjà ajouter dans l'agenda");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                isLoading.setValue(true);
                Log.d("Failure To add Event", Objects.requireNonNull(t.getLocalizedMessage()));
                messageLiveData.setValue("Failed to add Event " + t.getMessage());
            }
        });

    }

    public LiveData<Event> getEventLiveData() {
        return eventMutableLiveData;
    }
}
