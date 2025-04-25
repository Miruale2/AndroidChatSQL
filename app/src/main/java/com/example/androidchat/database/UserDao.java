package com.example.androidchat.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.androidchat.models.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE id = :userId")
    LiveData<User> getUserById(String userId);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmailSync(String email);

    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();

    @Query("DELETE FROM users")
    void deleteAll();

    @Query("SELECT * FROM users WHERE id != :currentUserId")
    LiveData<List<User>> getAllUsersExcept(String currentUserId);

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE id = :userId LIMIT 1)")
    LiveData<Boolean> isUserLoggedIn(String userId);

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE id = :userId LIMIT 1)")
    boolean isLoggedIn(String userId);

    @Query("SELECT DISTINCT u.* FROM users u " +
            "JOIN messages m ON (u.id = m.sender_id OR u.id = m.receiver_id) " +
            "WHERE (m.sender_id = :currentUserId OR m.receiver_id = :currentUserId) " +
            "AND u.id != :currentUserId " +
            "AND m.content != ''")
    LiveData<List<User>> getChatUsers(String currentUserId);

    @Query("SELECT * FROM users")
    List<User> getAllUsersSync();
}