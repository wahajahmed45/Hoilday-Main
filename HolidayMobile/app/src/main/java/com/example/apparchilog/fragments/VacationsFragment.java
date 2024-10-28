package com.example.apparchilog.fragments;

import android.os.Bundle;

import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.apparchilog.R;

import com.example.apparchilog.adapters.VacationAdapter;
import com.example.apparchilog.models.holiday.Vacation;
import com.example.apparchilog.viewModels.VacationViewModel;
import com.example.apparchilog.views.HomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class VacationsFragment extends Fragment implements VacationAdapter.OnItemClickListener {

    private FloatingActionButton floatingActionButton;
    private VacationViewModel mVacationViewModel;
    private VacationAdapter vacationAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vacations, container, false);
        initIdView(view);
        setupRecyclerViews(view);
        setupActionButtonOnClickListener();
        loadAllVacations();

        observeViewModel();

        return view;
    }


    private void setupActionButtonOnClickListener() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCreateVacationFragment();
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

    private void setupRecyclerViews(View view) {
        vacationAdapter = new VacationAdapter(getContext(), new ArrayList<>(),false);
        vacationAdapter.setOnItemClickListener(this);
        RecyclerView recyclerView = view.findViewById(R.id.resViewVacation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(vacationAdapter);
    }

    private void initIdView(View view) {
        floatingActionButton = view.findViewById(R.id.floatingActionButton2);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
    }

    private void loadAllVacations() {
        mVacationViewModel.loadAllVacations();
    }

    private void observeViewModel() {
        mVacationViewModel.getAllVacationsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Vacation>>() {
            @Override
            public void onChanged(List<Vacation> vacations) {
                vacationAdapter.updateData(vacations);
            }
        });
        mVacationViewModel.getVacationLiveData().observe(getViewLifecycleOwner(), new Observer<Vacation>() {
            @Override
            public void onChanged(Vacation vacation) {
                onVacationAdded(vacation);
            }
        });
        mVacationViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean isLoading) {
                progressBar.setVisibility(isLoading? View.VISIBLE :View.GONE);
            }
        });

        mVacationViewModel.getErrorMessageLiveDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onVacationAdded(Vacation vacation) {
        vacationAdapter.addVacation(vacation);
    }

    private void goToCreateVacationFragment() {

        assert getActivity() != null;
        CreateHolidayFragment createHolidayFragment = CreateHolidayFragment.newInstance(0L, 0L,false);
        ((HomeActivity) getActivity()).replaceFragment(createHolidayFragment, true, "Créer vacance");
    }

    @Override
    public void onItemClick(Long id) {
        assert getActivity() != null;
        DetailsVacationFragment detailsVacationFragment = DetailsVacationFragment.newInstance(id,0L);
        ((HomeActivity) getActivity()).replaceFragment(detailsVacationFragment, true, "Details Vacances");
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update the toolbar title
        assert getActivity() != null;
        ((HomeActivity) getActivity()).setToolbarTitle("Vacances");
        ((HomeActivity) getActivity()).showBackButton(false);
    }

}
