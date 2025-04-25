package com.example.androidchat.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidchat.models.Message;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    void insert(Message message);

    @Update
    void update(Message message);

    @Query("SELECT * FROM messages WHERE (sender_id = :user1 AND receiver_id = :user2) " +
            "OR (sender_id = :user2 AND receiver_id = :user1) " +
            "ORDER BY timestamp ASC")
    LiveData<List<Message>> getMessagesBetweenUsers(String user1, String user2);

    @Query("SELECT * FROM messages WHERE (sender_id = :user1 AND receiver_id = :user2) " +
            "OR (sender_id = :user2 AND receiver_id = :user1) " +
            "ORDER BY timestamp ASC")
    List<Message> getMessagesBetweenUsersSync(String user1, String user2);

    @Query("SELECT * FROM messages WHERE receiver_id = :userId OR sender_id = :userId " +
            "ORDER BY timestamp ASC")
    LiveData<List<Message>> getMessagesForUser(String userId);

    @Query("SELECT * FROM messages WHERE receiver_id = :userId OR sender_id = :userId " +
            "ORDER BY timestamp ASC")
    List<Message> getMessagesForUserSync(String userId);

    @Query("UPDATE messages SET is_read = 1 WHERE id = :messageId")
    void markAsRead(int messageId);

    @Query("SELECT * FROM messages WHERE (sender_id = :userId OR receiver_id = :userId) " +
            "ORDER BY timestamp DESC LIMIT 1")
    LiveData<Message> getLastMessageForUser(String userId);

    @Query("SELECT * FROM messages WHERE (sender_id = :userId OR receiver_id = :userId) " +
            "ORDER BY timestamp DESC LIMIT 1")
    Message getLastMessageForUserSync(String userId);

    @Query("SELECT * FROM messages WHERE (sender_id = :user1 AND receiver_id = :user2) OR " +
            "(sender_id = :user2 AND receiver_id = :user1) ORDER BY timestamp DESC LIMIT 1")
    LiveData<Message> getLastMessage(String user1, String user2);

    @Query("SELECT * FROM messages WHERE (sender_id = :user1 AND receiver_id = :user2) OR " +
            "(sender_id = :user2 AND receiver_id = :user1) ORDER BY timestamp DESC LIMIT 1")
    Message getLastMessageSync(String user1, String user2);
}