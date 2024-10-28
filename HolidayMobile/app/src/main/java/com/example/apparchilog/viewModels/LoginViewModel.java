package com.example.apparchilog.viewModels;

import android.app.Activity;
import androidx.annotation.NonNull;
import com.example.apparchilog.models.requests.LoginRequest;
import com.example.apparchilog.models.requests.ParticipantFirebase;
import com.example.apparchilog.models.responses.LoginResponse;
import com.example.apparchilog.repositories.OAuthRepository;
import com.example.apparchilog.repositories.inter_responses.ILoginResponse;
import com.example.apparchilog.services.instance.CurrentInstanceUser;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class LoginViewModel extends ViewModel {

    // This creates an object of Model class
    private final MutableLiveData<LoginResponse> loginResponseLiveData;
    private final MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();
    private final OAuthRepository OAuthRepository;
    private final MutableLiveData<Boolean> isLoading;
    private FirebaseAuth mAuth;

    // Creates a constructor of ViewModel class
    public LoginViewModel() {
        OAuthRepository = new OAuthRepository();
        loginResponseLiveData = new MutableLiveData<>();
        loginResponseLiveData.setValue(new LoginResponse());
        isLoading = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
    }

    public void login(String email, String password, Activity activity) {
        if (isValidLogin(email, password)) {
            isLoading.setValue(true);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                OAuthRepository.loginRemote(new LoginRequest(email, password), new ILoginResponse() {
                                    @Override
                                    public void onResponse(LoginResponse response) {
                                        isLoading.setValue(false);
                                        Log.d("Login Success", response.getJwtToken());
                                        CurrentInstanceUser.getInstance().setLoginResponse(response);
                                        loginResponseLiveData.setValue(response);
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        isLoading.setValue(false);
                                        Log.d("TAG_Login", Objects.requireNonNull(t.getLocalizedMessage()));
                                    }
                                });
                            } else {// Login failed, display a message to the user
                                isLoading.setValue(false);
                                errorMessageLiveData.setValue("Vous n'avez pas encore de compte: " /*+ task.getException().getMessage()*/);
                            }
                        }
                    });
        } else {
            errorMessageLiveData.setValue("Veuillez saisir tous les champs correctements");
        }

    }

    public void sendIdToken(String idToken, Activity activity) {
        if (idToken != null) {
            isLoading.postValue(true);
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                sendIdTokenRemote(idToken);
                                Log.d("TAG_OAUTH", "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                            } else {// If sign in fails, display a message to the user.
                                Log.w("TAG_OAUTH", "signInWithCredential:failure", task.getException());
                                errorMessageLiveData.setValue("Authentication Firebase failed: " + task.getException().getMessage());
                            }
                        }
                    });
        }

    }

    private void sendIdTokenRemote(String idToken) {
        OAuthRepository.sendIdTokenRemote(idToken, new ILoginResponse() {
            @Override
            public void onResponse(LoginResponse response) {
                isLoading.setValue(false);
                CurrentInstanceUser.getInstance().setLoginResponse(response);
                registerToFirestore(response);
                loginResponseLiveData.setValue(response);
            }

            @Override
            public void onFailure(Throwable t) {
                isLoading.setValue(false);
                Log.d("TAG_OAUTH", Objects.requireNonNull(t.getLocalizedMessage()));
                errorMessageLiveData.setValue(t.getLocalizedMessage());
            }
        });
    }

    private void registerToFirestore(LoginResponse response) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        String uid = firebaseUser.getUid();//String id, String email, String name, String avatarUrl, String lastMessage, String lastMessageTime
        ParticipantFirebase participantFirebase = new ParticipantFirebase(uid, response.getEmail(),
                response.getLastname() + " " + response.getFirstname(),
                response.getPictureUrl(), "", "");
        addUserToFirestore(participantFirebase);
    }

    private void addUserToFirestore(ParticipantFirebase user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getEmail())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }

    public LiveData<LoginResponse> getLoginResponseLiveData() {
        return loginResponseLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessageLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    //checks if the input fields are left null
    public boolean isValidLogin(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            errorMessageLiveData.setValue("Email is required");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessageLiveData.setValue("Email should be valid please");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            errorMessageLiveData.setValue("Password is required");
            return false;
        }
        if (password.length() < 3) {
            errorMessageLiveData.setValue("Password should be at least 3 characters long");
            return false;
        }
        return true;
    }

}
