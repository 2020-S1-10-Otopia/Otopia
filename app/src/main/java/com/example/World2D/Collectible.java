package com.example.World2D;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.Random;

public class Collectible {
    int type;
    int x;
    int y;
    int size;
    int xDisplacement = 0;
    int yDisplacement = 0;
    Random rand = new Random();
    Path path = new Path();
    int r = rand.nextInt(255), g = rand.nextInt(255), b = rand.nextInt(255), rTarget = rand.nextInt(255), gTarget = rand.nextInt(255), bTarget = rand.nextInt(255);
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Collectible(int x, int y, int size, int type){
        this.x = x;
        this.y = y;
        this.size = size;
        this.type = type;
    }
    void draw(Canvas canvas){
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
        canvas.drawCircle(x + xDisplacement, y + yDisplacement, size, paint);
    }
    void setDisplacement(int xDis, int yDis){
        xDisplacement = xDis;
        yDisplacement = yDis;
    }
    boolean checkCollision(BoundingBox box){
        return true;
    }
}
