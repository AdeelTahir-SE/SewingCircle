package com.adeeltahir.sewingcircle;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private DatabaseReference mDatabaseReference;
    private RecyclerView mRecyclerView;
    private CardAdapter mCardAdapter;
    private List<String> mCardList;

    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = rootView.findViewById(R.id.gridLayout);


        // Set up the SearchView listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search operation here
                //find the person
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Perform search operation as text changes
                //give suggestions
                performSearch(newText);
                return true;
            }
        });

        return rootView;
    }
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            mCardList = new ArrayList<>();
            mCardAdapter = new CardAdapter(mCardList);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            mRecyclerView.setAdapter(mCardAdapter);

            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("your_firebase_node");
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String cardText = snapshot.getValue(String.class);
                        mCardList.add(cardText);
                    }
                    mCardAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }




    private void performSearch(String query) {
        // Implement your search logic here
        // You can filter data, update UI, etc. based on the search query
    }


}
class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<String> mData;

    public CardAdapter(List<String> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        String cardText = mData.get(position);
        holder.bind(cardText);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textView);
        }

        public void bind(String text) {
            mTextView.setText(text);
        }
    }
}