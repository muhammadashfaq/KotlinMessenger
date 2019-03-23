package com.example.muhammadashfaq.snippet.Model;

public class ConversationModel {
    public boolean seen;
    public  long timestamp;

    public ConversationModel(boolean seen, long timestamp) {
        this.seen = seen;
        this.timestamp = timestamp;
    }

    public ConversationModel() {
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
