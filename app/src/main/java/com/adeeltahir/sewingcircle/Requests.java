package com.adeeltahir.sewingcircle;

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
    private List<Request> Requests;
    private List<Request> requestsList; // Initialize this list
    private DatabaseReference requestsRef;
    private DatabaseReference customerRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        // Initialize RecyclerView
        recyclerViewRequests = view.findViewById(R.id.recyclerViewReqs);
        recyclerViewRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewRequests.setHasFixedSize(true);

        // Initialize data
        Requests = new ArrayList<>();
        requestsList = new ArrayList<>(); // Initialize requestsList

        // Initialize adapter
        requestsAdapter = new RequestsAdapter(Requests);

        // Set adapter to RecyclerView
        recyclerViewRequests.setAdapter(requestsAdapter);

        // Add some sample previous customers
        // Initialize the database reference
        customerRef = FirebaseDatabase.getInstance().getReference().child("Customers");
        requestsRef = FirebaseDatabase.getInstance().getReference().child("Tailors").child("requests");

        // Add ValueEventListener to fetch requests from the database
        requestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestsList.clear(); // Clear the list before adding new requests

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String requestId = snapshot.getKey(); // Get the request ID

                    // Fetch customer details for the request ID
                    customerRef.child(requestId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot customerSnapshot) {
                            if (customerSnapshot.exists()) {
                                // Extract customer details
                                String name = customerSnapshot.child("name").getValue(String.class);
                                String address = customerSnapshot.child("address").getValue(String.class);
                                String contactInfo = customerSnapshot.child("contactInfo").getValue(String.class);
                                String email = customerSnapshot.child("email").getValue(String.class);

                                // Create a Request object and add it to the list
                                Request request = new Request(name, address, contactInfo, email);
                                requestsList.add(request);

                                // Notify the adapter that the data set has changed
                                requestsAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle onCancelled event
                            Log.e("RequestsFragment", "Error fetching customer details: " + databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled event
                Log.e("RequestsFragment", "Error fetching requests: " + databaseError.getMessage());
            }
        });

        return view;
    }
}

class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ReqsCustomerViewHolder> {


    private static List<Request> Reqslist;

    public RequestsAdapter(List<Request> reqs) {
        this.Reqslist = reqs;
    }



    @NonNull
    @Override
    public ReqsCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemrequests, parent, false);
        return new ReqsCustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReqsCustomerViewHolder holder, int position) {
        Request r1 = Reqslist.get(position);
        holder.bind(r1);
    }
    @Override
    public int getItemCount() {
        return Reqslist.size();
    }

    public static class ReqsCustomerViewHolder extends RecyclerView.ViewHolder {
        Bundle bundle;
        private TextView Name;
        private TextView Address;
        private TextView Contactinfo;
        private TextView Email;
        private Button buttonAccept;
        public ReqsCustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.Namereq);
            Address = itemView.findViewById(R.id.textViewAddress);
            Contactinfo = itemView.findViewById(R.id.Contactinforeq);
            Email = itemView.findViewById(R.id.Emailreq);
            buttonAccept = itemView.findViewById(R.id.buttonAccept);

        }


        public void bind(Request r) {
            Name.setText(r.getName());
            Address.setText(r.getAddress());
            Contactinfo.setText(r.getContactinfo());
            Email.setText(r.getEmail());

            buttonAccept.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), "Accepted the request", Toast.LENGTH_SHORT).show();
            });
        }
    }
}


class Request {
    private String Name;

    private String Address;
    private String Contactinfo;
    private String Email;


    public Request(String Name , String Address, String Contactinfo, String Email) {
        this.Name = Name;

        this.Address = Address;
        this.Contactinfo = Contactinfo;
        this.Email = Email;

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

