package com.adeeltahir.sewingcircle;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference myRef;
    private boolean customer = true; // Set this based on your user type logic
    String category;

    @Override
    public void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intentMain = new Intent(MainActivity.this, Register.class);
            startActivity(intentMain);
            finish();
            return;
        }

        checkUserCategory();
    }

    private void checkUserCategory() {
        myRef = FirebaseDatabase.getInstance().getReference().child("Tailor").child(user.getUid()).child("category");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                category = dataSnapshot.getValue(String.class);

                if (category != null && category.equals("TAILOR")) {
                    customer = false;
                    Log.d(TAG, "User is a Tailor");
                } else {
                    customer = true;
                    Log.d(TAG, "User is a Customer");
                }

                checkCustomerCategory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "DatabaseError: ", databaseError.toException());
            }
        });
    }

    private void checkCustomerCategory() {
        if (!customer) {
            myRef = FirebaseDatabase.getInstance().getReference().child("Customer").child(user.getUid()).child("category");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    category = dataSnapshot.getValue(String.class);
                    if (category != null) {
                        if (category.equals("CUSTOMER")) {
                            customer = true;
                            Log.d(TAG, "User is a Customer");
                        } else {
                            customer = false;
                            Log.d(TAG, "User is a Tailor");
                        }
                    }

                    setupBottomNavigation(); // Call setupBottomNavigation after retrieving category
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(TAG, "DatabaseError: ", databaseError.toException());
                }
            });
        } else {
            setupBottomNavigation(); // Call setupBottomNavigation if user is a customer
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (bottomNavigationView == null) {
            Log.e(TAG, "BottomNavigationView not found");
            return;
        }

        bottomNavigationView.getMenu().clear();
        if (!customer) {
            bottomNavigationView.inflateMenu(R.menu.bottom_nav_menucustomer);
        } else if(customer) {
            bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (customer) {
            ft.add(R.id.fragment_container, new CurrentCustomer());
        } else if (!customer) {
            ft.add(R.id.fragment_container, new CurrentTailor());
        }
        ft.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (customer) {
                    if (id == R.id.home1) {
                        replace_fragment(new HomeFragment());
                    } else if (id == R.id.CurrentCustomer1) {
                        replace_fragment(new CurrentCustomer());
                    } else if (id == R.id.PreviousOrders1) {
                        replace_fragment(new PreviousCustomers());
                    } else if (id == R.id.Accountinfo1) {
                        replace_fragment(new AccountInfo());
                    } else if (id == R.id.Requests1) {
                        replace_fragment(new Requests());
                    }
                    return true;
                } else if (!customer) {
                    if (id == R.id.home1_customer) {
                        replace_fragment(new HomeFragmentCustomer());
                    } else if (id == R.id.CurrentCustomer_customer) {
                        replace_fragment(new CurrentTailor());
                    } else if (id == R.id.PreviousOrders1_customer) {
                        replace_fragment(new PreviousTailors());
                    } else if (id == R.id.Accountinfo1_customer) {
                        replace_fragment(new AccountInfo());
                    } else if (id == R.id.Requests1_customer) {
                        replace_fragment(new SentRequests());
                    }
                    return true;
                }
                return true;
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
    }

    public void replace_fragment(Fragment f) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, f);
        ft.commit();
    }
}
