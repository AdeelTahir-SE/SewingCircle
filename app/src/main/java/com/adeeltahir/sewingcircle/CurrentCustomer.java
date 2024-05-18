package com.adeeltahir.sewingcircle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CurrentCustomer extends Fragment {

    private TextView headerTextView;
    private String senderId, receiverId;
    private DatabaseReference myRef;
    private ListView messagesListView;
    private EditText messageInputEditText;
    private Button sendMessageButton;
    private CustomAdapter adapter;
    private List<String> messages = new ArrayList<>();
    private DatabaseReference tailorRef;
    private DatabaseReference customerRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_customer, container, false);

        headerTextView = view.findViewById(R.id.chat_header_text_view);
        messagesListView = view.findViewById(R.id.chat_messages_list_view);
        messageInputEditText = view.findViewById(R.id.message_input_edit_text);
        sendMessageButton = view.findViewById(R.id.send_message_button);

        // Set up ListView with custom adapter
        adapter = new CustomAdapter(messages);
        messagesListView.setAdapter(adapter);

        // Initialize Firebase references
        myRef = FirebaseDatabase.getInstance().getReference().child("Chats");
        senderId = FirebaseAuth.getInstance().getUid();

        // Set up message sending
        sendMessageButton.setOnClickListener(v -> {
            String message = messageInputEditText.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
            }
        });

        // Fetch currentCustomerId from Tailor node
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String tailorId = user.getUid();
            tailorRef = FirebaseDatabase.getInstance().getReference().child("Tailor").child(tailorId);
            tailorRef.child("CurrentCustomer").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String currentCustomerId = dataSnapshot.getValue(String.class);
                    receiverId = currentCustomerId;
                    if (currentCustomerId != null) {
                        // Fetch customer name from Customer node
                        customerRef = FirebaseDatabase.getInstance().getReference().child("Customer").child(currentCustomerId);
                        customerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String customerName = snapshot.child("name").getValue(String.class);
                                if (customerName != null) {
                                    headerTextView.setText(customerName);
                                    loadMessages();
                                } else {
                                    headerTextView.setText("Unknown Customer");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("CurrentCustomer", "Error fetching customer name: " + error.getMessage());
                                headerTextView.setText("Error Loading Customer");
                            }
                        });
                    } else {
                        headerTextView.setText("No Current Customer");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("CurrentCustomer", "Error fetching currentCustomerId: " + databaseError.getMessage());
                    headerTextView.setText("Error Loading Customer");
                }
            });
        }

        return view;
    }

    private void sendMessage(String message) {
        String chatId = receiverId + senderId; // Reverse the order for customer-tailor chat ID
        DatabaseReference newRef = myRef.child(chatId).push();
        newRef.setValue("Tailor: " + message).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                messageInputEditText.getText().clear();
                messagesListView.smoothScrollToPosition(messages.size() - 1);
            } else {
                Toast.makeText(getContext(), "Failed to send message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMessages() {
        String chatId = receiverId+senderId  ;
        DatabaseReference chatRef = myRef.child(chatId);

        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String message = snapshot.getValue(String.class);
                messages.add(message);
                adapter.notifyDataSetChanged();
                messagesListView.smoothScrollToPosition(messages.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CurrentCustomer", "Failed to load messages: " + error.getMessage());
            }
        });
    }

    // Custom Adapter for ListView
    private class CustomAdapter extends ArrayAdapter<String> {

        private List<String> messages;

        public CustomAdapter(List<String> messages) {
            super(requireContext(), R.layout.item_message, messages);
            this.messages = messages;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.messageTextView = convertView.findViewById(R.id.message_text_view);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            String message = messages.get(position);
            viewHolder.messageTextView.setText(message);

            return convertView;
        }

        private class ViewHolder {
            TextView messageTextView;
        }
    }
}
