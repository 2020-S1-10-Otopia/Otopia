package com.example.World2D;

import android.content.res.Resources;

class Utilities {
    int screenWidth(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    int screenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
