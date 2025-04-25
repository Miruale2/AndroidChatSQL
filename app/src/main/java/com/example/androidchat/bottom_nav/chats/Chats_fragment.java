package com.example.androidchat.bottom_nav.chats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchat.R;
import com.example.androidchat.adapters.ChatsAdapter;
import com.example.androidchat.models.Message;
import com.example.androidchat.models.User;
import com.example.androidchat.viewmodels.ChatViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chats_fragment extends Fragment {
    private RecyclerView recyclerView;
    private ChatViewModel chatViewModel;
    private ChatsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_chats);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);

        chatViewModel.getActiveChats().observe(getViewLifecycleOwner(), users -> {
            if (users == null) return;

            Map<String, Message> lastMessages = new HashMap<>();

            for (User user : users) {
                chatViewModel.getLastMessage(user.getId()).observe(getViewLifecycleOwner(), message -> {
                    if (message != null) {
                        lastMessages.put(user.getId(), message);

                        if (adapter == null) {
                            adapter = new ChatsAdapter(users, lastMessages, userId -> {
                                ChatDetailFragment fragment = ChatDetailFragment.newInstance(userId);
                                requireActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, fragment)
                                        .addToBackStack(null)
                                        .commit();
                            });
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.updateData(users, lastMessages);
                        }
                    }
                });
            }
        });
    }
}