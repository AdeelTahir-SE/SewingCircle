package com.adeeltahir.sewingcircle;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import static com.adeeltahir.sewingcircle.R.layout.item_sent_requests;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SentRequests extends Fragment {

    private RecyclerView recyclerViewSentRequests;
    private SentRequestsAdapter sentRequestsAdapter;
    private List<SentRequest> sentRequests;
    private DatabaseReference sentRequestsRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sent_requests, container, false);

        // Initialize RecyclerView
        recyclerViewSentRequests = view.findViewById(R.id.recyclerViewSentReqs);
        recyclerViewSentRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSentRequests.setHasFixedSize(true);

        // Initialize Firebase Database reference
        sentRequestsRef = FirebaseDatabase.getInstance().getReference().child("Customer").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("sentRequests");

        // Initialize data
        sentRequests = new ArrayList<>();

        // Initialize adapter
        sentRequestsAdapter = new SentRequestsAdapter(sentRequests);

        // Set adapter to RecyclerView
        recyclerViewSentRequests.setAdapter(sentRequestsAdapter);

        // Fetch data from Firebase Database
        fetchSentRequestsData();

        return view;
    }

    private void fetchSentRequestsData() {
        sentRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sentRequests.clear();

                // Iterate through the DataSnapshot to get each sent request
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract tailor ID from the sent request
                    String tailorId = snapshot.getKey();

                    // Reference to the tailor's node in Firebase
                    DatabaseReference tailorRef = FirebaseDatabase.getInstance().getReference().child("Tailor").child(tailorId);

                    // Fetch tailor details
                    tailorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Extract tailor details
                                String name = dataSnapshot.child("name").getValue(String.class);
                                String address = dataSnapshot.child("address").getValue(String.class);
                                String contactInfo = dataSnapshot.child("contactInfo").getValue(String.class);
                                String email = dataSnapshot.child("email").getValue(String.class);

                                // Create a SentRequest object
                                SentRequest sentRequest = new SentRequest(name, address, contactInfo, email, tailorId);

                                // Add the sent request to the list
                                sentRequests.add(sentRequest);

                                // Update RecyclerView with the fetched data
                                sentRequestsAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle onCancelled event
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled event
            }
        });
    }




class SentRequestsAdapter extends RecyclerView.Adapter<SentRequestsAdapter.SentRequestViewHolder> {

    private List<SentRequest> sentRequestsList;

    public SentRequestsAdapter(List<SentRequest> sentRequestsList) {
        this.sentRequestsList = sentRequestsList;
    }

    @NonNull
    @Override
    public SentRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(item_sent_requests, parent, false);
        return new SentRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SentRequestViewHolder holder, int position) {
        SentRequest sentRequest = sentRequestsList.get(position);
        holder.bind(sentRequest);
    }

    @Override
    public int getItemCount() {
        return sentRequestsList.size();
    }

    public class SentRequestViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView address;
        private TextView contactInfo;
        private TextView email;
        private Button buttonCancel;

        FirebaseAuth auth;
        FirebaseUser user;
        public SentRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameSentRequest);
            address = itemView.findViewById(R.id.addressSentRequest);
            contactInfo = itemView.findViewById(R.id.contactInfoSentRequest);
            email = itemView.findViewById(R.id.emailSentRequest);
            buttonCancel = itemView.findViewById(R.id.buttonCancel);
        }

        public void bind(SentRequest sentRequest) {
//            if(!buttonCancel.hasOnClickListeners()) {
                // Set the text of the TextViews (name, address, contactInfo, email
                name.setText(sentRequest.getName());
                address.setText(sentRequest.getAddress());
                contactInfo.setText(sentRequest.getContactInfo());
                email.setText(sentRequest.getEmail());

            buttonCancel.setOnClickListener(v -> {
                auth = FirebaseAuth.getInstance();
                user = auth.getCurrentUser();
                if (user != null) {
                    // Get the tailor ID from the card
                    String tailorId = sentRequest.getTailorId();

                    // Reference to the tailor's requests node in Firebase
                    DatabaseReference tailorRequestsRef = FirebaseDatabase.getInstance().getReference().child("Tailor").child(tailorId).child("requests");

                    // Remove the user's ID from the tailor's requests node
                    tailorRequestsRef.child(user.getUid()).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Toast indicating request cancellation
                                    Toast.makeText(itemView.getContext(), "Request canceled ", Toast.LENGTH_SHORT).show();

                                    // Reference to the user's sent requests node in Firebase
                                    DatabaseReference userSentRequestsRef = FirebaseDatabase.getInstance().getReference().child("Customer").child(user.getUid()).child("sentRequests");

                                    // Remove the tailor's ID from the user's sent requests node
                                    userSentRequestsRef.child(tailorId).removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Toast indicating removal from sent requests

                                                    Toast.makeText(itemView.getContext(), "Request removed from sent requests", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Toast indicating failure to remove from sent requests
                                                    Toast.makeText(itemView.getContext(), "Failed to remove from sent requests", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Toast indicating failure to cancel request
                                    Toast.makeText(itemView.getContext(), "Failed to cancel request", Toast.LENGTH_SHORT).show();
                                }
                            });
                }});}}}

            }


 class SentRequest {
    private String name;
    private String address;
    private String contactInfo;
    private String email;
    private String tailorid;

    public SentRequest(String name, String address, String contactInfo, String email,String tailorid) {
        this.name = name;
        this.address = address;
        this.contactInfo = contactInfo;
        this.email = email;
        this.tailorid = tailorid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getEmail() {
        return email;
    }
    public String getTailorId() {
        return tailorid;
    }
}
