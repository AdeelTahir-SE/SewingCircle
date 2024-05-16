package com.adeeltahir.sewingcircle;

import android.content.Intent;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CardAdapter mCardAdapter;
    private List<Tailor> tailors;
    private List<TCard> list;

    String name ;
    String email;
    String address;
    String contactinfo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewCards);
        SearchView searchView = rootView.findViewById(R.id.searchView);

//        searchView.setQuery("Default Text", false);
        return rootView;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView I1 = view.findViewById(R.id.imgae);

         int i=0;
        tailors = new ArrayList<>();
        list= new ArrayList<>();
        mCardAdapter = new CardAdapter(list);

        mRecyclerView = view.findViewById(R.id.recyclerViewCards);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView .setHasFixedSize(true);
//        mCards = new ArrayList<>();
//        // Add some sample cards
//        mCards.add(new TCard(name,"email",address,contactinfo,email,I1));
//        mCards.add(new TCard("Jane Smith", "456 Elm St","123-456-7890","123-456-7890","123-456-7890",I1));
//        mCards.add(new TCard("Jane Smith", "456 Elm St","123-456-7890","123-456-7890","123-456-7890" ,I1));


//        mCardAdapter = new CardAdapter(mCards);

        // Set up RecyclerView with GridLayoutManager

        // Add some sample cards
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference().child("Tailor");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tailors.clear();
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Tailor tailor =dataSnapshot.getValue(Tailor.class);
                    tailors.add(tailor);
                    list.add(new TCard(tailor,I1));
                }
                mCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mRecyclerView.getContext(), "the data failed to load", Toast.LENGTH_SHORT).show();
            }
        });

//        mCards.add(new TCard(name,"email",address,contactinfo,email,I1));
//        mCards.add(new TCard("Jane Smith", "456 Elm St","123-456-7890","123-456-7890","123-456-7890",I1));
//        mCards.add(new TCard("Jane Smith", "456 Elm St","123-456-7890","123-456-7890","123-456-7890" ,I1));
        int spanCount = 2; // Number of columns in the grid
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        mRecyclerView.setAdapter(mCardAdapter);




        mRecyclerView.setAdapter(mCardAdapter);

    }

}

class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<TCard> mCards;

    public CardAdapter(List<TCard> cards) {
        this.mCards = cards;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        TCard card = mCards.get(position);
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
            Name = itemView.findViewById(R.id.textView1);
            Category = itemView.findViewById(R.id.textView2);
            Address = itemView.findViewById(R.id.textView3);
            Contactinfo = itemView.findViewById(R.id.textView4);
            Email = itemView.findViewById(R.id.textView5);
            ProfilePic =itemView.findViewById(R.id.image);
            HName=itemView.findViewById(R.id.HName);
            HAddress=itemView.findViewById(R.id.HAddress);
            HContactinfo=itemView.findViewById(R.id.HContactinfo);
            HEmail=itemView.findViewById(R.id.HEmail);
            HCategory=itemView.findViewById(R.id.HCategory);

        }

        public void bind(TCard card)
        {
            HName.setText("Name:");
            HAddress.setText("Address:");
            HContactinfo.setText("Contact Info:");
            HEmail.setText("Email:");
            HCategory.setText("Category:");

            Name.setText(card.getName());
            Category.setText(card.getCategory());
            Address.setText(card.getAddress());
            Contactinfo.setText(card.getContactinfo());
            Email.setText(card.getEmail());
            ProfilePic.setImageDrawable(card.getProfilePic().getDrawable());
        }
    }
}


 class TCard {
    private String Name;
    private String Category;
    private String Address;
    private String Contactinfo;
    private String Email;
    private ImageView ProfilePic;

    public TCard(String Name,String Category, String Address, String Contactinfo, String Email,ImageView ProfilePic) {
        this.Name = Name;
        this.Category = Category;
        this.Address = Address;
        this.Contactinfo = Contactinfo;
        this.Email = Email;
        this.ProfilePic=ProfilePic;
    }
    public TCard(Tailor tailor,ImageView I1)
    {
        this.Name = tailor.getName();
        this.Category = tailor.getCategory();
        this.Address = tailor.getAddress();
        this.Contactinfo = tailor.getContactInfo();
        this.Email = tailor.getEmail();
        this.ProfilePic=I1;
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
