package com.example.androidchat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.androidchat.auth.AuthManager;
import com.example.androidchat.databinding.ActivityLoginBinding;
import com.example.androidchat.viewmodels.ChatViewModel;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AuthManager authManager;
    private ChatViewModel chatViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authManager = AuthManager.getInstance(this);
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);


        binding.button.setOnClickListener(v -> attemptLogin());
        binding.registerLink.setOnClickListener(v -> startRegisterActivity());
    }
    private void attemptLogin() {
        String email = binding.emailText.getText().toString().trim();
        String password = binding.passwordText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        authManager.login(email, password).observe(this, success -> {
            if (success) {
                String userId = authManager.getCurrentUserId();
                chatViewModel.setCurrentUser(userId);

                startMainActivity();
            }
        });
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void startRegisterActivity() {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
