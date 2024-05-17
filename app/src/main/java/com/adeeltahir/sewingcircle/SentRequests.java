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
        sentRequestsRef = FirebaseDatabase.getInstance().getReference().child("SentRequests");

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
                    // Extract sent request details
                    String name = snapshot.child("name").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String contactInfo = snapshot.child("contactInfo").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);

                    // Create a SentRequest object
                    SentRequest sentRequest = new SentRequest(name, address, contactInfo, email);

                    // Add the sent request to the list
                    sentRequests.add(sentRequest);
                }

                // Update RecyclerView with the fetched data
                sentRequestsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled event
            }
        });
    }
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

    public static class SentRequestViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView address;
        private TextView contactInfo;
        private TextView email;
        private Button buttonCancel;

        public SentRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameSentRequest);
            address = itemView.findViewById(R.id.addressSentRequest);
            contactInfo = itemView.findViewById(R.id.contactInfoSentRequest);
            email = itemView.findViewById(R.id.emailSentRequest);
            buttonCancel = itemView.findViewById(R.id.buttonCancel);
        }

        public void bind(SentRequest sentRequest) {
            name.setText(sentRequest.getName());
            address.setText(sentRequest.getAddress());
            contactInfo.setText(sentRequest.getContactInfo());
            email.setText(sentRequest.getEmail());

            buttonCancel.setOnClickListener(v -> {
                // Handle cancel button click event
                Toast.makeText(itemView.getContext(), "Cancelled request for " + sentRequest.getName(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
 class SentRequest {
    private String name;
    private String address;
    private String contactInfo;
    private String email;

    public SentRequest(String name, String address, String contactInfo, String email) {
        this.name = name;
        this.address = address;
        this.contactInfo = contactInfo;
        this.email = email;
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
}
