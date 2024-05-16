package com.adeeltahir.sewingcircle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AccountInfo extends Fragment {

    private TextView userNameTextView, addressTextView, emailTextView, contactInfoTextView;
    private EditText editNameEditText, editAddressEditText, editEmailEditText, editContactInfoEditText;
    private Button saveButton, editButton, signOutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_info, container, false);

        userNameTextView = view.findViewById(R.id.userNameTextView);
        editNameEditText = view.findViewById(R.id.editNameEditText);
        addressTextView = view.findViewById(R.id.Address1);
        editAddressEditText = view.findViewById(R.id.Address2);
        emailTextView = view.findViewById(R.id.Emailuser1);
        editEmailEditText = view.findViewById(R.id.Emailuser2);
        contactInfoTextView = view.findViewById(R.id.Contactinfo1);
        editContactInfoEditText = view.findViewById(R.id.contactinfo2);

        saveButton = view.findViewById(R.id.saveButton);
        editButton = view.findViewById(R.id.editButton);
        signOutButton = view.findViewById(R.id.signOutButton);

        // Set initial text or fetch from a data source
        userNameTextView.setText("John Doe");
        editNameEditText.setText("John Doe");

        addressTextView.setText("123 Main St");
        editAddressEditText.setText("123 Main St");

        emailTextView.setText("john.doe@example.com");
        editEmailEditText.setText("john.doe@example.com");

        contactInfoTextView.setText("+1234567890");
        editContactInfoEditText.setText("+1234567890");

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEditing(true);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
                enableEditing(false);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement sign out action
            }
        });

        return view;
    }

    private void enableEditing(boolean isEditing) {
        userNameTextView.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        editNameEditText.setVisibility(isEditing ? View.VISIBLE : View.GONE);

        addressTextView.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        editAddressEditText.setVisibility(isEditing ? View.VISIBLE : View.GONE);

        emailTextView.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        editEmailEditText.setVisibility(isEditing ? View.VISIBLE : View.GONE);

        contactInfoTextView.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        editContactInfoEditText.setVisibility(isEditing ? View.VISIBLE : View.GONE);

        saveButton.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        editButton.setVisibility(isEditing ? View.GONE : View.VISIBLE);
    }

    private void saveChanges() {
        userNameTextView.setText(editNameEditText.getText().toString());
        addressTextView.setText(editAddressEditText.getText().toString());
        emailTextView.setText(editEmailEditText.getText().toString());
        contactInfoTextView.setText(editContactInfoEditText.getText().toString());
    }
}
