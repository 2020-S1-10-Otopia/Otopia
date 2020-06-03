package com.example.World2D;

import android.graphics.Canvas;

import java.util.ArrayList;

class Environment {
    Utilities util = new Utilities();
    ArrayList<Platform>mainList = new ArrayList<>();

    Environment(){
        mainList.add(new Platform(9640, 1280, 20000, 100));
        mainList.add(new Platform(3000, 1080, 300, 300));
        unpack(new Passage(6000, -350, 150, 3000,200,true));
        mainList.add(new Platform(6175, 1190, 150, 80));
        unpack(new Passage(6000, -1925, 1000, 150, 200, true));
        mainList.add(new Platform(3900, -1925, 2000, 150));
        mainList.add(new Platform(2975, -2250, 150, 800));
        mainList.add(new Platform(3275, -2675, 750, 150));
        mainList.add(new Platform(4950, -2280, 100, 100));
        mainList.add(new Platform(4300, -2480, 100, 100));
    }
    ArrayList<Platform>getCanDrawList(){
        ArrayList<Platform>canDrawList = new ArrayList<>();
        for (int i = 0; i<mainList.size(); i++){
            if(mainList.get(i).getCanDraw()){
                canDrawList.add(mainList.get(i));
            }
        }
        return canDrawList;
    }
    void draw(Canvas canvas){
        for (int i = 0; i < mainList.size(); i++) {
            mainList.get(i).draw(canvas);
        }

    }
    void setDisplacement(int xVal, int yVal){
        for(int i = 0; i<mainList.size(); i++){
            mainList.get(i).setDisplacement(mainList.get(i).getXDisplacement() + xVal, mainList.get(i).getYDisplacement() + yVal);
        }
    }
    int getXDisplacement(){
        return mainList.get(0).getXDisplacement();
    }
    int getYDisplacement(){
        return mainList.get(0).getYDisplacement();
    }
    void setColour(int index, int r, int g, int b){
        mainList.get(index).setColour(r, g, b);
    }
    void setColour(int r, int g, int b){
        for(int i = 0; i < mainList.size(); i++){
            mainList.get(i).setColour(r, g, b);
        }
    }
    boolean getCanDraw(int index){
        return mainList.get(index).getCanDraw();
    }
    private static int round(double doubleNum){
        int intNum = (int)(doubleNum);
        double decimal = (doubleNum - intNum);
        if(decimal >= 0.5){
            intNum++;
        }
        return intNum;
    }
    private void unpack(Passage passage){
        mainList.addAll(passage.getPlatforms());
    }
    private void unpack(Room room){
        mainList.addAll(room.getPlatforms());
    }
}