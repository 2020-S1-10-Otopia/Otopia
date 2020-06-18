package com.example.World2D;

import java.util.ArrayList;

class Passage {
    private ArrayList<Platform> list = new ArrayList<>();
    Passage(int x, int y, int width, int height, int separation, boolean isVertical){
        if(isVertical){
            list.add(new Platform(x - ((separation / 2) + (width / 2)), y, width, height));
            list.add(new Platform(x + ((separation / 2) + (width / 2)), y, width, height));
        }
        else{
            list.add(new Platform(x, y - ((separation / 2) + (height / 2)), width, height));
            list.add(new Platform(x, y + ((separation / 2) + (height / 2)), width, height));
        }
    }
    ArrayList<Platform> getPlatforms(){
        return list;
    }
}
