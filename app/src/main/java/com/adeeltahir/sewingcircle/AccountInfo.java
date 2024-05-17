package com.adeeltahir.sewingcircle;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountInfo extends Fragment {
    private boolean isCustomer;

    // Constructor to accept the boolean argument
    public AccountInfo(boolean isCustomer) {
        this.isCustomer = isCustomer;
    }

    public AccountInfo() {

    }

    private DatabaseReference myRef;
    ImageView imageView;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView userNameTextView, addressTextView, emailTextView, contactInfoTextView;
    private EditText editNameEditText, editAddressEditText, editEmailEditText, editContactInfoEditText;
    private Button saveButton, editButton, signOutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_info, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

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

        imageView = view.findViewById(R.id.imageView);
        int maxWidth = 100; // Set your desired maximum width
        int maxHeight = 150; // Set your desired maximum height

// Set the maximum width and maximum height
        imageView.setMaxWidth(maxWidth);
        imageView.setMaxHeight(maxHeight);// Assuming the ID of your ImageView is "imageView"
        imageView.setOnClickListener(v -> changeimg()
        );
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEditing(true);
            }
        });

        saveButton.setOnClickListener(v -> {
            saveChanges();
            enableEditing(false);
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                // Redirect the user to the login screen or perform any other desired action
                Intent intent = new Intent(getContext(), Register.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
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
        String newName = editNameEditText.getText().toString().trim();
        String newAddress = editAddressEditText.getText().toString().trim();
        String newEmail = editEmailEditText.getText().toString().trim();
        String newContactInfo = editContactInfoEditText.getText().toString().trim();

        myRef.child("name").setValue(newName);
        myRef.child("address").setValue(newAddress);
        myRef.child("email").setValue(newEmail);
        myRef.child("contactInfo").setValue(newContactInfo);

        // Update the TextViews with the new values
        userNameTextView.setText(newName);
        addressTextView.setText(newAddress);
        emailTextView.setText(newEmail);
        contactInfoTextView.setText(newContactInfo);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (user != null) {
            myRef = FirebaseDatabase.getInstance().getReference().child("Tailor").child(user.getUid());
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChildren()) {
                        // User not found in the Tailor node, switch to Customer node
                        myRef = FirebaseDatabase.getInstance().getReference().child("Customer").child(user.getUid());
                        fetchUserData();
                    } else {
                        fetchUserData();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(TAG, "DatabaseError: ", databaseError.toException());
                }
            });
        }
    }

    private void fetchUserData() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String contactInfo = dataSnapshot.child("contactInfo").getValue(String.class);
                    String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class); // Fetch the imageUrl

                    // Populate the fields with fetched data
                    userNameTextView.setText(name);
                    editNameEditText.setText(name);

                    addressTextView.setText(address);
                    editAddressEditText.setText(address);

                    emailTextView.setText(email);
                    editEmailEditText.setText(email);

                    contactInfoTextView.setText(contactInfo);
                    editContactInfoEditText.setText(contactInfo);
                    int width = 100; // Set your desired maximum width
                    int height = 150; // Set your desired maximum height
                    // Load the profile image using Glide
                    Glide.with(requireContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.customer) // Placeholder image while loading
                            .error(R.drawable.customer)
                            .override(width, height)// Error image if loading fails
                            .into(imageView);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "DatabaseError: ", databaseError.toException());
            }
        });
    }


    public static final int PICK_IMAGE_REQUEST = 1;

    public void changeimg() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
            uploadImageUrlToDatabase(imageUri);
        }
    }

    private void uploadImageUrlToDatabase(Uri imageUri) {
        String type = isCustomer ? "Customer" : "Tailor";
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(type).child(user.getUid()).child("imageUrl");

        databaseReference.setValue(imageUri.toString())
                .addOnSuccessListener(aVoid -> Toast.makeText(requireContext(), "Image URL uploaded successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Failed to upload image URL", Toast.LENGTH_SHORT).show());
    }
}