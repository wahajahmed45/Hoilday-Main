package com.example.apparchilog.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.apparchilog.R;
import com.example.apparchilog.models.responses.Weather;
import com.example.apparchilog.utils.Utils;
import com.example.apparchilog.viewModels.DetailsActivityViewModel;
import com.example.apparchilog.viewModels.DetailsVacationViewModel;
import com.example.apparchilog.viewModels.WeatherViewModel;
import com.example.apparchilog.views.HomeActivity;
import com.example.apparchilog.views.NavigationActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WeatherFragment extends Fragment {
    private TextView temperatureText, descriptionText, humidityText, windSpeedText, pressureText, sunriseText, sunsetText;
    private ImageView weatherIcon;
    private ProgressBar progressBar;
    private static final String ARG_VACATION_ID = "vacation_id";
    private static final String ARG_ACTIVITY_ID = "activity_id";
    private static final String ARG_IS_VACATION = "is_vacation";
    private DetailsVacationViewModel mDViewModel;
    private DetailsActivityViewModel mDAViewModel;
    private Long vacationId;
    private Long activityId;
    private boolean isVacation;
    private BottomNavigationView bottomNavigationView;
    private WeatherViewModel mWeatherViewModel;
    private int selectedItemId = -1;

    public static WeatherFragment newInstance(Long vacationId, Long activityId, boolean isVacation) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_VACATION_ID, vacationId);
        args.putLong(ARG_ACTIVITY_ID, activityId);
        args.putBoolean(ARG_IS_VACATION, isVacation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        mDViewModel = new ViewModelProvider(this).get(DetailsVacationViewModel.class);
        mDAViewModel = new ViewModelProvider(this).get(DetailsActivityViewModel.class);
        if (getArguments() != null) {
            vacationId = getArguments().getLong(ARG_VACATION_ID);
            activityId = getArguments().getLong(ARG_ACTIVITY_ID);
            isVacation = getArguments().getBoolean(ARG_IS_VACATION);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        initIdView(view);

        if (vacationId != 0 && isVacation) {
            mDViewModel.loadVacation(vacationId); // request to API
        }
        if (activityId != 0 && !isVacation) {
            mDAViewModel.loadActivity(activityId); // request to API
        }
        observerViewModel();
        setupBottomNavigationView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void observerViewModel() {
        mDViewModel.getVacationLiveData().observe(getViewLifecycleOwner(), vacation -> {
            if (vacation != null) {
                mWeatherViewModel.fetchWeather(vacation.getPlaceRequest().getLatitude(), vacation.getPlaceRequest().getLongitude());
            }
        });

        mDAViewModel.getActivityLiveData().observe(getViewLifecycleOwner(), activity -> {
            if (activity != null) {
                mWeatherViewModel.fetchWeather(activity.getPlaceRequest().getLatitude(), activity.getPlaceRequest().getLongitude());
            }
        });

        mWeatherViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        mWeatherViewModel.getWeatherMutableLiveData().observe(getViewLifecycleOwner(), this::updateWeatherInfos);

        mWeatherViewModel.getWeatherMessageLiveData().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });

        mDViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        mDAViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void updateWeatherInfos(Weather weather) {
        if (weather == null) return;

        double tempKelvin = weather.getMain().getTemp();
        double tempCelsius = tempKelvin - 273.15;
        temperatureText.setText(String.format("%.02f°C", tempCelsius));

        // Weather description and icon
        descriptionText.setText(weather.getWeather().get(0).getDescription());
        String iconCode = weather.getWeather().get(0).getIcon();
        String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + "@2x.png";
        Glide.with(requireActivity()).load(iconUrl).into(weatherIcon);

        // Additional weather info
        humidityText.setText(String.format("%d%%", weather.getMain().getHumidity())); // Humidity
        windSpeedText.setText(String.format("%.2f m/s", weather.getWind().getSpeed())); // Wind Speed
        pressureText.setText(String.format("%d hPa", weather.getMain().getPressure())); // Pressure

        // Sunrise and sunset time
        long sunrise = weather.getSys().getSunrise();
        long sunset = weather.getSys().getSunset();
        sunriseText.setText(String.format("%s", Utils.formatTime(sunrise)));
        sunsetText.setText(String.format("%s", Utils.formatTime(sunset)));
    }

    private void initIdView(View view) {
        // Initialize views
        temperatureText = view.findViewById(R.id.temperatureText);
        descriptionText = view.findViewById(R.id.descriptionText);
        humidityText = view.findViewById(R.id.humidityText);
        windSpeedText = view.findViewById(R.id.windSpeedText);
        pressureText = view.findViewById(R.id.pressureText);
        sunriseText = view.findViewById(R.id.sunriseText);
        sunsetText = view.findViewById(R.id.sunsetText);
        weatherIcon = view.findViewById(R.id.weatherIcon);
        progressBar = view.findViewById(R.id.progressBar);
        bottomNavigationView = view.findViewById(R.id.bottomVacationNavigationView);
    }

    private void goToHome() {
        if (isVacation) {
            DetailsVacationFragment detailsVacationFragment =
                    DetailsVacationFragment.newInstance(vacationId, activityId);
            ((HomeActivity) requireActivity()).replaceFragment(detailsVacationFragment,
                    true, "Détails vacances");
        } else {
            DetailsActivityFragment detailsActivityFragment =
                    DetailsActivityFragment.newInstance(vacationId, activityId);
            ((HomeActivity) requireActivity()).replaceFragment(detailsActivityFragment,
                    true, "Détails activités");
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void setupBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            boolean isItemSelected = false;

            switch (item.getItemId()) {
                case R.id.home:
                    if (selectedItemId != R.id.home) {
                        goToHome();
                        selectedItemId = R.id.home;
                        isItemSelected = true;
                    }
                    break;
                case R.id.weather:
                    if (selectedItemId != R.id.weather) {
                        selectedItemId = R.id.weather;
                        isItemSelected = true;
                        //bottomNavigationView.setSelectedItemId(selectedItemId);
                    }
                    break;
                case R.id.navigation:
                    if (selectedItemId != R.id.home) {
                        Intent intent = new Intent(getContext(), NavigationActivity.class);
                        intent.putExtra("vacation_id", vacationId);
                        intent.putExtra("activity_id", activityId);
                        intent.putExtra("is_vacation", isVacation);
                        startActivity(intent);
                        selectedItemId = R.id.navigation;
                        isItemSelected = true;
                    }
                    break;
                case R.id.files:
                    if (selectedItemId != R.id.files) {
                        DocumentFragment documentFragment = DocumentFragment.newInstance(vacationId, activityId, isVacation);
                        ((HomeActivity) requireActivity()).replaceFragment(documentFragment, true, "Documents");
                        selectedItemId = R.id.files;
                        isItemSelected = true;
                    }
                    break;
                default:
                    break;
            }
            return isItemSelected;
        });
        // Initialiser le premier élément sélectionné
        bottomNavigationView.setSelectedItemId(R.id.weather);
        selectedItemId = R.id.weather;
    }
}
