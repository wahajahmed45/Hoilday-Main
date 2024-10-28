package com.example.apparchilog.models.requests;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class ParticipantFirebase implements Parcelable, Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String lastname;
    private String email;
    private String avatarUrl;
    private String lastMessage;
    private String lastMessageTime;

    public ParticipantFirebase() {
    }

    public ParticipantFirebase(String userId, String email, String name, String avatarUrl, String lastMessage, String lastMessageTime) {
        this.userId = userId;
        this.lastname = name;
        this.avatarUrl = avatarUrl;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.email = email;
    }

    protected ParticipantFirebase(Parcel in) {
        userId = in.readString();
        lastname = in.readString();
        email = in.readString();
        avatarUrl = in.readString();
        lastMessage = in.readString();
        lastMessageTime = in.readString();
    }

    public static final Creator<ParticipantFirebase> CREATOR = new Creator<ParticipantFirebase>() {
        @Override
        public ParticipantFirebase createFromParcel(Parcel in) {
            return new ParticipantFirebase(in);
        }

        @Override
        public ParticipantFirebase[] newArray(int size) {
            return new ParticipantFirebase[size];
        }
    };

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getUserId() {
        return userId;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(lastname);
        dest.writeString(email);
        dest.writeString(avatarUrl);
        dest.writeString(lastMessage);
        dest.writeString(lastMessageTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantFirebase that = (ParticipantFirebase) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getEmail());
    }
}
