package com.example.gameviews;

import static android.content.Context.SENSOR_SERVICE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.entrance.SkinChosenActivity;
import com.example.game.BackDialog;
import com.example.game.Ball;
import com.example.game.Calculation;
import com.example.game.Hole;
import com.example.game.Level;
import com.example.game.MainActivity;
import com.example.game.MyThread;
import com.example.game.Obstacle;
import com.example.game.Point;
import com.example.game.R;
import com.example.game.Share;
import com.example.game.SuccessDialog;
import com.example.game.Vector;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class GameView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {

    Paint paint;
    SurfaceHolder surfaceHolder;
    MyThread mainThread;
    MyThread detectThread;
    Canvas canvas;
    boolean isRunning = true;
    boolean freeMove = true;
    boolean successful = false;
    Bitmap bitmap;
    Bitmap ballMap;
    Bitmap obstacleMap;
    Bitmap holeMap;
    Bitmap successMap;
    Ball ball;
    Hole successHole;
    int startX;
    int startY;
    int screenWidth;
    int screenHeight;
    int levelNumber;
    int time;
    int mode;
    float edgeX;
    float edgeY;
    float gravityX;
    float gravityY;
    Bitmap background;
    Bitmap ballAppearance;
    Bitmap obstacleAppearance;
    Bitmap holeAppearance;
    Bitmap successAppearance;
    Bitmap[] bitmaps;
    SensorManager sensorManager;
    Sensor sensor;
    boolean forceX;
    boolean forceY;
    boolean isReboundX;
    boolean isReboundY;
    boolean notAnimation;
    boolean stop;
    boolean isVibrate;
    boolean toShowDialog;
    boolean toShowBackDialog;
    Handler handler;
    Vibrator vibrator;
    ValueAnimator valueAnimator;
    Path overPath;
    Level level;
    Timer timer;
    SoundPool soundPool;
    HashMap<Integer, Integer> sounds;
    BackDialog backDialog;

    ArrayList<Obstacle> obstacles;
    ArrayList<Hole> holes;
    int test = 0;


    public GameView(Context context, int levelNumber) throws JSONException {
        super(context);

        ((MainActivity) context).setBackPressed(this::showBackPressedDialog);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        paint = new Paint();
//        paint.setColor(Color.BLACK);
//        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(10);

        this.levelNumber = levelNumber;
        this.mode = 0;

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_game);
        ballMap = BitmapFactory.decodeResource(getResources(), SkinChosenActivity.skinID);
        obstacleMap = BitmapFactory.decodeResource(getResources(), R.drawable.obstacle);
        holeMap = BitmapFactory.decodeResource(getResources(), R.drawable.hole);
        successMap = BitmapFactory.decodeResource(getResources(), R.drawable.success_hole);

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        stop = true;
        notAnimation = true;

        toShowDialog = true;
        toShowBackDialog = true;

        isReboundX = false;
        isReboundY = false;
        isVibrate = true;

        level = new Level(screenWidth, screenHeight);
        holes = new ArrayList<>();
        obstacles = new ArrayList<>();


        float scaleWidth = (float) screenWidth / bitmap.getWidth();
        float scaleHeight = (float) screenHeight / bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        background = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        float scaleWidth2 = (float) 2 * 37 / 2340 * screenWidth / ballMap.getWidth();
        float scaleHeight2 = (float) 2 * 37 / 2340 * screenWidth / ballMap.getHeight();
        Matrix matrix2 = new Matrix();
        matrix2.postScale(scaleWidth2, scaleHeight2);
        ballAppearance = Bitmap.createBitmap(ballMap, 0, 0, ballMap.getWidth(), ballMap.getHeight(), matrix2, true);

        startX = screenWidth * level.getLevel(levelNumber).getJSONObject("startPosition").getInt("x") / 2340;
        startY = screenHeight * level.getLevel(levelNumber).getJSONObject("startPosition").getInt("y") / 1080;
        ball = new Ball( screenWidth * 37 / 2340, ballAppearance, startX, startY );

        for (int i = 0; i < level.getLevel(levelNumber).getJSONArray("obstacles").length(); i++) {

            int x = screenWidth * level.getLevel(levelNumber).getJSONArray("obstacles").getJSONObject(i).getInt("x") / 2340;
            int y = screenHeight * level.getLevel(levelNumber).getJSONArray("obstacles").getJSONObject(i).getInt("y") / 1080;
            int width = screenWidth * level.getLevel(levelNumber).getJSONArray("obstacles").getJSONObject(i).getInt("width") / 2340;
            int height = screenHeight * level.getLevel(levelNumber).getJSONArray("obstacles").getJSONObject(i).getInt("height") / 1080;
            float scaleWidth3 = (float) width / obstacleMap.getWidth();
            float scaleHeight3 = (float) height / obstacleMap.getHeight();
            Matrix matrix3 = new Matrix();
            matrix3.postScale(scaleWidth3, scaleHeight3);
            obstacleAppearance = Bitmap.createBitmap(obstacleMap, 0, 0, obstacleMap.getWidth(), obstacleMap.getHeight(), matrix3, true);

            obstacles.add(new Obstacle(obstacleAppearance, x, y, width, height));

        }

        for (int i = 0; i < level.getLevel(levelNumber).getJSONArray("holes").length(); i++) {
            float scaleWidth4 = (float) 2 * 57 / 2340 * screenWidth / holeMap.getWidth();
            float scaleHeight4 = (float) 2 * 57 / 2340 * screenWidth / holeMap.getHeight();
            Matrix matrix4 = new Matrix();
            matrix4.postScale(scaleWidth4, scaleHeight4);
            holeAppearance = Bitmap.createBitmap(holeMap, 0, 0, holeMap.getWidth(), holeMap.getHeight(), matrix4, true);
            int x = screenWidth * level.getLevel(levelNumber).getJSONArray("holes").getJSONObject(i).getInt("x") / 2340;
            int y = screenHeight *  level.getLevel(levelNumber).getJSONArray("holes").getJSONObject(i).getInt("y") / 1080;

            holes.add(new Hole(57 * screenWidth / 2340, holeAppearance, x, y));
        }

        float scaleWidth5 = (float) 2 * 57 / 2340 * screenWidth / ballMap.getWidth();
        float scaleHeight5 = (float) 2 * 57 / 2340 * screenWidth / ballMap.getHeight();
        Matrix matrix5 = new Matrix();
        matrix5.postScale(scaleWidth5, scaleHeight5);
        successAppearance = Bitmap.createBitmap(successMap, 0, 0, successMap.getWidth(), successMap.getHeight(), matrix5, true);

        int successX = screenWidth * level.getLevel(levelNumber).getJSONObject("successPosition").getInt("x") / 2340;
        int successY = screenHeight * level.getLevel(levelNumber).getJSONObject("successPosition").getInt("y") / 1080;
        successHole = new Hole( screenWidth * 57 / 2340, successAppearance, successX, successY );

        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);

        Calculation.setBallRadius(ball.getRadius());

        timer = new Timer();
        time = 0;


        AudioAttributes audioAttributes = null;
        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(16)
                .setAudioAttributes(audioAttributes)
                .build();

        sounds = new HashMap<>();

        sounds.put(0, soundPool.load(GameView.this.getContext(), R.raw.success, 1));
        sounds.put(1, soundPool.load(GameView.this.getContext(), R.raw.enter_hole, 2));
