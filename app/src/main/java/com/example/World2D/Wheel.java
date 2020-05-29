package com.example.World2D;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class Wheel {
    private float rotationalVelocity = 0;
    private int x, y, r, g, b, radius;
    private ArrayList<Joint> list = new ArrayList<>();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Wheel(int x, int y, int radius, int spokes){
        this.x = x;
        this.y = y;
        this.radius = radius;
        if(spokes <= 1){
            spokes = 2;
        }
        if(radius<=1){
            radius = 2;
        }
        for(int i = 0; i < spokes; i++){
            list.add(new Joint(x, y, radius, i * 360 / spokes));
        }
    }
    void setRotationalVelocity(float num){
        rotationalVelocity = num;
    }
    float getRotationalVelocity(){
        return rotationalVelocity;
    }
    void rotate(){
        for(int i = 0; i < list.size(); i++){
            list.get(i).setCurrentRotation(list.get(i).getCurrentRotation() + rotationalVelocity);
        }
    }
    void setLocation(int x, int y){
        this.x = x;
        this.y = y;
    }
    void setColour(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }
    void draw(Canvas canvas){
        paint.setColor(Color.rgb(r, g, b));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        canvas.drawCircle(x, y, radius, paint);
        for(int i = 0; i<list.size(); i++){
            list.get(i).setWidth(5);
            list.get(i).drawAll(canvas);
        }
    }
}