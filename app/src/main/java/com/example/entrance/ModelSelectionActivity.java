package com.example.entrance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.game.R;
import com.example.online.OnlineSelectionActivity;

import java.util.Objects;

public class ModelSelectionActivity extends AppCompatActivity {

     Button plain;
     Button anti;
     Button hideHole;
     Button shake;
     ImageView chooseSkin;
     ImageView pk;

     TextView title;

     Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_model_selection);

        plain = this.findViewById(R.id.btn_plain);
        anti = this.findViewById(R.id.btn_anti);
        hideHole = this.findViewById(R.id.btn_hide_hole);
        shake = this.findViewById(R.id.btn_shake);
        chooseSkin = this.findViewById(R.id.choose_skin);
        pk = this.findViewById(R.id.pk);

        title = this.findViewById(R.id.model_title);

        typeface = Typeface.createFromAsset(getAssets(), "font-style3.ttf");

        plain.setTypeface(typeface);
        anti.setTypeface(typeface);
        hideHole.setTypeface(typeface);
        shake.setTypeface(typeface);
        title.setTypeface(typeface);

        Intent intent = new Intent(ModelSelectionActivity.this, LevelSelectionActivity.class);

        plain.setOnClickListener(view -> {
            intent.putExtra("model", 0);
            startActivity(intent);
        });

        anti.setOnClickListener(view -> {
            intent.putExtra("model", 1);
            startActivity(intent);
        });

        hideHole.setOnClickListener(view -> {
            intent.putExtra("model", 2);
            startActivity(intent);
        });

        shake.setOnClickListener(view -> {
            intent.putExtra("model", 3);
            startActivity(intent);
        });

        chooseSkin.setOnClickListener(view -> {
            Intent skinIntent = new Intent(ModelSelectionActivity.this, SkinChosenActivity.class);
            startActivity(skinIntent);
        });

        pk.setOnClickListener(view -> {
            Intent onlineIntent = new Intent(ModelSelectionActivity.this, OnlineSelectionActivity.class);
            startActivity(onlineIntent);
        });

    }
}