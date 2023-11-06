package com.example.game;

import android.graphics.Path;

public class Calculation {

    private static int ballRadius;

    public static void setBallRadius(int ballRadius) {
        Calculation.ballRadius = ballRadius;
    }

    public static Point findCenterPoint( Point ballCenter, Point holeCorner ){
        float distance = ballCenter.MinusPoint(holeCorner).length();
        float scale = ballRadius / distance;
        float newX = (ballCenter.x - holeCorner.x) * scale + holeCorner.x;
        float newY = (ballCenter.y - holeCorner.y) * scale + holeCorner.y;
        return new Point(newX, newY);
    }

    public static Vector findCornerSpeed( float speedX, float speedY, Point previousCenter, Point corner, int i ){
        Vector speed = new Vector(speedX, speedY);
        Vector direction = corner.MinusPoint(previousCenter).Normalize();
        if (((speedX > 0 && direction.x > 0) || (speedX < 0 && direction.x < 0)) && ((speedY > 0 && direction.y > 0) || (speedY < 0 && direction.y < 0))) {
            Vector newSpeed = direction.byScalar(Math.abs(speed.dot(direction))).NegateVector();
            if (newSpeed.length() * 0.3f > 1) {
                return new Vector(newSpeed.x * 0.3f, newSpeed.y * 0.3f);
            }
            else {
                return new Vector(0, 0);
            }
        }
        else {
            float velocityX = 0;
            float velocityY = 0;
            Vector forceX = new Vector(speedX, 0);
            Vector forceY = new Vector(0, speedY);
            Vector normal = direction.Normal();

            switch (i){
                case 0:
                    if (speedX > 0){
                        Vector vector = normal.byScalar(forceX.dot(normal));
                        velocityX += Math.abs(vector.x);
                        velocityY += - Math.abs(vector.y);
                    }
                    else {
                        velocityX += speedX;
                    }

                    if (speedY > 0){
                        Vector vector = normal.byScalar(forceY.dot(normal));
                        velocityX += - Math.abs(vector.x);
                        velocityY += Math.abs(vector.y);
                    }
                    else {
                        velocityY += speedY;
                    }
                    break;
                case 1:
                    if (speedX > 0){
                        Vector vector = normal.byScalar(forceX.dot(normal));
                        velocityX += Math.abs(vector.x);
                        velocityY += Math.abs(vector.y);
                    }
                    else {
                        velocityX += speedX;
                    }

                    if (speedY > 0){
                        velocityY += speedY;
                    }
                    else {
                        Vector vector = normal.byScalar(forceY.dot(normal));
                        velocityX += - Math.abs(vector.x);
                        velocityY += - Math.abs(vector.y);
                    }
                    break;
                case 2:
                    if (speedX > 0){
                        velocityX += speedX;
                    }
                    else {
                        Vector vector = normal.byScalar(forceX.dot(normal));
                        velocityX += - Math.abs(vector.x);
                        velocityY += - Math.abs(vector.y);
                    }

                    if (speedY > 0){
                        Vector vector = normal.byScalar(forceY.dot(normal));
                        velocityX += Math.abs(vector.x);
                        velocityY += Math.abs(vector.y);
                    }
                    else {
                        velocityY += speedY;
                    }
                    break;
                case 3:
                    if (speedX > 0){
                        velocityX += speedX;
                    }
                    else {
                        Vector vector = normal.byScalar(forceX.dot(normal));
                        velocityX += - Math.abs(vector.x);
                        velocityY += Math.abs(vector.y);
                    }

                    if (speedY > 0){
                        velocityY += speedY;
                    }
                    else {
                        Vector vector = normal.byScalar(forceY.dot(normal));
                        velocityX += Math.abs(vector.x);
                        velocityY += - Math.abs(vector.y);
                    }
            }

            return new Vector(velocityX, velocityY);
        }
    }

    public static Vector findCornerForce( float gravityX, float gravityY, Point center, Point corner, int i ){
        Vector direction = corner.MinusPoint(center);
        float accelerationX = 0;
        float accelerationY = 0;
        Vector forceX = new Vector(gravityX, 0);
        Vector forceY = new Vector(0, gravityY);
        Vector normal = direction.Normal();

        switch (i){
            case 0:
                if (gravityX > 0){
                    Vector vector = normal.byScalar(forceX.dot(normal));
                    accelerationX += Math.abs(vector.x);
                    accelerationY += - Math.abs(vector.y);
                }
                else {
                    accelerationX += gravityX;
                }

                if (gravityY > 0){
                    Vector vector = normal.byScalar(forceY.dot(normal));
                    accelerationX += - Math.abs(vector.x);
                    accelerationY += Math.abs(vector.y);
                }
                else {
                    accelerationY += gravityY;
                }
                break;
            case 1:
                if (gravityX > 0){
                    Vector vector = normal.byScalar(forceX.dot(normal));
                    accelerationX += Math.abs(vector.x);
                    accelerationY += Math.abs(vector.y);
                }
                else {
                    accelerationX += gravityX;
                }

                if (gravityY > 0){
                    accelerationY += gravityY;
                }
                else {
                    Vector vector = normal.byScalar(forceY.dot(normal));
                    accelerationX += - Math.abs(vector.x);
                    accelerationY += - Math.abs(vector.y);
                }
                break;
            case 2:
                if (gravityX > 0){
                    accelerationX += gravityX;
                }
                else {
                    Vector vector = normal.byScalar(forceX.dot(normal));
                    accelerationX += - Math.abs(vector.x);
                    accelerationY += - Math.abs(vector.y);
                }

                if (gravityY > 0){
                    Vector vector = normal.byScalar(forceY.dot(normal));
                    accelerationX += Math.abs(vector.x);
                    accelerationY += Math.abs(vector.y);
                }
                else {
                    accelerationY += gravityY;
                }
                break;
            case 3:
                if (gravityX > 0){
                    accelerationX += gravityX;
                }
                else {
                    Vector vector = normal.byScalar(forceX.dot(normal));
                    accelerationX += - Math.abs(vector.x);
                    accelerationY += Math.abs(vector.y);
                }

                if (gravityY > 0){
                    accelerationY += gravityY;
                }
                else {
                    Vector vector = normal.byScalar(forceY.dot(normal));
                    accelerationX += Math.abs(vector.x);
                    accelerationY += - Math.abs(vector.y);
                }
        }

        return new Vector(accelerationX, accelerationY);
    }

    public static Path getPath(Point start, Point end){
        Path path = new Path();
        path.moveTo(start.x, start.y);
        path.lineTo(end.x, end.y);
        return path;
    }

}
