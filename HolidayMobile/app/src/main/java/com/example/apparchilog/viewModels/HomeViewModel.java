package com.example.apparchilog.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.apparchilog.models.responses.LoginResponse;
import com.example.apparchilog.services.instance.CurrentInstanceUser;

public class HomeViewModel extends ViewModel {
    // This creates an object of Model class
    private final MutableLiveData<LoginResponse> userLiveData;
    private final CurrentInstanceUser currentInstanceUser;

    public HomeViewModel() {
        currentInstanceUser = CurrentInstanceUser.getInstance();
        userLiveData = new MutableLiveData<>(currentInstanceUser.getLoginResponse());
    }

    public LiveData<LoginResponse> getUserLiveData() {
        return userLiveData;
    }

    public void logout() {
        currentInstanceUser.setToken(null);
    }
}
