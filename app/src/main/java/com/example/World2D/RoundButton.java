package com.example.World2D;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class RoundButton {
    private boolean active = false;
    private int x;
    private int y;
    private int radius;
    private int r;
    private int g;
    private int b;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Arrow arrow;
    private boolean arrowIncluded = false;
    RoundButton(int x, int y, int radius){
        this.x = x;
        this.y = y;
        this.r = 0;
        this.g = 100;
        this.b = 200;
        this.radius = radius;
    }
    boolean getActive(){
        return active;
    }
    RoundButton(int x, int y, int radius, Arrow arrow){
        this.x = x;
        this.y = y;
        this.r = 0;
        this.g = 100;
        this.b = 200;
        this.radius = radius;
        this.arrow = arrow;
        arrowIncluded = true;
    }
    void handleTouchDown(float touchX, float touchY){
        touchX -= x;
        touchY -= y;
        float hypotenuse = (float)(Math.sqrt(((touchX * touchX) + (touchY * touchY))));
        active = hypotenuse <= radius;
    }
    void setColour(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
        if(arrowIncluded){
            arrow.setColour(r, g, b);
        }
    }
    void setLocation(int x, int y){
        this.x = x;
        this.y = y;
    }
    void setInactive(){
        active = false;
    }
//    int getR(){
//        return this.r;
//    }
//    int getG(){
//        return this.g;
//    }
//    int getB(){
//        return this.b;
//    }
//    int getX(){
//        return this.x;
//    }
//    int getY(){
//        return this.y;
//    }
    public void draw(Canvas canvas) {
        final int activeAlpha = 100;
        final int inactiveAlpha = 25;
        if(active){
            paint.setColor(Color.argb(activeAlpha, r, g, b));
        }
        else{
            paint.setColor(Color.argb(inactiveAlpha, r, g, b));
        }
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, radius, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.argb(255, r, g, b));
        canvas.drawCircle(x, y, radius, paint);
        if(arrowIncluded){
            arrow.draw(canvas);
        }
    }
    int getX(){
        return x;
    }
    int getY(){
        return y;
    }
}
