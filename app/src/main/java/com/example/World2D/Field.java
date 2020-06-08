package com.example.World2D;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class Field implements GameObject{
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    int x, y, width, height, xDisplacement = 0, yDisplacement = 0;
    Field(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    boolean check(Point point){
        return point.x >= x - (width / 2) && point.x <= x + (width / 2) && point.y >= y - (height / 2) && point.y <= y + (height / 2);
    }
    @Override
    public void update() {

    }
    @Override
    public void draw(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(50, 255, 175, 0));
        canvas.drawRect(x - (width / 2), y - (height / 2), x + (width / 2), y + (height / 2), paint);
    }
}