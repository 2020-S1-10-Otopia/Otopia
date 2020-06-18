package com.example.World2D;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.util.Random;

class BoundingBox {
    private int x, y, width, height;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean canDraw = false;
    private Utilities util = new Utilities();
    private boolean rightCollision = false;
    private boolean leftCollision = false;
    private boolean topCollision = false;
    private boolean bottomCollision = false;
    private Random rand = new Random();
    private int r = rand.nextInt(255), rTarget = rand.nextInt(255), g = rand.nextInt(255), gTarget = rand.nextInt(255), b = rand.nextInt(255), bTarget = rand.nextInt(255);
    BoundingBox(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }
    void setBounds(int width, int height){
        this.width = width;
        this.height = height;
    }
    int getX(){
        return this.x;
    }
    int getY(){
        return this.y;
    }
    int getWidth(){
        return this.width;
    }
    int getHeight(){
        return this.height;
    }
    void setLeftCollision(boolean b){
        this.leftCollision = b;
    }
    void setRightCollision(boolean b){
        this.rightCollision = b;
    }
    void setTopCollision(boolean b){
        this.topCollision = b;
    }
    void setBottomCollision(boolean b){
        this.bottomCollision = b;
    }
    boolean getLeftCollision(){
        return leftCollision;
    }
    boolean getRightCollision(){
        return rightCollision;
    }
    boolean getTopCollision(){
        return topCollision;
    }
    boolean getBottomCollision(){
        return bottomCollision;
    }
    void setCollisionsFalse(){
        leftCollision = false;
        rightCollision = false;
        topCollision = false;
        bottomCollision = false;
    }
    void setCanDraw(boolean b){
        this.canDraw = b;
    }
    void draw(Canvas canvas){
        if(canDraw) {
            if(r < rTarget){
                r++;
            }
            else if(r > rTarget){
                r--;
            }
            else{
                rTarget = rand.nextInt(255);
                if(r < rTarget){
                    r++;
                }
                else if(r > rTarget){
                    r--;
                }
            }
            if(g < gTarget){
                g++;
            }
            else if(g > gTarget){
                g--;
            }
            else{
                gTarget = rand.nextInt(255);
                if(g < gTarget){
                    g++;
                }
                else if(g > gTarget){
                    g--;
                }
            }
            if(b < bTarget){
                b++;
            }
            else if(b > bTarget){
                b--;
            }
            else{
                bTarget = rand.nextInt(255);
                if(b < rTarget){
                    b++;
                }
                else if(b > bTarget){
                    b--;
                }
            }
            if(g < gTarget){
                g++;
            }
            else if(g > gTarget){
                g--;
            }
            else{
                gTarget = rand.nextInt(255);
                if(g < gTarget){
                    g++;
                }
                else if(g > gTarget){
                    g--;
                }
            }
            paint.setColor(Color.rgb(r, g, b));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(util.screenHeight() / 360);
            canvas.drawRect(x - (width / 2), y - (height / 2), x + (width / 2), y + (height / 2), paint);
        }
    }
}
