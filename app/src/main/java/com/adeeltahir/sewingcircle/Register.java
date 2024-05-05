package com.adeeltahir.sewingcircle;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
//import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.adeeltahir.sewingcircle.ui.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
//import com.google.firebase.auth.FirebaseAuthException;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.SetOptions;
//
//import java.util.HashMap;
//import java.util.Map;

public class Register extends AppCompatActivity {
    FirebaseAuth mAuth ;

    EditText name;
    EditText email;
    EditText password;
    EditText contactinfo;
    EditText category;


    String Name;
    String Email;
    String Password;
    String ContactInfo;
    String Category;

    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
//         Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intentMain = new Intent(Register.this, MainActivity.class);
            intentMain.putExtra("email", currentUser.getEmail());
            intentMain.putExtra("name", currentUser.getDisplayName());
      startActivity(intentMain);
      finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
         name = findViewById(R.id.input_name);
         email = findViewById(R.id.et_email);
         password = findViewById(R.id.et_password);
         contactinfo =findViewById(R.id.input_contact);
         category = findViewById(R.id.user_type);


    }
    public void onLoginClick(View view) {
        Intent intent = new Intent(Register.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void Submitreg (View view) {
        String Name = name.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String ContactInfo = contactinfo.getText().toString();
        String Category = category.getText().toString();

        if (Name.isEmpty() || Email.isEmpty() || Password.isEmpty() ||
                ContactInfo.isEmpty() || Category.isEmpty()) {
            name.setError("Please enter your name");
            email.setError("Please enter your email");
            password.setError("Please enter your password");
            contactinfo.setError("Please enter your contact info");
            category.setError("Please enter your category");
            return; // Return if any field is empty
        }

        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            // Move the Intent to MainActivity here
                            Intent intent = new Intent(Register.this, MainActivity.class);
                            startActivity(intent);
                            // Finish the current activity to prevent the user from going back to the registration screen
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//           String mCustomToken = "your_custom_token";
//        mAuth.signInWithCustomToken(mCustomToken)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCustomToken:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
//                            Toast.makeText(Register.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//                    }
//                });



    }


    public void updateUI(FirebaseUser user) {
        user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(Name)
                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
//                .setPhotoUri(Uri.parse(null)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });



        user.updateEmail(Email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                        }
                    }
                });
        String newPassword = Password;

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference userRef = db.collection("users").document(user.getUid());
//
//        Map<String, Object> userData = new HashMap<>();
//        userData.put("type", Category);
//        userData.put("contactInformation", ContactInfo);
//// userData.put("location", userLocation);
//
//        SetOptions options = SetOptions.merge(); // Create merge options
//
//        userRef.set(userData, options) // Merge to update only specific fields
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "User data updated in Firestore.");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error updating user data in Firestore", e);
//                    }
//                });



    }


}