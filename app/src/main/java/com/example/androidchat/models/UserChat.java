package com.example.androidchat.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_chats")
public class UserChat {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String currentUserId;
    private String chatPartnerId;

    public UserChat(String currentUserId, String chatPartnerId) {
        this.currentUserId = currentUserId;
        this.chatPartnerId = chatPartnerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getChatPartnerId() {
        return chatPartnerId;
    }

    public void setChatPartnerId(String chatPartnerId) {
        this.chatPartnerId = chatPartnerId;
    }
}
