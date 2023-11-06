package com.example.online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.game.R;

import java.util.Objects;

public class OnlineSelectionActivity extends AppCompatActivity {

    ImageButton newRoom;
    ImageButton joinOthers;

    public static String enemyName;
    public static int enemySkinID;
    public static int startLevel;
    public static int endLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_online_selection);

        newRoom = this.findViewById(R.id.btn_new_room);
        joinOthers = this.findViewById(R.id.btn_join);

        newRoom.setOnClickListener(view -> {
            Intent intent = new Intent(OnlineSelectionActivity.this, CompetitionSetActivity.class);
            startActivity(intent);
        });

        joinOthers.setOnClickListener(view -> {
            Intent intent = new Intent(OnlineSelectionActivity.this, JoinActivity.class);
            startActivity(intent);
        });
    }
}