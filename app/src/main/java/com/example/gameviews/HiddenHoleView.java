package com.example.gameviews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

import com.example.game.Calculation;
import com.example.game.Hole;
import com.example.game.Obstacle;
import com.example.game.Point;
import com.example.game.R;
import com.example.game.Vector;

import org.json.JSONException;


public class HiddenHoleView extends GameView{

    ValueAnimator startAnimator;
    Hole fallenHole;

    public HiddenHoleView(Context context, int levelNumber) throws JSONException {
        super(context, levelNumber);
        this.mode = 2;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        notAnimation = false;

        startAnimator = ValueAnimator.ofFloat(0, 4);

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
            }
        });

        startAnimator.start();
    }

    private void superSurfaceCreated() {
        super.surfaceCreated(surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        super.surfaceDestroyed(surfaceHolder);
        if (startAnimator != null) {
            startAnimator.removeAllListeners();
            startAnimator.cancel();
        }
    }

    @Override
    public void draw() {
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
            int alpha = (int) (255*2 * (0.5f - Math.abs(0.5f - value%1)));
            numberPaint.setAlpha(alpha);
            Bitmap numberMap;
            Matrix numberMatrix;
            Bitmap numberAppearance;
            float scale = 2 * (0.5f - Math.abs(0.5f - value%1));
            switch ((int)(value)) {
                case 0:
                    drawNumber(R.drawable.number_3, scale, numberPaint);
                    break;
                case 1:
                    drawNumber(R.drawable.number_2, scale, numberPaint);
                    break;
                case 2:
                    drawNumber(R.drawable.number_1, scale, numberPaint);
                    break;
                case 3:
                    float scale1 = value % 1f;
                    float scale2 = 1 - scale1;
                    System.out.println(scale1);
                    for (Hole hole : holes){
                        int x = hole.getX();
                        int y = hole.getY();
                        if (scale2 >= 0.005f && scale2 != 1){
                            Matrix matrix = new Matrix();
                            matrix.postScale(scale2, scale2);
                            Bitmap holeLook = Bitmap.createBitmap(holeAppearance, 0, 0, holeAppearance.getWidth(), holeAppearance.getHeight(), matrix, true);
                            canvas.drawBitmap(holeLook, x - hole.getRadius() * scale2, y - hole.getRadius() * scale2, paint);
                        }
                    }
                    if (scale1 >= 0.005f){
//                        int alpha1 = (int) (255*(value%1));
//                        numberPaint.setAlpha(alpha1);
                        numberMap = BitmapFactory.decodeResource(getResources(), R.drawable.grimace);
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
    public void detectCollision() {
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
                fallenHole = hole;
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

    @Override
    public void overDraw(float x, float y, int value, float radius) {
        try {
            canvas = surfaceHolder.lockCanvas();
            Paint ballPaint = new Paint();
            ballPaint.setAlpha(value);
            canvas.drawBitmap(background, 0, 0, paint);
            if (fallenHole != null && !successful) {
                fallenHole.draw(canvas, paint);
//                Bitmap fallAppearance = Bitmap.createBitmap(holeAppearance, 0, 0, holeAppearance.getWidth(), holeAppearance.getHeight());
//                canvas.drawBitmap(fallAppearance, fallenHole.getX() - fallenHole.getRadius(), fallenHole.getY() - fallenHole.getRadius(), paint);
            }
            for (Obstacle obstacle : obstacles) {
                obstacle.draw(canvas, paint);
            }
            successHole.draw(canvas, paint);

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

    private void drawNumber(int id, float scale, Paint numberPaint) {
        for (Hole hole : holes){
            hole.draw(canvas, paint);
        }
        if (scale >= 0.005f){
            Bitmap numberMap = BitmapFactory.decodeResource(getResources(), id);
            Matrix numberMatrix = new Matrix();
            numberMatrix.postScale(scale * ((float) screenWidth / 3f / (float) numberMap.getWidth()), scale * ((float) screenWidth / 3f / (float) numberMap.getHeight()));
            Bitmap numberAppearance = Bitmap.createBitmap(numberMap, 0, 0, numberMap.getWidth(), numberMap.getHeight(), numberMatrix, true);
            canvas.drawBitmap(numberAppearance, (screenWidth - numberAppearance.getWidth()) / 2f, (screenHeight - numberAppearance.getHeight()) / 2f, numberPaint);
        }
    }
}