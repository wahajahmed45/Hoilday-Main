package com.example.apparchilog.repositories;


import androidx.lifecycle.MutableLiveData;


import com.example.apparchilog.models.requests.LoginRequest;
import com.example.apparchilog.models.requests.RegisterRequest;
import com.example.apparchilog.models.requests.TokenRequest;
import com.example.apparchilog.models.responses.LoginResponse;
import com.example.apparchilog.models.responses.RegisterResponse;
import com.example.apparchilog.repositories.inter_responses.ILoginResponse;
import com.example.apparchilog.repositories.inter_responses.IRegisterResponse;
import com.example.apparchilog.services.RetrofitClientInstance;
import com.example.apparchilog.services.inter.ILoginService;
import com.example.apparchilog.services.inter.IOAuthService;
import com.example.apparchilog.services.inter.IRegisterService;


import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OAuthRepository {


    public void loginRemote(LoginRequest loginBody, ILoginResponse iLoginResponse) {

        ILoginService loginService = RetrofitClientInstance.getRetrofitInstance().create(ILoginService.class);
        Call<LoginResponse> initiateLogin = loginService.login(loginBody);

        enqueue(iLoginResponse, initiateLogin);
    }

    private void enqueue(ILoginResponse iLoginResponse, Call<LoginResponse> initiateLogin) {
        initiateLogin.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    iLoginResponse.onResponse(response.body());
                } else {
                    iLoginResponse.onFailure(new Throwable(response.message()));
                }
            }
            @Override
            public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                iLoginResponse.onFailure(t);
            }
        });
    }

    public void sendIdTokenRemote(String idToken, ILoginResponse iLoginResponse) {

        IOAuthService loginService = RetrofitClientInstance.getRetrofitInstance().create(IOAuthService.class);
        Call<LoginResponse> initiateLogin = loginService.sendIdToken(new TokenRequest(idToken));

        enqueue(iLoginResponse, initiateLogin);
    }

    private static void handleErrorResponse(Response<LoginResponse> response, MutableLiveData<String> errorLiveData) {
        int statusCode = response.code();
        try (ResponseBody errorBody = response.errorBody()) {
            assert errorBody != null;
            JSONObject jObjError = new JSONObject(errorBody.string());
            if (jObjError.has("message")) {
                // Update LiveData with the error message
                errorLiveData.setValue(jObjError.getString("message"));
            } else {
                errorLiveData.setValue(jObjError.getJSONObject("error").getString("message"));
            }
        } catch (Exception ex) {
            // Handle exception if needed
        }
    }

    public void registerRemote(RegisterRequest registerBody, IRegisterResponse iRegisterResponse) {
        IRegisterService registerService = RetrofitClientInstance.getRetrofitInstance().create(IRegisterService.class);
        Call<RegisterResponse> initCall = registerService.register(registerBody);

        initCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NotNull Call<RegisterResponse> call, @NotNull Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    iRegisterResponse.onResponse(response.body());
                } else {
                    iRegisterResponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<RegisterResponse> call, @NotNull Throwable t) {
                iRegisterResponse.onFailure(t);
            }
        });
    }


}
