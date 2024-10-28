package com.example.apparchilog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.example.apparchilog.R;
import com.example.apparchilog.adapters.MessageAdapter;
import com.example.apparchilog.models.holiday.Message;
import com.example.apparchilog.models.requests.ParticipantFirebase;
import com.example.apparchilog.viewModels.ChatViewModel;
import com.example.apparchilog.views.HomeActivity;
import com.google.android.material.textfield.TextInputEditText;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {
    private static final String ARG_USER_ID = "user_email";
    private static final String ARG_USER_CURRENT = "current_user";
    private RecyclerView recyclerViewMessages;
    private MessageAdapter messageAdapter;
    private String emailUserReceiver;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ChatViewModel mViewModel;
    private TextInputEditText editTextMessage;
    private ImageButton buttonSend;
    private ParticipantFirebase participantFirebase;
    private CircleImageView imageViewAvatar;
    private ImageButton imageButtonBack;
    private TextView tvUsername;


    public static MessagesFragment newInstance(String userEmail, ParticipantFirebase participantFirebase) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER_CURRENT, participantFirebase);
        args.putString(ARG_USER_ID, userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        if (getArguments() != null) {
            emailUserReceiver = getArguments().getString(ARG_USER_ID);
            participantFirebase = getArguments().getParcelable(ARG_USER_CURRENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        // Hide the toolbar
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).hideToolbar();
        }

        initIdView(view);
        setupRecyclerViews();
//        swipeRefreshLayout.setRefreshing(true);
        fetchMessages();
        setupOnClickListener();
        observeViewModel();
        setProfile();
        return view;
    }

    private void setProfile() {
        tvUsername.setText(participantFirebase.getLastname());
        if (participantFirebase.getAvatarUrl() != null && !participantFirebase.getAvatarUrl().isEmpty())
            Glide.with(requireContext())
                    .load(participantFirebase.getAvatarUrl())
                    .placeholder(R.drawable.baseline_account_circle_24)
                    .into(imageViewAvatar);
        else {
            // Generate an image with the first letter of the first name
            String firstLetter = String.valueOf(participantFirebase.getLastname().charAt(0));
            ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
            int color = colorGenerator.getColor(participantFirebase.getLastname());

            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstLetter, color); // radius in px

            imageViewAvatar.setImageDrawable(drawable);
        }
    }

    private void observeViewModel() {
        mViewModel.getListMessagesLiveData().observe(getViewLifecycleOwner(), new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messageAdapter.addMessages(messages);
                int itemCount = messageAdapter.getItemCount();
                if (itemCount > 0) {
                    recyclerViewMessages.smoothScrollToPosition(itemCount - 1);
                }
            }
        });
        mViewModel.getLiveDataMessages().observe(getViewLifecycleOwner(), new Observer<Message>() {
            @Override
            public void onChanged(Message message) {
                editTextMessage.setText("");
                messageAdapter.addMessage(message);
                recyclerViewMessages.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
            }
        });
    }

    private void setupOnClickListener() {
        buttonSend.setOnClickListener(v -> {
            String messageText = editTextMessage.getText().toString().trim();
            if (!messageText.isEmpty()) {
                mViewModel.sendMessage(messageText, emailUserReceiver);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // fetchMessages();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToChatFragment();
            }
        });
    }

    private void setupRecyclerViews() {
        messageAdapter = new MessageAdapter(getContext(), new ArrayList<>());
        messageAdapter.setReceiverId(emailUserReceiver);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewMessages.setLayoutManager(layoutManager);
        recyclerViewMessages.setAdapter(messageAdapter);
    }

    private void initIdView(View view) {
        recyclerViewMessages = view.findViewById(R.id.recyclerViewMessages);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        editTextMessage = view.findViewById(R.id.editTextMessage);
        buttonSend = view.findViewById(R.id.buttonSend);
        tvUsername = view.findViewById(R.id.tvUsername);
        imageButtonBack = view.findViewById(R.id.back_btn);
        imageViewAvatar = view.findViewById(R.id.imageViewAvatar);

    }

    private void fetchMessages() {
        mViewModel.loadAllMessage(emailUserReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        assert getActivity() != null;
        ((HomeActivity) getActivity()).showBackButton(true);
        ((HomeActivity) requireActivity()).setToolbarTitle("Messages");
    }

    private void navigateToChatFragment() {
        Message message = messageAdapter.getLastMessage();
        if (message != null) {
            participantFirebase.setLastMessage(!message.getText().isEmpty() && message.getText().length() > 30 ? message.getText().substring(0, 30) : message.getText());
            participantFirebase.setLastMessageTime(message.getFormattedTime());
            mViewModel.updateUserToFirebase(participantFirebase);
        }
        requireActivity().onBackPressed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).showToolbar();
        }
    }
}


