package com.example.apparchilog.viewModels;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apparchilog.models.holiday.Message;
import com.example.apparchilog.models.holiday.Participant;
import com.example.apparchilog.models.requests.ParticipantFirebase;
import com.example.apparchilog.repositories.VacationRepository;
import com.example.apparchilog.repositories.inter_responses.IParticipant;
import com.example.apparchilog.services.instance.CurrentInstanceUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.EventListener;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static android.content.ContentValues.TAG;

public class ChatViewModel extends ViewModel {
    //  private final MutableLiveData<Participant> participantLiveData;
    private  MutableLiveData<List<Message>> listMessagesLiveData;
    private final MutableLiveData<List<Participant>> listParticipantLiveData;
    private final MutableLiveData<String> messageLiveData;
    private final MutableLiveData<Boolean> isLoading;
    private  MutableLiveData<Message> liveDataMessages;
    private final VacationRepository mVacationRepository;
    private final FirebaseFirestore db;
    private final List<Message> messageList;

    private ChatViewModel() {
        //  participantLiveData = new MutableLiveData<>();
        messageLiveData = new MutableLiveData<>();
        listParticipantLiveData = new MutableLiveData<>();
        mVacationRepository = new VacationRepository();
        listMessagesLiveData = new MutableLiveData<>();
        liveDataMessages = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        messageList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
    }

    public void loadAllParticipants() {
        isLoading.setValue(true);
        mVacationRepository.findAllParticipants(new IParticipant() {
            @Override
            public void onResponse(List<Participant> participants) {
                isLoading.setValue(false);
                listParticipantLiveData.setValue(participants);
                Log.d("Success Participant Response", String.valueOf(participants.size()));
            }

            @Override
            public void onResponse(Participant participant) {
            }

            @Override
            public void onFailure(Throwable t) {
                isLoading.setValue(true);
                Log.d("Failure To load Participant", Objects.requireNonNull(t.getLocalizedMessage()));
                messageLiveData.setValue("Failed to load Participant: " + t.getMessage());
            }
        });
    }


    public void loadAllMessage(String emailUserReceiver) {
        String emailSender = CurrentInstanceUser.getInstance().getLoginResponse().getEmail();

        // Effacer l'ancienne liste de messages pour éviter les doublons
        messageList.clear();

        // Charger les messages envoyés par emailSender à emailUserReceiver
        db.collection("messages")
                .whereEqualTo("sender", emailSender)
                .whereEqualTo("receiver", emailUserReceiver)
                .orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(TAG, "Error getting documents: ", e.fillInStackTrace());
                            return;
                        }
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Message message = document.toObject(Message.class);
                            message.setIssender(true);
                            messageList.add(message);
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                        // Charger les messages envoyés par emailUserReceiver à emailSender
                        loadMessagesFromReceiver(emailSender, emailUserReceiver);
                    }
                });
    }

    private void loadMessagesFromReceiver(String emailSender, String emailUserReceiver) {
        db.collection("messages")
                .whereEqualTo("sender", emailUserReceiver)
                .whereEqualTo("receiver", emailSender)
                .orderBy("timestamp")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Message message = document.toObject(Message.class);
                                message.setIssender(false);
                                messageList.add(message);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            // Trier les messages par timestamp avant de mettre à jour le LiveData
                            messageList.sort(new Comparator<Message>() {
                                @Override
                                public int compare(Message m1, Message m2) {
                                    return Long.compare(m1.getTimestamp(), m2.getTimestamp());
                                }
                            });
                            listMessagesLiveData.setValue(messageList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
  /*  private void loadMessagesFromReceiver(String emailSender, String emailUserReceiver) {
        db.collection("messages")
                .whereEqualTo("sender", emailUserReceiver)
                .whereEqualTo("receiver", emailSender)
                .orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(TAG, "Error getting documents: ", e.fillInStackTrace());
                            return;
                        }
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Message message = document.toObject(Message.class);
                            message.setIssender(false);
                            messageList.add(message);
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                        // Trier les messages par timestamp avant de mettre à jour le LiveData
                        messageList.sort(new Comparator<Message>() {
                            @Override
                            public int compare(Message m1, Message m2) {
                                return Long.compare(m1.getTimestamp(), m2.getTimestamp());
                            }
                        });
                        //    listMessagesLiveData = new MutableLiveData<>();
                        listMessagesLiveData.setValue(messageList);

                    }
                });
    }*/

    public void sendMessage(String messageText, String userEmailReceiver) {
        String emailSender = CurrentInstanceUser.getInstance().getLoginResponse().getEmail();
        Message message = new Message(messageText, userEmailReceiver, emailSender, System.currentTimeMillis());
        db.collection("messages").add(message);
        //liveDataMessages = new MutableLiveData<>();
        liveDataMessages.setValue(message);
    }

    public void updateUserToFirebase(ParticipantFirebase participantFirebase) {
        db.collection("users").document(participantFirebase.getEmail())
                .set(participantFirebase)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "User added successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error adding user", e);
                });
    }

    public LiveData<List<Participant>> getAllParticipantsLiveData() {
        return listParticipantLiveData;
    }

    public LiveData<Message> getLiveDataMessages() {
        return liveDataMessages;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getMessagesLiveData() {
        return messageLiveData;
    }

    public LiveData<List<Message>> getListMessagesLiveData() {
        return listMessagesLiveData;
    }
}