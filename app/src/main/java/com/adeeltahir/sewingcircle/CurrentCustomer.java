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

public class CurrentCustomer extends Fragment  {

    private TextView headerTextView;
    private ListView messagesListView;
    private EditText messageInputEditText;
    private Button sendMessageButton;
    private CustomAdapter adapter;
    private List<String> messages = new ArrayList<>();

    Bundle bundle;

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

        sendMessageButton.setOnClickListener(v -> {
            String message = messageInputEditText.getText().toString();
            if (!message.isEmpty()) {
                messages.add(message);
                adapter.notifyDataSetChanged();
                messageInputEditText.getText().clear();
                messagesListView.smoothScrollToPosition(messages.size() - 1);
            }
        });

        return view;

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

