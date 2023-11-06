package com.example.community.ui.chatting_room;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.entrance.EntranceActivity;
import com.example.game.MyThread;
import com.example.game.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;


public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    EditText editText;
    Button send;
    RecyclerView recyclerView;
    ArrayList<ChatMessage> messages;
    ChatAdapter chatAdapter;
    Handler handler;
    boolean isSent;
    boolean shown;
    boolean loaded;

    boolean isRunning;

    public ChatFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatting_room, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.editText = view.findViewById(R.id.edit_message);
        this.send = view.findViewById(R.id.btn_send);
        this.recyclerView = view.findViewById(R.id.message_recycler);

        this.isSent = false;
        this.loaded = false;
        this.shown = false;

        messages = new ArrayList<>();

        chatAdapter = new ChatAdapter(messages);
        ChattingLayoutManager linearLayoutManager = new ChattingLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatAdapter);


        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                chatAdapter.notifyItemInserted(messages.size()-1);
                if (isSent) {
                    recyclerView.scrollToPosition(messages.size() - 1);
                    isSent = false;
                }
            }
        };

        if (!EntranceActivity.isLogin) {
            editText.setEnabled(false);
            editText.setText("Login required");
            send.setClickable(false);
        }
        else {
            send.setOnClickListener(view1 -> {
                String typedString = editText.getText().toString();
                if (!typedString.equals("")) {
                    new Thread(() -> {
                        Connection connection = null;
                        PreparedStatement statement;

                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            connection = DriverManager.getConnection("jdbc:mysql://bj-cdb-he3zrqqs.sql.tencentcdb.com:60342/game_labyrinth?useSSL=false",
                                    "root", "132465798Mm");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            String insertSql = "insert into chatting_room (username,message,time) values (?,?,?)";


                            if (connection != null) {

                                statement = connection.prepareStatement(insertSql);

                                connection.setAutoCommit(false);

                                statement.setString(1, EntranceActivity.username);
                                statement.setString(2, typedString);
                                statement.setLong(3, System.currentTimeMillis());

                                statement.addBatch();
                                statement.executeBatch();

                                connection.commit();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }).start();
                    editText.setText("");
                    isSent  = true;
                }
            });
        }

        new Thread(() -> {
            Connection connection = null;
            PreparedStatement statement;

            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://bj-cdb-he3zrqqs.sql.tencentcdb.com:60342/game_labyrinth?useSSL=false",
                        "root", "132465798Mm");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                String sql = "select username,message,time from chatting_room";


                if (connection != null) {

                    statement = connection.prepareStatement(sql);

                    connection.setAutoCommit(false);

                    ResultSet resultSet = statement.executeQuery();


                    while (resultSet.next()) {

                        messages.add(new ChatMessage(resultSet.getString("message"),
                                resultSet.getString("username").equals(EntranceActivity.username) ? 1 : 0,
                                resultSet.getLong("time"), resultSet.getString("username")));
                    }
                    System.out.println("hello");

//                    handler.sendEmptyMessage(0);

                    ((Activity)getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("hi");
                            chatAdapter.notifyItemInserted(messages.size()-1);
                            recyclerView.scrollToPosition(messages.size() - 1);
                            isSent = false;
                            shown = true;
                        }
                    });

                    connection.commit();
                    resultSet.close();
                    statement.close();


                    System.out.println("here");
                    loaded = true;


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        while (!loaded) {
            System.out.println("loading");
        }

        this.isRunning = true;
        new MyThread(() -> {

            while (isRunning) {

                long startTime = System.currentTimeMillis();

                Connection connection = null;
                PreparedStatement statement;
                System.out.println("running");

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    connection = DriverManager.getConnection("jdbc:mysql://bj-cdb-he3zrqqs.sql.tencentcdb.com:60342/game_labyrinth?useSSL=false",
                            "root", "132465798Mm");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    String sql = "select username,message,time from chatting_room";


                    if (connection != null) {

                        statement = connection.prepareStatement(sql);

                        connection.setAutoCommit(false);

                        ResultSet resultSet = statement.executeQuery();

//                        Message removeMessage = new Message();
//                        Message addMessage = new Message();

//                        int size = messages.size();
//                        messages.clear();
//                        removeMessage.arg1 = 0;
//                        removeMessage.arg2 = size;
//                        handler.sendMessage(removeMessage);
                        ArrayList<ChatMessage> chatMessages = new ArrayList<>();

                        while (resultSet.next()) {
                            chatMessages.add(new ChatMessage(resultSet.getString("message"),
                                    resultSet.getString("username").equals(EntranceActivity.username) ? 1 : 0,
                                    resultSet.getLong("time"), resultSet.getString("username")));
                        }
                        if (!(chatMessages.size() == messages.size())) {
//                        addMessage.arg1 = 1;
//                        addMessage.arg2 = messages.size();
                            System.out.println(chatMessages.size() + " " + messages.size());
                            messages.addAll(chatMessages.subList(messages.size(), chatMessages.size()));
                            System.out.println(chatMessages.size() + " " + messages.size());
                            ((Activity)getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    chatAdapter.notifyItemInserted(messages.size()-1);
                                    if (isSent) {
                                        recyclerView.scrollToPosition(messages.size()-1);
                                    }
                                    isSent = false;
                                }
                            });
                        }

                        connection.commit();
                        resultSet.close();
                        statement.close();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                long endTime = System.currentTimeMillis();

                int diffTime = (int) (endTime - startTime);

                while (diffTime <= 2000) {
                    diffTime = (int) (System.currentTimeMillis() - startTime);
                    Thread.yield();
                }

            }
        }).start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        recyclerView = null;
        System.out.println("destroy");
    }
}