package com.adeeltahir.sewingcircle;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.Firebase;
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

BottomNavigationView bottomNavigationView;
HomeFragment homeFragment=new HomeFragment();
AccountInfo accountInfo=new AccountInfo();
CurrentCustomer currentCustomer=new CurrentCustomer();
PreviousCustomers previousCustomers=new PreviousCustomers();
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
        auth=FirebaseAuth.getInstance();
//        user = auth.getCurrentUser();
//        if(user==null){
//Intent intent = new Intent(MainActivity.this, Register.class);
//            startActivity(intent);
//            finish();
//        }
//        else {
            Intent intent = getIntent();
            String name = intent.getStringExtra("name");
            String email = intent.getStringExtra("email");
            String password = intent.getStringExtra("password");
            String contactinfo = intent.getStringExtra("contactinfo");
            String category = intent.getStringExtra("category");
            Toast.makeText(this, name + email + password, Toast.LENGTH_SHORT).show();
            Log.d("MainActivity", "Name: " + name);
            Log.d("MainActivity", "Email: " + email);
            Log.d("MainActivity", "Password: " + password);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("message");

            myRef.setValue("Hello Adeel");


            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Value is: " + value);
                    Toast.makeText(MainActivity.this, value, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                    Toast.makeText(MainActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
                }
            });


            bottomNavigationView = findViewById(R.id.bottom_navigation);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
            BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.CurrentCustomer);
            badgeDrawable.setVisible(true);
            badgeDrawable.setNumber(5);

            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {

                    if (menuItem.getItemId() == R.id.home) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                        return true;
                    } else if (menuItem.getItemId() == R.id.CurrentCustomer) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, currentCustomer).commit();
                        return true;
                    }
//                    else if (menuItem.getItemId()==R.id.CurrentCustomer)  {
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,loginFragment).commit();
//                        return true;
//                    }
                    else if (menuItem.getItemId() == R.id.PreviousOrders) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, previousCustomers).commit();
                        return true;
                    } else if (menuItem.getItemId() == R.id.Accountinfo) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, accountInfo).commit();
                        return true;
                    }
                    return false;


//                }

            }
            });
    }


    public void Signout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
        finish();
    }
}