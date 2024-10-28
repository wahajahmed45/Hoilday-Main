package com.example.apparchilog.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.developer.gbuttons.GoogleSignInButton;

import com.example.apparchilog.R;

import com.example.apparchilog.models.responses.LoginResponse;
import com.example.apparchilog.services.instance.CurrentInstanceUser;
import com.example.apparchilog.viewModels.LoginViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText inEmail, inPassword;
    private Button bLogin;
    private ProgressBar loadingIndicator;
    private ImageButton togglePasswordVisibilityButton;
    private boolean isPasswordVisible = false;
    private GoogleSignInButton googleBtn;
    private GoogleSignInOptions gOptions;
    private GoogleSignInClient gClient;
    private TextView mSignupRedirectText;
    private LoginViewModel loginViewModel;
    private static final String TAG_WRONG = "Something went wrong";
    private static final String TAG_SIGN = "GoogleSignIn";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        initViewId();//init ID
        configToolbar(); // Configure Toolbar
        observerViewModel(); //Observable
        // Event Click
        setUpButtonClick();
        //Config Auth Google
        configGoogle();

        // Toggle password visibility
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void setUpButtonClick() {
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.login(inEmail.getText().toString(), inPassword.getText().toString(), LoginActivity.this);
            }
        });

        mSignupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
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
    }

    private void observerViewModel() {
        loginViewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null && !message.isEmpty()) {
                    if (message.contains("Email")) {
                        inEmail.setError(message);
                        inEmail.requestFocus();
                    } else if (message.contains("Password")) {
                        inPassword.setError(message);
                        inPassword.requestFocus();
                    }
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginViewModel.getLoginResponseLiveData().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                Log.d("LOGIN", "signInResult:success, token: " + loginResponse.getJwtToken());
                if (loginResponse.getJwtToken() != null && !loginResponse.getJwtToken().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Connecté", Toast.LENGTH_SHORT).show();
                    goToNextActivity();
                }
            }
        });

        loginViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        String instanceToken = CurrentInstanceUser.getInstance().getToken();
        //  FirebaseUser currentUser = mAuth.getCurrentUser();
        if (instanceToken != null) {
            goToNextActivity();
        }
    }

    private void configGoogle() {
        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail().build();
        gClient = GoogleSignIn.getClient(this, gOptions);
        ActivityResultLauncher<Intent> activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                if (result.getResultCode() == Activity.RESULT_OK) {
                                    Intent data = result.getData();
                                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                                    try {
                                        task.getResult(ApiException.class);
                                        loginViewModel.sendIdToken(task.getResult().getIdToken(), LoginActivity.this);
                                    } catch (ApiException e) {
                                        Log.d(TAG_WRONG, "signInResult:Failure, personIdToken: " + e.getStatus());
                                    }
                                }
                            }
                        });
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = gClient.getSignInIntent();
                activityResultLauncher.launch(signInIntent);
            }
        });

    }

    private void initViewId() {
        loadingIndicator = findViewById(R.id.progressBar);
        inEmail = findViewById(R.id.inEmail);
        inPassword = findViewById(R.id.inPassword);
        bLogin = findViewById(R.id.bLogin);
        togglePasswordVisibilityButton = findViewById(R.id.togglePasswordVisibility);
        googleBtn = findViewById(R.id.googleBtn);
        mSignupRedirectText = findViewById(R.id.signUpRedirectText);
    }

    private void configToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void goToNextActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}