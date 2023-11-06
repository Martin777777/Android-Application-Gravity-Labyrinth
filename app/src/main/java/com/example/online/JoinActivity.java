package com.example.online;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.entrance.SkinChosenActivity;
import com.example.game.MainActivity;
import com.example.game.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class JoinActivity extends AppCompatActivity {

    DatagramSocket socket = null;
    boolean isRunning;
    RecyclerView recyclerView;

    ArrayList<String[]> list;
    int startLevel;
    int endLevel;

    ImageView cancel;
    ImageView refresh;
    ImageView skin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_join);

        list = new ArrayList<>();

        recyclerView = this.findViewById(R.id.rec_room);

        RoomAdapter adapter = new RoomAdapter(this, list);
        RoomLayoutManager manager = new RoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        isRunning = true;
        startLevel = 1;
        endLevel = 1;

        cancel = this.findViewById(R.id.join_cancel);
        refresh = this.findViewById(R.id.join_refresh);
        skin = this.findViewById(R.id.join_skin);

        cancel.setOnClickListener(view -> this.finish());

        refresh.setOnClickListener(view -> {
            list.clear();
            adapter.notifyDataSetChanged();
            new Thread(() -> {
                if (socket != null) {
                    byte[] data = "0".getBytes(StandardCharsets.UTF_8);
                    try {
                        DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName("255.255.255.255"), 7777);
                        socket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        });

        skin.setOnClickListener(view -> {
            Intent intent = new Intent(JoinActivity.this, SkinChosenActivity.class);
            startActivity(intent);
        });

        new Thread(() -> {
            try {
                socket = new DatagramSocket(6666);
                adapter.setSocket(socket);
            } catch (SocketException e) {
                e.printStackTrace();
            }

            if (socket != null) {
                byte[] data = "0".getBytes(StandardCharsets.UTF_8);
                try {
                    DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName("255.255.255.255"), 7777);
                    socket.send(packet);
                    System.out.println("sent broadcast");
                    while (isRunning) {
                        byte[] receivedData = new byte[1024];
                        DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);

                        socket.receive(receivedPacket);
                        new Thread(() -> {
                            String information = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
                            InetAddress address = receivedPacket.getAddress();
                            int port = receivedPacket.getPort();

                            String[] contents = information.split("\\|\\|");
                            if (port == 7777) {
                                switch (Integer.parseInt(contents[0])){
                                    case 0:
                                        list.add(new String[]{address.getHostAddress(), contents[1], contents[2], contents[3]});
                                        JoinActivity.this.runOnUiThread(() -> adapter.notifyItemInserted(list.size() - 1));
                                        this.startLevel = Integer.parseInt(contents[1]);
                                        this.endLevel = Integer.parseInt(contents[2]);
                                        break;
                                    case 1:
                                        //open the game
                                        OnlineSelectionActivity.enemyName = contents[1];
                                        OnlineSelectionActivity.enemySkinID = Integer.parseInt(contents[2]);

                                        OnlineSelectionActivity.startLevel = startLevel;
                                        OnlineSelectionActivity.endLevel = endLevel;

                                        Intent intent = new Intent(JoinActivity.this, MainActivity.class);
                                        intent.putExtra("startLevel", startLevel);
                                        intent.putExtra("model", 4);
                                        intent.putExtra("endLevel", endLevel);
                                        intent.putExtra("address", receivedPacket.getAddress().getHostAddress());
                                        intent.putExtra("port", receivedPacket.getPort());
                                        startActivity(intent);

                                        break;
                                    case 2:
                                        Looper.prepare();
                                        Toast.makeText(JoinActivity.this, "Your are rejected!", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                        break;
                                }
                            }
                        }).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        isRunning = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
            socket = null;
        }
        list = null;
        super.onDestroy();
    }
}