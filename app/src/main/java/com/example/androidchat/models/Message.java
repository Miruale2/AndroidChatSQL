package com.example.androidchat.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

@Entity(tableName = "messages")
public class Message {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "sender_id")
    @NonNull
    private String senderId;

    @ColumnInfo(name = "receiver_id")
    @NonNull
    private String receiverId;

    private String content;
    private long timestamp;

    @ColumnInfo(name = "is_read")
    private boolean isRead;

    public Message() {}

    public Message(@NonNull String senderId, @NonNull String receiverId,
                   String content, long timestamp, boolean isRead) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    @NonNull
    public String getSenderId() { return senderId; }

    public void setSenderId(@NonNull String senderId) { this.senderId = senderId; }

    @NonNull
    public String getReceiverId() { return receiverId; }

    public void setReceiverId(@NonNull String receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public long getTimestamp() { return timestamp; }

    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return isRead; }

    public void setRead(boolean read) { isRead = read; }

    public String getText() {
        return getContent();
    }
}