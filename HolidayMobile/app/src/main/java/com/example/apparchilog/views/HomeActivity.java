package com.example.apparchilog.views;

import android.annotation.SuppressLint;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.example.apparchilog.R;
import com.example.apparchilog.models.responses.LoginResponse;
import com.example.apparchilog.fragments.*;
import com.example.apparchilog.viewModels.HomeViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String EXTRA_FRAGMENT = "com.example.apparchilog.FRAGMENT";
    public static final int FRAGMENT_DETAIL_VACATION = 1;
    public static final int FRAGMENT_DETAIL_ACTIVITY = 2;
    public static final int FRAGMENT_WEATHER = 3;
    public static final int FRAGMENT_DOCUMENT = 4;
    private static final String ARG_VACATION_ID = "vacation_id";
    private static final String ARG_ACTIVITY_ID = "activity_id";
    private static final String ARG_IS_VACATION = "is_vacation";

    private DrawerLayout drawerLayout;
    private TextView nameUserTextView;
    private TextView emailUserTextView;
    private HomeViewModel homeViewModel;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private CircleImageView imageViewAvatar;
    private Long vacationId;
    private Long activityId;
    private boolean isVacation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        nameUserTextView = headerView.findViewById(R.id.nameUser);
        emailUserTextView = headerView.findViewById(R.id.emailUser);
        imageViewAvatar = headerView.findViewById(R.id.imageViewAvatar);

        drawerLayout = findViewById(R.id.drawer_layout);
        //Observable
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getUserLiveData().observe(this, new Observer<LoginResponse>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(LoginResponse loginResponse) {
                if (loginResponse != null) {
                    nameUserTextView.setText(loginResponse.getFirstname() + " " + loginResponse.getLastname());
                    emailUserTextView.setText(loginResponse.getEmail());
                    drawImageProfile(loginResponse);
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        toggle = new

                ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VacationsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_vacation);
            int fragmentId = getIntent().getIntExtra(EXTRA_FRAGMENT, -1);
            if (fragmentId != -1) {
                vacationId = getIntent().getLongExtra(ARG_VACATION_ID, 0L);
                activityId = getIntent().getLongExtra(ARG_ACTIVITY_ID, 0L);
                isVacation = getIntent().getBooleanExtra(ARG_IS_VACATION, false);
                displayFragment(fragmentId);
            }
        }

    }

    private void displayFragment(int fragmentId) {
        Fragment selectedFragment;
        String title = "";

        switch (fragmentId) {
            case FRAGMENT_DETAIL_VACATION:
                selectedFragment = DetailsVacationFragment.newInstance(vacationId, activityId);
                break;
            case FRAGMENT_DETAIL_ACTIVITY:
                selectedFragment = DetailsActivityFragment.newInstance(vacationId, activityId);
                title = "Détails activités";
                break;
            case FRAGMENT_WEATHER:
                selectedFragment = WeatherFragment.newInstance(vacationId, activityId, isVacation);
                title = "Météo";
                break;
            case FRAGMENT_DOCUMENT:
                selectedFragment = DocumentFragment.newInstance(vacationId, activityId, isVacation);
                title = "Documents";
                break;
            default:
                selectedFragment = new VacationsFragment();
                title = "Vacances";
                break;
        }
        replaceFragment(selectedFragment, true, title);

    }

    private void drawImageProfile(LoginResponse loginResponse) {
        if (loginResponse.getPictureUrl() != null && !loginResponse.getPictureUrl().isEmpty())
            Glide.with(HomeActivity.this)
                    .load(loginResponse.getPictureUrl())
                    .placeholder(R.drawable.baseline_account_circle_24)
                    .into(imageViewAvatar);
        else {
            // Generate an image with the first letter of the first name
            String firstLetter = String.valueOf(loginResponse.getFirstname().charAt(0));
            ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
            int color = colorGenerator.getColor(loginResponse.getFirstname());

            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstLetter, color); // radius in px

            imageViewAvatar.setImageDrawable(drawable);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_vacation:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VacationsFragment()).commit();
                setToolbarTitle("Vacances");
                break;
            case R.id.nav_act:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ActivityFragment()).commit();
                setToolbarTitle("Activités");
                break;
            case R.id.nav_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatsFragment()).commit();
                setToolbarTitle("Chats");
                break;
            case R.id.nav_emploi:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CalendarFragment()).commit();
                setToolbarTitle("Calendriers");
                break;
            case R.id.nav_logout:
                logout();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        GoogleSignInOptions gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail().build();
        GoogleSignInClient gClient = GoogleSignIn.getClient(this, gOptions);
        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (gAccount != null || currentUser != null) {
            gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    homeViewModel.logout();
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(HomeActivity.this, WelcomeActivity.class));
                    Toast.makeText(HomeActivity.this, "Merci de votre visite ", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack, String title) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //toggle.setDrawerIndicatorEnabled(false);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            // toggle.setDrawerIndicatorEnabled(true);
            // toggle.syncState();
            toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                    R.string.close_nav);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setCheckedItem(R.id.nav_vacation);
        }

        transaction.commit();
        setToolbarTitle(title);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment instanceof CreateHolidayFragment || fragment instanceof DetailsVacationFragment ||
                    fragment instanceof CreateDocumentFragment) {
                navigateToVacationsFragment();
            } else if (fragment instanceof DetailsActivityFragment) {
                navigateToActivityFragment();
            } else if (fragment instanceof MessagesFragment) {
                navigateToChatsFragment();
            } else {
                super.onBackPressed();
            }
        }
    }

    private void navigateToChatsFragment() {
        replaceFragment(new ChatsFragment(), false, "Chats");
        navigationView.setCheckedItem(R.id.nav_chat);
    }

    private void navigateToActivityFragment() {
        replaceFragment(new ActivityFragment(), false, "Activity");
        navigationView.setCheckedItem(R.id.nav_act);
    }

    /*  @Override
      public boolean onOptionsItemSelected(MenuItem item) {
          if (item.getItemId() == android.R.id.home) {
              onBackPressed();
              return true;
          }
          return super.onOptionsItemSelected(item);
      }*/
    private void navigateToVacationsFragment() {
        // getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        replaceFragment(new VacationsFragment(), false, "Vacances");
        navigationView.setCheckedItem(R.id.nav_vacation);
    }

    public void setToolbarTitle(String title) {
        if (toolbar != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void showBackButton(boolean show) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(show);
            actionBar.setDisplayShowHomeEnabled(show);
        }
    }

    public void hideToolbar() {
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }
    }

    public void showToolbar() {
        if (toolbar != null) {
            toolbar.setVisibility(View.VISIBLE);
        }
    }
}
