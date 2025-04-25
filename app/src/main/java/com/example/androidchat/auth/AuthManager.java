package com.example.androidchat.auth;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidchat.database.AppDatabase;
import com.example.androidchat.database.UserDao;
import com.example.androidchat.models.User;

import java.util.List;

public class AuthManager {
    private static AuthManager instance;
    private final UserDao userDao;
    private final MutableLiveData<String> currentUserId = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginStatus = new MutableLiveData<>(false);

    private AuthManager(UserDao userDao) {
        this.userDao = userDao;
    }

    public static synchronized AuthManager getInstance(Context context) {
        if (instance == null) {
            AppDatabase db = AppDatabase.getInstance(context);
            instance = new AuthManager(db.userDao());
        }
        return instance;
    }

    public LiveData<Boolean> login(String email, String password) {
        new Thread(() -> {
            User user = userDao.getUserByEmailSync(email);
            if (user != null && user.getPassword().equals(password)) {
                currentUserId.postValue(user.getId());
                loginStatus.postValue(true);
            } else {
                loginStatus.postValue(false);
            }
        }).start();
        return loginStatus;
    }

    public void register(User user) {
        new Thread(() -> {
            userDao.insert(user);
            currentUserId.postValue(user.getId());
        }).start();
    }

    public boolean isLoggedIn() {
        return currentUserId.getValue() != null;
    }

    public String getCurrentUserId() {
        return currentUserId.getValue();
    }

    public LiveData<String> getCurrentUserIdLiveData() {
        return currentUserId;
    }

    public LiveData<User> getCurrentUser() {
        return userDao.getUserById(currentUserId.getValue());
    }

    public LiveData<List<User>> getAllUsersExceptCurrent() {
        return userDao.getAllUsersExcept(currentUserId.getValue());
    }
} 