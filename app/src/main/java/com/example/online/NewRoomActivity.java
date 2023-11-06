package com.example.online;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.WindowManager;

import com.example.entrance.EntranceActivity;
import com.example.entrance.SkinChosenActivity;
import com.example.game.MainActivity;
import com.example.game.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class NewRoomActivity extends AppCompatActivity {

    boolean isRunning;
    DatagramSocket server = null;

    int startLevel;
    int endLevel;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_new_room);

        intent = this.getIntent();
        startLevel = intent.getIntExtra("start", 1);
        endLevel = intent.getIntExtra("end", 1);


        isRunning = true;
        new Thread(() ->{
            try {
                server = new DatagramSocket(7777);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            DatagramPacket packet;
            byte[] data;

            while (isRunning) {
                data = new byte[1024];
                packet = new DatagramPacket(data, data.length);
                if (server != null) {
                    try {
                        System.out.println("opened");
                        server.receive(packet);
                        System.out.println("received");
                        DatagramPacket finalPacket = packet;
                        new Thread(() -> {
                            String information = new String(finalPacket.getData(), 0, finalPacket.getLength());
                            InetAddress clientAddress = finalPacket.getAddress();
                            int clientPort = finalPacket.getPort();

                            String[] contents = information.split("\\|\\|");

                            switch (Integer.parseInt(contents[0])) {
                                //looking for rooms
                                case 0:
                                    byte[] returnData = ("0||"+startLevel+"||"+endLevel+"||"+ (EntranceActivity.isLogin?EntranceActivity.username:"Anonymous")).getBytes(StandardCharsets.UTF_8);
                                    DatagramPacket returnPacket = new DatagramPacket(returnData, returnData.length, clientAddress, clientPort);
                                    try {
                                        server.send(returnPacket);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                //want to connect
                                case 1:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(NewRoomActivity.this);
                                    Looper.prepare();
                                    builder.setTitle(contents[1] + " wants to play with you")
                                            .setPositiveButton("Accept", (dialogInterface, i) -> {

                                                //send the accept message
                                                byte[] acceptData = ("1||"+(EntranceActivity.isLogin?EntranceActivity.username:"Anonymous") + "||" + SkinChosenActivity.skinID).getBytes(StandardCharsets.UTF_8);
                                                DatagramPacket acceptPacket = new DatagramPacket(acceptData, acceptData.length, clientAddress, clientPort);
                                                try {
                                                    server.send(acceptPacket);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                OnlineSelectionActivity.enemyName = contents[1];
                                                OnlineSelectionActivity.enemySkinID = Integer.parseInt(contents[2]);

                                                OnlineSelectionActivity.startLevel = startLevel;
                                                OnlineSelectionActivity.endLevel = endLevel;

                                                //open the game
                                                Intent intent = new Intent(NewRoomActivity.this, MainActivity.class);
                                                intent.putExtra("startLevel", startLevel);
                                                intent.putExtra("endLevel", endLevel);
                                                intent.putExtra("model", 4);
                                                intent.putExtra("address", clientAddress.getHostAddress());
                                                intent.putExtra("port", clientPort);
                                                startActivity(intent);

                                            }).setNegativeButton("Reject", (dialogInterface, i) -> {
                                                byte[] rejectData = "2".getBytes(StandardCharsets.UTF_8);
                                                DatagramPacket rejectPacket = new DatagramPacket(rejectData, rejectData.length, clientAddress, clientPort);
                                                try {
                                                    server.send(rejectPacket);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }).show();
                                    Looper.loop();
                                    break;

                            }
                        }).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        isRunning = false;
        if (server != null && !server.isClosed()) {
            server.close();
            server = null;
        }
        super.onDestroy();
    }
}