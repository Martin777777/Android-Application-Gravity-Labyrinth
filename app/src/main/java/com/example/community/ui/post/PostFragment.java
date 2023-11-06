package com.example.community.ui.post;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.community.ui.chatting_room.ChatMessage;
import com.example.entrance.EntranceActivity;
import com.example.game.R;
import com.example.user.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Objects;


public class PostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FloatingActionButton publishPost;
    Connection connection;
    PreparedStatement statement;

    ArrayList<Post> posts;
    PostAdapter adapter;
    RecyclerView recyclerView;

    SwipeRefreshLayout refreshLayout;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragmentA.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
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
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        publishPost = view.findViewById(R.id.write_post);
        publishPost.setOnClickListener(view1 -> {
            if (EntranceActivity.isLogin){
                Intent intent = new Intent(getContext(), PostPublishActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(getContext(), "Please login first!", Toast.LENGTH_SHORT).show();
            }
        });

        refreshLayout = view.findViewById(R.id.post_refresh);

        posts = new ArrayList<>();
        recyclerView = view.findViewById(R.id.posts_recycler);
        adapter = new PostAdapter(posts, (Activity) getContext());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        refreshLayout.setColorSchemeColors(Color.CYAN);
        refreshLayout.post(() -> refreshLayout.setRefreshing(true));
        refresh();

        refreshLayout.setOnRefreshListener(this::refresh);

    }

    private void refresh() {
        new Thread(() -> {
            try {
                System.out.println("before connect");
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://bj-cdb-he3zrqqs.sql.tencentcdb.com:60342/game_labyrinth?useSSL=false",
                        "root", "132465798Mm");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                posts.clear();
                String sql = "select cover,title,username,body,time from post";
                if (connection != null) {

                    statement = connection.prepareStatement(sql);

                    connection.setAutoCommit(false);

                    ResultSet resultSet = statement.executeQuery();//创建数据对象

                    while (resultSet.next()) {

                        posts.add(new Post(getBitmap(resultSet.getBlob("cover")),
                                resultSet.getString("title"),
                                resultSet.getString("username"),
                                resultSet.getString("body"), resultSet.getLong("time")));
                    }

                    connection.commit();
                    resultSet.close();
                    statement.close();

                    ((Activity) requireContext()).runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private Bitmap getBitmap(Blob blob) {
        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(blob.getBinaryStream());
            byte[] bytes = new byte[(int) blob.length()];
            int len = bytes.length;
            int offset = 0;
            int read = 0;
            while (offset < len && (read = is.read(bytes, offset, len - offset)) >= 0) {
                offset += read;
            }

            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
            }
        }
    }
}