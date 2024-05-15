package com.adeeltahir.sewingcircle;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class Requests extends Fragment  {

    private RecyclerView recyclerViewRequests;
    private RequestsAdapter requestsAdapter;
    private List<Request> Requests;


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
        // Add some sample previous customers
        Requests.add(new Request("John Doe", "123 Main St", "555-1234", "john.doe@example.com"));
        Requests.add(new Request("Jane Smith", "456 Elm St", "555-5678", "jane.smith@example.com"));
        Requests.add(new Request("Mike Johnson", "789 Oak St", "555-9012", "mike.johnson@example.com"));
        Requests.add(new Request("Emily Davis", "101 Pine St", "555-3456", "emily.davis@example.com"));
        Requests.add(new Request("David Wilson", "202 Maple St", "555-7890", "david.wilson@example.com"));
        Requests.add(new Request("Sophia Brown", "303 Birch St", "555-2345", "sophia.brown@example.com"));
        Requests.add(new Request("Liam Martin", "404 Cedar St", "555-6789", "liam.martin@example.com"));
        Requests.add(new Request("Olivia Garcia", "505 Walnut St", "555-1235", "olivia.garcia@example.com"));
        Requests.add(new Request("Noah Martinez", "606 Chestnut St", "555-5679", "noah.martinez@example.com"));
        Requests.add(new Request("Ava Rodriguez", "707 Spruce St", "555-9013", "ava.rodriguez@example.com"));



        // Initialize adapter
        requestsAdapter = new RequestsAdapter(Requests);

        // Set adapter to RecyclerView
        recyclerViewRequests.setAdapter(requestsAdapter);

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

