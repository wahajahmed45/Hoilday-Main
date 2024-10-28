package com.example.apparchilog.viewModels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.apparchilog.models.holiday.Event;
import com.example.apparchilog.models.holiday.Vacation;
import com.example.apparchilog.models.holiday.Activity;
import com.example.apparchilog.models.holiday.Participant;
import com.example.apparchilog.repositories.VacationRepository;
import com.example.apparchilog.repositories.inter_responses.IEvent;
import com.example.apparchilog.repositories.inter_responses.IVacation;

import java.util.List;
import java.util.Objects;

public class DetailsVacationViewModel extends ViewModel {

    private final MutableLiveData<List<Participant>> participants;
    private final MutableLiveData<List<Activity>> activities;
    private final MutableLiveData<Vacation> vacationLiveData;
    private final MutableLiveData<List<Event>> eventMutableLiveData;
    private final VacationRepository mVacationRepository;
    private final MutableLiveData<String> messageLiveData;
    private final MutableLiveData<Boolean> isLoading;

    public DetailsVacationViewModel() {
        vacationLiveData = new MutableLiveData<>();
        mVacationRepository = new VacationRepository();
        messageLiveData = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        participants = new MutableLiveData<>();
        activities = new MutableLiveData<>();
        eventMutableLiveData = new MutableLiveData<>();
    }

    public void loadVacation(Long vacationId) {
        isLoading.setValue(true);
        mVacationRepository.findVacation(vacationId, new IVacation() {
            @Override
            public void onResponse(List<Vacation> vacation) {

            }

            @Override
            public void onFailure(Throwable t) {
                isLoading.setValue(true);
                Log.d("Failure To load Vacation", Objects.requireNonNull(t.getLocalizedMessage()));
                messageLiveData.setValue("Failed to load Vacation: " + t.getMessage());
            }

            @Override
            public void onResponse(Vacation vacation) {
                messageLiveData.setValue("success");
                isLoading.setValue(false);
                vacationLiveData.setValue(vacation);
                participants.setValue(vacation.getParticipants());
                activities.setValue(vacation.getActivities());
                Log.d("Success loading Vacation", vacation.toString());
            }
        });
    }


    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getMessageLiveDate() {
        return messageLiveData;
    }

    public LiveData<Vacation> getVacationLiveData() {
        return vacationLiveData;
    }

    public LiveData<List<Participant>> getParticipants() {
        return participants;
    }

    public LiveData<List<Activity>> getActivities() {
        return activities;
    }

    public LiveData<List<Event>> getEventLiveData() {
        return eventMutableLiveData;
    }

    public void addEventToCalendar(Long vacationId) {
        isLoading.postValue(true);
        mVacationRepository.addEvent(vacationId, new IEvent() {
            @Override
            public void onResponse(List<Event> events) {
                if (!events.isEmpty()) {
                    messageLiveData.setValue("success");
                    eventMutableLiveData.setValue(events);
                    isLoading.setValue(false);
                    Log.d("Success adding Event", events.size() + " events");
                } else {
                    isLoading.setValue(false);
                    messageLiveData.setValue("les activités déjà ajouter dans votre agenda ou vous n'avez pas d'activité");
                }

            }

            @Override
            public void onResponse(Event event) {

            }

            @Override
            public void onFailure(Throwable t) {
                isLoading.setValue(true);
                Log.d("Failure To add Event", Objects.requireNonNull(t.getLocalizedMessage()));
                messageLiveData.setValue("Failed to add Event: " + t.getMessage());
            }
        });
    }
}
