package com.example.apparchilog.viewModels;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apparchilog.models.holiday.Participant;
import com.example.apparchilog.models.requests.ParticipantRequest;
import com.example.apparchilog.models.responses.MessageResponse;
import com.example.apparchilog.repositories.ActivityRepository;
import com.example.apparchilog.repositories.VacationRepository;
import com.example.apparchilog.repositories.inter_responses.IMessageResponse;
import com.example.apparchilog.repositories.inter_responses.IParticipant;

import java.util.List;
import java.util.Objects;

public class ParticipantViewModel extends ViewModel {

    private final MutableLiveData<Participant> participantLiveData;
    private final VacationRepository mVacationRepository;
    private final ActivityRepository mActivityRepository;
    private final MutableLiveData<String> messageLiveData;
    private final MutableLiveData<Boolean> isLoading;

    public ParticipantViewModel() {
        participantLiveData = new MutableLiveData<>();
        mVacationRepository = new VacationRepository();
        mActivityRepository = new ActivityRepository();
        messageLiveData = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
    }

    private void createParticipantFromVacation(Long id_vacation, String firstName, String lastName, String email) {
        if (isValid(firstName, lastName, email)) {
            isLoading.setValue(true);
            mVacationRepository.createParticipantFromVacation(id_vacation,
                    new ParticipantRequest(email, lastName, firstName), new IParticipant() {
                        @Override
                        public void onResponse(List<Participant> participants) {
                        }

                        @Override
                        public void onResponse(Participant participant) {
                            isLoading.setValue(false);
                            participantLiveData.setValue(participant);
                            Log.d("Success Created participant", participant.toString());
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            isLoading.setValue(true);
                            Log.d("Failure To create Participant", Objects.requireNonNull(t.getLocalizedMessage()));
                            messageLiveData.setValue("Failed to create Participant: " + t.getMessage());
                        }
                    });
        }

    }

    private void createParticipantFromActivity(Long id_vacation, String firstName, String lastName, String email) {
        if (isValid(firstName, lastName, email)) {
            isLoading.setValue(true);
            mActivityRepository.createParticipantFromActivity(id_vacation,
                    new ParticipantRequest(email, lastName, firstName), new IParticipant() {
                        @Override
                        public void onResponse(List<Participant> participants) {

                        }

                        @Override
                        public void onResponse(Participant participant) {
                            isLoading.setValue(false);
                            participantLiveData.setValue(participant);
                            Log.d("Success Created participant", participant.toString());
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            isLoading.setValue(true);
                            Log.d("Failure To create Participant", Objects.requireNonNull(t.getLocalizedMessage()));
                            messageLiveData.setValue("Failed to create Participant: " + t.getMessage());
                        }
                    });
        }

    }

    private boolean isValid(String firstName, String lastName, String email) {
        if (TextUtils.isEmpty(firstName)) {
            messageLiveData.setValue("Nom est requis");
            return false;
        }
        if (TextUtils.isEmpty(lastName)) {
            messageLiveData.setValue("Prenom est requis");
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            messageLiveData.setValue("Email est requis");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            messageLiveData.setValue("Email doit être valide S.V.P");
            return false;
        }
        return true;
    }

    private void removeParticipantFromVacation(Long vacationId, Long participantId) {
        isLoading.setValue(true);
        mVacationRepository.removeParticipantFromVacation(vacationId, participantId, new IMessageResponse() {
            @Override
            public void onResponse(MessageResponse messageResponse) {
                isLoading.setValue(false);
                messageLiveData.setValue(messageResponse.getMessage());
                Log.d("Success Deleted participant from Vacation", messageResponse.getMessage());
            }

            @Override
            public void onFailure(Throwable t) {
                isLoading.setValue(true);
                Log.d("Failure To delete participant", Objects.requireNonNull(t.getLocalizedMessage()));
                messageLiveData.setValue("Failed to create Participant: " + t.getMessage());
            }
        });
    }

    public void removeParticipant(Long vacationId, Long participantId, boolean isVacation) {
            if (isVacation) {
                removeParticipantFromVacation(vacationId, participantId);
            }else{
                removeParticipantFromActivity(vacationId, participantId);
            }
    }

    private void removeParticipantFromActivity(Long vacationId, Long participantId) {
        isLoading.setValue(true);
        mActivityRepository.removeParticipantFromActivity(vacationId, participantId, new IMessageResponse() {
            @Override
            public void onResponse(MessageResponse messageResponse) {
                isLoading.setValue(false);
                messageLiveData.setValue(messageResponse.getMessage());
                Log.d("Success Deleted participant from Vacation", messageResponse.getMessage());
            }

            @Override
            public void onFailure(Throwable t) {
                isLoading.setValue(true);
                Log.d("Failure To delete participant", Objects.requireNonNull(t.getLocalizedMessage()));
                messageLiveData.setValue("Failed to create Participant: " + t.getMessage());
            }
        });
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getMessageLiveDate() {
        return messageLiveData;
    }

    public LiveData<Participant> getParticipantLiveData() {
        return participantLiveData;
    }

    public void createParticipant(boolean isVacationCreation, Long mParamId, String firstname, String lastname, String email) {
        if (isVacationCreation) {
            createParticipantFromVacation(mParamId, firstname, lastname, email);
        } else {
            createParticipantFromActivity(mParamId, firstname, lastname, email);
        }
    }
}
