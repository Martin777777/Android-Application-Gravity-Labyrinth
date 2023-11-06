package com.example.community.ui.chatting_room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.game.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private ArrayList<ChatMessage> messages;
    SimpleDateFormat simpleDateFormat;

    public ChatAdapter(ArrayList<ChatMessage> messages) {
        this.messages = messages;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" , Locale.CHINA);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        String information = simpleDateFormat.format(message.getTime()) + " " + message.getUsername() + ":";
        if (message.getType() == 0) {
//            other message
            holder.otherLayout.setVisibility(View.VISIBLE);
            holder.senderLayout.setVisibility(View.GONE);
            holder.otherMessage.setText(message.getContent());
            holder.otherUsername.setText(information);
        }
        else if (message.getType() == 1) {
//            sender message
            holder.senderLayout.setVisibility(View.VISIBLE);
            holder.otherLayout.setVisibility(View.GONE);
            holder.senderMessage.setText(message.getContent());
            holder.senderUsername.setText(information);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout otherLayout;
        LinearLayout senderLayout;

        TextView otherMessage;
        TextView senderMessage;

        TextView otherUsername;
        TextView senderUsername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.otherLayout = itemView.findViewById(R.id.other_message_layout);
            this.senderLayout = itemView.findViewById(R.id.sender_message_layout);
            this.otherMessage = itemView.findViewById(R.id.other_message);
            this.senderMessage = itemView.findViewById(R.id.sender_message);
            this.otherUsername = itemView.findViewById(R.id.other_username);
            this.senderUsername = itemView.findViewById(R.id.sender_username);
        }
    }
}
