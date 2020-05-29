package com.example.World2D;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    private static final int Max_FPS = 60;
    private static double average_FPS;
    private final SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;
    void setRunning(boolean running){
        this.running = running;
    }
    MainThread(SurfaceHolder holder, GamePanel gamePanel){
        super();
        this.surfaceHolder = holder;
        this.gamePanel = gamePanel;
    }
    @Override
    public void run(){
        long startTime;
        long timeMillis = 1000/Max_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = timeMillis;
        while (running){
            startTime = System.nanoTime();
            canvas = null;
            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally {
                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;
            try {
                if (waitTime > 0){
                    sleep(waitTime);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == Max_FPS){
                average_FPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
            }
        }
    }
    static int getFPS(){
        return  round(average_FPS);
    }
    private static int round(double doubleNum){
        int intNum = (int)(doubleNum);
        double decimal = (doubleNum - intNum);
        if(decimal >= 0.5){
            intNum++;
        }
        return intNum;
    }
}