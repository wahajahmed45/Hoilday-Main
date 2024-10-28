package com.example.apparchilog.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.view.*;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.apparchilog.R;
import com.example.apparchilog.models.holiday.Event;
import com.example.apparchilog.adapters.ParticipantAdapter;
import com.example.apparchilog.models.holiday.Participant;
import com.example.apparchilog.models.holiday.Activity;
import com.example.apparchilog.utils.DateConvertor;
import com.example.apparchilog.utils.Utils;
import com.example.apparchilog.viewModels.ActivityViewModel;
import com.example.apparchilog.viewModels.DetailsActivityViewModel;
import com.example.apparchilog.viewModels.ParticipantViewModel;
import com.example.apparchilog.views.HomeActivity;
import com.example.apparchilog.views.NavigationActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class DetailsActivityFragment extends Fragment implements ParticipantAdapter.OnDeleteClickListener {
    private static final String ARG_ACTIVITY_ID = "activity_id";
    private static final String ARG_VACATION_ID = "vacation_id";
    private DetailsActivityViewModel mDViewModel;
    private TextView tvActivityName, tvActivityDescription, tvActivityPeriod, tvActivityOwner, tvActivityPlace, tvActivityStatus;
    private RecyclerView recyclerViewParticipants;
    private FloatingActionButton fabAddParticipant;
    private ProgressBar progressBar;
    private ParticipantViewModel mParticipantViewModel;
    private ActivityViewModel mActivityViewModel;
    private ParticipantAdapter participantAdapter;
    private BottomNavigationView bottomNavigationView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Long activityId;
    private Long vacationId;
    private Toolbar toolbar;
    private int selectedItemId = -1;

    public static DetailsActivityFragment newInstance(Long idVacation, Long idActivity) {
        DetailsActivityFragment fragment = new DetailsActivityFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_VACATION_ID, idVacation);
        args.putLong(ARG_ACTIVITY_ID, idActivity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDViewModel = new ViewModelProvider(this).get(DetailsActivityViewModel.class);
        mParticipantViewModel = new ViewModelProvider(this).get(ParticipantViewModel.class);
        mActivityViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        if (getArguments() != null) {
            vacationId = getArguments().getLong(ARG_VACATION_ID);
            activityId = getArguments().getLong(ARG_ACTIVITY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        initIdView(view);
        setupRecyclerViews(view);
        setupActionButtonOnClickListener();
        setupBottomNavigationView();
        loadActivityById();
        // setupBottomNavigationView();
        observeViewModel();

    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void loadActivityById() {
        mDViewModel.loadActivity(activityId);
    }

    private void observeViewModel() {
        mDViewModel.getParticipants().observe(getViewLifecycleOwner(), new Observer<List<Participant>>() {

            @Override
            public void onChanged(List<Participant> participants) {
                participantAdapter.updateParticipant(participants);
            }
        });
        mDViewModel.getActivityLiveData().observe(getViewLifecycleOwner(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                tvActivityName.setText(activity.getNom());
                tvActivityDescription.setText(activity.getDescription());
                String startD = DateConvertor.convertOffsetDateTimeToString(activity.getDateDebut());
                String endD = DateConvertor.convertOffsetDateTimeToString(activity.getDateFin());
                tvActivityPeriod.setText(String.format("%s - %s", startD, endD));
                tvActivityStatus.setText(activity.getStatus());
                Utils.changeColor(tvActivityStatus, getContext());
                tvActivityPlace.setText(String.format("%s - %s", activity.getPlaceRequest().getPays(), activity.getPlaceRequest().getVille()));
            }
        });
        mDViewModel.getMessageLiveDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();

            }
        });
        mDViewModel.getEventLiveData().observe(getViewLifecycleOwner(), new Observer<Event>() {
            @Override
            public void onChanged(Event event) {
                if (event != null) {
                    showConfirmationDialog(event);
                }
            }
        });
    }

    private void setupActionButtonOnClickListener() {
        fabAddParticipant.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Go to create participant
                ParticipantFragment participantFragment = ParticipantFragment.newInstance(vacationId, activityId, false);
                ((HomeActivity) requireActivity()).replaceFragment(participantFragment, true, "Ajouter participant");

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadActivityById();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setupRecyclerViews(View view) {
        participantAdapter = new ParticipantAdapter(getContext(), new ArrayList<>());
        participantAdapter.setOnDeleteClickListener(this);
        recyclerViewParticipants.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewParticipants.setAdapter(participantAdapter);
    }

    private void initIdView(View view) {
        // Initialize views
        //   toolbar = view.findViewById(R.id.toolbar);
        tvActivityName = view.findViewById(R.id.tvActivityName);
        tvActivityDescription = view.findViewById(R.id.tvActivityDescription);
        tvActivityPeriod = view.findViewById(R.id.tvActivityPeriod);
        progressBar = view.findViewById(R.id.progressBar);
        tvActivityPlace = view.findViewById(R.id.tvActivityPlace);
        tvActivityStatus = view.findViewById(R.id.tvActivityStatus);
        recyclerViewParticipants = view.findViewById(R.id.recyclerViewParticipants);
        fabAddParticipant = view.findViewById(R.id.floatingActionButton2);
        bottomNavigationView = view.findViewById(R.id.bottomVacationNavigationView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
    }

    @Override
    public void onDeleteClick(Long id_participant) {
        showConfirmationDialog(id_participant);
    }

    @Override
    public void onResume() {
        super.onResume();
        assert getActivity() != null;
        ((HomeActivity) getActivity()).showBackButton(true);
        ((HomeActivity) requireActivity()).setToolbarTitle("Détails activités");
    }

    private void showConfirmationDialog(Long participantId) {
        ConfirmationDialogFragment confirmationDialog = ConfirmationDialogFragment.newInstance(participantId, vacationId, activityId, 0L);
        confirmationDialog.setOnConfirmationDialogListener(new ConfirmationDialogFragment.ConfirmationDialogListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog, Long participantId, Long vacationID, Long activityId) {
                mParticipantViewModel.removeParticipant(activityId, participantId, false);
                mParticipantViewModel.getMessageLiveDate().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s.contains("success")) {
                            participantAdapter.removeParticipant(participantId);
                        }
                    }
                });
                mParticipantViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isLoading) {
                        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                    }
                });
                dialog.dismiss();
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
                dialog.dismiss();
            }
        });

        confirmationDialog.show(getParentFragmentManager(), "ConfirmationDialog");
    }

    private void showActivityDialog(Long vacationId, Long activityId) {
        ConfirmationDialogFragment confirmationDialog = ConfirmationDialogFragment.newInstance(0L, vacationId, activityId, 0L);
        confirmationDialog.setOnConfirmationDialogListener(new ConfirmationDialogFragment.ConfirmationDialogListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog, Long participantId, Long vacationID, Long activityID) {
                mActivityViewModel.removerActivity(vacationID, activityID);
                mActivityViewModel.getMessageToastLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s.contains("success")) {
                            assert getActivity() != null;
                            ActivityFragment activityFragment = new ActivityFragment();
                            ((HomeActivity) getActivity()).replaceFragment(activityFragment, false, "Activité");
                            Toast.makeText(requireContext(), s, Toast.LENGTH_LONG).show();
                        }
                    }
                });
                mActivityViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isLoading) {
                        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                    }
                });
                dialog.dismiss();
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
                dialog.dismiss();
            }
        });

        confirmationDialog.show(getParentFragmentManager(), "ConfirmationDialog");
    }

    private void showConfirmationDialog(Event event) {
        ConfirmationDialogFragment confirmationDialog = ConfirmationDialogFragment.newInstance(0L, vacationId, 0L, event.getId());
        confirmationDialog.setOnConfirmationDialogListener(new ConfirmationDialogFragment.ConfirmationDialogListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog, Long participantId, Long vacationId, Long activityId) {
                CalendarFragment calendarFragment = new CalendarFragment();
                ((HomeActivity) requireActivity()).replaceFragment(calendarFragment, false, "Calendar");
                dialog.dismiss();
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
                dialog.dismiss();
            }
        });

        confirmationDialog.show(getParentFragmentManager(), "ConfirmationDialog");

    }

    @SuppressLint("NonConstantResourceId")
    private void setupBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            boolean isItemSelected = false;
            switch (item.getItemId()) {
                case R.id.home:
                    if (selectedItemId != R.id.home) {
                         selectedItemId = R.id.home;
                        isItemSelected = true;
                    }
                    break;
                case R.id.weather:
                    WeatherFragment weatherFragment = WeatherFragment.newInstance(vacationId, activityId, false);
                    ((HomeActivity) requireActivity()).replaceFragment(weatherFragment, true, "Météo");
                    selectedItemId = R.id.weather;
                    isItemSelected = true;
                    return true;
                case R.id.navigation:
                    if (selectedItemId != R.id.navigation) {
                        Intent intent = new Intent(getContext(), NavigationActivity.class);
                        intent.putExtra("vacation_id", vacationId);
                        intent.putExtra("activity_id", activityId);
                        intent.putExtra("is_vacation", false);
                        startActivity(intent);

                        selectedItemId = R.id.navigation;
                        isItemSelected = true;
                    }
                    break;
                case R.id.files:
                    if (selectedItemId != R.id.files) {
                        DocumentFragment documentFragment = DocumentFragment.newInstance(vacationId, activityId, false);
                        ((HomeActivity) requireActivity()).replaceFragment(documentFragment, true, "Documents");
                        //bottomNavigationView.setSelectedItemId(R.id.files);
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
        bottomNavigationView.setSelectedItemId(R.id.home);
        selectedItemId = R.id.home;
    }


    // Gérer les actions du menu
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                goToUpdateActivity();
                return true;
            case R.id.action_delete:
                showActivityDialog(vacationId, activityId);
                return true;
            case R.id.action_add_calendar:
                addEventToCalendar(activityId);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addEventToCalendar(Long activityId) {
        mDViewModel.addEvenToCalendar(activityId);
    }

    private void goToUpdateActivity() {
        CreateHolidayFragment createHolidayFragment =
                CreateHolidayFragment.newInstance(vacationId, activityId, false);
        ((HomeActivity) requireActivity()).replaceFragment(createHolidayFragment, true, "Edite activité");

    }

}