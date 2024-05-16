package com.adeeltahir.sewingcircle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    ImageView I1;

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
         I1 = view.findViewById(R.id.imge);
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
                    tailors.add(tailor);
//                    list.add(new TCardCustomer(tailor));

                    list.add(new TCardCustomer(tailor,I1));
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

    private List<TCardCustomer> mCards;

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
        private  TextView HCategory;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.textViewNameCustomer);
            Category = itemView.findViewById(R.id.textViewCategoryCustomer);
            Address = itemView.findViewById(R.id.textViewAddressCustomer);
            Contactinfo = itemView.findViewById(R.id.textViewContactInfoCustomer);
            Email = itemView.findViewById(R.id.textViewEmailCustomer);
            ProfilePic=itemView.findViewById(R.id.image1);
            HName=itemView.findViewById(R.id.HName);
            HAddress=itemView.findViewById(R.id.HAddress);
            HContactinfo=itemView.findViewById(R.id.HContactinfo);
            HEmail=itemView.findViewById(R.id.HEmail);
            HCategory=itemView.findViewById(R.id.HCategory);

        }

        public void bind(TCardCustomer card) {
            Name.setText(card.getName());
            Category.setText(card.getCategory());
            Address.setText(card.getAddress());
            Contactinfo.setText(card.getContactinfo());
            Email.setText(card.getEmail());
            ProfilePic.setImageDrawable(card.getProfilePic().getDrawable());
            HName.setText("Name:");
            HAddress.setText("Address:");
            HContactinfo.setText("Contact Info:");
            HEmail.setText("Email:");
            HCategory.setText("Category:");
        }
    }
}

class TCardCustomer {
    private String Name;
    private String Category;
    private String Address;
    private String Contactinfo;
    private String Email;
    private ImageView ProfilePic;

    public TCardCustomer(Tailor tailor, ImageView profilePic) {
//    public TCardCustomer(Tailor tailor) {
        this.Name = tailor.getName();
        this.Category = tailor.getCategory();
        this.Address = tailor.getAddress();
        this.Contactinfo = tailor.getContactInfo();
        this.Email = tailor.getEmail();
        this.ProfilePic=profilePic;
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
    public ImageView getProfilePic() {
        return ProfilePic;
    }
    public String getEmail() {
        return Email;
    }
}
