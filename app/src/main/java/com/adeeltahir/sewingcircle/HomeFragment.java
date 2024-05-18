package com.adeeltahir.sewingcircle;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CardAdapter mCardAdapter;
    private List<Tailor> tailors;

    private DatabaseReference databaseRef;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewCards);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        tailors = new ArrayList<>();
        mCardAdapter = new CardAdapter(tailors);
        mRecyclerView.setAdapter(mCardAdapter);

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Tailor");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        loadTailorsAndImages();
    }

    private void loadTailorsAndImages() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tailors.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Tailor tailor = dataSnapshot.getValue(Tailor.class);
                    if (tailor != null) {
                        tailor.setImageUrl(tailor.getImageUrl() != null ? tailor.getImageUrl() : "https://ibb.co/tX1fJSF");
                        tailors.add(tailor);
                    }
                }
                mCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<Tailor> mTailors;

    public CardAdapter(List<Tailor> tailors) {
        this.mTailors = tailors;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.bind(mTailors.get(position));
    }

    @Override
    public int getItemCount() {
        return mTailors.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameTextView, mCategoryTextView, mAddressTextView, mContactInfoTextView, mEmailTextView;
        private ImageView mProfilePicImageView;
        private TextView HName;
        private TextView HAddress;
        private TextView HContactinfo;
        private TextView HEmail;
        private  TextView HCategory;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.textView1);
            mCategoryTextView = itemView.findViewById(R.id.textView2);
            mAddressTextView = itemView.findViewById(R.id.textView3);
            mContactInfoTextView = itemView.findViewById(R.id.textView4);
            mEmailTextView = itemView.findViewById(R.id.textView5);
            mProfilePicImageView = itemView.findViewById(R.id.image);

            HName=itemView.findViewById(R.id.HName);
            HAddress=itemView.findViewById(R.id.HAddress);
            HContactinfo=itemView.findViewById(R.id.HContactinfo);
            HEmail=itemView.findViewById(R.id.HEmail);
            HCategory=itemView.findViewById(R.id.HCategory);
        }

        public void bind(Tailor tailor) {
            mNameTextView.setText(tailor.getName());
            mCategoryTextView.setText(tailor.getCategory());
            mAddressTextView.setText(tailor.getAddress());
            mContactInfoTextView.setText(tailor.getContactInfo());
            mEmailTextView.setText(tailor.getEmail());
            HName.setText("Name:");
            HAddress.setText("Address:");
            HContactinfo.setText("Contact Info:");
            HEmail.setText("Email:");
            HCategory.setText("Category:");

            Glide.with(itemView.getContext())
                    .load(tailor.getImageUrl())
                    .placeholder(R.drawable.customer)
                    .error(R.drawable.customer)

                    .into(mProfilePicImageView);


        }
    }
}



