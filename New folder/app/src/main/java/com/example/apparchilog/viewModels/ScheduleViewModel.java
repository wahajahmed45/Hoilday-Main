package com.example.apparchilog.viewModels;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.apparchilog.models.holiday.Event;
import com.example.apparchilog.repositories.VacationRepository;
import com.example.apparchilog.repositories.inter_responses.IEvent;

import java.util.List;

public class ScheduleViewModel extends ViewModel {

    private final VacationRepository vacationRepository;
    private final MutableLiveData<String> schelduleMessageLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Event>> listEventMutableLiveData;

    private final MutableLiveData<Boolean> isLoading;

    public ScheduleViewModel() {
        vacationRepository = new VacationRepository();
        isLoading = new MutableLiveData<>();
        listEventMutableLiveData = new MutableLiveData<>();
    }

    public void loadSchedules() {
        isLoading.setValue(true);
        vacationRepository.getAllSchedules(new IEvent() {
            @Override
            public void onResponse(List<Event> events) {
                    isLoading.setValue(false);
                    listEventMutableLiveData.setValue(events);
                    schelduleMessageLiveData.setValue("Loading success");
                    Log.d("TAG_SCHEDULE", events.size() + " events found");
            }

            @Override
            public void onResponse(Event event) {
            }

            @Override
            public void onFailure(Throwable throwable) {
                isLoading.setValue(false);
                schelduleMessageLiveData.setValue("Loading failed");
            }
        });

    }

    public MutableLiveData<List<Event>> getListEventMutableLiveData() {
        return listEventMutableLiveData;
    }

    public MutableLiveData<String> getEventMessageLiveData() {
        return schelduleMessageLiveData;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }


}

