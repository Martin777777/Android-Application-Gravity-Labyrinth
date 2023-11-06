package com.example.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Obstacle {

    int x;
    int y;
    int width;
    int height;
    Bitmap appearance;
    Point pointLT;
    Point pointLB;
    Point pointRT;
    Point pointRB;
    Point[] points;

    public Point getPointLT() {
        return pointLT;
    }

    public Point getPointLB() {
        return pointLB;
    }

    public Point getPointRT() {
        return pointRT;
    }

    public Point getPointRB() {
        return pointRB;
    }

    public Obstacle(Bitmap appearance, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.appearance = appearance;
        pointLT = new Point(x, y);
        pointLB = new Point(x, y + height);
        pointRT = new Point(x + width, y);
        pointRB = new Point(x + width, y + height);
        points = new Point[]{ pointLT, pointLB, pointRT, pointRB };
    }

    public int getLeft() {
        return x;
    }

    public int getTop() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getRight() {
        return x + width;
    }

    public int getBottom() {
        return y + height;
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(appearance, x, y, paint);
    }

    public Point[] getPoints() {
        return points;
    }
}
