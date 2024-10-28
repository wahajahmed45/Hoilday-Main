package com.example.apparchilog.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.apparchilog.R;

public class ConfirmationDialogFragment extends DialogFragment {
    private static final String ARG_ACTIVITY_ID = "activity_id";
    private static final String ARG_VACATION_ID = "vacation_id";
    private static final String ARG_PARTICIPANT_ID = "participant_id";
    private static final String ARG_EVENT_ID = "Event_id";
    private ConfirmationDialogListener listener;
    private Long participantId;
    private Long activityId;
    private Long vacationId;
    private Long eventId;

    public static ConfirmationDialogFragment newInstance(Long participantId, Long vacationId, Long activityId, Long evenId) {
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARTICIPANT_ID, participantId);
        args.putLong(ARG_VACATION_ID, vacationId);
        args.putLong(ARG_ACTIVITY_ID, activityId);
        args.putLong(ARG_EVENT_ID, evenId);
        fragment.setArguments(args);
        return fragment;
    }

    public interface ConfirmationDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, Long participantId, Long vacationId, Long activityId);

        void onDialogNegativeClick(DialogFragment dialog);
    }

    public void setOnConfirmationDialogListener(ConfirmationDialogListener listener) {
        this.listener = listener;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            participantId = getArguments().getLong(ARG_PARTICIPANT_ID);
            vacationId = getArguments().getLong(ARG_VACATION_ID);
            activityId = getArguments().getLong(ARG_ACTIVITY_ID);
            eventId = getArguments().getLong(ARG_EVENT_ID);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_popup_fragment, null);

        builder.setView(view);

        TextView btnCancel = view.findViewById(R.id.btnCancel);
        TextView btnDelete = view.findViewById(R.id.btnDelete);
        TextView tvConfirmationMessage = view.findViewById((R.id.tvConfirmationMessage));
        if (participantId != 0L) {
            tvConfirmationMessage.setText("Etes vous sur de vouloir supprimer ce participant ?");
        }
        if(activityId != 0L){
            tvConfirmationMessage.setText("Etes vous sur de vouloir supprimer cette activité ?");
        }
        if(eventId != 0L){
            tvConfirmationMessage.setText("Vos activités ont été ajouter a votre agenda !");
            btnDelete.setText("Votre agenda");
            btnDelete.setBackgroundColor(Color.LTGRAY);
            btnCancel.setText("Ok");
        }
        btnCancel.setOnClickListener(v -> listener.onDialogNegativeClick(ConfirmationDialogFragment.this));
        btnDelete.setOnClickListener(v -> listener.onDialogPositiveClick(ConfirmationDialogFragment.this, participantId, vacationId, activityId));

        return builder.create();
    }
}
