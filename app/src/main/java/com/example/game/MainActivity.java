package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.gameviews.AntiGravityView;
import com.example.gameviews.GameView;
import com.example.gameviews.HiddenHoleView;
import com.example.gameviews.LocationChangeView;
import com.example.gameviews.OnlineGameView;

import org.json.JSONException;

import java.net.UnknownHostException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    GameView gameView;
    AntiGravityView antiGravityView;
    HiddenHoleView hiddenHoleView;
    LocationChangeView locationChangeView;
    OnlineGameView onlineGameView;

    Intent intent;

    BackPressed backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        intent = this.getIntent();
        int level = intent.getIntExtra("level", 1);
        int model = intent.getIntExtra("model", 0);

        switch (model) {
            case 0:
                try {
                    gameView = new GameView(MainActivity.this, level);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                gameView.setVibrator( (Vibrator) getSystemService(VIBRATOR_SERVICE));

                setContentView(gameView);
                break;
            case 1:
                try {
                    antiGravityView = new AntiGravityView(MainActivity.this, level);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                antiGravityView.setVibrator( (Vibrator) getSystemService(VIBRATOR_SERVICE));

                setContentView(antiGravityView);
                break;
            case 2:
                try {
                    hiddenHoleView = new HiddenHoleView(MainActivity.this, level);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hiddenHoleView.setVibrator( (Vibrator) getSystemService(VIBRATOR_SERVICE));

                setContentView(hiddenHoleView);
                break;
            case 3:
                try {
                    locationChangeView = new LocationChangeView(MainActivity.this, level);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                locationChangeView.setVibrator( (Vibrator) getSystemService(VIBRATOR_SERVICE));

                setContentView(R.layout.shake_background);

                addContentView(locationChangeView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

                break;
            case 4:
                String address = intent.getStringExtra("address");
                int port = intent.getIntExtra("port", 7777);

                int startLevel = intent.getIntExtra("startLevel", 1);
                int endLevel = intent.getIntExtra("endLevel", 1);
                try {
                    onlineGameView = new OnlineGameView(MainActivity.this, startLevel, address, port, endLevel);
                } catch (JSONException | UnknownHostException e) {
                    e.printStackTrace();
                }

                onlineGameView.setVibrator( (Vibrator) getSystemService(VIBRATOR_SERVICE));

                setContentView(onlineGameView);
                break;
        }


    }

    @Override
    public void onBackPressed() {
        if (backPressed != null) {
            backPressed.onBackPressed();
        }
    }

    public interface BackPressed {
        void onBackPressed();
    }

    public void setBackPressed(BackPressed backPressed) {
        this.backPressed = backPressed;
    }
}