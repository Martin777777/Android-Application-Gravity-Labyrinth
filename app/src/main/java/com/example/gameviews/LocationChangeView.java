package com.example.gameviews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.SensorEvent;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

import com.example.game.Hole;
import com.example.game.MyThread;
import com.example.game.Obstacle;
import com.example.game.R;

import org.json.JSONException;

public class LocationChangeView extends GameView{
    float speedX;
    float speedY;
    MyThread viewThread;
    float viewGravityX;
    float viewGravityY;

    boolean viewRunning;
    boolean start;

    ValueAnimator startAnimator;

    public LocationChangeView(Context context, int levelNumber) throws JSONException {
        super(context, levelNumber);

        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        this.mode = 3;

        start = false;
        viewRunning = true;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        super.onSensorChanged(sensorEvent);
        if (start){
            viewGravityX = sensorEvent.values[1] * 0.07f;
            viewGravityY = sensorEvent.values[0] * 0.07f;
            if (!(((this.getX() <= -screenWidth / 2f) && (viewGravityX < 0)) || ((this.getX() >= screenWidth / 2f) && (viewGravityX > 0)))) {
                this.speedX += viewGravityX;
            }
            if (!(((this.getY() <= -screenHeight / 2f) && (viewGravityY < 0)) || ((this.getY() >= screenHeight / 2f) && (viewGravityY > 0)))) {
                this.speedY += viewGravityY;
            }
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        notAnimation = false;

        startAnimator = ValueAnimator.ofFloat(0, 5);

        startAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                new Thread(() -> startDraw((Float) valueAnimator.getAnimatedValue())).start();
            }
        });

        startAnimator.setRepeatCount(0);

        startAnimator.setDuration(5000);

        startAnimator.setInterpolator(new LinearInterpolator());

        startAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                notAnimation = true;
                superSurfaceCreated();
                viewThread = new MyThread(() -> {
                    while (viewRunning) {

                        long startTime = System.currentTimeMillis();

                        synchronized (surfaceHolder) {
                            if (start){
                                move();
                            }
                        }

                        long endTime = System.currentTimeMillis();

                        int diffTime  = (int)(endTime - startTime);

                        while(diffTime <= 17) {
                            diffTime = (int)(System.currentTimeMillis() - startTime);
                            Thread.yield();
                        }

                    }
                });
                viewThread.start();
            }
        });

        startAnimator.start();

    }

    private void superSurfaceCreated() {
        super.surfaceCreated(surfaceHolder);
    }


    private void startDraw(float value) {
        try {
            canvas = surfaceHolder.lockCanvas();
            Paint numberPaint = new Paint();
            canvas.drawBitmap(background, 0, 0, paint);
            for (Obstacle obstacle : obstacles) {
                obstacle.draw(canvas, paint);
            }
            successHole.draw(canvas, paint);

            ball.draw(canvas, paint);

            for (Hole hole: holes) {
                hole.draw(canvas, paint);
            }
            int alpha = (int) (255*2 * (0.5f - Math.abs(0.5f - value%1)));
            numberPaint.setAlpha(alpha);
            Bitmap numberMap;
            Matrix numberMatrix;
            Bitmap numberAppearance;
            float location = (0.5f - Math.abs(0.5f - value%1));
            switch ((int)(value)) {
                case 0:
                    this.setX( - location * 300 );
                    this.setY( - location * 300 );
                    break;
                case 1:
                    this.setX( location * 300 );
                    this.setY( location * 300 );

                    break;
                case 2:
                    this.setX( - location * 300 );
                    this.setY( location * 300 );
                    break;
                case 3:
                    this.setX( location * 300 );
                    this.setY( - location * 300 );
                    break;
                case 4:
                    this.setX(0);
                    this.setY(0);
                    float scale1 = value % 1f;
                    if (scale1 >= 0.005f){
//                        int alpha1 = (int) (255*(value%1));
//                        numberPaint.setAlpha(alpha1);
                        numberMap = BitmapFactory.decodeResource(getResources(), R.drawable.demon);
                        numberMatrix = new Matrix();
                        numberMatrix.postScale(scale1 * ((float)screenWidth / 3f / (float)numberMap.getWidth()), scale1 * ((float)screenWidth / 3f / (float)numberMap.getHeight()));
                        numberAppearance = Bitmap.createBitmap(numberMap, 0, 0, numberMap.getWidth(), numberMap.getHeight(), numberMatrix, true);
                        canvas.drawBitmap(numberAppearance, (screenWidth - numberAppearance.getWidth()) / 2f, (screenHeight - numberAppearance.getHeight()) / 2f, numberPaint);
                    }

                    break;
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

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        super.surfaceDestroyed(surfaceHolder);
        viewRunning = false;
        if (startAnimator != null) {
            startAnimator.removeAllListeners();
            startAnimator.cancel();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (notAnimation && stop){
                stop = false;
                start = true;
            }
        }
        return true;
    }

    private void move(){
        if (this.getX() + this.speedX >= screenWidth / 2f) {
            this.setX(screenWidth / 2f);
            this.reboundX();
        }
        else if (this.getX() + this.speedX <= - screenWidth / 2f) {
            this.setX(- screenWidth / 2f);
            this.reboundX();
        }

        if (this.getY() + this.speedY >= screenHeight / 2f) {
            this.setY(screenHeight / 2f);
            this.reboundY();
        }
        else if (this.getY() + this.speedY <= - screenHeight / 2f) {
            this.setY(- screenHeight / 2f);
            this.reboundY();
        }

        this.setX(this.getX() + this.speedX);
        this.setY(this.getY() + this.speedY);
    }

    private void reboundX(){
        if (Math.abs(this.speedX * 0.3f) < 1) {
            this.speedX = 0;
        }
        else {
            this.speedX = - this.speedX * 0.3f;
        }
    }

    private void reboundY(){
        if (Math.abs(this.speedY * 0.3f) < 1) {
            this.speedY = 0;
        }
        else {
            this.speedY = - this.speedY * 0.3f;
        }
    }

}
