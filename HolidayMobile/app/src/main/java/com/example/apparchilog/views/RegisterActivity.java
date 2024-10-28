package com.example.apparchilog.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.apparchilog.R;
import com.example.apparchilog.models.responses.RegisterResponse;
import com.example.apparchilog.utils.Utils;
import com.example.apparchilog.viewModels.RegisterViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {

    private EditText inFirstName, inLastName, inEmail, inPassword;
    private Button bRegister;
    private TextView mSignInRedirectText;
    private RegisterViewModel registerViewModel;
    private ImageButton togglePasswordVisibilityButton;
    private boolean isPasswordVisible = false;
    private ProgressBar progressBar;

    // private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inFirstName = findViewById(R.id.inFirstName);
        inLastName = findViewById(R.id.inLastName);
        inEmail = findViewById(R.id.inEmail);
        inPassword = findViewById(R.id.inPassword);
        bRegister = findViewById(R.id.bRegister);
        mSignInRedirectText = findViewById(R.id.signInRedirectText);
        progressBar = findViewById(R.id.progressBar);
        // progressBar = findViewById(R.id.progressBar);
        togglePasswordVisibilityButton = findViewById(R.id.togglePasswordVisibility);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // Configure Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Register");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.button_click);
                bRegister.startAnimation(animation);
                registerUser();
            }
        });

        mSignInRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        registerViewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Utils.showToast(RegisterActivity.this, errorMessage, 5000);
                    if (errorMessage.contains("Last name")) {
                        inLastName.setError(errorMessage);
                        inLastName.requestFocus();
                    } else if (errorMessage.contains("First name")) {
                        inFirstName.setError(errorMessage);
                        inFirstName.requestFocus();
                    } else if (errorMessage.contains("Email")) {
                        inEmail.setError(errorMessage);
                        inEmail.requestFocus();
                    } else if (errorMessage.contains("Password")) {
                        inPassword.setError(errorMessage);
                        inPassword.requestFocus();
                    }
                    Toast.makeText(RegisterActivity.this, "Registration failed: " + errorMessage, Toast.LENGTH_SHORT).show();

                }
            }
        });
        registerViewModel.getIsLoadingLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
        registerViewModel.getRegisterResponseLiveData().observe(this, new Observer<RegisterResponse>() {

            @Override
            public void onChanged(RegisterResponse registerResponse) {
                Log.d("Register", registerResponse.getMessage());
                if (registerResponse.getMessage().contains("Already Exists")) {
                    inEmail.setError("This email already Exists");
                    inEmail.requestFocus();
                    Toast.makeText(RegisterActivity.this, "Your email already exists", Integer.bitCount(3)).show();
                } else if (registerResponse.getMessage().contains("successfully")) {
                    Toast.makeText(RegisterActivity.this, registerResponse.getMessage(), Integer.bitCount(3)).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Error", Integer.bitCount(3)).show();
                }
            }
        });
        // Toggle password visibility
        togglePasswordVisibilityButton.setOnClickListener(v -> {
            if (isPasswordVisible) {
                inPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                togglePasswordVisibilityButton.setImageResource(R.drawable.baseline_visibility_24);
            } else {
                inPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                togglePasswordVisibilityButton.setImageResource(R.drawable.baseline_visibility_off_24);
            }
            inPassword.setSelection(inPassword.getText().length());
            isPasswordVisible = !isPasswordVisible;
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void registerUser() {
        String firstName = inFirstName.getText().toString().trim();
        String lastName = inLastName.getText().toString().trim();
        String email = inEmail.getText().toString().trim();
        String password = inPassword.getText().toString().trim();
        registerViewModel.register(firstName, lastName, email, password, RegisterActivity.this);
    }
}
