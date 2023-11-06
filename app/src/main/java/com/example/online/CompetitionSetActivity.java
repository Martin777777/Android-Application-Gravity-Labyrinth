package com.example.online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.entrance.SkinChosenActivity;
import com.example.game.R;

import java.util.Objects;

public class CompetitionSetActivity extends AppCompatActivity {

    EditText startLevel;
    EditText endLevel;
    ImageView skin;
    Button createRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_competition_set);

        startLevel = this.findViewById(R.id.start_level);
        endLevel = this.findViewById(R.id.end_level);
        skin = this.findViewById(R.id.competition_skin);
        createRoom = this.findViewById(R.id.btn_create_room);

        skin.setOnClickListener(view -> {
            Intent intent = new Intent(CompetitionSetActivity.this, SkinChosenActivity.class);
            startActivity(intent);
        });

        createRoom.setOnClickListener(view -> {

            String startString = startLevel.getText().toString();
            String endString = endLevel.getText().toString();

            if (!startString.matches("^[1-9][0-9]*$") || !endString.matches("^[1-9][0-9]*$")) {
                Toast.makeText(CompetitionSetActivity.this, "You have to input numbers!", Toast.LENGTH_SHORT).show();
            }
            else {
                int start = Integer.parseInt(startString);
                int end = Integer.parseInt(endString);

                if (start > 15 || end > 15) {
                    Toast.makeText(CompetitionSetActivity.this, "Please input an existing level!", Toast.LENGTH_SHORT).show();
                }
               else if (start > end) {
                    Toast.makeText(CompetitionSetActivity.this, "The end level can not be less than the start level!", Toast.LENGTH_SHORT).show();
                }
               else {
                   Intent intent = new Intent(CompetitionSetActivity.this, NewRoomActivity.class);
                   intent.putExtra("start", start);
                   intent.putExtra("end", end);

                   startActivity(intent);
                }
            }
        });

    }
}