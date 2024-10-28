package com.example.apparchilog.fragments;

import android.os.Bundle;

import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.apparchilog.R;
import com.example.apparchilog.viewModels.ParticipantViewModel;
import com.example.apparchilog.views.HomeActivity;
import com.google.android.material.textfield.TextInputEditText;


public class ParticipantFragment extends Fragment {

    private static final String ARG_PARAM_VACATION_CREATION = "param2";
    private static final String ARG_ACTIVITY_ID = "activity_id";
    private static final String ARG_VACATION_ID = "vacation_id";
    private ParticipantViewModel mPViewModel;
    private TextInputEditText etEmail;
    private TextInputEditText etFirstname;
    private TextInputEditText etLastname;
    private Button btnCreateParticipant;
    private ProgressBar progressBar;

    private Long vacationId;
    private Long activityId;
    private boolean isVacationCreation;

    public static ParticipantFragment newInstance(Long vacationId, Long activityId, boolean vacationCreation) {
        ParticipantFragment fragment = new ParticipantFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_VACATION_ID, vacationId);
        args.putLong(ARG_ACTIVITY_ID, activityId);
        args.putBoolean(ARG_PARAM_VACATION_CREATION, vacationCreation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPViewModel = new ViewModelProvider(this).get(ParticipantViewModel.class);
        if (getArguments() != null) {
            vacationId = getArguments().getLong(ARG_VACATION_ID);
            activityId = getArguments().getLong(ARG_ACTIVITY_ID);
            isVacationCreation = getArguments().getBoolean(ARG_PARAM_VACATION_CREATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_participant, container, false);

        initIdView(view);
        setupActionButtonOnClickListener();
        observeViewModel();

        return view;
    }

    private void observeViewModel() {
        mPViewModel.getParticipantLiveData().observe(getViewLifecycleOwner(), participant -> {
            if (participant != null && participant.getId() != null) {
                backToParentFragment();
            }
            Toast.makeText(requireContext(), "Participant added successfully", Toast.LENGTH_SHORT).show();
        });

        mPViewModel.getMessageLiveDate().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                if (message.contains("Nom")) {
                    etLastname.setError(message);
                    etFirstname.requestFocus();
                } else if (message.contains("Prenom")) {
                    etFirstname.setError(message);
                    etLastname.requestFocus();
                } else if (message.contains("Email")) {
                    etEmail.setError(message);
                    etFirstname.requestFocus();
                }

                Toast.makeText(requireContext(), "Error: " + message, Toast.LENGTH_LONG).show();

            }
        });
        mPViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (!isLoading) {
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void backToParentFragment() {
        if (isVacationCreation) {
            DetailsVacationFragment detailsVacationFragment = DetailsVacationFragment.newInstance(vacationId, activityId);
            ((HomeActivity) requireActivity()).replaceFragment(detailsVacationFragment, true, "Détails vacances");
        } else {
            DetailsActivityFragment detailsVacationFragment = DetailsActivityFragment.newInstance(vacationId, activityId);
            ((HomeActivity) requireActivity()).replaceFragment(detailsVacationFragment, true, "Détails Activités");
        }

    }

    private void setupActionButtonOnClickListener() {
        btnCreateParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(etEmail.getText());
                String firstname = String.valueOf(etFirstname.getText());
                String lastname = String.valueOf(etLastname.getText());
                if (isVacationCreation) {
                    mPViewModel.createParticipant(true, vacationId, firstname, lastname, email);
                } else {
                    mPViewModel.createParticipant(false, activityId, firstname, lastname, email);

                }
            }
        });
    }

    private void initIdView(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        etEmail = view.findViewById(R.id.etEmail);
        etLastname = view.findViewById(R.id.etLastname);
        etFirstname = view.findViewById(R.id.etFirstname);
        TextView tvInfo = view.findViewById(R.id.tvInfo);
        btnCreateParticipant = view.findViewById(R.id.btnCreateParticipant);
        if (isVacationCreation) {
            tvInfo.setText("Ajouter une personne pour vos vacances");
        } else {
            tvInfo.setText("Ajouter une personnne pour vos activités");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        assert getActivity() != null;
        ((HomeActivity) getActivity()).showBackButton(true);
        ((HomeActivity) requireActivity()).setToolbarTitle("Ajouter  participant");
    }


}