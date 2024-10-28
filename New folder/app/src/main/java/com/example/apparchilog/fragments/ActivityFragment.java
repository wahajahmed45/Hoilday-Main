package com.example.apparchilog.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.apparchilog.R;
import com.example.apparchilog.adapters.ActivityAdapter;
import com.example.apparchilog.adapters.VacationAdapter;
import com.example.apparchilog.utils.Utils;
import com.example.apparchilog.viewModels.ActivityViewModel;
import com.example.apparchilog.viewModels.VacationViewModel;
import com.example.apparchilog.views.HomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ActivityFragment extends Fragment implements ActivityAdapter.OnItemClickListener {

    private ActivityViewModel mAViewModel;
    private ActivityAdapter activityAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton floatingActionButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initIdView(view);
        setupRecyclerViews(view);
        setupActionButtonOnClickListener();
        loadAllActivity();
        observeViewModel();
    }

    private void loadAllActivity() {
        mAViewModel.loadAllActivity();
    }

    private void observeViewModel() {
        mAViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });
        mAViewModel.getAllActivityLiveData().observe(getViewLifecycleOwner(), activities -> activityAdapter.updateActivities(activities));
        mAViewModel.getActivityLiveData().observe(getViewLifecycleOwner(), activity -> activityAdapter.addActivity(activity));
        mAViewModel.getActivityErrorMessageLiveDate().observe(getViewLifecycleOwner(), s -> Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show());
    }

    private void setupActionButtonOnClickListener() {
        floatingActionButton.setOnClickListener(view -> {
            Utils.showVacationSelectionDialog(getContext(), requireActivity(), ActivityFragment.this, true, getViewLifecycleOwner(),0L,false);

        });
        swipeRefreshLayout.setOnRefreshListener(() -> {
            mAViewModel.loadAllActivity();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void goToFragmentCreation(Long id_vacation) {
        CreateHolidayFragment createHolidayFragment = CreateHolidayFragment.newInstance(id_vacation, 0L, true);
        assert getActivity() != null;
        ((HomeActivity) getActivity()).replaceFragment(createHolidayFragment, true, "Créer Activité");
    }

    private void showVacationSelectionDialog() {
        AlertDialog vacationDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_select_vacation, null);
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);
        RecyclerView recyclerViewVacations = dialogView.findViewById(R.id.recyclerViewVacations);
        recyclerViewVacations.setLayoutManager(new LinearLayoutManager(getContext()));
        builder.setView(dialogView);

        VacationViewModel viewModel = new ViewModelProvider(ActivityFragment.this).get(VacationViewModel.class);
        VacationAdapter adapter = new VacationAdapter(getContext(), new ArrayList<>(), true);
        recyclerViewVacations.setAdapter(adapter);

        vacationDialog = builder.create();
        vacationDialog.show();

        viewModel.loadAllVacations();
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerViewVacations.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                recyclerViewVacations.setVisibility(View.VISIBLE);
            }
        });
        viewModel.getAllVacationsLiveData().observe(getViewLifecycleOwner(), newVacations -> {
            if (newVacations.isEmpty()) {
                tvDialogTitle.setText("Vous n'avez pas encore de vacance!");
            }
            adapter.updateData(newVacations);
        });
        adapter.setOnVacationClickListener(vacation -> {
            if (!vacation.getStatus().equals("Passé")) {
                goToFragmentCreation(vacation.getId());
                vacationDialog.dismiss();
            }
        });
    }

    private void setupRecyclerViews(View view) {
        activityAdapter = new ActivityAdapter(getContext(), new ArrayList<>());
        activityAdapter.setOnItemClickListener(this);
        RecyclerView recyclerViewActivities = view.findViewById(R.id.recyclerViewActivities);
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewActivities.setAdapter(activityAdapter);
    }

    private void initIdView(View view) {
        floatingActionButton = view.findViewById(R.id.floatingActionButton2);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
    }

    @Override
    public void onItemClick(Long id_activity) {
        // Go to detail activity
        Long vacationId = activityAdapter.getVacationId(id_activity);
        DetailsActivityFragment detailsActivityFragment =
                DetailsActivityFragment.newInstance(vacationId, id_activity);
        ((HomeActivity) requireActivity()).replaceFragment(detailsActivityFragment, true, "Détails activités");

    }

    @Override
    public void onResume() {
        super.onResume();
        // Update the toolbar title
        assert getActivity() != null;
        ((HomeActivity) getActivity()).setToolbarTitle("Activités");
        ((HomeActivity) getActivity()).showBackButton(false);
    }
}
