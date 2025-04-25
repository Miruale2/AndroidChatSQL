package com.example.androidchat.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidchat.auth.AuthManager;
import com.example.androidchat.database.AppDatabase;
import com.example.androidchat.database.UserDao;
import com.example.androidchat.models.User;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final UserDao userDao;
    private final AuthManager authManager;
    private final MutableLiveData<List<User>> chatUsers = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
        authManager = AuthManager.getInstance(application);
        authManager.getCurrentUserIdLiveData().observeForever(currentUserId -> {
            if (currentUserId != null) {
                loadChatUsers(currentUserId);
            }
        });
    }

    private void loadChatUsers(String currentUserId) {
        userDao.getChatUsers(currentUserId).observeForever(chatUsers::setValue);
    }

    public LiveData<List<User>> getChatUsers() {
        return chatUsers;
    }
    public LiveData<Boolean> isUserLoggedIn() {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>(authManager.isLoggedIn());
        return liveData;
    }
    public LiveData<List<User>> getUsers() {
        String currentUserId = authManager.getCurrentUserId();
        return userDao.getAllUsersExcept(currentUserId);
    }
}
