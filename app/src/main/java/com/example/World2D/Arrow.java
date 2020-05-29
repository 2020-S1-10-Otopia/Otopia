package com.example.World2D;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

class Arrow {
    private int r, g, b;
    private Path path = new Path();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Point point = new Point();
    private Point leftCorner = new Point();
    private Point rightCorner = new Point();
    private Point backLeftCorner = new Point();
    private Point backRightCorner = new Point();
    private Point leftInnerCorner = new Point();
    private Point rightInnerCorner = new Point();
    Arrow(int angle, int size, int x, int y){
        point.set(round(size*Math.cos(Math.toRadians(angle)) + x), round(size * Math.sin(Math.toRadians(angle)) + y));
        leftCorner.set(round(size * 0.7 * Math.cos(Math.toRadians(angle - 90)) + x), round(size * 0.7 * Math.sin(Math.toRadians(angle - 90)) + y));
        rightCorner.set(round(size * 0.7 * Math.cos(Math.toRadians(angle + 90)) + x), round(size * 0.7 * Math.sin(Math.toRadians(angle + 90)) + y));
        leftInnerCorner.set(round(size * 0.33 * Math.cos(Math.toRadians(angle - 90)) + x), round(size * 0.33 * Math.sin(Math.toRadians(angle - 90)) + y));
        rightInnerCorner.set(round(size * 0.33 * Math.cos(Math.toRadians(angle + 90)) + x), round(size * 0.33 * Math.sin(Math.toRadians(angle + 90)) + y));
        backLeftCorner.set(round(size*Math.cos(Math.toRadians(angle - 160)) + x), round(size * Math.sin(Math.toRadians(angle - 160)) + y));
        backRightCorner.set(round(size*Math.cos(Math.toRadians(angle + 160)) + x), round(size * Math.sin(Math.toRadians(angle + 160)) + y));
    }
    void setColour(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }
    void draw(Canvas canvas){
        paint.setColor(Color.rgb(r, g, b));
        paint.setStrokeWidth(3);
        path.setFillType(Path.FillType.WINDING);
        path.moveTo(point.x, point.y);
        path.lineTo(leftCorner.x, leftCorner.y);
        path.lineTo(leftInnerCorner.x, leftInnerCorner.y);
        path.lineTo(backLeftCorner.x, backLeftCorner.y);
        path.lineTo(backRightCorner.x, backRightCorner.y);
        path.lineTo(rightInnerCorner.x, rightInnerCorner.y);
        path.lineTo(rightCorner.x, rightCorner.y);
        path.lineTo(point.x, point.y);
        path.close();
        canvas.drawPath(path, paint);
        path.reset();
    }
    private static int round(double doubleNum){
        int intNum = (int)(doubleNum);
        float decimal = (float) (doubleNum - intNum);
        if(decimal >= 0.5){
            intNum++;
        }
        return intNum;
    }
}