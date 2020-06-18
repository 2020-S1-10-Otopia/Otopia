package com.example.World2D;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.util.Random;

public class Platform implements GameObject {
    private int width, height;
    private float x, y;
    private int r = 0, g = 0, b = 0;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Utilities util = new Utilities();
    private boolean canDraw = false;
    private boolean boxCollision = false;
    private int rightEdge;
    private int leftEdge;
    private int topEdge;
    private int bottomEdge;
    private int xDisplacement = 0;
    private int yDisplacement = -720;
    private Random rand = new Random();
    private int rTarget = rand.nextInt(255);
    private int gTarget = rand.nextInt(255);
    private int bTarget = rand.nextInt(255);


    Platform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        leftEdge = x - (width / 2);
        rightEdge = x + (width / 2);
        topEdge = y - (height / 2);
        bottomEdge = y + (height / 2);
    }
    void setColour(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    void setColour(){
        if(r<rTarget){
            r++;
        }
        else if(r > rTarget){
            r--;
        }
        else{
            rTarget = rand.nextInt(255);
            if(r<rTarget){
                r++;
            }
            else if(r > rTarget){
                r--;
            }
        }
        if(g<gTarget){
            g++;
        }
        else if(g > gTarget){
            g--;
        }
        else{
            gTarget = rand.nextInt(255);
            if(g<gTarget){
                g++;
            }
            else if(g > gTarget){
                g--;
            }
        }
        if(b<bTarget){
            b++;
        }
        else if(b > bTarget){
            b--;
        }
        else{
            bTarget = rand.nextInt(255);
            if(b<bTarget){
                b++;
            }
            else if(b > bTarget){
                b--;
            }
        }

    }
    //    void setPosition(float x, float y) {
//        this.x = x;
//        this.y = y;
//        leftEdge = round((x + xDisplacement) - round(width / 2));
//        rightEdge = round((x + xDisplacement) + round(width / 2));
//        topEdge = round((y + yDisplacement) - round(height / 2));
//        bottomEdge = round((y + yDisplacement) + round(height / 2));
//    }
//    void setBounds(int width, int height) {
//        this.width = width;
//        this.height = height;
//        leftEdge = round(x - round(width / 2));
//        rightEdge = round(x + round(width / 2));
//        topEdge = round(y - round(height / 2));
//        bottomEdge = round(y + round(height / 2));
//    }
    boolean getCanDraw(){
        return this.canDraw;
    }
    //    boolean getBoxCollision(){
//        return boxCollision;
//    }
    @Override
    public void update() {

    }
    @Override
    public void draw(Canvas canvas) {
        paint.setColor(Color.rgb(r, g, b));
        if (/*((rightEdge + xDisplacement) >= util.screenWidth() * 0) && ((rightEdge + xDisplacement) <= util.screenWidth()) && ((bottomEdge + yDisplacement) >= util.screenHeight() * 0) && ((bottomEdge + yDisplacement) <= util.screenHeight()) ||

                ((rightEdge + xDisplacement) >= util.screenWidth() * 0) && ((rightEdge + xDisplacement) <= util.screenWidth()) && ((topEdge + yDisplacement) >= util.screenHeight() * 0) && ((topEdge + yDisplacement) <= util.screenHeight()) ||

                ((leftEdge + xDisplacement) >= util.screenWidth() * 0) && ((leftEdge + xDisplacement) <= util.screenWidth()) && ((bottomEdge + yDisplacement) >= util.screenHeight() * 0) && ((bottomEdge + yDisplacement) <= util.screenHeight()) ||

                ((leftEdge + xDisplacement) >= util.screenWidth() * 0) && ((leftEdge + xDisplacement) <= util.screenWidth()) && ((topEdge + yDisplacement) >= util.screenHeight() * 0) && ((topEdge + yDisplacement) <= util.screenHeight()) ||
*/
                ((topEdge + yDisplacement >= 0 && topEdge + yDisplacement <= util.screenHeight()) || (bottomEdge + yDisplacement >= 0 && bottomEdge + yDisplacement <= util.screenHeight()) || (leftEdge + xDisplacement >= 0 && leftEdge + xDisplacement <= util.screenWidth()) || (rightEdge + xDisplacement >= 0 && rightEdge + xDisplacement <= util.screenWidth()))){

            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect((x + xDisplacement) - round(width / 2), (y + yDisplacement) - round(height / 2), (x + xDisplacement) + round(width / 2), (y + yDisplacement) + round(height / 2), paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setColor(Color.rgb(30, 30, 30));
            canvas.drawRect((x + xDisplacement) - round(width / 2) + 2, (y + yDisplacement) - round(height / 2) + 2, (x + xDisplacement) + round(width / 2) - 2, (y + yDisplacement) + round(height / 2) - 2, paint);
            canDraw = true;
        }
        else{
            canDraw = false;
        }
    }
    boolean checkCollision(BoundingBox box){
        int bLeft = box.getX() - (box.getWidth() / 2);
        int bRight = box.getX() + (box.getWidth() / 2);
        int bTop = box.getY() - (box.getHeight() / 2);
        int bBottom = box.getY() + (box.getHeight() / 2);
        this.boxCollision = ((bRight >= (x + xDisplacement) - width / 2) && (bRight <= (x + xDisplacement) + width / 2) && (bTop >= (y + yDisplacement) - height / 2) && (bTop <= (y + yDisplacement) + height / 2)) ||
                ((bRight >= (x + xDisplacement) - width / 2) && (bRight <= (x + xDisplacement) + width / 2) && (bBottom >= (y + yDisplacement) - height / 2) && (bBottom <= (y + yDisplacement) + height / 2)) ||
                ((bLeft >= (x + xDisplacement) - width / 2) && (bLeft <= (x + xDisplacement) + width / 2) && (bTop >= (y + yDisplacement) - height / 2) && (bTop <= (y + yDisplacement) + height / 2)) ||
                ((bLeft >= (x + xDisplacement) - width / 2) && (bLeft <= (x + xDisplacement) + width / 2) && (bBottom >= (y + yDisplacement) - height / 2) && (bBottom <= (y + yDisplacement) + height / 2));

        return((bRight >= (x + xDisplacement) - width / 2) && (bRight <= (x + xDisplacement) + width / 2) && (bTop >= (y + yDisplacement) - height / 2) && (bTop <= (y + yDisplacement) + height / 2)) ||
                ((bRight >= (x + xDisplacement) - width / 2) && (bRight <= (x + xDisplacement) + width / 2) && (bBottom >= (y + yDisplacement) - height / 2) && (bBottom <= (y + yDisplacement) + height / 2)) ||
                ((bLeft >= (x + xDisplacement) - width / 2) && (bLeft <= (x + xDisplacement) + width / 2) && (bTop >= (y + yDisplacement) - height / 2) && (bTop <= (y + yDisplacement) + height / 2)) ||
                ((bLeft >= (x + xDisplacement) - width / 2) && (bLeft <= (x + xDisplacement) + width / 2) && (bBottom >= (y + yDisplacement) - height / 2) && (bBottom <= (y + yDisplacement) + height / 2));
    }
    int getWidth(){
        return width;
    }
    int getHeight(){
        return height;
    }
    //    void handleCharacterCollision(BoundingBox box){
//        int bLeft = box.getX() - (box.getWidth() / 2);
//        int bRight = box.getX() + (box.getWidth() / 2);
//        int bTop = box.getY() - (box.getHeight() / 2);
//        int bBottom = box.getY() + (box.getHeight() / 2);
//        int topDist;
//        int leftDist;
//        int rightDist;
//        int bottomDist;
//        boxCollision =
//                ((bRight >= (x + xDisplacement) - width / 2) && (bRight <= (x + xDisplacement) + width / 2) && (bTop >= (y + yDisplacement) - height / 2) && (bTop <= (y + yDisplacement) + height / 2)) ||
//                        ((bRight >= (x + xDisplacement) - width / 2) && (bRight <= (x + xDisplacement) + width / 2) && (bBottom >= (y + yDisplacement) - height / 2) && (bBottom <= (y + yDisplacement) + height / 2)) ||
//                        ((bLeft >= (x + xDisplacement) - width / 2) && (bLeft <= (x + xDisplacement) + width / 2) && (bTop >= (y + yDisplacement) - height / 2) && (bTop <= (y + yDisplacement) + height / 2)) ||
//                        ((bLeft >= (x + xDisplacement) - width / 2) && (bLeft <= (x + xDisplacement) + width / 2) && (bBottom >= (y + yDisplacement) - height / 2) && (bBottom <= (y + yDisplacement) + height / 2));
//        if (boxCollision) {
//            topDist = round(Math.abs(bBottom - ((y + yDisplacement) - height / 2)));
//            leftDist = round(Math.abs(bRight - ((x + xDisplacement) - width / 2)));
//            rightDist = round(Math.abs(bLeft - ((x + xDisplacement) + width / 2)));
//            bottomDist = round(Math.abs(bTop - ((y + yDisplacement) + height / 2)));
//            if (topDist <= leftDist && topDist <= rightDist && topDist <= bottomDist) {
//                box.setPosition(box.getX(), round(((y + yDisplacement) - round(height / 2)) - round(box.getHeight() / 2)));
//                box.setBottomCollision(true);
//            }
//            if (leftDist < topDist && leftDist < rightDist && leftDist < bottomDist) {
//                box.setPosition(round((x + xDisplacement) - round(width / 2)) - round(box.getWidth() / 2), box.getY());
//                box.setRightCollision(true);
//            }
//            if (rightDist < topDist && rightDist < leftDist && rightDist < bottomDist) {
//                box.setPosition(round((x + xDisplacement) + round(width / 2)) + round(box.getWidth() / 2), box.getY());
//                box.setLeftCollision(true);
//            }
//            if(bottomDist < topDist && bottomDist <= leftDist && bottomDist <= rightDist) {
//                box.setPosition(box.getX(), round(((y + yDisplacement) + round(height / 2)) + round(box.getHeight() / 2)));
//                box.setTopCollision(true);
//            }
//        }
//        else {
//            box.setCollisionsFalse();
//        }
//    }
    void setDisplacement(int xDisplacement, int yDisplacement){
        this.xDisplacement = xDisplacement;
        this.yDisplacement = yDisplacement;
    }
    int getXDisplacement(){
        return xDisplacement;
    }
    int getYDisplacement(){
        return yDisplacement;
    }
    //    int getXOverlap(BoundingBox box){
//        int num = 0;
//        if(boxCollision){
//            num = ((box.getWidth() / 2) + (width / 2)) -  box.getX() + round(x);
//        }
//        return num;
//    }
//    int getYOverlap(BoundingBox box){
//        int num = 0;
//        if(boxCollision){
//            num = ((box.getHeight() / 2) + (height / 2)) -  box.getY() + round(y);
//        }
//        return num;
//    }
    float getX(){
        return x + xDisplacement;
    }
    float getY(){
        return y + yDisplacement;
    }
    private static int round ( float floatNum){
        int intNum = (int) (floatNum);
        float decimal = floatNum - intNum;
        if (decimal >= 0.5) {
            intNum++;
        }
        return intNum;
    }
}