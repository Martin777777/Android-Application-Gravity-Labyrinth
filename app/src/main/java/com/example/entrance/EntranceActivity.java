package com.example.entrance;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.community.CommunityActivity;
import com.example.game.R;
import com.example.user.LoginActivity;

import java.util.Objects;


public class EntranceActivity extends AppCompatActivity {

    Button playButton;
    Button logButton;
    Button exitButton;
    Button communityButton;
    TextView title;
    SharedPreferences userData;
    Intent intent;
    public static boolean isLogin;
    public static String username;
    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_entrance);

        title = this.findViewById(R.id.title);

        playButton = this.findViewById(R.id.btn_play);
        logButton = this.findViewById(R.id.btn_log);
        exitButton = this.findViewById(R.id.btn_exit);
        communityButton = this.findViewById(R.id.btn_community);
        userData = this.getSharedPreferences("user_data", MODE_PRIVATE);

        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "font-style1.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "font-style2.ttf");
        title.setTypeface(typeface1);
        playButton.setTypeface(typeface2);
        logButton.setTypeface(typeface2);
        exitButton.setTypeface(typeface2);
        communityButton.setTypeface(typeface2);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Toast.makeText(EntranceActivity.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(EntranceActivity.this, ModelSelectionActivity.class);
                startActivity(intent);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });

        communityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(EntranceActivity.this, CommunityActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        logButton = this.findViewById(R.id.btn_log);

        if (userData.getBoolean("keepIn", false)){
            isLogin = true;
            username = userData.getString("username", "");
        }

        if (isLogin){
            logButton.setText("Logout");
            logButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = userData.edit();
                    editor.putBoolean("keepIn", false);
                    editor.putString("username", "");
                    isLogin = false;
                    username = "";
                    editor.apply();
                    onStart();
                    Toast.makeText(EntranceActivity.this, "Logout successfully!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            logButton.setText("Login");
            logButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(EntranceActivity.this, LoginActivity.class);
                    launcher.launch(intent);
                }
            });
        }
    }
}