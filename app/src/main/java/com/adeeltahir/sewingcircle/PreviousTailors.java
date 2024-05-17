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

import java.util.ArrayList;
import java.util.List;

public class PreviousTailors extends Fragment {

    private RecyclerView recyclerViewPreviousTailors;
    private PreviousTailorsAdapter previousTailorsAdapter;
    private List<PreviousTailor> previousTailors;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_previous_tailors, container, false);

        // Initialize RecyclerView
        recyclerViewPreviousTailors = view.findViewById(R.id.recyclerViewPreviousTailors);
        recyclerViewPreviousTailors.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPreviousTailors.setHasFixedSize(true);

        // Initialize data
        previousTailors = new ArrayList<>();
        // Add some sample previous tailors
        previousTailors.add(new PreviousTailor("John Doe", "123 Main St"));
        previousTailors.add(new PreviousTailor("Jane Smith", "456 Elm St"));
        previousTailors.add(new PreviousTailor("Sam Wilson", "789 Oak St"));

        // Initialize adapter
        previousTailorsAdapter = new PreviousTailorsAdapter(previousTailors);

        // Set adapter to RecyclerView
        recyclerViewPreviousTailors.setAdapter(previousTailorsAdapter);

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

        public PreviousTailorViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
        }

        public void bind(PreviousTailor previousTailor) {
            textViewName.setText(previousTailor.getName());
            textViewAddress.setText(previousTailor.getAddress());
        }
    }
}
class PreviousTailor {
    private String name;
    private String address;

    public PreviousTailor(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}