package com.example.World2D;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class Joint{
    private int x, y;
    private int distance;
    private int cwRotationMax;
    private int ccwRotationMax;
    private float currentRotation;
    private boolean rotationLimited = false;
    private int anchorX;
    private int anchorY;
    private boolean isMirrored = false;
    private int r1 = 0, r2 = 0, g1 = 0, g2 = 0, b1 = 0, b2 = 0;
    private int width = 1;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Joint(int x, int y){
        this.x = x;
        this.y = y;
        this.anchorX = x;
        this.anchorY = y;
        this.currentRotation = 0;
    }
    Joint(int anchorX, int anchorY, int distance, float rotation){
        this.anchorX = anchorX;
        this.anchorY = anchorY;
        this.distance = distance;
        this.currentRotation = rotation;
        this.x = round(Math.sin(Math.toRadians(currentRotation)) * distance) + this.anchorX;
        this.y = -1 * (round(Math.cos(Math.toRadians(currentRotation)) * distance)) + this.anchorY;
    }
    Joint(){
        anchorX = 0;
        anchorY = 0;
        distance = 0;
        currentRotation = 0;
    }
    void setIsMirrored(boolean b){
        isMirrored = b;
        if(!isMirrored) {
            x = round(Math.sin(Math.toRadians(currentRotation / 2)) * distance) + anchorX;
            y = -1 * (round(Math.cos(Math.toRadians(currentRotation / 2)) * distance)) + anchorY;
        }
        else{
            x = round(Math.sin(Math.toRadians(360 - currentRotation / 2)) * distance + anchorX);
            y = -1 * (round(Math.cos(Math.toRadians(360 - currentRotation / 2)) * distance)) + anchorY;
        }
    }
    void setWidth(int w){
        width = w;
    }
    float getCurrentRotation(){
        return currentRotation;
    }
    void setAnchorLocation(int x, int y){
        this.anchorX = x;
        this.anchorY = y;
        if(!isMirrored) {
            this.x = round(Math.sin(Math.toRadians(currentRotation)) * distance) + anchorX;
            this.y = -1 * (round(Math.cos(Math.toRadians(currentRotation)) * distance)) + anchorY;
        }
        else{
            this.x = round(Math.sin(Math.toRadians(360 - currentRotation)) * distance + anchorX);
            this.y = -1 * (round(Math.cos(Math.toRadians(360 - currentRotation)) * distance)) + anchorY;
        }
    }
    int getAnchorX(){
        return anchorX;
    }
    int getAnchorY(){
        return anchorY;
    }
    int getCwRotationMax(){
        return cwRotationMax;
    }
    int getCcwRotationMax(){
        return ccwRotationMax;
    }
    int getX(){
        return x;
    }
    int getY(){
        return y;
    }
    int getDistance(){
        return distance;
    }
    void setBoneColour(int r, int g, int b){
        r1 = r;
        g1 = g;
        b1 = b;
    }
    void setJunctionColour(int r, int g, int b){
        r2 = r;
        g2 = g;
        b2 = b;
    }
    void setCurrentRotation(float rotation){
        currentRotation = rotation;
        while(currentRotation > 360){
            currentRotation -= 360;
        }
        while(currentRotation < 0){
            currentRotation += 360;
        }
        if(rotationLimited){
            if(currentRotation > cwRotationMax){
                currentRotation = cwRotationMax;
            }
            else if(currentRotation < ccwRotationMax){
                currentRotation = ccwRotationMax;
            }
        }
        if(!isMirrored) {
            x = round(Math.sin(Math.toRadians(currentRotation / 2)) * distance) + anchorX;
            y = -1 * (round(Math.cos(Math.toRadians(currentRotation / 2)) * distance)) + anchorY;
        }
        else{
            x = round(Math.sin(Math.toRadians(360 - currentRotation / 2)) * distance + anchorX);
            y = -1 * (round(Math.cos(Math.toRadians(360 - currentRotation / 2)) * distance)) + anchorY;
        }
    }
    void setRotationLimits(int cwLimit, int ccwLimit){
        cwRotationMax = cwLimit;
        ccwRotationMax = ccwLimit;
        rotationLimited = true;
    }
    void removeRotationLimits(){
        rotationLimited = false;
    }
    private static int round(double doubleNum){
        int intNum = (int)(doubleNum);
        double decimal = (doubleNum - intNum);
        if(decimal >= 0.5){
            intNum++;
        }
        return intNum;
    }
    void drawAll(Canvas canvas){
        paint.setStrokeWidth(width);
        paint.setColor(Color.rgb(r1, g1, b1));
        canvas.drawLine(anchorX, anchorY, x, y, paint);
        paint.setColor(Color.rgb(r2, g2, b2));
        canvas.drawCircle(x, y, round(width / 1.2), paint);
    }
    void drawJunction(Canvas canvas){
        paint.setColor(Color.rgb(r2, g2, b2));
        canvas.drawCircle(x, y, width, paint);
    }
    void drawBone(Canvas canvas){
        paint.setStrokeWidth(width);
        paint.setColor(Color.rgb(r1, g1, b1));
        canvas.drawLine(anchorX, anchorY, x, y, paint);
    }
}