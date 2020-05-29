package com.example.World2D;

import java.util.ArrayList;

class Room {
    private ArrayList<Platform> list = new ArrayList<>();
    Room(int x, int y, int width, int height, int separation, int rotation){
        if(rotation <= 1){
            list.add(new Platform(x, y - ((separation / 2) + (height / 2)), width, height));
            list.add(new Platform(x, y + ((separation / 2) + (height / 2)), width, height));
            list.add(new Platform((x - (width / 2)) + (height / 2), y, height, separation));
        }
        else if(rotation == 2){
            list.add(new Platform(x - ((separation / 2) + (width / 2)), y, width, height));
            list.add(new Platform(x + ((separation / 2) + (width / 2)), y, width, height));
            list.add(new Platform(x, (y - (height / 2)) + (width / 2), separation, width));
        }
        else if(rotation == 3){
            list.add(new Platform(x, y - ((separation / 2) + (height / 2)), width, height));
            list.add(new Platform(x, y + ((separation / 2) + (height / 2)), width, height));
            list.add(new Platform((x + (width / 2)) - (height / 2), y, height, separation));
        }
        else {
            list.add(new Platform(x - ((separation / 2) + (width / 2)), y, width, height));
            list.add(new Platform(x + ((separation / 2) + (width / 2)), y, width, height));
            list.add(new Platform(x, (y + (height / 2)) - (width / 2), separation, width));
        }
    }
    ArrayList<Platform>getPlatforms(){
        return list;
    }
}
