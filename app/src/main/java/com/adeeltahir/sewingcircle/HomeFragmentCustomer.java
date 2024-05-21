package com.adeeltahir.sewingcircle;

import static android.content.ContentValues.TAG;

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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

public class HomeFragmentCustomer extends Fragment {


    private RecyclerView mRecyclerView;
    private CardAdapterCustomer mCardAdapter;
    private List<Tailor> tailors;
    private List<TCardCustomer> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_customer, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewCardsCustomer);
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        tailors = new ArrayList<>();
        list = new ArrayList<>();
        mCardAdapter = new CardAdapterCustomer(list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Tailor");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tailors.clear();
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Tailor tailor = dataSnapshot.getValue(Tailor.class);
                    if (tailor != null) {
                        String imageUrl = tailor.getImageUrl() != null ? tailor.getImageUrl() : "https://ibb.co/tX1fJSF";

                        list.add(new TCardCustomer(tailor, imageUrl, dataSnapshot.getKey()));
                    }
                }
                mCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mRecyclerView.getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();

            }
        });

        mRecyclerView.setAdapter(mCardAdapter);
    }
}


class CardAdapterCustomer extends RecyclerView.Adapter<CardAdapterCustomer.CardViewHolder> {

    private static List<TCardCustomer> mCards;

    public CardAdapterCustomer(List<TCardCustomer> cards) {
        this.mCards = cards;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_customer, parent, false);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        TCardCustomer card = mCards.get(position);
        holder.bind(card);
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        FirebaseAuth auth;
        FirebaseUser user;

        private TextView Name;
        private TextView Category;
        private TextView Address;
        private TextView Contactinfo;
        private TextView Email;
        private ImageView ProfilePic;
        private TextView HName;
        private TextView HAddress;
        private TextView HContactinfo;
        private TextView HEmail;
        private TextView HCategory;
        private Button requestButton;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.textViewNameCustomer);
            Category = itemView.findViewById(R.id.textViewCategoryCustomer);
            Address = itemView.findViewById(R.id.textViewAddressCustomer);
            Contactinfo = itemView.findViewById(R.id.textViewContactInfoCustomer);
            Email = itemView.findViewById(R.id.textViewEmailCustomer);
            ProfilePic = itemView.findViewById(R.id.image1);
            HName = itemView.findViewById(R.id.HName);
            HAddress = itemView.findViewById(R.id.HAddress);
            HContactinfo = itemView.findViewById(R.id.HContactinfo);
            HEmail = itemView.findViewById(R.id.HEmail);
            HCategory = itemView.findViewById(R.id.HCategory);
            requestButton = itemView.findViewById(R.id.buttonAccept);
            requestButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    TCardCustomer currentCard = mCards.get(position);
                    handleRequestButtonClick(currentCard);
                }
            });

        }

        public void bind(TCardCustomer card) {
            Name.setText(card.getName());
            Category.setText(card.getCategory());
            Address.setText(card.getAddress());
            Contactinfo.setText(card.getContactinfo());
            Email.setText(card.getEmail());
            HName.setText("Name:");
            HAddress.setText("Address:");
            HContactinfo.setText("Contact Info:");
            HEmail.setText("Email:");
            HCategory.setText("Category:");

            Glide.with(itemView.getContext())
                    .load(card.getProfilePic())
                    .placeholder(R.drawable.customer)
                    .error(R.drawable.customer)
                    .into(ProfilePic);
        }

        private void handleRequestButtonClick(TCardCustomer card) {
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();

            if (user != null) {
                DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference().child("Tailor").child(card.getKey()).child("requests");
                DatabaseReference sentreqsref = FirebaseDatabase.getInstance().getReference().child("Customer").child(user.getUid()).child("sentRequests");

                // Push the current user's ID to the tailor's requests node
                requestsRef.child(user.getUid()).setValue(true)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Push the tailor's ID to the current user's sent requests node
                                sentreqsref.child(card.getKey()).setValue(true)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Toast indicating successful request
                                                Toast.makeText(itemView.getContext(), "Request sent for: " + card.getName(), Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Toast indicating failure to add to sent requests
                                                Toast.makeText(itemView.getContext(), "Failed to add to sent requests", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Toast indicating failure to send request
                                Toast.makeText(itemView.getContext(), "Failed to send request", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }

    }
}

class TCardCustomer {
    private String Name;
    private String Category;
    private String Address;
    private String Contactinfo;
    private String Email;
    String  ProfilePic;
    String key;

    public TCardCustomer(Tailor tailor, String profilePic,String key) {
//    public TCardCustomer(Tailor tailor) {
        this.Name = tailor.getName();
        this.Category = tailor.getCategory();
        this.Address = tailor.getAddress();
        this.Contactinfo = tailor.getContactInfo();
        this.Email = tailor.getEmail();
        this.ProfilePic=profilePic;
        this.key=key;
    }

    public String getName() {
        return Name;
    }

    public String getCategory() {
        return Category;
    }

    public String getAddress() {
        return Address;
    }

    public String getContactinfo() {
        return Contactinfo;
    }
    public String getProfilePic() {
        return ProfilePic;
    }
    public String getEmail() {
        return Email;
    }
    public String getKey(){
        return key;
    }
}
