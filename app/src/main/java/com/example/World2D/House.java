package com.example.World2D;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class House {
    private int xDisplacement = 0;
    private int yDisplacement = 0;
    private int x, y, size;
    private Path path = new Path();
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    House(int x, int y, int size){
        this.x = x;
        this.y = y;
        this.size = size;
    }
    void setDisplacement(int x, int y){
        this.xDisplacement = x;
        this.yDisplacement = y;
    }
    void draw(Canvas canvas){
        paint.setColor(Color.rgb(175, 200, 175));
        path.moveTo(x + xDisplacement - (size / 2), y - size / 2);
        path.lineTo(x + xDisplacement, y - size);
        path.lineTo(x + xDisplacement + (size / 2), y - size / 2);
        path.setFillType(Path.FillType.WINDING);
        path.close();
        canvas.drawPath(path, paint);
        path.reset();
        path.addRect(x + xDisplacement - size / 2, y - size / 2, x + xDisplacement + (size / 2), y + size / 2, Path.Direction.CW);
        paint.setColor(Color.rgb(200, 0, 0));
        canvas.drawPath(path, paint);
        path.reset();
        paint.setColor(Color.WHITE);
        path.addRect((x - size / 4) - size / 8, (y - size / 4) - size / 8, (x - size / 4) + size / 8, (y  - size / 4) + size / 8, Path.Direction.CW);
        path.addRect((x + size / 4) - size / 8, (y - size / 4) - size / 8, (x + size / 4) + size / 8, (y  - size / 4) + size / 8, Path.Direction.CW);
        canvas.drawPath(path, paint);
        path.reset();
        paint.setColor(Color.argb(100, 0, 120, 200));
        path.addRect((x - size / 4) - size / 8, (y - size / 4) - size / 8, (x - size / 4) + size / 8, (y  - size / 4) + size / 8, Path.Direction.CW);
        path.addRect((x + size / 4) - size / 8, (y - size / 4) - size / 8, (x + size / 4) + size / 8, (y  - size / 4) + size / 8, Path.Direction.CW);
        canvas.drawPath(path, paint);
        path.reset();
    }
}
