package com.example.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball {

    int radius;
    Bitmap appearance;
    volatile float x;
    volatile float y;
    volatile float speedX;
    volatile float speedY;
    volatile Point center;

    public Point getCenter() {
        return center;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public void setCenter(Point center) {
        this.center = center;
        this.x = center.x;
        this.y = center.y;
    }

    public Ball(int radius, Bitmap appearance, int x, int y) {
        this.radius = radius;
        this.appearance = appearance;
        this.x = x;
        this.y = y;
        center = new Point(x, y);

        this.speedX = 0;
        this.speedY = 0;
    }

    public int getRadius() {
        return radius;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSpeedX() {
        return speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void move() {
        this.x = this.x + speedX;
        this.y = this.y + speedY;
    }

    public void lateralForce(float gravity) {
        this.speedX += gravity;
    }

    public void longitudinalForce(float gravity) {
        this.speedY += gravity;
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(appearance, x - radius, y - radius, paint);
    }

    public float getTop(){
        return this.y - this.radius;
    }

    public float getBottom(){
        return this.y + this.radius;
    }

    public float getLeft(){
        return this.x - this.radius;
    }

    public float getRight(){
        return (this.x + this.radius);
    }

    public void reboundX(){
        if (Math.abs(this.speedX * 0.3f) < 1) {
            this.speedX = 0;
        }
        else {
            this.speedX = - this.speedX * 0.3f;
        }
    }

    public void reboundY(){
        if (Math.abs(this.speedY * 0.3f) < 1) {
            this.speedY = 0;
        }
        else {
            this.speedY = - this.speedY * 0.3f;
        }
    }

    public void setX(float x) {
        this.x = x;
        center.setX(x);
    }

    public void setY(float y) {
        this.y = y;
        center.setY(y);
    }

    public void touchCorner(Point corner, int i){
        Vector speed = Calculation.findCornerSpeed(this.speedX, this.speedY, this.center, corner, i);
        this.speedX = speed.x;
        this.speedY = speed.y;
    }

    public void stop(){
        this.speedX = 0;
        this.speedY = 0;
    }


}
