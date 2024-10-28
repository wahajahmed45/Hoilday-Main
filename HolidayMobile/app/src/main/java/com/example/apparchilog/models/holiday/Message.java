package com.example.apparchilog.models.holiday;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {
    private String text;
    private String sender;
    private String receiver;
    private long timestamp;
    private boolean issender;

    public Message() {
        this.text = "";
        this.sender = "";
        this.receiver = "";
        this.timestamp = 0;
    }

    public Message(String text, String sender, String receiver, long timestamp) {
        this.text = text;
        this.sender = sender;
        this.timestamp = timestamp;
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isIssender() {
        // Replace with actual sender check logic
        return issender;
    }

    public String getFormattedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public void setIssender(boolean b) {
        issender = b;
    }
}



