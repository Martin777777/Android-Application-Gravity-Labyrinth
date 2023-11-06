package com.example.online;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entrance.EntranceActivity;
import com.example.entrance.SkinChosenActivity;
import com.example.game.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {

    ArrayList<String[]> list;
    Context context;
    DatagramSocket socket;

    public RoomAdapter(Context context, ArrayList<String[]> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] strings = list.get(position);

        holder.button.setText(strings[3] + "\n" + "Levels: " + strings[1] + " - " + strings[2]);
        holder.button.setOnClickListener(view -> {
            if (socket != null && !socket.isClosed()) {
                new Thread(() -> {
                    byte[] sendData = ("1||"+(EntranceActivity.isLogin?EntranceActivity.username:"Anonymous") + "||" + SkinChosenActivity.skinID).getBytes(StandardCharsets.UTF_8);
                    DatagramPacket sendPacket = null;
                    try {
                        sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(strings[0]), 7777);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    System.out.println("clicked");
                    try {
                        if (sendPacket != null){
                            socket.send(sendPacket);
                            System.out.println("sent");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            else {
                System.out.println("null");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.button = itemView.findViewById(R.id.btn_room);
        }
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }
}
