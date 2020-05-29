package com.example.World2D;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class StatusBar {
    private boolean isVisible = true;
    private float maxHealth;
    private float currentHealth;
    private int xLocation;
    private int yLocation;
    private float length;
    private int width;
    private String colour;
    public static final int DISPLAY_PERCENT = 0;
    public static final int DISPLAY_FRACTION = 1;
    public static final int NO_DISPLAY = 2;
    private int display;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Utilities util = new Utilities();

    StatusBar(float maxHealth, int xLocation, int yLocation, float length, int width, String colour){
        this.maxHealth = maxHealth;
        currentHealth = round(maxHealth);
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.length = length;
        this.width = width;
        if(!colour.equalsIgnoreCase("blue") && !colour.equalsIgnoreCase("green")){
            this.colour = "green";
        }
        else{
            this.colour = colour;
        }
    }
    StatusBar(float maxHealth, int xLocation, int yLocation, float length, int width){
        this.maxHealth = maxHealth;
        currentHealth = round(maxHealth);
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.length = length;
        this.width = width;
    }
    StatusBar(float maxHealth, int xLocation, int yLocation, float length, int width, int display){
        this.maxHealth = maxHealth;
        currentHealth = round(maxHealth);
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.length = length;
        this.width = width;
        this.display = display;
    }
    StatusBar(float maxHealth, int xLocation, int yLocation, float length, int width, int display, String colour){
        this.maxHealth = maxHealth;
        currentHealth = round(maxHealth);
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.length = length;
        this.width = width;
        this.display = display;
        if(!colour.equalsIgnoreCase("blue") && !colour.equalsIgnoreCase("green")){
            this.colour = "green";
        }
        else{
            this.colour = colour;
        }
    }
    public void draw(Canvas canvas) {
        int r;
        int g;
        int b;
        if(isVisible){
//            allowing more accurate
//            float currentHealthF = (float)(currentHealth);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(round(util.screenWidth() / 1280) * 3);
            canvas.drawRect(xLocation - round(length / 2), yLocation - round(width / 2), xLocation + round(length / 2), yLocation + round(width / 2), paint);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(width);
            paint.setColor(Color.rgb(150, 150, 150));
            canvas.drawLine(xLocation - round(length / 2), yLocation, xLocation + round(length / 2), yLocation, paint);
            float pixelsPerHP = length / maxHealth;
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(width);
            r = round(1 - (currentHealth / maxHealth * 255));
            if (colour.equalsIgnoreCase("green")) {
                g = round((currentHealth / maxHealth * 255));
                b = 0;
            }
            else{
                g = 0;
                b = round((currentHealth / maxHealth) * 255);
            }
            paint.setColor(Color.rgb(r, g, b));
            paint.setTextSize(width);
            canvas.drawLine(xLocation - round(length / 2), yLocation, xLocation - round(length / 2) + round(pixelsPerHP * currentHealth), yLocation, paint);
            paint.setColor(Color.WHITE);
            if(display == DISPLAY_FRACTION) {
                canvas.drawText("" + round(currentHealth) + "/" + round(maxHealth), (xLocation - round(length / 2)) + (util.screenWidth() / 1280) * 10, yLocation + round(0.35 * width), paint);
            }
            else if(display == DISPLAY_PERCENT){
                canvas.drawText("" + round((currentHealth / maxHealth) * 100)  + "%", (xLocation - round(length / 2)) + (util.screenWidth() / 1280) * 10, yLocation + round(0.35 * width), paint);
            }
        }
    }
    private static int round(double floatNum){
        int intNum = (int)(floatNum);
        float decimal = (float) (floatNum - intNum);
        if(decimal >= 0.5){
            intNum++;
        }
        return intNum;
    }
    void setCurrentHealth(int health){
        if(health > maxHealth){
            this.currentHealth = round(maxHealth);
        }
        else if(health < 0){
            this.currentHealth = 0;
        }
        else{
            this.currentHealth = health;
        }
    }
    void setMaxHealth(int maxHealth){
        this.maxHealth = maxHealth;
    }
    int getMaxHealth(){
        return round(maxHealth);
    }
    int getCurrentHealth(){
        return round(currentHealth);
    }
    void toggleVisible(){
        isVisible = !isVisible;
    }
}
