package com.example.androidchat.bottom_nav.chats;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchat.R;
import com.example.androidchat.adapters.MessagesAdapter;
import com.example.androidchat.models.Message;
import com.example.androidchat.viewmodels.ChatViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatDetailFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessagesAdapter adapter;
    private EditText editTextMessage;
    private Button buttonSend;
    private ChatViewModel chatViewModel;

    private String receiverId;
    private String currentUserId;
    private final List<Message> messageList = new ArrayList<>();

    public static ChatDetailFragment newInstance(String receiverId) {
        ChatDetailFragment fragment = new ChatDetailFragment();
        Bundle args = new Bundle();
        args.putString("receiver_id", receiverId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_chat);
        editTextMessage = view.findViewById(R.id.edit_text_message);
        buttonSend = view.findViewById(R.id.button_send);

        chatViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
        receiverId = getArguments() != null ? getArguments().getString("receiver_id") : "";

        if (receiverId.isEmpty()) return;

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        chatViewModel.getCurrentUserId().observe(getViewLifecycleOwner(), userId -> {
            if (userId == null) return;

            currentUserId = userId;
            adapter = new MessagesAdapter(messageList, currentUserId);
            recyclerView.setAdapter(adapter);

            chatViewModel.getChatMessages(currentUserId, receiverId).observe(getViewLifecycleOwner(), messages -> {
                messageList.clear();
                messageList.addAll(messages);
                adapter.updateMessages(messages);
                recyclerView.scrollToPosition(messageList.size() - 1);
            });

            buttonSend.setOnClickListener(v -> sendMessage());
        });
    }

    private void sendMessage() {
        String content = editTextMessage.getText().toString().trim();
        if (TextUtils.isEmpty(content)) return;

        Message message = new Message(
                currentUserId,
                receiverId,
                content,
                System.currentTimeMillis(),
                false
        );

        chatViewModel.sendMessage(message);
        editTextMessage.setText("");
        hideKeyboard();
        recyclerView.scrollToPosition(messageList.size() - 1);
    }

    private void hideKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm =
                    (InputMethodManager) requireActivity().getSystemService(requireContext().INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
