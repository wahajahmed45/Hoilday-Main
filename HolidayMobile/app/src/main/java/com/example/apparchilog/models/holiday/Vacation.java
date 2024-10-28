package com.example.apparchilog.models.holiday;

import com.example.apparchilog.utils.OffsetDateTimeSerializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class Vacation implements Serializable {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;
    @SerializedName("startDate")
    @JsonAdapter(OffsetDateTimeSerializer.class)
    private OffsetDateTime startDate;

    @SerializedName("endDate")
    @JsonAdapter(OffsetDateTimeSerializer.class)
    private OffsetDateTime endDate;

    @SerializedName("placeDTO")
    private Place placeRequest;
    @SerializedName("status")
    private String status;
    @SerializedName("owner")
    private User owner;

    @SerializedName("participants")
    private List<Participant> participants;

    @SerializedName("activities")
    private List<Activity> activities = new ArrayList<>();
    @SerializedName("documents")
    private List<Document> documents = new ArrayList<>();

    @SerializedName("messages")
    private List<Message> messages;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public OffsetDateTime getEndDate() {
        return endDate;
    }

    public Place getPlaceRequest() {
        return placeRequest;
    }

    public String getStatus() {
        return status;
    }

    public User getOwner() {
        return owner;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public List<Message> getMessages() {
        return messages;
    }
// Getters and Setters (if needed)
}
