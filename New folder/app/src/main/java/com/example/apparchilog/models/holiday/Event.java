package com.example.apparchilog.models.holiday;

import com.example.apparchilog.utils.OffsetDateTimeSerializer;
import com.google.auto.value.AutoValue;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class Event implements Serializable {
    @SerializedName("id")
    private Long id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @JsonAdapter(OffsetDateTimeSerializer.class)
    @SerializedName("startDate")
    private OffsetDateTime startDate;
    @SerializedName("endDate")
    @JsonAdapter(OffsetDateTimeSerializer.class)
    private OffsetDateTime endDate;
    @SerializedName("scheduleDTO")
    private Schedule scheduleDTO;
    @SerializedName("activityDTO")
    private Activity activityDTO;

    public static android.R.id builder() {
        return new android.R.id();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
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

  /*  public Schedule getScheduleDTO() {
        return scheduleDTO;
    }
*/
   /* public Activity getActivityDTO() {
        return activityDTO;
    }*/
}
