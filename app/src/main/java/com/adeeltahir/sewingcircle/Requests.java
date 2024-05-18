package com.adeeltahir.sewingcircle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Requests extends Fragment {

    private RecyclerView recyclerViewRequests;
    private RequestsAdapter requestsAdapter;
    private List<Request> requestsList;
    private DatabaseReference requestsRef;
    private DatabaseReference customerRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        recyclerViewRequests = view.findViewById(R.id.recyclerViewReqs);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewRequests.setHasFixedSize(true);

        requestsList = new ArrayList<>();
        requestsAdapter = new RequestsAdapter(requestsList);
        recyclerViewRequests.setAdapter(requestsAdapter);

        customerRef = FirebaseDatabase.getInstance().getReference().child("Customer");
        requestsRef = FirebaseDatabase.getInstance().getReference().child("Tailor").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("requests");

        requestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestsList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String requestId = snapshot.getKey();
                    customerRef.child(requestId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot customerSnapshot) {
                            if (customerSnapshot.exists()) {
                                String name = customerSnapshot.child("name").getValue(String.class);
                                String address = customerSnapshot.child("address").getValue(String.class);
                                String contactInfo = customerSnapshot.child("contactInfo").getValue(String.class);
                                String email = customerSnapshot.child("email").getValue(String.class);

                                requestsList.add(new Request(name, address, contactInfo, email, requestId));
                                requestsAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("RequestsFragment", "Error fetching customer details: " + databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("RequestsFragment", "Error fetching requests: " + databaseError.getMessage());
            }
        });
    }
}

class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ReqsCustomerViewHolder> {

    private List<Request> requestsList;

    public RequestsAdapter(List<Request> reqs) {
        this.requestsList = reqs;
    }

    @NonNull
    @Override
    public ReqsCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemrequests, parent, false);
        return new ReqsCustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReqsCustomerViewHolder holder, int position) {
        Request request = requestsList.get(position);
        holder.bind(request);
    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    public static class ReqsCustomerViewHolder extends RecyclerView.ViewHolder {
        private TextView Name;
        private TextView Address;
        private TextView Contactinfo;
        private TextView Email;
        private Button buttonAccept;

        public ReqsCustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.Namereqr);
            Address = itemView.findViewById(R.id.textViewAddressr);
            Contactinfo = itemView.findViewById(R.id.Contactinforeqr);
            Email = itemView.findViewById(R.id.Emailreqr);
            buttonAccept = itemView.findViewById(R.id.buttonAccept);
        }

        public void bind(Request request) {
            Name.setText(request.getName());
            Address.setText(request.getAddress());
            Contactinfo.setText(request.getContactinfo());
            Email.setText(request.getEmail());

            buttonAccept.setOnClickListener(v -> {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    DatabaseReference tailorRequestsRef = FirebaseDatabase.getInstance().getReference().child("Tailor").child(user.getUid()).child("requests");
                    DatabaseReference tailorCurrentCustomers = FirebaseDatabase.getInstance().getReference().child("Tailor").child(user.getUid()).child("CurrentCustomers");
                    DatabaseReference customerCurrentTailorsRef = FirebaseDatabase.getInstance().getReference().child("Customer").child(request.getRequestId()).child("CurrentTailors").child(user.getUid());

                    // Add current tailor ID to Customer's CurrentTailors node
                    customerCurrentTailorsRef.setValue(true)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(v.getContext(), "Added current tailor ID to Customer's CurrentTailors node", Toast.LENGTH_SHORT).show();

                                // Proceed with other operations only if adding to CurrentTailors is successful
                                tailorCurrentCustomers.child(request.getRequestId()).setValue(true)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(v.getContext(), "Customer request accepted", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(v.getContext(), "Failed to write Customer data " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                tailorRequestsRef.child(request.getRequestId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(itemView.getContext(), "Request Accepted", Toast.LENGTH_SHORT).show();

                                        DatabaseReference userSentRequestsRef = FirebaseDatabase.getInstance().getReference().child("Customer").child(request.getRequestId()).child("sentRequests");

                                        userSentRequestsRef.child(user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(itemView.getContext(), "Request Accepted from requests", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(itemView.getContext(), "Failed to remove from sent requests", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(itemView.getContext(), "Failed to cancel request", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(v.getContext(), "Failed to add current tailor ID to Customer's CurrentTailors node: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(v.getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

class Request {
    private String Name;
    private String Address;
    private String Contactinfo;
    private String Email;
    private String requestId;

    public Request(String Name, String Address, String Contactinfo, String Email, String requestId) {
        this.Name = Name;
        this.Address = Address;
        this.Contactinfo = Contactinfo;
        this.Email = Email;
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return Address;
    }

    public String getContactinfo() {
        return Contactinfo;
    }

    public String getEmail() {
        return Email;
    }
}
