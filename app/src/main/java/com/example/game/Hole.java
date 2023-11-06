package com.example.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Hole {

    int radius;
    Bitmap appearance;
    int x;
    int y;
    Point center;

    public Point getCenter() {
        return center;
    }

    public Hole(int radius, Bitmap appearance, int x, int y) {
        this.radius = radius;
        this.appearance = appearance;
        this.x = x;
        this.y = y;
        center = new Point(x, y);
    }

    public int getRadius() {
        return radius;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(appearance, x - radius, y - radius, paint);
    }

}
