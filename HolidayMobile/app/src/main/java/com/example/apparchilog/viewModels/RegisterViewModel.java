package com.example.apparchilog.viewModels;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apparchilog.models.requests.RegisterRequest;
import com.example.apparchilog.models.requests.ParticipantFirebase;
import com.example.apparchilog.models.responses.RegisterResponse;
import com.example.apparchilog.repositories.OAuthRepository;
import com.example.apparchilog.repositories.inter_responses.IRegisterResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class RegisterViewModel extends ViewModel {

    private final MutableLiveData<RegisterResponse> registerLiveData;

    private final MutableLiveData<String> mRegisterResultMutableData;
    private final MutableLiveData<String> errorMessageLiveData;
    private final MutableLiveData<Boolean> isLoading;
    private final OAuthRepository mOAuthRepository;
    private FirebaseAuth mAuth;

    public RegisterViewModel() {
        registerLiveData = new MutableLiveData<>();
        mRegisterResultMutableData = new MutableLiveData<>();
        errorMessageLiveData = new MutableLiveData<>();
        // mRegisterResultMutableData.postValue("");
        mOAuthRepository = new OAuthRepository();
        isLoading = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
    }

    public void register(String firstName, String lastName, String email, String password, Activity activity) {
        if (isValidRegister(firstName, lastName, email, password)) {
            isLoading.setValue(true);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                registerRemote(lastName, firstName, email, password);
                            } else {
                                // Registration failed, display a message to the user
                                errorMessageLiveData.setValue("Registration failed: " + task.getException().getMessage());
                            }
                        }
                    });
        } else {
            errorMessageLiveData.setValue("Veuillez saisir tous les champs");
        }
    }

    private void registerRemote(String lastName, String firstName, String email, String password) {
        mOAuthRepository.registerRemote(new RegisterRequest(lastName, firstName, email, password), new IRegisterResponse() {
            @Override
            public void onResponse(RegisterResponse response) {
                isLoading.setValue(false);
                mRegisterResultMutableData.postValue("Registration Success");
                registerLiveData.setValue(response);
                registerToFirestore(email, lastName, firstName);

            }

            @Override
            public void onFailure(Throwable t) {
                isLoading.setValue(false);
                mRegisterResultMutableData.postValue("Registration failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void registerToFirestore(String email, String lastName, String firstName) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        String uid = firebaseUser.getUid();//String id, String email, String name, String avatarUrl, String lastMessage, String lastMessageTime
        ParticipantFirebase participantFirebase = new ParticipantFirebase(uid, email, lastName + " " + firstName, "", "", "");
        addUserToFirestore(participantFirebase);

    }

    private void addUserToFirestore(ParticipantFirebase user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getEmail())
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "User added successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error adding user", e);
                });
    }

    public boolean isValidRegister(String lastName, String firstName, String email, String password) {
        if (TextUtils.isEmpty(lastName)) {
            errorMessageLiveData.setValue("Last name is required");
            return false;
        }
        if (TextUtils.isEmpty(firstName)) {
            errorMessageLiveData.setValue("First name is required");
            return false;
        }
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

    public LiveData<RegisterResponse> getRegisterResponseLiveData() {
        return registerLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessageLiveData;
    }

    public LiveData<String> getRegisterResult() {
        return mRegisterResultMutableData;
    }

    public LiveData<Boolean> getIsLoadingLiveData() {
        return isLoading;
    }
}
