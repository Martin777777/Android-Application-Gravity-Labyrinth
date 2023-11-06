package com.example.community.ui.profile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.community.ui.post.Post;
import com.example.community.ui.post.PostAdapter;
import com.example.entrance.EntranceActivity;
import com.example.game.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView username;
    TextView email;
    TextView phone;
    TextView gender;
    RecyclerView posts;
    SwipeRefreshLayout refreshLayout;
    PostAdapter adapter;
    ArrayList<Post> list;


    public ProfileFragment() {
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
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = view.findViewById(R.id.profile_username);
        email = view.findViewById(R.id.profile_email);
        phone = view.findViewById(R.id.profile_phone);
        gender = view.findViewById(R.id.profile_gender);
        posts = view.findViewById(R.id.rec_profile_post);
        refreshLayout = view.findViewById(R.id.profile_refresh);

        list = new ArrayList<>();
        adapter = new PostAdapter(list, (Activity) getContext());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        posts.setLayoutManager(layoutManager);
        posts.setAdapter(adapter);

        refreshLayout.setColorSchemeColors(Color.CYAN);

        if (!EntranceActivity.isLogin){
            username.setText("Please login first!");
        }
        else {
            username.setText("Username: " + EntranceActivity.username);
            getUserInformation();
            refreshLayout.post(() -> refreshLayout.setRefreshing(true));
            getPosts();

            refreshLayout.setOnRefreshListener(this::getPosts);
        }

    }

    private void getUserInformation() {
        new Thread(() -> {
            Connection connection = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://bj-cdb-he3zrqqs.sql.tencentcdb.com:60342/game_labyrinth?useSSL=false",
                        "root", "132465798Mm");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                String sql = "select email,phone_number,gender from account where username=?";
                if (connection != null) {

                    PreparedStatement statement = connection.prepareStatement(sql);

                    connection.setAutoCommit(false);

                    statement.setString(1, EntranceActivity.username);

                    ResultSet resultSet = statement.executeQuery();//创建数据对象

                    if (resultSet.next()){
                        ((Activity) requireContext()).runOnUiThread(() -> {
                            try {
                                String emailString = resultSet.getString(1);
                                String phoneNumber = resultSet.getString(2);
                                String genderString = resultSet.getString(3);
                                email.setText("Email: " + emailString);
                                phone.setText("Phone Number:" + phoneNumber);
                                gender.setText("Gender: " + genderString);
                            } catch (Exception throwable) {
                                throwable.printStackTrace();
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
        }).start();

    }

    private void getPosts() {

        new Thread(() -> {
            System.out.println("getting");
            Connection connection = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://bj-cdb-he3zrqqs.sql.tencentcdb.com:60342/game_labyrinth?useSSL=false",
                        "root", "132465798Mm");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                list.clear();
                String sql = "select title, body, cover, time from post where username=?";
                if (connection != null) {

                    PreparedStatement statement = connection.prepareStatement(sql);

                    connection.setAutoCommit(false);

                    statement.setString(1, EntranceActivity.username);

                    ResultSet resultSet = statement.executeQuery();//创建数据对象

                    while (resultSet.next()){
                        try {
                            list.add(new Post(getBitmap(resultSet.getBlob("cover")), resultSet.getString("title"),
                                    EntranceActivity.username, resultSet.getString("body"), resultSet.getLong("time")));
                        } catch (SQLException throwable) {
                            throwable.printStackTrace();
                        }
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