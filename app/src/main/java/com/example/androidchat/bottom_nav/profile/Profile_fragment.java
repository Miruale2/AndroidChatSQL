package com.example.androidchat.bottom_nav.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import com.example.androidchat.R;
import com.example.androidchat.auth.AuthManager;
import com.example.androidchat.models.User;

public class Profile_fragment extends Fragment {
    private TextView usernameTextView, emailTextView, uidTextView;
    private AuthManager authManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);

        authManager = AuthManager.getInstance(requireContext());

        authManager.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    usernameTextView.setText(user.getUsername());
                    emailTextView.setText(user.getEmail());
                    uidTextView.setText(user.getId());
                } else {
                    setErrorState();
                }
            }
        });

        return view;
    }

    private void initViews(View view) {
        usernameTextView = view.findViewById(R.id.username_tv);
        emailTextView = view.findViewById(R.id.email_tv);
        uidTextView = view.findViewById(R.id.uid_tv);
    }

    private void setErrorState() {
        usernameTextView.setText(R.string.not_authorized);
        emailTextView.setText("-");
        uidTextView.setText("-");
    }
}