//        sounds.put(2, soundPool.load(GameView.this.getContext(), R.raw.collide, 1));

        bitmaps = new Bitmap[]{bitmap, ballMap, obstacleMap, holeMap, successMap, background, ballAppearance, obstacleAppearance, holeAppearance, successAppearance};

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                ball.stop();

                float[] currentPosition = new float[2];
                PathMeasure pathMeasure = new PathMeasure(overPath, false);
                pathMeasure.getPosTan(0, currentPosition, null);

                valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
                valueAnimator.setDuration(2000);
                valueAnimator.setRepeatCount(0);
                valueAnimator.addUpdateListener(valueAnimator1 -> {
                    float value = (Float) valueAnimator1.getAnimatedValue();
                    pathMeasure.getPosTan(value, currentPosition, null);
                    overDraw(currentPosition[0], currentPosition[1], (int) (255 - (value * 255 / pathMeasure.getLength())), ball.getRadius()*(1 - (value / (2 * pathMeasure.getLength()))));
                    postInvalidate();
                });

                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

                        notAnimation = true;

                        if (!successful){
                            ball.setX(startX);
                            ball.setY(startY);
                            ball.setCenter(new Point(startX, startY));
                            isRunning = true;
                            mainThread.restart();
                        }
                        else {

                            if (toShowDialog){

                                if (backDialog != null && backDialog.isShowing()) {
                                    backDialog.dismiss();
                                    backDialog = null;
                                }

                                SuccessDialog successDialog = new SuccessDialog(GameView.this.getContext(), time, levelNumber, mode);

                                successDialog.getMenu().setOnClickListener(view -> {
                                    successDialog.dismiss();
                                    ((Activity) GameView.this.getContext()).finish();
                                });
                                successDialog.getAgain().setOnClickListener(view -> {
                                    successDialog.dismiss();
                                    try {
                                        GameView nextGame = null;
                                        switch (mode) {
                                            case 0:
                                                nextGame = new GameView(GameView.this.getContext(), levelNumber);
                                                break;
                                            case 1:
                                                nextGame = new AntiGravityView(GameView.this.getContext(), levelNumber);
                                                break;
                                            case 2:
                                                nextGame = new HiddenHoleView(GameView.this.getContext(), levelNumber);
                                                break;
                                            case 3:
                                                nextGame = new LocationChangeView(GameView.this.getContext(), levelNumber);
                                                break;
                                        }
                                        if (nextGame != null){
                                            nextGame.setVibrator(vibrator);
                                        }
                                        ((ViewGroup) getParent()).addView(nextGame);
                                        ((ViewGroup) getParent()).removeView(GameView.this);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                });
                                successDialog.getNext().setOnClickListener(view -> {
                                    successDialog.dismiss();
                                    try {
                                        if (levelNumber < level.getLevelNumber()) {
                                            GameView nextGame = null;
                                            switch (mode) {
                                                case 0:
                                                    nextGame = new GameView(GameView.this.getContext(), levelNumber + 1);
                                                    break;
                                                case 1:
                                                    nextGame = new AntiGravityView(GameView.this.getContext(), levelNumber + 1);
                                                    break;
                                                case 2:
                                                    nextGame = new HiddenHoleView(GameView.this.getContext(), levelNumber + 1);
                                                    break;
                                                case 3:
                                                    nextGame = new LocationChangeView(GameView.this.getContext(), levelNumber + 1);
                                                    break;

                                            }
                                            if (nextGame != null){
                                                nextGame.setVibrator(vibrator);
                                            }
                                            ((ViewGroup) getParent()).addView(nextGame);
                                            ((ViewGroup) getParent()).removeView(GameView.this);
                                        } else {
                                            Toast.makeText(GameView.this.getContext(), "You have passed all levels!", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                });
                                successDialog.getShare().setOnClickListener(view -> {
                                    Bitmap backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.share_background);
                                    float scaleWidth = (float) screenWidth / backgroundBitmap.getWidth();
                                    float scaleHeight = (float) screenHeight / backgroundBitmap.getHeight();
                                    Matrix matrix = new Matrix();
                                    matrix.postScale(scaleWidth, scaleHeight);
                                    Bitmap sharedBitmap = Bitmap.createBitmap(backgroundBitmap, 0, 0, backgroundBitmap.getWidth(), backgroundBitmap.getHeight(), matrix, true);
                                    Canvas shareCanvas = new Canvas(sharedBitmap);
                                    Paint textPaint = new Paint();
                                    textPaint.setTextSize(251);
                                    textPaint.setTextAlign(Paint.Align.CENTER);
                                    textPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font-style3.ttf"));
                                    shareCanvas.drawText("You passed Level " + levelNumber + "!", sharedBitmap.getWidth()/2f, 230, textPaint);
                                    String modeString = "";
                                    switch (mode) {
                                        case 0:
                                            modeString = "Plain";
                                            break;
                                        case 1:
                                            modeString = "Anti-Gravity";
                                            break;
                                        case 2:
                                            modeString = "Hole-Hidden";
                                            break;
                                        case 3:
                                            modeString = "Location-Change";
                                            break;
                                    }
                                    textPaint.setTextSize(200);
                                    textPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font-style2.ttf"));
                                    shareCanvas.drawText("Mode: " + modeString, sharedBitmap.getWidth()/2f, 463, textPaint);
                                    shareCanvas.drawText(String.format(Locale.ENGLISH ,"Time used: %02d : %02d", time / 60, time % 60 ), sharedBitmap.getWidth()/2f, 700, textPaint);
                                    if (successDialog.isBreakWorld()) {
                                        shareCanvas.drawText("New World Record!", sharedBitmap.getWidth()/2f, 950, textPaint);
                                    }
                                    else if (successDialog.isBreakPersonal()) {
                                        shareCanvas.drawText("New Personal Record!", sharedBitmap.getWidth()/2f, 950, textPaint);
                                    }
                                    else {
                                        System.out.println("no");
                                    }
                                    Share.share((Activity) getContext(), sharedBitmap);
                                    ((Activity) GameView.this.getContext()).finish();
                                });

                                successDialog.show();

                                toShowDialog = false;
                            }
                        }
                    }
                });
                valueAnimator.start();
            }
        };

        mainThread = new MyThread(() -> {
            while (isRunning) {

                long startTime = System.currentTimeMillis();

                synchronized (surfaceHolder) {
                    detectCollision();
                    draw();
                }

                long endTime = System.currentTimeMillis();

                int diffTime  = (int)(endTime - startTime);

                while(diffTime <= 17) {
                    diffTime = (int)(System.currentTimeMillis() - startTime);
                    Thread.yield();
                }

            }
        });
        mainThread.start();

