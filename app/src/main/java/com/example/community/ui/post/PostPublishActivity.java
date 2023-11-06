package com.example.community.ui.post;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entrance.EntranceActivity;
import com.example.game.R;
import com.example.user.RegisterActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;


public class PostPublishActivity extends AppCompatActivity {

    EditText title;
    EditText body;
    TextView len;
    Button addCover;
    Button publish;
    ImageView cover;

    Connection connection;
    PreparedStatement statement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_post_publish);

        title = this.findViewById(R.id.post_title);
        body = this.findViewById(R.id.post_body);
        len = this.findViewById(R.id.body_length);
        addCover = this.findViewById(R.id.add_cover);
        publish = this.findViewById(R.id.btn_publish_post);
        cover = this.findViewById(R.id.post_cover);

        body.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                len.setText(body.getText().length() + "/500");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData()!= null) {
                Uri uri = result.getData().getData();
                cover.setImageURI(uri);
            }
        });

        addCover.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            launcher.launch(intent);
        });

        publish.setOnClickListener(view -> {
            if (title.getText().toString().equals("")) {
                Toast.makeText(PostPublishActivity.this, "Please enter the title!", Toast.LENGTH_SHORT).show();
            }
            else if (body.getText().toString().equals("")) {
                Toast.makeText(PostPublishActivity.this, "Please enter the content!", Toast.LENGTH_SHORT).show();
            }
            else {
                new Thread(() -> {
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        connection = DriverManager.getConnection("jdbc:mysql://bj-cdb-he3zrqqs.sql.tencentcdb.com:60342/game_labyrinth?useSSL=false",
                                "root", "132465798Mm");
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try {
                        String sql = "insert into post (title,body,cover,username,time) values (?,?,?,?,?)";
                        if (connection!=null) {

                            statement = connection.prepareStatement(sql);

                            connection.setAutoCommit(false);

                            statement.setString(1, title.getText().toString());
                            statement.setString(2, body.getText().toString());

                            BitmapDrawable drawable = (BitmapDrawable) cover.getDrawable();

                            Bitmap bitmap = drawable.getBitmap();
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            byte[] imageByte = outputStream.toByteArray();

                            Blob blob = connection.createBlob();
                            blob.setBytes(1, imageByte);
                            statement.setBlob(3, blob);

                            statement.setString(4, EntranceActivity.username);
                            statement.setLong(5, System.currentTimeMillis());

                            statement.addBatch();
                            statement.executeBatch();
                            connection.commit();

                            setResult(Activity.RESULT_OK);
                            finish();

                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });

    }
}