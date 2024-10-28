package com.example.apparchilog.repositories;

import com.example.apparchilog.models.holiday.Event;
import com.example.apparchilog.models.holiday.Participant;
import com.example.apparchilog.models.requests.ParticipantRequest;
import com.example.apparchilog.models.requests.VacationRequest;
import com.example.apparchilog.models.holiday.Vacation;

import com.example.apparchilog.models.responses.MessageResponse;
import com.example.apparchilog.repositories.inter_responses.IEvent;
import com.example.apparchilog.repositories.inter_responses.IMessageResponse;
import com.example.apparchilog.repositories.inter_responses.IParticipant;
import com.example.apparchilog.repositories.inter_responses.IVacation;
import com.example.apparchilog.services.RetrofitClientInstance;

import com.example.apparchilog.services.instance.CurrentInstanceUser;
import com.example.apparchilog.services.inter.*;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class VacationRepository {

    private final String token = CurrentInstanceUser.getInstance().getToken();

    public void findAllVacations(IVacation iVacation) {
        IVacationsServiceGET vacationService = RetrofitClientInstance.getRetrofitInstance().create(IVacationsServiceGET.class);
        Call<List<Vacation>> initCall = vacationService.getAllVacations("Bearer " + token);

        initCall.enqueue(new Callback<List<Vacation>>() {
            @Override
            public void onResponse(@NotNull Call<List<Vacation>> call, @NotNull Response<List<Vacation>> response) {
                if (response.isSuccessful()) {
                    iVacation.onResponse(response.body());
                } else {
                    iVacation.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Vacation>> call, @NotNull Throwable t) {
                iVacation.onFailure(t);
            }
        });
    }

    public void createVacation(VacationRequest vacationRequest, IVacation iVacation) {
        IVacationServicePOST vacationService = RetrofitClientInstance.getRetrofitInstance().create(IVacationServicePOST.class);
        Call<Vacation> initCall = vacationService.createVacation(vacationRequest, "Bearer " + token);

        enqueue(iVacation, initCall);
    }

    public void findVacation(long id, IVacation iVacation) {
        IVacationServiceGET vacationService = RetrofitClientInstance.getRetrofitInstance().create(IVacationServiceGET.class);
        Call<Vacation> initCall = vacationService.getVacation(id, "Bearer " + token);

        enqueue(iVacation, initCall);
    }

    public void createParticipantFromVacation(Long id_vacation, ParticipantRequest participantRequest, IParticipant iParticipant) {
        IParticipantVacationServicePOST servicePOST = RetrofitClientInstance.getRetrofitInstance().create(IParticipantVacationServicePOST.class);
        Call<Participant> initCall = servicePOST.createParticipantFromVacation(id_vacation, participantRequest, "Bearer " + token);
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

    public void removeParticipantFromVacation(Long id_vacation, Long id_participant, IMessageResponse iMessageResponse) {
        IParticipantVacationServiceDelete service = RetrofitClientInstance.getRetrofitInstance().create(IParticipantVacationServiceDelete.class);
        Call<MessageResponse> intitCall = service.removeParticipantFromVacation(id_vacation, id_participant, "Bearer " + token);
        enqueueMessage(iMessageResponse, intitCall);
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


    private void enqueue(IVacation iVacation, Call<Vacation> initCall) {
        initCall.enqueue(new Callback<Vacation>() {
            @Override
            public void onResponse(@NotNull Call<Vacation> call, @NotNull Response<Vacation> response) {
                if (response.isSuccessful()) {
                    iVacation.onResponse(response.body());
                } else {
                    if (response.code() == 400) {
                        iVacation.onFailure(new Throwable("L'utilisateur a dèja une activité a cette date"));
                    }
                    iVacation.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<Vacation> call, @NotNull Throwable t) {
                iVacation.onFailure(t);
            }
        });
    }


    public void addEvent(Long vacationId, IEvent iEvent) {
        IEventVacationServiceGET service = RetrofitClientInstance.getRetrofitInstance().create(IEventVacationServiceGET.class);
        Call<List<Event>> initCall = service.addEventToCalendar(vacationId, "Bearer " + token);
        initCall.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(@NotNull Call<List<Event>> call, @NotNull Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    iEvent.onResponse(response.body());
                } else {
                    iEvent.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Event>> call, @NotNull Throwable t) {
                iEvent.onFailure(t);
            }
        });
    }

    public void getAllSchedules(IEvent iEvent) {
        IScheduleServiceGET service = RetrofitClientInstance.getRetrofitInstance().create(IScheduleServiceGET.class);
        Call<List<Event>> initCall = service.getAllSchedules("Bearer " + token);
        initCall.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(@NotNull Call<List<Event>> call, @NotNull Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    iEvent.onResponse(response.body());
                } else {
                    iEvent.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Event>> call, @NotNull Throwable t) {
                iEvent.onFailure(t);
            }
        });
    }

    public void findAllParticipants(IParticipant iParticipant) {
        IParticipantsServiceGET serviceGET = RetrofitClientInstance.getRetrofitInstance().create(IParticipantsServiceGET.class);
        Call<List<Participant>> listCall = serviceGET.findAllParticipants("Bearer " + token);
        listCall.enqueue(new Callback<List<Participant>>() {
            @Override
            public void onResponse(@NotNull Call<List<Participant>> call, @NotNull Response<List<Participant>> response) {
                if (response.isSuccessful()) {
                    iParticipant.onResponse(response.body());
                } else {
                    iParticipant.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Participant>> call, @NotNull Throwable t) {
                iParticipant.onFailure(t);
            }
        });
    }
}