//        detectThread = new MyThread(() -> {
//            while (isRunning) {
//
//                long startTime = System.currentTimeMillis();
//
//                synchronized (surfaceHolder) {
//                    detectCollision();
//                }
//
//                long endTime = System.currentTimeMillis();
//
//                int diffTime  = (int)(endTime - startTime);
//
//                while(diffTime <= 17) {
//                    diffTime = (int)(System.currentTimeMillis() - startTime);
//                    Thread.yield();
//                }
//
//            }
//        });
//        detectThread.start();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                time += 1;
                if (backDialog != null && backDialog.isShowing()) {
                    backDialog.setTime(time);
                }
            }
        }, 0, 1000);

        Toast.makeText(GameView.this.getContext(), "Tap the screen to play", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (notAnimation && stop){
                stop = false;
            }
        }
        return true;
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        isRunning = false;
        surfaceHolder.removeCallback(GameView.this);

        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            sensorManager = null;
        }

        if (valueAnimator != null) {
            valueAnimator.removeAllListeners();
            valueAnimator.cancel();
        }

        if (soundPool != null) {
            soundPool.autoPause();
            soundPool.release();
            soundPool = null;
        }

        for (Bitmap map: bitmaps) {
            if (map != null && !map.isRecycled()) {
                map.recycle();
            }
        }

        bitmaps = null;
        obstacles = null;
        holes = null;

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (!stop){
            gravityX = sensorEvent.values[1] * 0.07f;
            gravityY = sensorEvent.values[0] * 0.07f;

            forceX = true;
            forceY = true;

            if (!((ball.getRight() < screenWidth && ball.getLeft() > 0) ||
                    (ball.getRight() == screenWidth && gravityX < 0) ||
                    (ball.getLeft() == 0 && gravityX > 0))) {
                forceX = false;
            }

            if (!((ball.getBottom() < screenHeight && ball.getTop() > 0) ||
                    (ball.getBottom() == screenHeight && gravityY < 0) ||
                    (ball.getTop() == 0 && gravityY > 0))) {
                forceY = false;
            }

            for (Obstacle obstacle : obstacles) {
                if (ball.getX() >= obstacle.getLeft() &&
                        ball.getX() <= obstacle.getRight() &&
                        ((ball.getBottom() >= obstacle.getTop() && ball.getTop() <= obstacle.getTop() && gravityY > 0) ||
                                ball.getTop() <= obstacle.getBottom() && ball.getBottom() >= obstacle.getBottom() && gravityY < 0)) {
                    forceY = false;
                }

                if (ball.getY() >= obstacle.getTop() &&
                        ball.getY() <= obstacle.getBottom() &&
                        ((ball.getRight() >= obstacle.getLeft() && ball.getLeft() <= obstacle.getLeft() && gravityX > 0) ||
                                (ball.getLeft() <= obstacle.getRight() && ball.getRight() >= obstacle.getRight() && gravityX < 0))) {
                    forceX = false;
                }

                for (int i = 0; i < 4; i++) {
                    if (Math.pow(ball.getX() - obstacle.getPoints()[i].x, 2) + Math.pow(ball.getY() - obstacle.getPoints()[i].y, 2) >= Math.pow(ball.getRadius() * 0.9, 2) &&
                            Math.pow(ball.getX() - obstacle.getPoints()[i].x, 2) + Math.pow(ball.getY() - obstacle.getPoints()[i].y, 2) <= Math.pow(ball.getRadius() * 1.1f, 2)) {
                        forceX = false;
                        forceY = false;
                        Vector acceleration = Calculation.findCornerForce(gravityX, gravityY, ball.getCenter(), obstacle.getPoints()[i], i);
                        ball.lateralForce(acceleration.x);
                        ball.longitudinalForce(acceleration.y);
                        break;
                    }
                }
            }


            if (forceX && !isReboundX) {
                ball.lateralForce(gravityX);
            }

            if (forceY && !isReboundY) {
                ball.longitudinalForce(gravityY);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void draw(){
        try {

            canvas = surfaceHolder.lockCanvas();
            if (canvas != null){
                canvas.drawBitmap(background, 0, 0, paint);
                if (!stop) {
                    if (freeMove) {
                        ball.move();
                        isReboundX = false;
                        isReboundY = false;
                    } else {
                        ball.setX(edgeX);
                        ball.setY(edgeY);
                        freeMove = true;
                        isReboundX = false;
                        isReboundY = false;
                        if (isVibrate) {
                            vibrator.vibrate(5);
                        }
                        isVibrate = true;
                    }
                }
                for (Obstacle obstacle : obstacles) {
                    obstacle.draw(canvas, paint);
                }
                successHole.draw(canvas, paint);
                for (Hole hole : holes) {
                    hole.draw(canvas, paint);
                }
                ball.draw(canvas, paint);
//                canvas.drawText("SpeedX: " + ball.getSpeedX(), 30, 50, paint);
//                canvas.drawText("SpeedY: " + ball.getSpeedY(), 30, 70, paint);
//                canvas.drawText("gravityX: " + gravityX, 30, 90, paint);
//                canvas.drawText("gravityY: " + gravityY, 30, 110, paint);
//                canvas.drawText("X: " + ball.getRight(), 30, 130, paint);
//                canvas.drawText("Y: " + ball.getY(), 30, 150, paint);
//                canvas.drawText("ScreenWidth: " + screenWidth, 30, 170, paint);
//                canvas.drawText("ScreenHeight: " + screenHeight, 30, 190, paint);
//                canvas.drawText("Radius: " + ball.getRadius(), 30, 210, paint);
//                canvas.drawText("Test: " + freeMove + " " + forceX + " " + forceY, 30, 230, paint);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (canvas != null){
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void detectCollision(){

        if ( ball.getRight() + ball.getSpeedX() >= screenWidth && ball.getSpeedX() > 0 ){
            freeMove = false;
            edgeX = screenWidth - ball.getRadius();
            ball.reboundX();

            isReboundX = true;
//            this.playMusic(2);

        }
        else if ( ball.getLeft() + ball.getSpeedX() <= 0 && ball.getSpeedX() < 0 ){
            freeMove = false;
            edgeX = ball.getRadius();
            ball.reboundX();

            isReboundX = true;
//            this.playMusic(2);
        }

        if ( ball.getBottom() + ball.getSpeedY() >= screenHeight && ball.getSpeedY() > 0 ){
            freeMove = false;
            edgeY = screenHeight - ball.getRadius();
            ball.reboundY();

            isReboundY = true;
//            this.playMusic(2);
        }
        else if ( ball.getTop() + ball.getSpeedY() <= 0 && ball.getSpeedY() < 0 ){
            freeMove = false;
            edgeY = ball.getRadius();
            ball.reboundY();

            isReboundY = true;
//            this.playMusic(2);
        }

        if (!freeMove) {
            if (!isReboundX) {
                edgeX = ball.getX() + ball.getSpeedX();
            }
            if (!isReboundY) {
                edgeY = ball.getY() + ball.getSpeedY();
            }
        }

        for (Obstacle obstacle : obstacles) {

            if (ball.getX() >= obstacle.getLeft() &&
                    ball.getX() <= obstacle.getRight() &&
                    ((ball.getBottom() == obstacle.getTop() && ball.getSpeedY() > 0) ||
                            (ball.getTop() == obstacle.getBottom() && ball.getSpeedY() < 0))) {
                ball.setSpeedY(0);
            }


            if (ball.getY() >= obstacle.getTop() &&
                    ball.getY() <= obstacle.getBottom() &&
                    ((ball.getRight() == obstacle.getLeft() && ball.getSpeedX() > 0) ||
                            (ball.getLeft() == obstacle.getRight() && ball.getSpeedX() < 0))) {
                ball.setSpeedX(0);
            }

            if (ball.getX() >= obstacle.getLeft() &&
                    ball.getX() <= obstacle.getRight() &&
                    ball.getBottom() + ball.getSpeedY() >= obstacle.getTop() &&
                    ball.getTop() <= obstacle.getTop() &&
                    ball.getSpeedY() > 0) {
                freeMove = false;
                edgeY = obstacle.getTop() - ball.getRadius();

                if (ball.getBottom() > obstacle.getTop()) {
                    isVibrate = false;
                }

                ball.reboundY();

                isReboundY = true;
//                this.playMusic(2);
//                test = 1;

            } else if (ball.getX() >= obstacle.getLeft() &&
                    ball.getX() <= obstacle.getRight() &&
                    ball.getTop() + ball.getSpeedY() <= obstacle.getBottom() &&
                    ball.getBottom() >= obstacle.getBottom() &&
                    ball.getSpeedY() < 0) {
                freeMove = false;
                edgeY = obstacle.getBottom() + ball.getRadius();

                if (ball.getTop() < obstacle.getBottom()) {
                    isVibrate = false;
                }

                ball.reboundY();

                isReboundY = true;
//                this.playMusic(2);
//                test = 2;

            }

            if (ball.getY() >= obstacle.getTop() &&
                    ball.getY() <= obstacle.getBottom() &&
                    ball.getRight() + ball.getSpeedX() >= obstacle.getLeft() &&
                    ball.getLeft() <= obstacle.getLeft() &&
                    ball.getSpeedX() > 0) {
                freeMove = false;
                edgeX = obstacle.getLeft() - ball.getRadius();

                if (ball.getRight() > obstacle.getLeft()) {
                    isVibrate = false;
                }

                ball.reboundX();

                isReboundX = true;
//                this.playMusic(2);
//                test = 1;

            } else if (ball.getY() >= obstacle.getTop() &&
                    ball.getY() <= obstacle.getBottom() &&
                    ball.getLeft() + ball.getSpeedX() <= obstacle.getRight() &&
                    ball.getRight() >= obstacle.getRight() &&
                    ball.getSpeedX() < 0
                    ) {

                freeMove = false;
                edgeX = obstacle.getRight() + ball.getRadius();

                if (ball.getLeft() < obstacle.getRight()) {
                    isVibrate = false;
                }

                ball.reboundX();

                isReboundX = true;
//                this.playMusic(2);
//                test = 2;

            }

            if (!freeMove) {
                if (!isReboundX) {
                    edgeX = ball.getX() + ball.getSpeedX();
                }
                if (!isReboundY) {
                    edgeY = ball.getY() + ball.getSpeedY();
                }
            }

            for (int i = 0; i < 4; i++) {
                Point point = obstacle.getPoints()[i];
                if (Math.pow(ball.getX() + ball.getSpeedX() - point.x, 2) + Math.pow(ball.getY() + ball.getSpeedY() - point.y, 2) <= Math.pow(ball.getRadius(), 2)) {
                    freeMove = false;
                    ball.setCenter(new Point(ball.getX(), ball.getY()));
                    Point centerPoint = Calculation.findCenterPoint(ball.getCenter(), point);
                    Vector direction = point.MinusPoint(ball.getCenter());
                    if (!(((ball.getSpeedX() > 1 && direction.x > 0) || (ball.getSpeedX() < -1 && direction.x < 0)) &&
                            ((ball.getSpeedY() > 1 && direction.y > 0) || (ball.getSpeedY() < -1 && direction.y < 0)))) {
                        isVibrate = false;
                    }

                    edgeX = centerPoint.x;
                    edgeY = centerPoint.y;
                    ball.touchCorner(point, i);
                    break;
                }
            }
        }

        for (Hole hole : holes) {
            if (Math.pow(ball.getX() - hole.getX(), 2) + Math.pow(ball.getY() - hole.getY(), 2) <= Math.pow(hole.getRadius(), 2)) {
                isRunning = false;
                notAnimation = false;
                stop = true;
                ball.setCenter(new Point(ball.getX(), ball.getY()));
                overPath = Calculation.getPath(ball.getCenter(), hole.getCenter());
                handler.sendEmptyMessage(0);
                this.playMusic(1);
            }
        }

        if (Math.pow(ball.getX() - successHole.getX(), 2) + Math.pow(ball.getY() - successHole.getY(), 2) <= Math.pow(successHole.getRadius(), 2)) {
            timer.cancel();
            timer.purge();
            isRunning = false;
            notAnimation = false;
            stop = true;
            ball.setCenter(new Point(ball.getX(), ball.getY()));
            overPath = Calculation.getPath(ball.getCenter(), successHole.getCenter());
            successful = true;
            handler.sendEmptyMessage(0);
            this.playMusic(0);
        }

    }

    public void overDraw(float x, float y, int value, float radius){
        try {
            canvas = surfaceHolder.lockCanvas();
            Paint ballPaint = new Paint();
            ballPaint.setAlpha(value);
            canvas.drawBitmap(background, 0, 0, paint);
            for (Obstacle obstacle : obstacles) {
                obstacle.draw(canvas, paint);
            }
            successHole.draw(canvas, paint);
            for (Hole hole : holes){
                hole.draw(canvas, paint);
            }

            float scale = radius / ball.getRadius();
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap ballLook = Bitmap.createBitmap(ballAppearance, 0, 0, ballAppearance.getWidth(), ballAppearance.getHeight(), matrix, true);
            canvas.drawBitmap(ballLook, x - radius , y - radius, ballPaint);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (canvas != null){
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void setVibrator(Vibrator vibrator) {
        this.vibrator = vibrator;
    }

    void playMusic(int sound) {
        AudioManager audioManager = (AudioManager) GameView.this.getContext().getSystemService(Context.AUDIO_SERVICE);
        float currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = currVolume / maxVolume;
        soundPool.play(sounds.get(sound), volume, volume, 1, 0, 1.0f);
    }

    private void showBackPressedDialog() {
        backDialog = new BackDialog(GameView.this.getContext(), time, levelNumber, mode);

        backDialog.getMenu().setOnClickListener(view -> {
            backDialog.dismiss();
            backDialog = null;
            ((Activity) GameView.this.getContext()).finish();
        });

        backDialog.getAgain().setOnClickListener(view -> {
            backDialog.dismiss();
            backDialog = null;
            try {
                GameView nextGame = null;
                switch (mode) {
                    case 0:
                        nextGame = new GameView(GameView.this.getContext(), levelNumber);
                        break;
                    case 1:
                        nextGame = new AntiGravityView(GameView.this.getContext(), levelNumber);
                        break;
                    case 2:
                        nextGame = new HiddenHoleView(GameView.this.getContext(), levelNumber);
                        break;
                    case 3:
                        nextGame = new LocationChangeView(GameView.this.getContext(), levelNumber);
                        break;
                }
                if (nextGame != null){
                    nextGame.setVibrator(vibrator);
                }
                ((ViewGroup) getParent()).addView(nextGame);
                ((ViewGroup) getParent()).removeView(GameView.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        backDialog.getContinuePlay().setOnClickListener(view -> {
            backDialog.dismiss();
            toShowBackDialog = true;
            backDialog = null;
        });

        backDialog.show();

        toShowBackDialog = false;
    }

//    public void setLevelNumber(int levelNumber) {
//        this.levelNumber = levelNumber;
//    }

}
