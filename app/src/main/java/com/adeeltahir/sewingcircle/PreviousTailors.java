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
        previousTailorsAdapter = new PreviousTailorsAdapter(previousTailors);
        recyclerViewPreviousTailors.setAdapter(previousTailorsAdapter);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Customer").child(user.getUid()).child("PreviousTailors");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                previousTailors.clear(); // Clear the list to avoid duplication
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String tailorId = dataSnapshot.getKey();
                    if (tailorId != null) {
                        // Fetch tailor details from "Tailors" node
                        DatabaseReference tailorRef = FirebaseDatabase.getInstance().getReference().child("Tailors").child(tailorId);
                        tailorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot tailorSnapshot) {
                                PreviousTailor tailor = tailorSnapshot.getValue(PreviousTailor.class);
                                if (tailor != null) {
                                    previousTailors.add(tailor);
                                    previousTailorsAdapter.notifyDataSetChanged();
                                }
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

    public PreviousTailorsAdapter(List<PreviousTailor> previousTailors) {
        this.previousTailors = previousTailors;
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
        holder.bind(previousTailor);
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
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewEmail = itemView.findViewById(R.id.Emailcustomer);
            textViewContactInfo = itemView.findViewById(R.id.textViewContactInfo);
            currentClientButton = itemView.findViewById(R.id.currentclient);
        }

        public void bind(final PreviousTailor previousTailor) {
            textViewName.setText(previousTailor.getName());
            textViewAddress.setText(previousTailor.getAddress());
            textViewEmail.setText(previousTailor.getEmail());
            textViewContactInfo.setText(previousTailor.getContactInfo());

            currentClientButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        DatabaseReference currentTailorRef = FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Customer")
                                .child(user.getUid())
                                .child("CurrentTailor");

                        currentTailorRef.setValue(previousTailor.getId())
                                .addOnSuccessListener(aVoid -> Toast.makeText(v.getContext(), "Set current tailor: " + previousTailor.getName(), Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(v.getContext(), "Failed to set current tailor", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(v.getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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
