package com.example.apparchilog.viewModels;

import android.location.Address;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apparchilog.models.requests.PlaceRequest;
import com.example.apparchilog.models.requests.VacationRequest;
import com.example.apparchilog.models.holiday.Vacation;
import com.example.apparchilog.repositories.VacationRepository;
import com.example.apparchilog.repositories.inter_responses.IVacation;
import com.example.apparchilog.utils.Utils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

public class VacationViewModel extends ViewModel {
    private final MutableLiveData<Vacation> vacationLiveData;
    private final MutableLiveData<String> errorMessageLiveData;
    private final VacationRepository mVacationRepository;
    private final MutableLiveData<List<Vacation>> listVacationsLiveDate;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> responseServerLiveData;

    private VacationViewModel() {
        mVacationRepository = new VacationRepository();
        vacationLiveData = new MutableLiveData<>();
        errorMessageLiveData = new MutableLiveData<>();
        listVacationsLiveDate = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        responseServerLiveData = new MutableLiveData<>();
    }

    public void createVacation(String name, String description, OffsetDateTime startDate, OffsetDateTime endDate,
                                Address address, String city, String country) {
        if (Utils.isValid(name, description, startDate, endDate, city, country, errorMessageLiveData)) {
            isLoading.setValue(true);
            PlaceRequest placeRequest = Utils.parseAddress(address, city, country);
            mVacationRepository.createVacation(new VacationRequest(startDate, endDate, description, placeRequest, name),
                    new IVacation() {
                        @Override
                        public void onResponse(Vacation vacation) {
                            isLoading.setValue(false);
                            addNewItem(vacation);
                            responseServerLiveData.setValue("Success");
                            // navigateToVacations.setValue(new Event<>(true));
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            isLoading.setValue(false);
                            responseServerLiveData.setValue("Failed to create vacation: " + t.getMessage());
                            Log.d("Failure To Create", Objects.requireNonNull(t.getLocalizedMessage()));
                        }

                        @Override
                        public void onResponse(List<Vacation> vacations) {
                            isLoading.setValue(false);
                        }
                    });
        }
    }

    private void addNewItem(Vacation newVacation) {
        List<Vacation> currentVacations = listVacationsLiveDate.getValue();
        if (currentVacations != null) {
            currentVacations.add(newVacation);
            listVacationsLiveDate.setValue(currentVacations);
            vacationLiveData.setValue(newVacation);
        }
    }

    public void loadAllVacations() {
        isLoading.setValue(true);
        mVacationRepository.findAllVacations(new IVacation() {
            @Override
            public void onResponse(List<Vacation> vacationList) {
                isLoading.setValue(false);
                listVacationsLiveDate.setValue(vacationList);
                Log.d("Success Vacation Response", String.valueOf(vacationList.size()));
            }

            @Override
            public void onFailure(Throwable t) {
                isLoading.setValue(true);
                Log.d("Failure To load Vacation", Objects.requireNonNull(t.getLocalizedMessage()));
                errorMessageLiveData.setValue("Failed to load Vacation: " + t.getMessage());
            }

            @Override
            public void onResponse(Vacation vacation) {
            }
        });
    }

    public LiveData<String> getResponseServerLiveData() {
        return responseServerLiveData;
    }

    public LiveData<Vacation> getVacationLiveData() {
        return vacationLiveData;
    }


    public LiveData<List<Vacation>> getAllVacationsLiveData() {
        return listVacationsLiveDate;
    }


    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessageLiveDate() {
        return errorMessageLiveData;
    }


}