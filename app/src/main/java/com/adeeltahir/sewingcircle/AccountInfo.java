package com.adeeltahir.sewingcircle;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.adeeltahir.sewingcircle.databinding.ActivityMainBinding;
import com.adeeltahir.sewingcircle.ui.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class AccountInfo extends Fragment {
ActivityMainBinding binding;

    StorageReference fileRef;
    private TextView mUserNameTextView;

    private EditText mEditNameEditText;
    private EditText mEditEmail;
    private EditText mEditAddress;
    private EditText mEditContact;
    private Button mSaveButton;
    private Button mSignOutButton;
    ImageView ProfilePic;
    private FirebaseAuth mAuth;
    Uri imageUri;
    StorageReference storageReference;

    String UserName;
    String Addressinfo;
    String Email;
    String Contact;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_account_info, container, false);

        mEditNameEditText = rootView.findViewById(R.id.editNameEditText);
        mEditEmail = rootView.findViewById(R.id.Emailuser2);
        mEditContact = rootView.findViewById(R.id.contactinfo2);
        mEditAddress = rootView.findViewById(R.id.Address2);

        mSaveButton = rootView.findViewById(R.id.saveButton);
        mSignOutButton = rootView.findViewById(R.id.signOutButton);
         ProfilePic = rootView.findViewById(R.id.imageView);
//        FirebaseStorage storage= FirebaseStorage.getInstance();

        mAuth = FirebaseAuth.getInstance();
        ProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
                ProfilePic.setImageURI(imageUri);
            }
        });


        UserName = mAuth.getCurrentUser().getDisplayName();
        mEditNameEditText.setText(UserName);
        Addressinfo = mAuth.getCurrentUser().getDisplayName();
        mEditAddress.setText(Addressinfo);
        Contact = mAuth.getCurrentUser().getDisplayName();
        mEditContact.setText(Contact);
        Email = mAuth.getCurrentUser().getDisplayName();
        mEditEmail.setText(Email);


        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
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


    private void saveUser() {
        String newUserName = mEditNameEditText.getText().toString().trim();
        String newUserEmail = mEditEmail.getText().toString().trim();
        String newUserContact = mEditContact.getText().toString().trim();
        String newUserAddress = mEditAddress.getText().toString().trim();

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
        startActivity(intent);
        getActivity().finish();
    }

    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    // Handle the result of selecting an image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadImageToFirebase(imageUri);
            ProfilePic.setImageURI(imageUri);
        }
    }


    // Upload the selected image to Firebase Storage
    private void uploadImageToFirebase(Uri imageUri) {
        storageReference = FirebaseStorage.getInstance().getReference();
         fileRef = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");

        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Handle successful upload
                })
                .addOnFailureListener(e -> {
                    // Handle unsuccessful upload
                });
    }



}

