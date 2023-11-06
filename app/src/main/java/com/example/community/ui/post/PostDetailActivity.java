package com.example.community.ui.post;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.game.R;

import java.util.Objects;

public class PostDetailActivity extends AppCompatActivity {

    TextView title;
    TextView content;
    TextView author;
    TextView time;
    ImageView cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_post_detail);

        Intent intent = this.getIntent();

        this.title = this.findViewById(R.id.post_detail_title);
        this.content = this.findViewById(R.id.post_detail_content);
        this.author = this.findViewById(R.id.post_detail_author);
        this.time = this.findViewById(R.id.post_detail_time);
        this.cover = this.findViewById(R.id.post_detail_image);

        title.setText(intent.getStringExtra("title"));
        content.setText(intent.getStringExtra("content"));
        author.setText(intent.getStringExtra("author"));
        time.setText(intent.getStringExtra("time"));
        cover.setImageURI(intent.getData());
    }
}