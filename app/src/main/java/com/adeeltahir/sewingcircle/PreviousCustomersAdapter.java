package com.adeeltahir.sewingcircle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PreviousCustomersAdapter extends RecyclerView.Adapter<PreviousCustomersAdapter.PreviousCustomerViewHolder> {

    private List<PreviousCustomer> previousCustomers;

    public PreviousCustomersAdapter(List<PreviousCustomer> previousCustomers) {
        this.previousCustomers = previousCustomers;
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
        holder.bind(previousCustomer);
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

        public void bind(final PreviousCustomer previousCustomer) {
            textViewName.setText(previousCustomer.getName());
            textViewAddress.setText(previousCustomer.getAddress());
            textViewEmail.setText(previousCustomer.getEmail());
            textViewContactInfo.setText(previousCustomer.getContactInfo());

            currentClientButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle button click
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        // Reference to the current customer's node in the tailor's data
                        DatabaseReference currentCustomerRef = FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Tailor")
                                .child(user.getUid())
                                .child("CurrentCustomer");

                        // Set the value of the current customer's node to the selected customer's ID
                        currentCustomerRef.child(previousCustomer.getId()).setValue(true)
                                .addOnSuccessListener(aVoid -> Toast.makeText(v.getContext(), "Set current customer: " + previousCustomer.getName(), Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(v.getContext(), "Failed to set current customer", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(v.getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
                    }
                }
            });
                }
            }
        }


