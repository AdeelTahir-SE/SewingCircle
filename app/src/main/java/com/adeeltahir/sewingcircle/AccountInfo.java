package com.adeeltahir.sewingcircle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.adeeltahir.sewingcircle.ui.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AccountInfo extends Fragment {

    private TextView mUserNameTextView;
    private EditText mEditNameEditText;
    private Button mSaveButton;
    private Button mSignOutButton;

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account_info, container, false);

        mUserNameTextView = rootView.findViewById(R.id.userNameTextView);
        mEditNameEditText = rootView.findViewById(R.id.editNameEditText);
        mSaveButton = rootView.findViewById(R.id.saveButton);
        mSignOutButton = rootView.findViewById(R.id.signOutButton);

        mAuth = FirebaseAuth.getInstance();

        // Set initial user name
        String userName = mAuth.getCurrentUser().getDisplayName();
        mUserNameTextView.setText(userName);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserName();
            }
        });

        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        return rootView;
    }

    private void saveUserName() {
        String newUserName = mEditNameEditText.getText().toString().trim();
        if (!newUserName.isEmpty()) {
            // Update user name in Firebase
            mAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(newUserName)
                            .build())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mUserNameTextView.setText(newUserName);
                                Toast.makeText(getContext(), "User name updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to update user name", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Please enter a new user name", Toast.LENGTH_SHORT).show();
        }
    }

    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(getContext(), LoginActivity.class);
    }
}
