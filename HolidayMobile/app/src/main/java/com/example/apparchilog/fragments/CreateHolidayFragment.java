package com.example.apparchilog.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.apparchilog.R;
import com.example.apparchilog.models.holiday.Activity;
import com.example.apparchilog.utils.DateConvertor;
import com.example.apparchilog.viewModels.ActivityViewModel;
import com.example.apparchilog.viewModels.DetailsActivityViewModel;
import com.example.apparchilog.viewModels.VacationViewModel;
import com.example.apparchilog.views.HomeActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.textfield.TextInputEditText;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateHolidayFragment extends Fragment {

    private TextInputEditText etName, etDescription, etStartDate, etEndDate, etCountry, etCity;
    private Button btnCreate, btnCreateActivity;
    private ImageView ivStartDate, ivEndDate;
    private ProgressBar progressBar;
    private VacationViewModel mVacationViewModel;
    private DetailsActivityViewModel mDetailsActivityViewModel;
    private ActivityViewModel mActivityViewModel;
    private OffsetDateTime startDate, endDate;
    private AutocompleteSupportFragment autocompleteFragment;
    private Address address;
    private static final String ARG_VACATION_ID = "vacation_id";
    private static final String ARG_ACTIVITY_F = "activity_fragment";
    private static final String ARG_ACTIVITY_ID = "activity_id";
    private Long activityId;
    private Long vacationId;
    private boolean isActivityFragment;

    public static CreateHolidayFragment newInstance(Long vacationId, Long activityId, boolean activityFragment) {
        CreateHolidayFragment fragment = new CreateHolidayFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_VACATION_ID, vacationId);
        args.putBoolean(ARG_ACTIVITY_F, activityFragment);
        args.putLong(ARG_ACTIVITY_ID, activityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);
        mDetailsActivityViewModel = new ViewModelProvider(this).get(DetailsActivityViewModel.class);
        mActivityViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        if (getArguments() != null) {
            vacationId = getArguments().getLong(ARG_VACATION_ID);
            isActivityFragment = getArguments().getBoolean(ARG_ACTIVITY_F);
            activityId = getArguments().getLong(ARG_ACTIVITY_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_vacation_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initIdView(view);
        OnClickDatePicker();
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.google_place_apiKey));
            Log.d("init", "init ok ");
        }
        autoCompleteFragment();
        onClickListener();
        liveDataObserver();
        loadActivity();

    }

    private void loadActivity() {
        if (vacationId != 0L && activityId != 0L) {
            mDetailsActivityViewModel.loadActivity(activityId);
        }
    }

    private void onClickListener() {
        btnCreate.setOnClickListener(v -> {
            String name = String.valueOf(etName.getText());
            String description = String.valueOf(etDescription.getText());
            OffsetDateTime startDate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                startDate = DateConvertor.convertStringToOffsetDateTime(String.valueOf(etStartDate.getText()));
            }
            OffsetDateTime endDate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                endDate = DateConvertor.convertStringToOffsetDateTime(String.valueOf(etEndDate.getText()));
            }
            String country = String.valueOf(etCountry.getText());
            String city = String.valueOf(etCity.getText());
            if (vacationId == 0L && activityId == 0L) {//creation vacation
                mVacationViewModel.createVacation(name, description, startDate, endDate, address, city, country);
            } else if (vacationId != 0L && activityId == 0L) {
                mActivityViewModel.createActivity(vacationId, name, description, startDate, endDate, address, city, country);
            } else if (vacationId != 0L) {
                mActivityViewModel.updateActivity(vacationId, activityId, name, description, startDate, endDate, address, city, country);
            }
            //navigateToVacationsFragment();
        });

    }

    private void autoCompleteFragment() {
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        autocompleteFragment.setHint("Chercher un lieu");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull @NotNull Place place) {
                if (place.getName() != null) {
                    //tvPlaceDetail.setText(place.getAddress());
                    etCity.setText(place.getName());
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    LatLng coordinates = place.getLatLng();
                    List<Address> addresses = null;
                    try {
                        assert coordinates != null;
                        addresses = geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1);
                        address = addresses.get(0);
                        etCountry.setText(address.getCountryName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onError(@NonNull @NotNull Status status) {
                Log.e("error", status.getStatusMessage());
            }

        });
    }

    private void liveDataObserver() {
        mVacationViewModel.getErrorMessageLiveDate().observe(getViewLifecycleOwner(), new Observer<String>() {

            @Override
            public void onChanged(String message) {
                if (message != null && !message.isEmpty()) {
                    if (message.contains("1.")) {
                        etName.setError(message.substring(2));
                        etName.requestFocus();
                    } else if (message.contains("2.")) {
                        etDescription.setError(message.substring(2));
                        etDescription.requestFocus();
                    } else if (message.contains("3.")) {
                        etStartDate.setError(message.substring(2));
                        etStartDate.requestFocus();
                    } else if (message.contains("4.")) {
                        etEndDate.setError(message.substring(2));
                        etEndDate.requestFocus();
                    } else if (message.contains("5.")) {
                        etEndDate.setError(message.substring(2));
                        etEndDate.requestFocus();
                    } else if (message.contains("6.")) {
                        etStartDate.setError(message.substring(2));
                        etStartDate.requestFocus();
                    } else if (message.contains("7.")) {
                        etStartDate.setError(message.substring(2));
                        etStartDate.requestFocus();
                    } else if (message.contains("8.")) {
                        etCountry.setError(message.substring(2));
                        etCountry.requestFocus();
                    } else if (message.contains("9.")) {
                        etCity.setError(message.substring(2));
                        etCity.requestFocus();
                    }
                    Toast.makeText(requireContext(), message.substring(2), Toast.LENGTH_LONG).show();
                }
            }
        });
        mVacationViewModel.getResponseServerLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null && !message.isEmpty()) {
                    if (message.contains("Success")) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
                        navigateToVacationsFragment(message);
                    } else {
                        Log.d("CreateVacationFragment", message);
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        mActivityViewModel.getActivityErrorMessageLiveDate().observe(getViewLifecycleOwner(), new Observer<String>() {

            @Override
            public void onChanged(String message) {
                if (message != null && !message.isEmpty()) {
                    if (message.contains("1.")) {
                        etName.setError(message.substring(2));
                        etName.requestFocus();
                    } else if (message.contains("2.")) {
                        etDescription.setError(message.substring(2));
                        etDescription.requestFocus();
                    } else if (message.contains("3.")) {
                        etStartDate.setError(message.substring(2));
                        etStartDate.requestFocus();
                    } else if (message.contains("4.")) {
                        etEndDate.setError(message.substring(2));
                        etEndDate.requestFocus();
                    } else if (message.contains("5.")) {
                        etEndDate.setError(message.substring(2));
                        etEndDate.requestFocus();
                    } else if (message.contains("6.")) {
                        etStartDate.setError(message.substring(2));
                        etStartDate.requestFocus();
                    } else if (message.contains("7.")) {
                        etStartDate.setError(message.substring(2));
                        etStartDate.requestFocus();
                    } else if (message.contains("8.")) {
                        etCountry.setError(message.substring(2));
                        etCountry.requestFocus();
                    } else if (message.contains("9.")) {
                        etCity.setError(message.substring(2));
                        etCity.requestFocus();
                    }
                    Toast.makeText(requireContext(), message.substring(2), Toast.LENGTH_LONG).show();
                }
            }
        });
        mActivityViewModel.getMessageToastLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message.contains("Success")) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
                    navigateToVacationsFragment(message);
                } else {
                    Log.d("TAG_CREATION", message);
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
                }

            }
        });
     /*   mActivityViewModel.getActivityLiveData().observe(getViewLifecycleOwner(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {

            }
        });*/

        mDetailsActivityViewModel.getActivityLiveData().observe(getViewLifecycleOwner(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                etName.setText(activity.getNom());
                etDescription.setText(activity.getDescription());
                String startD = DateConvertor.convertOffsetDateTimeToString(activity.getDateDebut());
                String endD = DateConvertor.convertOffsetDateTimeToString(activity.getDateFin());
                etStartDate.setText(startD);
                etEndDate.setText(endD);
                etCity.setText(activity.getPlaceRequest().getVille());
                etCountry.setText(activity.getPlaceRequest().getPays());
            }
        });
        mDetailsActivityViewModel.getMessageLiveDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
            }
        });

        mVacationViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean isLoading) {
                if (!isLoading) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
        mActivityViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean isLoading) {
                if (!isLoading) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
        mDetailsActivityViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean isLoading) {
                if (!isLoading) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void OnClickDatePicker() {
        ivStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                final int[] dayMonth = {calendar.get(Calendar.DAY_OF_MONTH)};

                DatePickerDialog dialog;
                dialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;

                        String formattedDay = String.format("%02d", dayOfMonth);
                        String formattedMonth = String.format("%02d", month);
                        //etStartDate.clearComposingText();
                        etStartDate.setText(formattedDay + "/" + formattedMonth + "/" + year);
                    }
                }, year, month, dayMonth[0]);
                dialog.show();
            }
        });
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                final int[] dayMonth = {calendar.get(Calendar.DAY_OF_MONTH)};

                DatePickerDialog dialog;
                dialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;

                        String formattedDay = String.format("%02d", dayOfMonth);
                        String formattedMonth = String.format("%02d", month);
                        etStartDate.setText(formattedDay + "/" + formattedMonth + "/" + year);
                    }
                }, year, month, dayMonth[0]);
                dialog.show();
            }
        });
        ivEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                final int[] dayMonth = {calendar.get(Calendar.DAY_OF_MONTH)};

                DatePickerDialog dialog;
                dialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String formattedDay = String.format("%02d", dayOfMonth);
                        String formattedMonth = String.format("%02d", month);
                        etEndDate.setText(formattedDay + "/" + formattedMonth + "/" + year);
                    }
                }, year, month, dayMonth[0]);
                dialog.show();
            }
        });
        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                final int[] dayMonth = {calendar.get(Calendar.DAY_OF_MONTH)};

                DatePickerDialog dialog;
                dialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String formattedDay = String.format("%02d", dayOfMonth);
                        String formattedMonth = String.format("%02d", month);
                        etEndDate.setText(formattedDay + "/" + formattedMonth + "/" + year);
                    }
                }, year, month, dayMonth[0]);
                dialog.show();
            }
        });

    }

    private void initIdView(View view) {
        etName = view.findViewById(R.id.etLastname);
        etDescription = view.findViewById(R.id.etFirstname);
        etStartDate = view.findViewById(R.id.etStartDate);
        etEndDate = view.findViewById(R.id.etEndDate);
        ivStartDate = view.findViewById(R.id.ivStartDate);
        ivEndDate = view.findViewById(R.id.ivEndDate);
        etCountry = view.findViewById(R.id.etEmail);
        etCity = view.findViewById(R.id.etCity);
        btnCreate = view.findViewById(R.id.btnCreate);
        progressBar = view.findViewById(R.id.progressBar);
        //tvPlaceDetail = view.findViewById(R.id.tvPlaceDetails);
        autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.place_fragment_auto);
    }


    private void navigateToVacationsFragment(String message) {
        if (vacationId == 0L && activityId == 0L) {
            ((HomeActivity) requireActivity()).replaceFragment(new VacationsFragment(), false, "Vacances");
        } else if (vacationId != 0L && activityId == 0L) {
            if (isActivityFragment) {
                // Go to Home activity
                ((HomeActivity) requireActivity()).replaceFragment(new ActivityFragment(), false, "Activités");
            } else {
                DetailsVacationFragment detailsVacationFragment = DetailsVacationFragment.newInstance(vacationId, 0L);
                ((HomeActivity) requireActivity()).replaceFragment(detailsVacationFragment, true, "Détails vacances");

            }
        } else if (vacationId != 0L) {
            DetailsActivityFragment activityFragment = DetailsActivityFragment.newInstance(vacationId, activityId);
            ((HomeActivity) requireActivity()).replaceFragment(activityFragment, true, "Détails Activités");

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        assert getActivity() != null;
        ((HomeActivity) getActivity()).showBackButton(true);
        if (vacationId == 0 && activityId == 0L) {
            ((HomeActivity) requireActivity()).setToolbarTitle("Créer vacance");
        } else if (vacationId != 0L && activityId == 0L) {
            ((HomeActivity) requireActivity()).setToolbarTitle("Créer Activité");

        } else if (vacationId != 0L) {
            ((HomeActivity) requireActivity()).setToolbarTitle("Edite Activité");
        }
    }

}
