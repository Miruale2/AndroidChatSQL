package com.example.androidchat.bottom_nav.new_chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchat.R;
import com.example.androidchat.adapters.UsersAdapter;
import com.example.androidchat.auth.AuthManager;
import com.example.androidchat.bottom_nav.chats.ChatDetailFragment;
import com.example.androidchat.databinding.FragmentNewChatBinding;
import com.example.androidchat.models.User;
import com.example.androidchat.viewmodels.ChatViewModel;

import java.util.ArrayList;
import java.util.List;

public class New_chat_fragment extends Fragment {

    private FragmentNewChatBinding binding;
    private UsersAdapter adapter;
    private final List<User> users = new ArrayList<>();
    private ChatViewModel chatViewModel;
    private AuthManager authManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNewChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chatViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
        authManager = AuthManager.getInstance(requireContext());

        setupRecyclerView();
        observeUsers();

        chatViewModel.setCurrentUser(authManager.getCurrentUserId());
    }

    private void setupRecyclerView() {
        adapter = new UsersAdapter(users);
        RecyclerView recyclerView = binding.usersRv;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        adapter.setOnUserClickListener(user -> {
            users.remove(user);
            adapter.notifyDataSetChanged();

            chatViewModel.startChatWithUser(user, () -> {
                Toast.makeText(requireContext(), "Чат с " + user.getUsername(), Toast.LENGTH_SHORT).show();
                ChatDetailFragment fragment = ChatDetailFragment.newInstance(user.getId());
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            });
        });
    }

    private void observeUsers() {
        authManager.getAllUsersExceptCurrent().observe(getViewLifecycleOwner(), userList -> {
            users.clear();
            if (userList != null) {
                users.addAll(userList);
            }
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}