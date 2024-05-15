package com.adeeltahir.sewingcircle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        public PreviousCustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
        }

        public void bind(PreviousCustomer previousCustomer) {
            textViewName.setText(previousCustomer.getName());
            textViewAddress.setText(previousCustomer.getAddress());
        }
    }
}