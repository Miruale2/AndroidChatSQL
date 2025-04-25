package com.example.androidchat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchat.R;
import com.example.androidchat.models.Message;
import com.example.androidchat.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder> {

    private List<User> users;
    private Map<String, Message> lastMessages = new HashMap<>();
    private final OnChatClickListener listener;

    public interface OnChatClickListener {
        void onChatClick(String userId);
    }

    public ChatsAdapter(List<User> users, Map<String, Message> lastMessages, OnChatClickListener listener) {
        this.users = users;
        this.lastMessages = lastMessages;
        this.listener = listener;
    }

    public void updateData(List<User> newUsers, Map<String, Message> newLastMessages) {
        this.users = newUsers;
        this.lastMessages = newLastMessages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        User user = users.get(position);
        holder.userName.setText(user.getUsername());

        Message lastMessage = lastMessages.get(user.getId());
        if (lastMessage != null && lastMessage.getText() != null && !lastMessage.getText().isEmpty()) {
            holder.lastMessage.setText(lastMessage.getText());
        } else {
            holder.lastMessage.setText("No messages.");
        }

        holder.itemView.setOnClickListener(v -> listener.onChatClick(user.getId()));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView lastMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.chat_user_name);
            lastMessage = itemView.findViewById(R.id.chat_last_message);
        }
    }
}