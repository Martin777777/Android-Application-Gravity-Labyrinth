package com.example.entrance;

import androidx.appcompat.app.AppCompatActivity;



import android.content.Intent;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.game.MainActivity;
import com.example.game.R;

import java.util.Objects;


public class LevelSelectionActivity extends AppCompatActivity {

    int page = 0;
    Button[] buttons = new Button[10];
    Typeface typeface3;

    TextView title;
    ImageView pageNext;
    ImageView pageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_level_selection);

        title = this.findViewById(R.id.level_title);

        typeface3 = Typeface.createFromAsset(getAssets(), "font-style3.ttf");

        title.setTypeface(typeface3);

         for (int i = 1; i <= 10; i++ ){
            buttons[i-1] = this.findViewById(getResources().getIdentifier("btn_"+i, "id", this.getApplicationContext().getPackageName()));
            buttons[i-1].setText(i + "");
            buttons[i-1].setTypeface(typeface3);
            buttons[i - 1].setOnClickListener(view -> {
                Intent intent = new Intent(LevelSelectionActivity.this, MainActivity.class);
                intent.putExtra("level", Integer.parseInt(((Button) view).getText().toString()));
                intent.putExtra("model", getIntent().getIntExtra("model", 0));

                startActivity(intent);
            });

            pageNext = this.findViewById(R.id.page_next);
            pageBack = this.findViewById(R.id.page_back);

            pageNext.setOnClickListener(view -> {
                if (page < 1) {
                    page += 1;
                    changeButtonProperty();
                }
            });

            pageBack.setOnClickListener(view -> {
                if (page > 0) {
                    page -= 1;
                    changeButtonProperty();
                }
            });

        }


    }

    private void changeButtonProperty(){
        for (int i = 1; i <= 10; i++) {
            buttons[i-1].setText((i+page*10)+"");
            buttons[i-1].setTypeface(typeface3);
            int finalI = i;
            buttons[i-1].setOnClickListener(view -> {
                if (finalI +page*10 <= 15){
                    Intent intent = new Intent(LevelSelectionActivity.this, MainActivity.class);
                    intent.putExtra("level", Integer.parseInt(((Button) view).getText().toString()));
                    intent.putExtra("model", getIntent().getIntExtra("model", 0));

                    startActivity(intent);
                }
                else {
                    Toast.makeText(LevelSelectionActivity.this, "This level will be released soon!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}