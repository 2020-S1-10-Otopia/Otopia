package com.example.World2D;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class BoundingCircle {
    private int x, y, radius;
    private boolean canDraw = false;
    private Utilities util = new Utilities();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    BoundingCircle(int x, int y, int radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
    }
    void setCanDraw(boolean b){
        canDraw = b;
    }
    void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }
    void setRadius(int radius){
        this.radius = radius;
    }
    int getX(){
        return x;
    }
    int getY(){
        return y;
    }
    int getRadius(){
        return radius;
    }
    void draw(Canvas canvas){
        if(canDraw){
            paint.setColor(Color.rgb(0, 150, 255));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(util.screenHeight() / 360);
            canvas.drawCircle(x, y, radius, paint);
        }
    }
}