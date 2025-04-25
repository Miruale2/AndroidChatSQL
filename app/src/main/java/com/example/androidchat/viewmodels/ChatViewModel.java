package com.example.androidchat.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidchat.database.AppDatabase;
import com.example.androidchat.database.MessageDao;
import com.example.androidchat.database.UserDao;
import com.example.androidchat.models.Message;
import com.example.androidchat.models.User;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends AndroidViewModel {

    private final MessageDao messageDao;
    private final UserDao userDao;
    private final MutableLiveData<String> currentChatId = new MutableLiveData<>();
    private final MutableLiveData<String> currentUserId = new MutableLiveData<>();
    private final MutableLiveData<List<User>> activeChats = new MutableLiveData<>(new ArrayList<>());

    public ChatViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        messageDao = db.messageDao();
        userDao = db.userDao();
    }

    public void setCurrentUser(String userId) {
        currentUserId.setValue(userId);
    }

    public LiveData<String> getCurrentUserId() {
        return currentUserId;
    }

    public LiveData<List<Message>> getMessages(String userId) {
        return messageDao.getMessagesForUser(userId);
    }

    public LiveData<Message> getLastMessage(String chatId) {
        String currentUserIdValue = currentUserId.getValue();
        if (currentUserIdValue != null) {
            return messageDao.getLastMessage(currentUserIdValue, chatId);
        }
        return new MutableLiveData<>(null);
    }

    public LiveData<List<Message>> getChatMessages(String currentUserId, String otherUserId) {
        currentChatId.setValue(otherUserId);
        return messageDao.getMessagesBetweenUsers(currentUserId, otherUserId);
    }

    public void sendMessage(Message message) {
        AppDatabase.databaseWriteExecutor.execute(() -> messageDao.insert(message));
    }

    public LiveData<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }

    public LiveData<List<User>> getChatPartners(String currentUserId) {
        return userDao.getChatUsers(currentUserId);
    }

    public LiveData<User> getUser(String userId) {
        return userDao.getUserById(userId);
    }

    public LiveData<Message> getLastMessageWithUser(String userId) {
        String currentUserIdValue = currentUserId.getValue();
        if (currentUserIdValue != null) {
            return messageDao.getLastMessage(currentUserIdValue, userId);
        }
        return new MutableLiveData<>(null);
    }

    public void markMessagesAsRead(List<Integer> messageIds) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            for (int id : messageIds) {
                messageDao.markAsRead(id);
            }
        });
    }

    public void setCurrentChat(String chatId) {
        currentChatId.setValue(chatId);
    }

    public LiveData<String> getCurrentChatId() {
        return currentChatId;
    }

    public void startChatWithUser(User otherUser, Runnable onComplete) {
        String currentUserIdValue = currentUserId.getValue();
        if (currentUserIdValue == null || otherUser == null || currentUserIdValue.equals(otherUser.getId())) return;

        messageDao.getLastMessage(currentUserIdValue, otherUser.getId()).observeForever(existingMessage -> {
            if (existingMessage == null) {
                Message initialMessage = new Message(currentUserIdValue, otherUser.getId(), "", System.currentTimeMillis(), true);
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    messageDao.insert(initialMessage);

                    addActiveChat(otherUser);
                    if (onComplete != null) onComplete.run();
                });
            } else {
                addActiveChat(otherUser);
                if (onComplete != null) onComplete.run();
            }
        });
    }

    private void addActiveChat(User user) {
        List<User> current = activeChats.getValue();
        if (current != null && current.stream().noneMatch(u -> u.getId().equals(user.getId()))) {
            current.add(user);
            activeChats.postValue(new ArrayList<>(current));
        }
    }

    public LiveData<List<User>> getActiveChats() {
        return activeChats;
    }

    public List<User> getAllUsersSync() {
        return userDao.getAllUsersSync();
    }

    public LiveData<List<User>> getAllUsersExceptCurrent() {
        String currentUserIdValue = currentUserId.getValue();
        if (currentUserIdValue != null) {
            return userDao.getAllUsersExcept(currentUserIdValue);
        }
        return new MutableLiveData<>();
    }
}