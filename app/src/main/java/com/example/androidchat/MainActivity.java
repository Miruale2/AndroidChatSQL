package com.example.androidchat;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidchat.auth.AuthManager;
import com.example.androidchat.bottom_nav.chats.Chats_fragment;
import com.example.androidchat.bottom_nav.new_chat.New_chat_fragment;
import com.example.androidchat.bottom_nav.profile.Profile_fragment;
import com.example.androidchat.databinding.ActivityMainBinding;
import com.example.androidchat.viewmodels.MainViewModel;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AuthManager authManager;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        authManager = AuthManager.getInstance(this);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.isUserLoggedIn().observe(this, isLoggedIn -> {
            if (!isLoggedIn) {
                startLoginActivity();
            } else {
                initFragments();
                observeViewModel();
            }
        });
    }

    private void initFragments() {
        Map<Integer, Fragment> fragmentMap = new HashMap<>();
        fragmentMap.put(R.id.chats, new Chats_fragment());
        fragmentMap.put(R.id.new_chat, new New_chat_fragment());
        fragmentMap.put(R.id.profile, new Profile_fragment());

        binding.bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = fragmentMap.get(item.getItemId());
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(binding.fragmentContainer.getId(), fragment)
                        .commit();
                return true;
            }
            return false;
        });

        binding.bottomNav.setSelectedItemId(R.id.chats);
    }

    private void observeViewModel() {

        viewModel.getChatUsers().observe(this, users -> {
        });
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
