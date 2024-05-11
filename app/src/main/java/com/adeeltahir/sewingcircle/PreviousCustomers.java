package com.adeeltahir.sewingcircle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adeeltahir.sewingcircle.PreviousCustomer;
import com.adeeltahir.sewingcircle.PreviousCustomersAdapter;
import com.adeeltahir.sewingcircle.R;

import java.util.ArrayList;
import java.util.List;

public class PreviousCustomers extends Fragment {

    private RecyclerView recyclerViewPreviousCustomers;
    private PreviousCustomersAdapter previousCustomersAdapter;
    private List<PreviousCustomer> previousCustomers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_previous_customers, container, false);

        // Initialize RecyclerView
        recyclerViewPreviousCustomers = view.findViewById(R.id.recyclerViewPreviousCustomers);
        recyclerViewPreviousCustomers.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPreviousCustomers.setHasFixedSize(true);

        // Initialize data
        previousCustomers = new ArrayList<>();
        // Add some sample previous customers
        previousCustomers.add(new PreviousCustomer("John Doe", "123 Main St"));
        previousCustomers.add(new PreviousCustomer("Jane Smith", "456 Elm St"));
        previousCustomers.add(new PreviousCustomer("Jane Smith", "456 Elm St"));

        // Initialize adapter
        previousCustomersAdapter = new PreviousCustomersAdapter(previousCustomers);

        // Set adapter to RecyclerView
        recyclerViewPreviousCustomers.setAdapter(previousCustomersAdapter);

        return view;
    }
}
