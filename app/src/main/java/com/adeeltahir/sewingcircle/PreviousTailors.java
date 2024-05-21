package com.adeeltahir.sewingcircle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import android.widget.Button;
import android.widget.TextView;



public class PreviousTailors extends Fragment {
    FirebaseAuth auth;
    FirebaseUser user;

    private RecyclerView recyclerViewPreviousTailors;
    private PreviousTailorsAdapter previousTailorsAdapter;
    private List<PreviousTailor> previousTailors;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_previous_tailors, container, false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Initialize RecyclerView
        recyclerViewPreviousTailors = view.findViewById(R.id.recyclerViewPreviousTailors);
        recyclerViewPreviousTailors.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPreviousTailors.setHasFixedSize(true);

        // Initialize data
        previousTailors = new ArrayList<>();

        // Initialize adapter
        previousTailorsAdapter = new PreviousTailorsAdapter(previousTailors,this);
        recyclerViewPreviousTailors.setAdapter(previousTailorsAdapter);

        // Fetch data from Firebase
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Customer").child(user.getUid()).child("CurrentTailors");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                previousTailors.clear(); // Clear the list to avoid duplication
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String tailorId = dataSnapshot.getKey();
                    if (tailorId != null) {
                        // Fetch tailor details from "Tailors" node
                        DatabaseReference tailorRef = FirebaseDatabase.getInstance().getReference().child("Tailor").child(tailorId);
                        tailorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot tailorSnapshot) {

                                String tailorName = tailorSnapshot.child("name").getValue(String.class);
                                String tailorAddress = tailorSnapshot.child("address").getValue(String.class);
                                String tailorContactInfo = tailorSnapshot.child("contactInfo").getValue(String.class);
                                String tailorEmail = tailorSnapshot.child("email").getValue(String.class);

                                    previousTailors.add(new PreviousTailor(tailorId, tailorName, tailorAddress, tailorEmail, tailorContactInfo));
                                    previousTailorsAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "Failed to load tailor data", Toast.LENGTH_SHORT).show();
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





 class PreviousTailorsAdapter extends RecyclerView.Adapter<PreviousTailorsAdapter.PreviousTailorViewHolder> {

    private List<PreviousTailor> previousTailors;
     private Fragment fragment;

     public PreviousTailorsAdapter(List<PreviousTailor> previousTailors, Fragment fragment) {
        this.previousTailors = previousTailors;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public PreviousTailorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_previous_tailor, parent, false);
        return new PreviousTailorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviousTailorViewHolder holder, int position) {
        PreviousTailor previousTailor = previousTailors.get(position);
        holder.bind(previousTailor,fragment);
    }

    @Override
    public int getItemCount() {
        return previousTailors.size();
    }

    public static class PreviousTailorViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewAddress;
        private TextView textViewEmail;
        private TextView textViewContactInfo;
        private Button currentClientButton;

        public PreviousTailorViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewNamet);
            textViewAddress = itemView.findViewById(R.id.textViewAddresst);
            textViewEmail = itemView.findViewById(R.id.textViewEmailaddresst);
            textViewContactInfo = itemView.findViewById(R.id.textViewcontactinformationt);
            currentClientButton = itemView.findViewById(R.id.currenttailor);
        }

        public void bind(final PreviousTailor previousTailor,final Fragment fragment) {
            textViewName.setText(previousTailor.getName());
            textViewAddress.setText(previousTailor.getAddress());
            textViewEmail.setText(previousTailor.getEmail());
            textViewContactInfo.setText(previousTailor.getContactInfo());

            currentClientButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String userUid = user.getUid();
                        String tailorId = previousTailor.getId();
                        String tailorName = previousTailor.getName();

                        // Log statements for debugging
                        Log.d("CurrentClientButton", "User UID: " + userUid);
                        Log.d("CurrentClientButton", "Tailor ID: " + tailorId);
                        Log.d("CurrentClientButton", "Tailor Name: " + tailorName);

                        if (tailorId != null && tailorName != null) {
                            DatabaseReference currentTailorRef = FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child("Customer")
                                    .child(userUid)
                                    .child("CurrentTailor");

                            currentTailorRef.setValue(tailorId)
                                    .addOnSuccessListener(aVoid -> {
                                        // Get the activity context
                                        switchFragment(fragment);
                                        Log.d("CurrentClientButton", "Successfully set current tailor");
                                        Toast.makeText(v.getContext(), "Set current tailor: " + tailorName, Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("CurrentClientButton", "Failed to set current tailor: " + e.getMessage());
                                        Toast.makeText(v.getContext(), "Failed to set current tailor", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Log.e("CurrentClientButton", "Previous tailor ID or Name is null");
                            Toast.makeText(v.getContext(), "Previous tailor information is missing", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("CurrentClientButton", "User not authenticated");
                        Toast.makeText(v.getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
                    }
                }});
            }
        private void switchFragment(Fragment fragment) {
            FragmentManager fragmentManager = fragment.getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            CurrentTailor newFragment = new CurrentTailor(); // Replace with your target fragment
            fragmentTransaction.replace(R.id.fragment_container, newFragment); // replace with your container id
            fragmentTransaction.addToBackStack(null); // Optional: if you want to add to backstack
            fragmentTransaction.commit();
        }
        }
        }


 class PreviousTailor {
    private String id;
    private String name;
    private String address;
    private String email;
    private String contactInfo;

    // Default constructor required for calls to DataSnapshot.getValue(PreviousTailor.class)
    public PreviousTailor() {
    }

    public PreviousTailor(String id, String name, String address, String email, String contactInfo) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.contactInfo = contactInfo;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getContactInfo() {
        return contactInfo;
    }
}
