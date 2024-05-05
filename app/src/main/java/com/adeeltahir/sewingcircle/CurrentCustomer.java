package com.adeeltahir.sewingcircle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class CurrentCustomer extends Fragment {

    private TextView headerTextView;
    private ListView messagesListView;
    private EditText messageInputEditText;
    private Button sendMessageButton;
    private ArrayAdapter<String> adapter;
    private List<String> messages = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_customer, container, false);

        headerTextView = view.findViewById(R.id.chat_header_text_view);
        messagesListView = view.findViewById(R.id.chat_messages_list_view);
        messageInputEditText = view.findViewById(R.id.message_input_edit_text);
        sendMessageButton = view.findViewById(R.id.send_message_button);

        // Set up ListView
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, messages);
        messagesListView.setAdapter(adapter);

        sendMessageButton.setOnClickListener(v -> {
            String message = messageInputEditText.getText().toString();
            if (!message.isEmpty()) {
                messages.add(message);
                adapter.notifyDataSetChanged();
                messageInputEditText.getText().clear();
                messagesListView.smoothScrollToPosition(adapter.getCount() - 1);
            }
        });

        return view;
    }
}
