package com.example.apparchilog.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.apparchilog.R;
import com.example.apparchilog.models.holiday.Event;
import com.example.apparchilog.adapters.ActivityAdapter;
import com.example.apparchilog.adapters.ParticipantAdapter;
import com.example.apparchilog.models.holiday.Vacation;
import com.example.apparchilog.models.holiday.Activity;
import com.example.apparchilog.models.holiday.Participant;
import com.example.apparchilog.utils.DateConvertor;
import com.example.apparchilog.utils.Utils;
import com.example.apparchilog.viewModels.ActivityViewModel;
import com.example.apparchilog.viewModels.DetailsVacationViewModel;
import com.example.apparchilog.viewModels.ParticipantViewModel;
import com.example.apparchilog.views.HomeActivity;
import com.example.apparchilog.views.NavigationActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DetailsVacationFragment extends Fragment implements ParticipantAdapter.OnDeleteClickListener, ActivityAdapter.OnItemClickListener {
    private static final String ARG_VACATION_ID = "vacation_id";
    private static final String ARG_ACTIVITY_ID = "activity_id";
    private DetailsVacationViewModel mDViewModel;
    private ActivityViewModel mAViewModel;
    private ParticipantViewModel mParticipantViewModel;
    private ParticipantAdapter participantAdapter;
    private ActivityAdapter activityAdapter;
    private FloatingActionButton floatingActionButton;
    private BottomNavigationView bottomNavigationView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    private Long activityId;
    private Long vacationId;
    private TextView tvVacationName;
    private TextView tvVacationDescription;
    private TextView tvVacationPeriod;
    private TextView tvVacationStatus;
    private TextView tvVacationPlace;
    private Button btnAddParticipant;
    private int selectedItemId = -1;

    public static DetailsVacationFragment newInstance(Long vacationId, Long activityId) {
        DetailsVacationFragment fragment = new DetailsVacationFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_VACATION_ID, vacationId);
        args.putLong(ARG_ACTIVITY_ID, activityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDViewModel = new ViewModelProvider(this).get(DetailsVacationViewModel.class);
        mAViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        mParticipantViewModel = new ViewModelProvider(this).get(ParticipantViewModel.class);
        if (getArguments() != null) {
            vacationId = getArguments().getLong(ARG_VACATION_ID);
            activityId = getArguments().getLong(ARG_ACTIVITY_ID);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.vacation_detail_menu, menu);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_vacation, container, false);
        setHasOptionsMenu(true);
        initIdView(view);
        setupRecyclerViews(view);
        setupActionButtonOnClickListener();
        setupBottomNavigationView();
        loadVacationById();

        observeViewModel();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadVacationById() {
        mDViewModel.loadVacation(vacationId);
    }

    private void initIdView(View view) {
        // Initialize the TextViews
        tvVacationName = view.findViewById(R.id.tvVacationName);
        tvVacationDescription = view.findViewById(R.id.tvVacationDescription);
        tvVacationPeriod = view.findViewById(R.id.tvVacationPeriod);
        tvVacationStatus = view.findViewById(R.id.tvVacationStatus);
        tvVacationPlace = view.findViewById(R.id.tvVacationPlace);
        btnAddParticipant = view.findViewById(R.id.btnAddParticipant);
        floatingActionButton = view.findViewById(R.id.floatingActionButton2);
        bottomNavigationView = view.findViewById(R.id.bottomVacationNavigationView);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
    }

    private void setupRecyclerViews(View view) {
        participantAdapter = new ParticipantAdapter(getContext(), new ArrayList<>());
        participantAdapter.setOnDeleteClickListener(this);
        RecyclerView recyclerViewParticipants = view.findViewById(R.id.recyclerViewParticipants);
        recyclerViewParticipants.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewParticipants.setAdapter(participantAdapter);

        activityAdapter = new ActivityAdapter(getContext(), new ArrayList<>());
        activityAdapter.setOnItemClickListener(this);
        RecyclerView recyclerViewActivities = view.findViewById(R.id.recyclerViewActivities);
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewActivities.setAdapter(activityAdapter);
    }

    private void setupActionButtonOnClickListener() {
        floatingActionButton.setOnClickListener(v -> {
            //Go to create Activity
            CreateHolidayFragment createHolidayFragment = CreateHolidayFragment.newInstance(vacationId, 0L, false);
            ((HomeActivity) requireActivity()).replaceFragment(createHolidayFragment, true, "Créer activité");

        });
        btnAddParticipant.setOnClickListener(v -> {
            // Go to create participant
            //  goToCreateParticipant();

        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadVacationById();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void goToCreateParticipant() {
        ParticipantFragment participantFragment = ParticipantFragment.newInstance(vacationId, 0L, true);
        ((HomeActivity) requireActivity()).replaceFragment(participantFragment, true, "Créer participant");
    }


    private void observeViewModel() {
        mDViewModel.getParticipants().observe(getViewLifecycleOwner(), new Observer<List<Participant>>() {

            @Override
            public void onChanged(List<Participant> participants) {
                participantAdapter.updateParticipant(participants);
            }
        });

        mDViewModel.getActivities().observe(getViewLifecycleOwner(), new Observer<List<Activity>>() {
            @Override
            public void onChanged(List<Activity> activities) {
                activityAdapter.updateActivities(activities);

            }
        });
        mDViewModel.getVacationLiveData().observe(getViewLifecycleOwner(), new Observer<Vacation>() {
            @Override
            public void onChanged(Vacation vacation) {
                tvVacationName.setText(vacation.getName());
                tvVacationDescription.setText(vacation.getDescription());
                String startD = DateConvertor.convertOffsetDateTimeToString(vacation.getStartDate());
                String endD = DateConvertor.convertOffsetDateTimeToString(vacation.getEndDate());
                tvVacationPeriod.setText(String.format("%s - %s", startD, endD));
                tvVacationStatus.setText(vacation.getStatus());
                Utils.changeColor(tvVacationStatus, getContext());
                tvVacationPlace.setText(String.format("%s - %s", vacation.getPlaceRequest().getPays(), vacation.getPlaceRequest().getVille()));
            }
        });
        mDViewModel.getEventLiveData().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> event) {
                if (!event.isEmpty()) {
                    showConfirmationDialog(event);
                }
            }
        });
        mDViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
        mParticipantViewModel.getParticipantLiveData().observe(getViewLifecycleOwner(), new Observer<Participant>() {
            @Override
            public void onChanged(Participant participant) {
                participantAdapter.addParticipant(participant);
                Toast.makeText(requireContext(), "Participant ajouté avec success", Toast.LENGTH_SHORT).show();
            }
        });
       /* mParticipantViewModel.getMessageLiveDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {

            }
        });*/
        mAViewModel.getActivityLiveData().observe(getViewLifecycleOwner(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                activityAdapter.addActivity(activity);
            }
        });
        mDViewModel.getMessageLiveDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(requireContext(), s, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDeleteClick(Long participantId) {
        showConfirmationDialog(participantId);
    }

    @Override
    public void onItemClick(Long id_activity) {
        DetailsActivityFragment detailsActivityFragment =
                DetailsActivityFragment.newInstance(vacationId, id_activity);
        ((HomeActivity) requireActivity()).replaceFragment(detailsActivityFragment, true, "Détails activités");

    }

    @Override
    public void onResume() {
        super.onResume();
        assert getActivity() != null;
        ((HomeActivity) getActivity()).showBackButton(true);
        ((HomeActivity) requireActivity()).setToolbarTitle("Détails vacances");
    }

    private void showConfirmationDialog(Long participantId) {
        ConfirmationDialogFragment confirmationDialog = ConfirmationDialogFragment.newInstance(participantId, vacationId, 0L, 0L);
        confirmationDialog.setOnConfirmationDialogListener(new ConfirmationDialogFragment.ConfirmationDialogListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog, Long participantId, Long vacationId, Long activityId) {
                mParticipantViewModel.removeParticipant(vacationId, participantId, true);
                mParticipantViewModel.getMessageLiveDate().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s.contains("success")) {
                            participantAdapter.removeParticipant(participantId);
                            Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
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

    private void showConfirmationDialog(List<Event> event) {
        ConfirmationDialogFragment confirmationDialog = ConfirmationDialogFragment.newInstance(0L, vacationId, 0L, (long) event.size());
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                goToCreateParticipant();
                return true;
            case R.id.action_add_calendar:
                addEventToCalendar(vacationId);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addEventToCalendar(Long vacationId) {
        mDViewModel.addEventToCalendar(vacationId);
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
                    if (selectedItemId != R.id.weather) {

                        WeatherFragment weatherFragment = WeatherFragment.newInstance(vacationId, activityId, true);
                        ((HomeActivity) requireActivity()).replaceFragment(weatherFragment, true, "Météo");
                        selectedItemId = R.id.weather;
                        isItemSelected = true;
                    }
                    break;
                case R.id.navigation:
                    if (selectedItemId != R.id.navigation) {
                        Intent intent = new Intent(getContext(), NavigationActivity.class);
                        intent.putExtra("vacation_id", vacationId);
                        intent.putExtra("activity_id", activityId);
                        intent.putExtra("is_vacation", true);
                        startActivity(intent);
                        //bottomNavigationView.setSelectedItemId(R.id.navigation);
                        selectedItemId = R.id.navigation;
                        isItemSelected = true;
                    }
                    break;
                case R.id.files:
                    if (selectedItemId != R.id.files) {
                        DocumentFragment documentFragment = DocumentFragment.newInstance(vacationId, activityId, true);
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
}
