package com.example.androidchat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidchat.auth.AuthManager;
import com.example.androidchat.databinding.ActivityRegisterBinding;
import com.example.androidchat.models.User;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authManager = AuthManager.getInstance(getApplicationContext());
        setupListeners();
    }

    private void setupListeners() {
        binding.register.setOnClickListener(v -> attemptRegistration());
        binding.backToLogin.setOnClickListener(v -> navigateToLogin());
    }

    private void attemptRegistration() {
        String email = binding.emailText.getText().toString().trim();  // email
        String username = binding.userText.getText().toString().trim(); // username
        String password = binding.passwordText.getText().toString().trim(); // password

        if (validateInputs(email, username, password)) {
            registerUser(email, username, password);
        }
    }

    private boolean validateInputs(String email, String username, String password) {
        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void registerUser(String email, String username, String password) {
        new Thread(() -> {
            User user = new User(username, email, password);
            authManager.register(user);

            runOnUiThread(() -> {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                navigateToMain();
            });
        }).start();
    }

    private void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void navigateToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
