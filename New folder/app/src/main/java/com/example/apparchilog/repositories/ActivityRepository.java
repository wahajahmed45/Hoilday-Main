package com.example.apparchilog.repositories;

import com.example.apparchilog.models.holiday.Event;
import com.example.apparchilog.models.requests.ActivityRequest;
import com.example.apparchilog.models.holiday.Participant;
import com.example.apparchilog.models.requests.ParticipantRequest;
import com.example.apparchilog.models.responses.MessageResponse;
import com.example.apparchilog.models.holiday.Activity;
import com.example.apparchilog.repositories.inter_responses.IActivity;
import com.example.apparchilog.repositories.inter_responses.IEvent;
import com.example.apparchilog.repositories.inter_responses.IMessageResponse;
import com.example.apparchilog.repositories.inter_responses.IParticipant;
import com.example.apparchilog.services.RetrofitClientInstance;
import com.example.apparchilog.services.instance.CurrentInstanceUser;
import com.example.apparchilog.services.inter.*;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class ActivityRepository {

    private final String token = CurrentInstanceUser.getInstance().getToken();

    public void findAllActivities(IActivity iActivity) {
        IActivitiesServiceGET vacationService = RetrofitClientInstance.getRetrofitInstance().create(IActivitiesServiceGET.class);
        Call<List<Activity>> initCall = vacationService.getAllActivities("Bearer " + token);

        initCall.enqueue(new Callback<List<Activity>>() {
            @Override
            public void onResponse(@NotNull Call<List<Activity>> call, @NotNull Response<List<Activity>> response) {
                if (response.isSuccessful()) {
                    iActivity.onResponse(response.body());
                } else {
                    iActivity.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Activity>> call, @NotNull Throwable t) {
                iActivity.onFailure(t);
            }
        });
    }

    public void createActivity(Long id_vacation, ActivityRequest vacationRequest, IActivity iVacation) {
        IActivityServicePOST activityService = RetrofitClientInstance.getRetrofitInstance().create(IActivityServicePOST.class);
        Call<Activity> initCall = activityService.createActivity(id_vacation, vacationRequest, "Bearer " + token);

        enqueue(iVacation, initCall);
    }

    public void findActivity(Long activityId, IActivity iActivity) {
        IActivityServiceGET vacationService = RetrofitClientInstance.getRetrofitInstance().create(IActivityServiceGET.class);
        Call<Activity> initCall = vacationService.getActivity(activityId, "Bearer " + token);

        enqueue(iActivity, initCall);
    }

    public void createParticipantFromActivity(Long id_activity, ParticipantRequest participantRequest, IParticipant iParticipant) {
        IParticipantActivityServicePOST activityService = RetrofitClientInstance.getRetrofitInstance().create(IParticipantActivityServicePOST.class);
        Call<Participant> initCall = activityService.createParticipantFromActivity(id_activity, participantRequest, "Bearer " + token);
        initCall.enqueue(new Callback<Participant>() {
            @Override
            public void onResponse(@NotNull Call<Participant> call, @NotNull Response<Participant> response) {
                if (response.isSuccessful()) {
                    iParticipant.onResponse(response.body());
                } else {
                    iParticipant.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<Participant> call, @NotNull Throwable t) {
                iParticipant.onFailure(t);
            }
        });
    }

    public void updateActivity(Long vacationId, Long activityId, ActivityRequest activityRequest, IActivity iActivity) {
        IActivityServicePUT servicePUT = RetrofitClientInstance.getRetrofitInstance().create(IActivityServicePUT.class);
        Call<Activity> initCall = servicePUT.updateActivity(vacationId, activityId, activityRequest, "Bearer " + token);
        callQueue(iActivity, initCall);
    }

    private void callQueue(IActivity iActivity, Call<Activity> initCall) {
        initCall.enqueue(new Callback<Activity>() {
            @Override
            public void onResponse(@NotNull Call<Activity> call, @NotNull Response<Activity> response) {
                if (response.isSuccessful()) {
                    iActivity.onResponse(response.body());
                } else {
                        if (response.code() == 400) {
                            iActivity.onFailure(new Throwable("L'utilisateur a dèja une activité a cette date"));
                        }
                        iActivity.onFailure(new Throwable(response.message()));

                    iActivity.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<Activity> call, @NotNull Throwable t) {
                iActivity.onFailure(t);
            }
        });
    }

    public void removeActivityFromVacation(Long id_vacation, Long id_activity, IMessageResponse iMessageResponse) {
        IVacationServiceDelete serviceDelete = RetrofitClientInstance.getRetrofitInstance().create(IVacationServiceDelete.class);
        Call<MessageResponse> initiCall = serviceDelete.removeActivityFromVacation(id_vacation, id_activity, "Bearer " + token);
        enqueueMessage(iMessageResponse, initiCall);
    }

    private void enqueueMessage(IMessageResponse iMessageResponse, Call<MessageResponse> intitCall) {
        intitCall.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NotNull Call<MessageResponse> call, @NotNull Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    iMessageResponse.onResponse(response.body());
                } else {
                    iMessageResponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<MessageResponse> call, @NotNull Throwable t) {
                iMessageResponse.onFailure(t);
            }
        });
    }

    public void removeParticipantFromActivity(Long id_activity, Long id_participant, IMessageResponse iParticipant) {
        IParticipantActivityServiceDelete service = RetrofitClientInstance.getRetrofitInstance().create(IParticipantActivityServiceDelete.class);
        Call<MessageResponse> initCall = service.removeParticipantFromActivity(id_activity, id_participant, "Bearer " + token);
        initCall.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NotNull Call<MessageResponse> call, @NotNull Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    iParticipant.onResponse(response.body());
                } else {
                    iParticipant.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<MessageResponse> call, @NotNull Throwable t) {
                iParticipant.onFailure(t);
            }
        });
    }

    private void enqueue(IActivity iVacation, Call<Activity> initCall) {
        callQueue(iVacation, initCall);
    }

    public void addEvent(Long activityId, IEvent iEvent) {
        IEventActivityServiceGET service = RetrofitClientInstance.getRetrofitInstance().create(IEventActivityServiceGET.class);
        Call<Event> initCall = service.addEventToCalendar(activityId, "Bearer " + token);
        initCall.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(@NotNull Call<Event> call, @NotNull Response<Event> response) {
                if (response.isSuccessful()) {
                    iEvent.onResponse(response.body());
                } else {
                    iEvent.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<Event> call, @NotNull Throwable t) {
                iEvent.onFailure(t);
            }
        });
    }
}
