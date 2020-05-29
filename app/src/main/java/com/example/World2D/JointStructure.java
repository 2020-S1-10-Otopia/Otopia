package com.example.World2D;

import java.util.ArrayList;

public class JointStructure {
    ArrayList<Joint> list = new ArrayList<>();
    ArrayList<Joint> getList(){
        return list;
    }
    void add(Joint joint){
        list.add(joint);
    }
}