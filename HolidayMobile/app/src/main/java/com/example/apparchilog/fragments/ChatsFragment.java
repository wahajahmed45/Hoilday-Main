package com.example.apparchilog.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.apparchilog.R;
import com.example.apparchilog.adapters.ChatAdapter;
import com.example.apparchilog.models.holiday.Participant;
import com.example.apparchilog.models.requests.ParticipantFirebase;
import com.example.apparchilog.viewModels.ChatViewModel;
import com.example.apparchilog.views.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ChatsFragment extends Fragment implements ChatAdapter.OnUserClickListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewUsers;
    private ChatAdapter chatAdapter;
    private List<ParticipantFirebase> userList;
    private FirebaseFirestore db;
    private ChatViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        initIdView(view);
        setupRecyclerViews(view);
        setupActionButtonOnClickListener();
        loadAllParticipants();
        observeViewModel();

        return view;
    }


    private void loadAllParticipants() {
        mViewModel.loadAllParticipants();
    }

    private void observeViewModel() {
        mViewModel.getAllParticipantsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Participant>>() {
            @Override
            public void onChanged(List<Participant> participants) {
                for (Participant participant : participants) {
                    DocumentReference docRef = db.collection("users").document(participant.getEmail());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    ParticipantFirebase participant = document.toObject(ParticipantFirebase.class);
                                    chatAdapter.addParticipant(participant);
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());

                            }
                        }
                    });
                }
            }
        });

    }

    private void setupRecyclerViews(View view) {
        chatAdapter = new ChatAdapter(getContext(), new ArrayList<>());
        chatAdapter.setOnUserClickListener(this);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewUsers.setAdapter(chatAdapter);
    }

    private void initIdView(View view) {
        recyclerViewUsers = view.findViewById(R.id.recyclerViewUsers);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
    }

    private void setupActionButtonOnClickListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAllParticipants();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onUserClick(ParticipantFirebase userFirebase) {
        MessagesFragment fragment = MessagesFragment.newInstance(userFirebase.getEmail(), userFirebase);
        ((HomeActivity) requireActivity()).replaceFragment(fragment, true, "Messages");
    }

    @Override
    public void onResume() {
        super.onResume();
        assert getActivity() != null;
        //swipeRefreshLayout.setRefreshing(true);
        ((HomeActivity) requireActivity()).setToolbarTitle("Chats");
        ((HomeActivity) getActivity()).showBackButton(false);
    }
}
