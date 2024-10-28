package com.example.apparchilog.viewModels;

import android.location.Address;
import android.util.Log;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.apparchilog.models.requests.ActivityRequest;
import com.example.apparchilog.models.requests.PlaceRequest;
import com.example.apparchilog.models.responses.MessageResponse;
import com.example.apparchilog.models.holiday.Activity;
import com.example.apparchilog.repositories.ActivityRepository;
import com.example.apparchilog.repositories.inter_responses.IActivity;
import com.example.apparchilog.repositories.inter_responses.IMessageResponse;
import com.example.apparchilog.utils.Utils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;


public class ActivityViewModel extends ViewModel {

    private final MutableLiveData<Activity> activityLiveData;
    private final MutableLiveData<String> errorMessageLiveData;
    private final ActivityRepository mActivityRepository;
    private final MutableLiveData<List<Activity>> listActivityLiveDate;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> messageToastLiveData;

    private ActivityViewModel() {
        mActivityRepository = new ActivityRepository();
        activityLiveData = new MutableLiveData<>();
        errorMessageLiveData = new MutableLiveData<>();
        listActivityLiveDate = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        messageToastLiveData = new MutableLiveData<>();

    }

    public void loadAllActivity() {
        isLoading.setValue(true);
        mActivityRepository.findAllActivities(new IActivity() {
            @Override
            public void onResponse(List<Activity> activityList) {
                isLoading.setValue(false);
                listActivityLiveDate.setValue(activityList);
                messageToastLiveData.setValue("Load successful");
                Log.d("Success Activity Response", String.valueOf(activityList.size()));
            }

            @Override
            public void onFailure(Throwable t) {
                isLoading.setValue(true);
                Log.d("Failure To load Activity", Objects.requireNonNull(t.getLocalizedMessage()));
                messageToastLiveData.setValue("Failed to load Activity: " + t.getMessage());
            }

            @Override
            public void onResponse(Activity activity) {
                isLoading.setValue(false);
            }
        });
    }


    public void removerActivity(Long vacationID, Long activityID) {
        isLoading.setValue(true);
        mActivityRepository.removeActivityFromVacation(vacationID, activityID, new IMessageResponse() {
            @Override
            public void onResponse(MessageResponse messageResponse) {
                isLoading.setValue(false);
                messageToastLiveData.setValue(messageResponse.getMessage());
            }

            @Override
            public void onFailure(Throwable t) {
                isLoading.setValue(false);
                messageToastLiveData.setValue("Failed to Remove Activity: " + t.getMessage());
            }
        });
    }

    public void updateActivity(Long vacationId, Long activityId, String name, String description, OffsetDateTime startDate,
                               OffsetDateTime endDate, Address address, String city, String country) {
        if (Utils.isValid(name, description, startDate, endDate, city, country, errorMessageLiveData)) {
            isLoading.setValue(true);
            PlaceRequest placeRequest = Utils.parseAddress(address, city, country);
            mActivityRepository.updateActivity(vacationId, activityId, new ActivityRequest(startDate, endDate, description, placeRequest, name), new IActivity() {
                @Override
                public void onResponse(List<Activity> activities) {
                    isLoading.setValue(false);
                }

                @Override
                public void onFailure(Throwable t) {
                    isLoading.setValue(false);
                    messageToastLiveData.setValue("Failed to create Activity: " + t.getMessage());
                }

                @Override
                public void onResponse(Activity activity) {
                    isLoading.setValue(false);
                    updateNewItem(activity);
                    messageToastLiveData.setValue("Success");
                }
            });
        }

    }

    private void updateNewItem(Activity newActivity) {
        List<Activity> currentActivity = listActivityLiveDate.getValue();
        if (currentActivity != null) {
            for (Activity activity : currentActivity) {
                if (Objects.equals(activity.getId(), newActivity.getId())) {
                    currentActivity.remove(activity);
                    currentActivity.add(newActivity);
                }
            }
            listActivityLiveDate.setValue(currentActivity);
            activityLiveData.setValue(newActivity);
        }
    }

    public void createActivity(Long vacationId, String name, String description, OffsetDateTime startDate,
                               OffsetDateTime endDate, Address address, String city, String country) {
        if (Utils.isValid(name, description, startDate, endDate, city, country, errorMessageLiveData)) {
            isLoading.setValue(true);
            PlaceRequest placeRequest = Utils.parseAddress(address, city, country);
            mActivityRepository.createActivity(vacationId, new ActivityRequest(startDate, endDate, description, placeRequest, name), new IActivity() {
                @Override
                public void onResponse(Activity activity) {
                    isLoading.setValue(false);
                    addNewItem(activity);
                    messageToastLiveData.setValue("Success");
                    // navigateToVacations.setValue(new Event<>(true));
                }
                @Override
                public void onFailure(Throwable t) {
                    isLoading.setValue(false);
                    messageToastLiveData.setValue("Failed to create Activity: " + t.getMessage());
                    Log.d("Failed To Create Vacation", Objects.requireNonNull(t.getLocalizedMessage()));
                }

                @Override
                public void onResponse(List<Activity> activities) {
                    isLoading.setValue(false);
                    // Handle the response
                }
            });
        }

    }

    private void addNewItem(Activity newActivity) {
        List<Activity> currentActivity = listActivityLiveDate.getValue();
        if (currentActivity != null) {
            currentActivity.add(newActivity);
            listActivityLiveDate.setValue(currentActivity);
            activityLiveData.setValue(newActivity);
        }
    }

    public LiveData<String> getMessageToastLiveData() {
        return messageToastLiveData;
    }

    public LiveData<Activity> getActivityLiveData() {
        return activityLiveData;
    }

    public LiveData<List<Activity>> getAllActivityLiveData() {
        return listActivityLiveDate;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getActivityErrorMessageLiveDate() {
        return errorMessageLiveData;
    }

}
