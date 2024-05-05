package com.adeeltahir.sewingcircle;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.adeeltahir.sewingcircle.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    HomeFragment homeFragment;
    AccountInfo accountInfo;
    CurrentCustomer currentCustomer;
    PreviousCustomers previousCustomers;



    public void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
//         Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Intent intentMain = new Intent(MainActivity.this, Register.class);
            startActivity(intentMain);
            finish();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        homeFragment = new HomeFragment();
//        accountInfo = new AccountInfo();
//        currentCustomer = new CurrentCustomer();
//        previousCustomers = new PreviousCustomers();
        auth = FirebaseAuth.getInstance();
//        if (user == null) {
//            Intent intent = new Intent(MainActivity.this, Register.class);
//            startActivity(intent);
//            finish();
//        }
        if (user != null) {
            getIntent();
            Intent I1 = getIntent();
            String name = I1.getStringExtra("name");
            String email = I1.getStringExtra("email");

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("message");

            myRef.setValue(user.getEmail());


            // Read from the database
//            myRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // This method is called once with the initial value and again
//                    // whenever data at this location is updated.
//                    String value = dataSnapshot.getValue(String.class);
//                    Log.d(TAG, "Value is: " + value);
//                    Toast.makeText(MainActivity.this, value, Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    // Failed to read value
//                    Log.w(TAG, "Failed to read value.", error.toException());
//                    Toast.makeText(MainActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
//                }
//            });

            homeFragment = new HomeFragment();
            accountInfo = new AccountInfo();
            currentCustomer = new CurrentCustomer();
            previousCustomers = new PreviousCustomers();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
            Toast.makeText(this, (CharSequence) getSupportFragmentManager().toString(), Toast.LENGTH_SHORT).show();

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnItemSelectedListener(menuItem -> {
                final int id = menuItem.getItemId();

                if (id == R.id.home1) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                } else if (id == R.id.CurrentCustomer1) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, currentCustomer).commit();
                } else if (id == R.id.PreviousOrders1) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, previousCustomers).commit();
                } else if (id == R.id.Accountinfo1) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, accountInfo).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                }
                return true;
            });
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();

        }
    }


}
