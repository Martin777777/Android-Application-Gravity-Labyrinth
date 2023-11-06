package com.example.gameviews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.SensorEvent;
import android.view.SurfaceHolder;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

import com.example.game.Calculation;

import com.example.game.Hole;
import com.example.game.Obstacle;

import com.example.game.R;
import com.example.game.Vector;

import org.json.JSONException;



public class AntiGravityView extends GameView {

    ValueAnimator startAnimator;

    public AntiGravityView(Context context, int levelNumber) throws JSONException {
        super(context, levelNumber);
        this.mode = 1;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        notAnimation = false;

        startAnimator = ValueAnimator.ofFloat(0, 3);

        startAnimator.addUpdateListener(valueAnimator -> new Thread(() -> startDraw((Float) valueAnimator.getAnimatedValue())).start());

        startAnimator.setRepeatCount(0);

        startAnimator.setDuration(3000);

        startAnimator.setInterpolator(new LinearInterpolator());

        startAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                notAnimation = true;
                superSurfaceCreated();
            }
        });

        startAnimator.start();
    }



    private void superSurfaceCreated() {
        super.surfaceCreated(surfaceHolder);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (!stop){
            gravityX = - sensorEvent.values[1] * 0.07f;
            gravityY = - sensorEvent.values[0] * 0.07f;

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
            int alpha = (int) (255 * ((1.5 - Math.abs(1.5 - value)) / 1.5));
            numberPaint.setAlpha(alpha);
            Bitmap numberMap;
            Matrix numberMatrix;
            Bitmap numberAppearance;

            float scale1 = value / 3;

            if (scale1 >= 0.005f){

                numberMap = BitmapFactory.decodeResource(getResources(), R.drawable.dizziness);
                numberMatrix = new Matrix();
                numberMatrix.postRotate(value * 240 );
                numberMatrix.postScale(scale1 * ((float)screenWidth / 5f / (float)numberMap.getWidth()), scale1 * ((float)screenWidth / 5f / (float)numberMap.getHeight()));
                numberAppearance = Bitmap.createBitmap(numberMap, 0, 0, numberMap.getWidth(), numberMap.getHeight(), numberMatrix, true);
                canvas.drawBitmap(numberAppearance, (screenWidth - numberAppearance.getWidth()) / 2f, (screenHeight - numberAppearance.getHeight()) / 2f, numberPaint);
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
        if (startAnimator != null) {
            startAnimator.removeAllListeners();
            startAnimator.cancel();
        }
    }
}
