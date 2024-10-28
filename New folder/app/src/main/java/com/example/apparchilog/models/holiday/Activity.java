package com.example.apparchilog.models.holiday;


import com.example.apparchilog.utils.OffsetDateTimeSerializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

public class Activity implements Serializable {
    @SerializedName("id")
    private Long id;
    @SerializedName("dateDebut")
    @JsonAdapter(OffsetDateTimeSerializer.class)
    private OffsetDateTime dateDebut;
    @SerializedName("dateFin")
    @JsonAdapter(OffsetDateTimeSerializer.class)
    private OffsetDateTime dateFin;
    @SerializedName("description")
    private String description;
    @SerializedName("placeDTO")
    private Place placeRequest;
    @SerializedName("nom")
    private String nom;
    @SerializedName("owner")
    private User owner;
    @SerializedName("status")
    private String status;
    @SerializedName("participants")
    private List<Participant> participants;
    @SerializedName("vacationDTO")
    private Vacation vacationDTO;

    public Long getId() {
        return id;
    }

    public OffsetDateTime getDateDebut() {
        return dateDebut;
    }

    public OffsetDateTime getDateFin() {
        return dateFin;
    }

    public String getDescription() {
        return description;
    }

    public Place getPlaceRequest() {
        return placeRequest;
    }

    public String getNom() {
        return nom;
    }

    public User getOwner() {
        return owner;
    }

    public String getStatus() {
        return status;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public Vacation getVacationDTO() {
        return vacationDTO;
    }
    public Long getVacationId(){
        return vacationDTO.getId();
    }
}
