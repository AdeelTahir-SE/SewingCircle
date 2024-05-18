package com.adeeltahir.sewingcircle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PreviousCustomers extends Fragment {
    FirebaseAuth auth;
    FirebaseUser user;

    private RecyclerView recyclerViewPreviousCustomers;
    private PreviousCustomersAdapter previousCustomersAdapter;
    private List<PreviousCustomer> previousCustomers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_previous_customers, container, false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Initialize RecyclerView
        recyclerViewPreviousCustomers = view.findViewById(R.id.recyclerViewPreviousCustomers);
        recyclerViewPreviousCustomers.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPreviousCustomers.setHasFixedSize(true);

        // Initialize data
        previousCustomers = new ArrayList<>();

        // Initialize adapter
        previousCustomersAdapter = new PreviousCustomersAdapter(previousCustomers);
        recyclerViewPreviousCustomers.setAdapter(previousCustomersAdapter);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Tailor").child(user.getUid()).child("CurrentCustomers");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                previousCustomers.clear(); // Clear the list to avoid duplication
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String customerId = dataSnapshot.getKey();
                    if (customerId != null) {
                        // Fetch customer details from "Customers" node
                        DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference().child("Customer").child(customerId);
                        customerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot customerSnapshot) {
                                String customerName = customerSnapshot.child("name").getValue(String.class);
                                String customerAddress = customerSnapshot.child("address").getValue(String.class);
                                String customerContactInfo = customerSnapshot.child("contactInfo").getValue(String.class);
                                String customerEmail = customerSnapshot.child("email").getValue(String.class);

                                // Create a PreviousCustomer object
                                PreviousCustomer customer = new PreviousCustomer(customerId,customerName, customerAddress,customerEmail, customerContactInfo) ;

                                // Add the customer to the previousCustomers list
                                previousCustomers.add(customer);

                                // Notify the adapter of the dataset change
                                previousCustomersAdapter.notifyDataSetChanged();
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "Failed to load customer data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
