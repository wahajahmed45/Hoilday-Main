package com.example.apparchilog.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.apparchilog.R;
import com.example.apparchilog.adapters.VacationDocumentAdapter;
import com.example.apparchilog.models.holiday.Vacation;
import com.example.apparchilog.utils.Utils;
import com.example.apparchilog.viewModels.VacationViewModel;
import com.example.apparchilog.views.HomeActivity;
import com.example.apparchilog.views.NavigationActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DocumentFragment extends Fragment {
    private static final String ARG_VACATION_ID = "vacation_id";
    private static final String ARG_ACTIVITY_ID = "activity_id";
    private static final String ARG_IS_VACATION = "is_vacation";
    private FloatingActionButton floatingActionButton;
    private VacationViewModel mVacationViewModel;
    private VacationDocumentAdapter vacationDocumentAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BottomNavigationView bottomNavigationView;
    private ProgressBar progressBar;
    private Long activityId;
    private Long vacationId;
    private boolean isVacation;
    private int selectedItemId = -1;

    @SuppressWarnings("unused")
    public static DocumentFragment newInstance(Long vacationId, Long activityI, boolean isVacation) {
        DocumentFragment fragment = new DocumentFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_VACATION_ID, vacationId);
        args.putLong(ARG_ACTIVITY_ID, activityI);
        args.putBoolean(ARG_IS_VACATION, isVacation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);
        if (getArguments() != null) {
            vacationId = getArguments().getLong(ARG_VACATION_ID);
            activityId = getArguments().getLong(ARG_ACTIVITY_ID);
            isVacation = getArguments().getBoolean(ARG_IS_VACATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_document, container, false);
        initIdView(view);
        setupRecyclerViews(view);
        setupActionButtonOnClickListener();
        loadAllVacations();
        setupBottomNavigationView();
        observeViewModel();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initIdView(View view) {
        floatingActionButton = view.findViewById(R.id.floatingActionButton2);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        bottomNavigationView = view.findViewById(R.id.bottomVacationNavigationView);

    }

    private void setupRecyclerViews(View view) {
        vacationDocumentAdapter = new VacationDocumentAdapter(getContext(), new ArrayList<>());
        RecyclerView recyclerView = view.findViewById(R.id.resViewVacationsDocument);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(vacationDocumentAdapter);
    }

    private void setupActionButtonOnClickListener() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showVacationSelectionDialog(getContext(), requireActivity(), DocumentFragment.this,
                        true, getViewLifecycleOwner(), activityId, isVacation);

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAllVacations();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadAllVacations() {
        mVacationViewModel.loadAllVacations();
    }

    private void observeViewModel() {
        mVacationViewModel.getAllVacationsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Vacation>>() {
            @Override
            public void onChanged(List<Vacation> vacations) {
                vacationDocumentAdapter.updateData(vacations);
            }
        });
        mVacationViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
        mVacationViewModel.getErrorMessageLiveDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        assert getActivity() != null;
        ((HomeActivity) getActivity()).showBackButton(true);
        ((HomeActivity) requireActivity()).setToolbarTitle("Documents");
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
                        WeatherFragment weatherFragment = WeatherFragment.newInstance(vacationId, activityId, isVacation);
                        ((HomeActivity) requireActivity()).replaceFragment(weatherFragment, true, "Météo");
                        // bottomNavigationView.setSelectedItemId(R.id.weather);
                        selectedItemId = R.id.weather;
                        isItemSelected = true;
                    }
                    break;
                case R.id.navigation:
                    if (selectedItemId != R.id.navigation) {
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
        bottomNavigationView.setSelectedItemId(R.id.files);
        selectedItemId = R.id.files;
    }
}