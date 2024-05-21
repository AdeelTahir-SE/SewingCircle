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
import java.util.List;        import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        previousCustomersAdapter = new PreviousCustomersAdapter(previousCustomers, this);
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
                                PreviousCustomer customer = new PreviousCustomer(customerId, customerName, customerAddress, customerEmail, customerContactInfo);

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




 class PreviousCustomersAdapter extends RecyclerView.Adapter<PreviousCustomersAdapter.PreviousCustomerViewHolder> {

    private List<PreviousCustomer> previousCustomers;
    private Fragment fragment; // Add this line

    public PreviousCustomersAdapter(List<PreviousCustomer> previousCustomers, Fragment fragment) {
        this.previousCustomers = previousCustomers;
        this.fragment = fragment; // Add this line
    }

    @NonNull
    @Override
    public PreviousCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_previous_customer, parent, false);
        return new PreviousCustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviousCustomerViewHolder holder, int position) {
        PreviousCustomer previousCustomer = previousCustomers.get(position);
        holder.bind(previousCustomer, fragment); // Pass the fragment here
    }

    @Override
    public int getItemCount() {
        return previousCustomers.size();
    }

    public static class PreviousCustomerViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewAddress;
        private TextView textViewEmail;
        private TextView textViewContactInfo;
        private Button currentClientButton;

        public PreviousCustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewEmail = itemView.findViewById(R.id.Emailcustomer);
            textViewContactInfo = itemView.findViewById(R.id.textViewContactInfo);
            currentClientButton = itemView.findViewById(R.id.currentclient);
        }

        public void bind(final PreviousCustomer previousCustomer, final Fragment fragment) { // Add the fragment parameter
            textViewName.setText(previousCustomer.getName());
            textViewAddress.setText(previousCustomer.getAddress());
            textViewEmail.setText(previousCustomer.getEmail());
            textViewContactInfo.setText(previousCustomer.getContactInfo());

            currentClientButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        DatabaseReference currentCustomerRef = FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Tailor")
                                .child(user.getUid())
                                .child("CurrentCustomer");

                        currentCustomerRef.setValue(previousCustomer.getId())
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(v.getContext(), "Set current customer: " + previousCustomer.getName(), Toast.LENGTH_SHORT).show();
                                    switchFragment(fragment);
                                })
                                .addOnFailureListener(e -> Toast.makeText(v.getContext(), "Failed to set current customer", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(v.getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void switchFragment(Fragment fragment) {
            FragmentManager fragmentManager = fragment.getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            CurrentCustomer newFragment = new CurrentCustomer(); // Replace with your target fragment
            fragmentTransaction.replace(R.id.fragment_container, newFragment); // replace with your container id
            fragmentTransaction.addToBackStack(null); // Optional: if you want to add to backstack
            fragmentTransaction.commit();
        }
    }
}


 class PreviousCustomer {
    private String id;
    private String name;
    private String address;
    private String email;
    private String contactInfo;

    public PreviousCustomer(String id, String name, String address, String email, String contactInfo) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.contactInfo = contactInfo;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}
