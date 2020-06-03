package com.example.World2D;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Shader;

import java.util.ArrayList;

class Player {
    private Utilities util = new Utilities();
    private int heals = 3;
    private BoundingBox box = new BoundingBox(util.screenWidth() / 2, round(util.screenHeight() / 1.6), util.screenWidth() / 25, util.screenHeight() / 6);
    private int health = 100;
    StatusBar healthBar = new StatusBar(health, 255, 10, 350, 20, StatusBar.DISPLAY_FRACTION, "green");
    private int maxNumberOfJumps = 2;
    private int currentJumps = maxNumberOfJumps;
    private static final int abdomen = 0;
    private static final int torso = 1;
    private static final int head = 2;
    private static final int leftBicep = 3;
    private static final int leftForearm = 4;
    private static final int rightBicep = 5;
    private static final int rightForearm = 6;
    private static final int leftThigh = 7;
    private static final int leftLeg = 8;
    private static final int rightThigh = 9;
    private static final int rightLeg = 10;
    private ArrayList<Joint> parts = new ArrayList<>();
    private ArrayList<float[]> walkList = new ArrayList<>();
    private ArrayList<float[]> crouchList = new ArrayList<>();
    private ArrayList<float[]> crouchWalkList = new ArrayList<>();
    private ArrayList<float[]> jumpList = new ArrayList<>();
    private ArrayList<float[]> wallRunList = new ArrayList<>();
    private ArrayList<float[]> deathList = new ArrayList<>();

//    private ArrayList<float[]> flipList = new ArrayList<>();
    private float[]idleKeyFrame = {4, 0, 10, 20,    200, 180,   180, 150,   150, 170,   190, 200};
    private float[]onWallKeyFrame = {2, 0, 10, 20,  35, 10,     210, 190,   30,  170,   150, 170};
//    private float[]crouchKeyFrame = {5, 30, 60, 90,  180, 60,     170, 80,   30,  170,   40, 190};
    private ArrayList<float[]> fallList = new ArrayList<>();
    private boolean isCrouched = false;
    private boolean isFacingLeft = false;
    private int boneWidth = box.getWidth() / 15;
    private int walkKeyFrame = 0;
    private int jumpKeyFrame = 0;
    private int fallKeyFrame = 0;
    private int wallRunKeyFrame = 0;
    private int crouchWalkKeyFrame = 0;
    private int deathKeyFrame = 0;
    private int deathCount = 0;
//    private int flipKeyFrame = 0;
    private int crouchKeyFrame = 0;
    private int jumpCount = 0;
    private int walkCount = 0;
    private int fallCount = 0;
    private int idleCount = 0;
//    private int flipCount = 0;
    private int crouchCount = 0;
    private int crouchWalkCount = 0;
    private int onWallCount = 0;
    private int wallRunCount = 0;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Shader shader = new RadialGradient(util.screenWidth() / 2, round(util.screenHeight() / 1.6), 200, new int[]{Color.argb(50, 0, 120, 200), Color.argb(200, 0, 120, 200)}, null, Shader.TileMode.CLAMP);
    private Matrix m = new Matrix();
    Player(){
        Point centerOfMass = new Point(box.getX(), box.getY());
        int rateOfChange = 2;
        parts.add(new Joint(centerOfMass.x, centerOfMass.y, box.getHeight() / 5, 0));//0, abdomen
        parts.add(new Joint(parts.get(abdomen).getX(), parts.get(abdomen).getY(), box.getHeight() / 5, 10));//1, torso
        parts.add(new Joint(parts.get(torso).getX(), parts.get(torso).getY(), box.getHeight() / 20, 20));//2, head
        parts.add(new Joint(parts.get(torso).getX(), parts.get(torso).getY(), round(box.getHeight() / 4.5), 210));//3, leftBicep
        parts.add(new Joint(parts.get(leftBicep).getX(), parts.get(leftBicep).getY(), box.getHeight() / 4, parts.get(leftBicep).getCurrentRotation() - 40));//4, leftForearm
        parts.add(new Joint(parts.get(torso).getX(), parts.get(torso).getY(), round(box.getHeight() / 4.5), 150));//5, rightBicep
        parts.add(new Joint(parts.get(rightBicep).getX(), parts.get(rightBicep).getY(), box.getHeight() / 4, parts.get(rightBicep).getCurrentRotation() - 40));//6, rightForearm
        parts.add(new Joint(parts.get(abdomen).getAnchorX(), parts.get(abdomen).getAnchorY(), box.getHeight() / 4, 140));//7, leftThigh
        parts.add(new Joint(parts.get(leftThigh).getX(), parts.get(leftThigh).getY(), box.getHeight() / 4, 170));//8, leftLeg
        parts.add(new Joint(parts.get(abdomen).getAnchorX(), parts.get(abdomen).getAnchorY(), box.getHeight() / 4, 200));
        parts.add(new Joint(parts.get(rightThigh).getX(), parts.get(rightThigh).getY(), box.getHeight() / 4, 220));

        walkList.add(new float[]{rateOfChange * 2, 20, 30, 60,     205, 170,   195, 105,   105, 195,   185, 195});
        walkList.add(new float[]{rateOfChange, 30, 28, 60,     215, 160,   185, 100,   110, 180,   205, 205});
        walkList.add(new float[]{rateOfChange, 30, 36, 60,     230, 190,   175, 90,   110, 165,   195, 240});
        walkList.add(new float[]{rateOfChange, 30, 44, 60,     260, 215,   165, 70,   125, 140,   195, 300});
        walkList.add(new float[]{rateOfChange, 30, 36, 60,     240, 200,   165, 80,   140, 195,   185, 280});
        walkList.add(new float[]{rateOfChange * 2, 28, 30, 60,     200, 195,   195, 135,   150, 195,   145, 250});
        walkList.add(new float[]{rateOfChange * 2, 20, 30, 60,     195, 105,   205, 170,   185, 195,   105, 195});
        walkList.add(new float[]{rateOfChange, 30, 28, 60,     185, 100,   215, 160,   205, 205,   110, 180});
        walkList.add(new float[]{rateOfChange, 30, 36, 60,     175, 90,   230, 190,   195, 240,   110, 165});
        walkList.add(new float[]{rateOfChange, 30, 44, 60,     165, 70,   260, 215,   195, 300,   125, 140});
        walkList.add(new float[]{rateOfChange, 30, 36, 60,     165, 80,   240, 200,   185, 280,   140, 195});
        walkList.add(new float[]{rateOfChange * 2, 28, 30, 60,     195, 135,   200, 195,   145, 250,   150, 195});

        jumpList.add(new float[]{1,  0, 10, 20,    200, 180,   180, 150,   150, 170,   190, 200});
        jumpList.add(new float[]{4,  10, 20, 30,    250, 210,    240, 200,    120, 220,    185, 185});
        jumpList.add(new float[]{8, 30, 50, 60,      230, 190,     220, 180,   80, 190,     100, 210});

        fallList.add(new float[]{1, 30, 50, 60,      230, 190,     220, 180,   80, 190,     100, 210});
        fallList.add(new float[]{5, 0, 0, 0,    230, 190,   220, 180,    100, 200,   120, 220});
        fallList.add(new float[]{5, 0, 0, 0,   230, 190,   220, 180,    175, 220,   160, 240});

        crouchList.add(new float[]{5, 15, 30, 45,   160, 110,      140, 90,     130, 180,   110, 110});
        crouchList.add(new float[]{5, 30, 60, 90,   200, 150,      180, 130,    90, 220,    70, 180});

        wallRunList.add(new float[]{10, 0, 10, 20,      35, 10,     240, 90,    150, 170,    30, 170});
//        walkList.add(new float[]{5, 0, 10, 20,      });
        wallRunList.add(new float[]{10, 0, 10, 20,      240, 90,    35, 10,     30, 170,    150, 170});
//        walkList.add(new float[]{5, 0, 10, 20,      });
        //        fallList.add(new float[]{1, 0, 0, 35,        200, 190,   220, 180,    100, 210,   120, 230 });
//        flipList.add(new float[]{5, 72, 92, 112,        202, 122,       192, 112,       122, 232,   112, 222});
//        flipList.add(new float[]{5, 144, 164, 184,      274, 194,       264, 184,       194, 304,   184, 294});
//        flipList.add(new float[]{5, 216, 236, 256,      346, 266,       336, 256,       266, 16,    256, 6});
//        flipList.add(new float[]{5, 288, 308, 328,      58, 338,        48, 328,        338, 88,    238, 78});
//        flipList.add(new float[]{5, 0, 20, 40,          130, 50,        120, 40,        50,  160,   40,  150});
        crouchWalkList.add(new float[]{5, 30, 60, 90,   280, 200,   160, 80,   90, 220,    70, 180});
        crouchWalkList.add(new float[]{5, 30, 60, 90,   220, 140,   220, 140,   80, 200,    80, 200});
        crouchWalkList.add(new float[]{5, 30, 60, 90,   160, 80,   280, 200,   70, 180,    90, 220});
        crouchWalkList.add(new float[]{5, 30, 60, 90,   220, 140,   220, 140,   80, 200,    80, 200});

        deathList.add(new float[]{1, 0, 10, 20,    200, 180,   180, 150,   150, 170,   190, 200});
        deathList.add(new float[]{10, 0, 10, 20, 175, 175, 185, 185, 175, 270, 185, 270});
        deathList.add(new float[]{10, 0, 10, 20, 175, 175, 185, 185, 175, 270, 185, 270});
        deathList.add(new float[]{10, 90, 90, 90, 270, 270, 270, 270, 270, 270, 270, 270});
//        deathList.add(new float[]{});

    }
    void setHeals(int heals){
        this.heals = heals;
    }
    int getHeals(){
        return heals;
    }
    private void mirrorJoints(boolean b) {
        for(int i = 0; i<parts.size(); i++){
            parts.get(i).setIsMirrored(b);
        }
        connectJoints(parts);
    }
    void setHealth(int value){
        health = value;
        healthBar.setCurrentHealth(health);
        health = healthBar.getCurrentHealth();
    }
    int getHealth(){
        return health;
    }
    void setFacingLeft(boolean b){
        this.isFacingLeft = b;
        mirrorJoints(b);
    }
//    boolean getIsFacingLeft(){
//        return isFacingLeft;
//    }
    private void resetWalkAnimation(){
        walkKeyFrame = 0;
        walkCount = 0;
    }
    void resetJumpAnimation(){
        jumpKeyFrame = 0;
        jumpCount = 0;
    }
    private void resetWallRunAnimation(){
        wallRunKeyFrame = 0;
        wallRunCount = 0;
    }
//    private void resetFlipAnimation(){
//        flipKeyFrame = 0;
//        flipCount = 0;
//    }
    private void resetCrouchWalkAnimation(){
        crouchWalkKeyFrame = 0;
        crouchWalkCount = 0;
    }
    private void resetFallAnimation(){
        fallKeyFrame = 0;
        fallCount = 0;
    }
    private void resetCrouchAnimation(){
        crouchKeyFrame = 0;
        crouchCount = 0;
    }
    private void connectJoints(ArrayList<Joint> list){
        list.get(abdomen).setAnchorLocation(box.getX(), box.getY());
        list.get(torso).setAnchorLocation(list.get(abdomen).getX(), list.get(abdomen).getY());
        list.get(head).setAnchorLocation(list.get(torso).getX(), list.get(torso).getY());
        list.get(leftBicep).setAnchorLocation(list.get(torso).getX(), list.get(torso).getY());
        list.get(leftForearm).setAnchorLocation(list.get(leftBicep).getX(), list.get(leftBicep).getY());
        list.get(rightBicep).setAnchorLocation(list.get(torso).getX(), list.get(torso).getY());
        list.get(rightForearm).setAnchorLocation(list.get(rightBicep).getX(), list.get(rightBicep).getY());
        list.get(leftThigh).setAnchorLocation(list.get(abdomen).getAnchorX(), list.get(abdomen).getAnchorY());
        list.get(leftLeg).setAnchorLocation(list.get(leftThigh).getX(), list.get(leftThigh).getY());
        list.get(rightThigh).setAnchorLocation(list.get(abdomen).getAnchorX(), list.get(abdomen).getAnchorY());
        list.get(rightLeg).setAnchorLocation(list.get(rightThigh).getX(), list.get(rightThigh).getY());
    }
    void walk(){
        for (int i = 0; i < parts.size(); i++){
            if(parts.get(i).getCurrentRotation() >= 180){
                if(walkList.get(walkKeyFrame)[i + 1] <= parts.get(i).getCurrentRotation() && walkList.get(walkKeyFrame)[i + 1] > (parts.get(i).getCurrentRotation() - 180)){
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((parts.get(i).getCurrentRotation() - walkList.get(walkKeyFrame)[i + 1])/(walkList.get(walkKeyFrame)[0] - walkCount)));
                }
                else{
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((walkList.get(walkKeyFrame)[i + 1] - parts.get(i).getCurrentRotation())/(walkList.get(walkKeyFrame)[0] - walkCount)));
                }
            }
            else{
                if(walkList.get(walkKeyFrame)[i + 1] >= parts.get(i).getCurrentRotation() && walkList.get(walkKeyFrame)[i + 1] < (parts.get(i).getCurrentRotation() + 180)){
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((walkList.get(walkKeyFrame)[i + 1] - parts.get(i).getCurrentRotation())/(walkList.get(walkKeyFrame)[0] - walkCount)));
                }
                else{
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((parts.get(i).getCurrentRotation() - walkList.get(walkKeyFrame)[i + 1])/(walkList.get(walkKeyFrame)[0] - walkCount)));
                }
            }
        }
        connectJoints(parts);
        walkCount++;
        if(walkCount >= walkList.get(walkKeyFrame)[0]){
            walkCount = 0;
            walkKeyFrame++;
            if(walkKeyFrame >= walkList.size()){
                walkKeyFrame = 0;
            }
        }
        resetJumpAnimation();
//        resetFlipAnimation();
        resetFallAnimation();
        resetCrouchAnimation();
        resetWallRunAnimation();
        resetCrouchWalkAnimation();
        idleCount = 0;
    }
    void death() {
        for (int i = 0; i < parts.size(); i++) {
            if (deathList.get(deathKeyFrame)[i + 1] >= parts.get(i).getCurrentRotation()) {
                if((deathList.get(deathKeyFrame)[i + 1] - parts.get(i).getCurrentRotation()) >= 180){
//                    --
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((deathList.get(deathKeyFrame)[i + 1] - parts.get(i).getCurrentRotation()) / (deathList.get(deathKeyFrame)[0] - deathCount)));
                }
                else if((deathList.get(deathKeyFrame)[i + 1] - parts.get(i).getCurrentRotation()) < 180){
//                    ++
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((deathList.get(deathKeyFrame)[i + 1] - parts.get(i).getCurrentRotation()) / (deathList.get(deathKeyFrame)[0] - deathCount)));
                }
            }
            else if(parts.get(i).getCurrentRotation() > deathList.get(deathKeyFrame)[i + 1]){
                if((parts.get(i).getCurrentRotation() - deathList.get(deathKeyFrame)[i + 1]) >= 180){
//                  ++
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() +- ((deathList.get(deathKeyFrame)[i + 1] - parts.get(i).getCurrentRotation()) / (deathList.get(deathKeyFrame)[0] - deathCount)));
                }
                else if((parts.get(i).getCurrentRotation() - deathList.get(deathKeyFrame)[i + 1]) < 180){
//                  --
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((deathList.get(deathKeyFrame)[i + 1] - parts.get(i).getCurrentRotation()) / (deathList.get(deathKeyFrame)[0] - deathCount)));
                }
            }
            connectJoints(parts);
        }
        if(deathCount<deathList.get(deathKeyFrame)[0] && deathKeyFrame<deathList.size() - 1){
            deathCount++;
            if(deathCount >= deathList.get(deathKeyFrame)[0]){
                deathKeyFrame++;
                deathCount = 0;
            }
        }
    }
    void jump(){
        for(int i = 0; i < parts.size(); i++){
            if(parts.get(i).getCurrentRotation() >= 180){
                if(jumpList.get(jumpKeyFrame)[i + 1] <= parts.get(i).getCurrentRotation() && jumpList.get(jumpKeyFrame)[i + 1] > (parts.get(i).getCurrentRotation() - 180)){
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((parts.get(i).getCurrentRotation() - jumpList.get(jumpKeyFrame)[i + 1])/(jumpList.get(jumpKeyFrame)[0] - jumpCount)));
                }
                else{
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((jumpList.get(jumpKeyFrame)[i + 1] - parts.get(i).getCurrentRotation())/(jumpList.get(jumpKeyFrame)[0] - jumpCount)));
                }
            }
            else{
                if(jumpList.get(jumpKeyFrame)[i + 1] >= parts.get(i).getCurrentRotation() && jumpList.get(jumpKeyFrame)[i + 1] < (parts.get(i).getCurrentRotation() + 180)){
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((jumpList.get(jumpKeyFrame)[i + 1] - parts.get(i).getCurrentRotation())/(jumpList.get(jumpKeyFrame)[0] - jumpCount)));
                }
                else{
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((parts.get(i).getCurrentRotation() - jumpList.get(jumpKeyFrame)[i + 1])/(jumpList.get(jumpKeyFrame)[0] - jumpCount)));
                }
            }
        }
        if(jumpCount < jumpList.get(jumpKeyFrame)[0]){
            jumpCount++;
            if(jumpCount >= jumpList.get(jumpKeyFrame)[0]){
                jumpCount = 0;
                if(jumpKeyFrame < (jumpList.size() - 1)){
                    jumpKeyFrame++;
                }
            }
        }
        connectJoints(parts);
        resetWalkAnimation();
        resetFallAnimation();
//        resetFlipAnimation();
        resetCrouchAnimation();
        resetWallRunAnimation();
        resetCrouchWalkAnimation();
        idleCount = 0;
    }
//    void flip(){
//        float difference = 0;
//        for(int i = 0; i < parts.size(); i++){
//            if(flipList.get(flipKeyFrame)[i + 1] >= parts.get(i).getCurrentRotation()){
//                difference = flipList.get(flipKeyFrame)[i + 1] - parts.get(i).getCurrentRotation();
//            }
//            else{
//                difference = parts.get(i).getCurrentRotation() - flipList.get(flipKeyFrame)[i + 1];
//            }
//            if(difference >= 180){
//                if(flipList.get(flipKeyFrame)[i + 1] < parts.get(i).getCurrentRotation()){
//                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((360 - difference)/(flipList.get(flipKeyFrame)[0] - flipCount)));
//                }
//                else{
//                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((360 - difference)/(flipList.get(flipKeyFrame)[0] - flipCount)));
//                }
//            }
//            else{
//                if(flipList.get(flipKeyFrame)[i + 1] < parts.get(i).getCurrentRotation()){
//                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((parts.get(i).getCurrentRotation() - flipList.get(flipKeyFrame)[i + 1])/(flipList.get(flipKeyFrame)[0] - flipCount)));
//                }
//                else{
//                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((parts.get(i).getCurrentRotation() - flipList.get(flipKeyFrame)[i + 1])/(flipList.get(flipKeyFrame)[0] - flipCount)));
//                }
//            }
////            if(parts.get(i).getCurrentRotation() >= 180){
////                if(flipList.get(flipKeyFrame)[i + 1] <= parts.get(i).getCurrentRotation() && flipList.get(flipKeyFrame)[i + 1] > (parts.get(i).getCurrentRotation() - 180)){
////                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((parts.get(i).getCurrentRotation() - flipList.get(flipKeyFrame)[i + 1])/(flipList.get(flipKeyFrame)[0] - flipCount)));
////                }
////                else{
////                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((flipList.get(flipKeyFrame)[i + 1] - parts.get(i).getCurrentRotation())/(flipList.get(flipKeyFrame)[0] - flipCount)));
////                }
////            }
////            else{
////                if(flipList.get(flipKeyFrame)[i + 1] >= parts.get(i).getCurrentRotation() && flipList.get(flipKeyFrame)[i + 1] < (parts.get(i).getCurrentRotation() + 180)){
////                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((flipList.get(flipKeyFrame)[i + 1] - parts.get(i).getCurrentRotation())/(flipList.get(flipKeyFrame)[0] - flipCount)));
////                }
////                else{
////                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((parts.get(i).getCurrentRotation() - flipList.get(flipKeyFrame)[i + 1])/(flipList.get(flipKeyFrame)[0] - flipCount)));
////                }
////            }
//        }
//        if(flipCount < flipList.get(flipKeyFrame)[0]){
//            flipCount++;
//            if(flipCount >= flipList.get(flipKeyFrame)[0]){
//                flipCount = 0;
//                if(flipKeyFrame < (flipList.size() - 1)){
//                    flipKeyFrame++;
//                }
//            }
//        }
//        connectJoints(parts);
//        resetWalkAnimation();
//        resetJumpAnimation();
//        resetFallAnimation();
//        resetCrouchAnimation();
//    }
    void fall(){
        for(int i = 0; i < parts.size(); i++) {
            if (fallList.get(fallKeyFrame)[0] == 1) {
                parts.get(i).setCurrentRotation(fallList.get(fallKeyFrame)[i + 1]);
            }
            else {
                if (parts.get(i).getCurrentRotation() >= 180) {
                    if (fallList.get(fallKeyFrame)[i + 1] <= parts.get(i).getCurrentRotation() && fallList.get(fallKeyFrame)[i + 1] > (parts.get(i).getCurrentRotation() - 180)) {
                        parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((parts.get(i).getCurrentRotation() - fallList.get(fallKeyFrame)[i + 1]) / (fallList.get(fallKeyFrame)[0] - fallCount)));
                    } else {
                        parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((fallList.get(fallKeyFrame)[i + 1] - parts.get(i).getCurrentRotation()) / (fallList.get(fallKeyFrame)[0] - fallCount)));
                    }
                } else if (parts.get(i).getCurrentRotation() < 180) {
                    if (fallList.get(fallKeyFrame)[i + 1] >= parts.get(i).getCurrentRotation() && fallList.get(fallKeyFrame)[i + 1] < (parts.get(i).getCurrentRotation() + 180)) {
                        parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((fallList.get(fallKeyFrame)[i + 1] - parts.get(i).getCurrentRotation()) / (fallList.get(fallKeyFrame)[0] - fallCount)));
                    } else if (fallList.get(fallKeyFrame)[i + 1] < parts.get(i).getCurrentRotation() && fallList.get(fallKeyFrame)[i + 1] >= (parts.get(i).getCurrentRotation() + 180)) {
                        parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((parts.get(i).getCurrentRotation() - fallList.get(fallKeyFrame)[i + 1]) / (fallList.get(fallKeyFrame)[0] - fallCount)));
                    }
                }
            }
        }
        if(fallCount < fallList.get(fallKeyFrame)[0]){
            fallCount++;
            if(fallCount >= fallList.get(fallKeyFrame)[0]){
                fallCount = 0;
                if(fallKeyFrame < (fallList.size() - 1)){
                    fallKeyFrame++;
                }
            }
        }
        connectJoints(parts);
        resetWalkAnimation();
        resetJumpAnimation();
//        resetFlipAnimation();
        resetCrouchAnimation();
        resetWallRunAnimation();
        resetCrouchWalkAnimation();
        idleCount = 0;
    }
    void setIdle(){
        for (int i = 0; i < parts.size(); i++){
            if(parts.get(i).getCurrentRotation() >= idleKeyFrame[i + 1]){
                parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((parts.get(i).getCurrentRotation() - idleKeyFrame[i + 1]) / idleKeyFrame[0]));
            }
            else{
                parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((idleKeyFrame[i + 1] - parts.get(i).getCurrentRotation()) / idleKeyFrame[0]));
            }
        }
        if(idleCount < (idleKeyFrame[0])) {
            idleCount++;
        }
        connectJoints(parts);
        resetWalkAnimation();
        resetJumpAnimation();
        resetFallAnimation();
        resetCrouchAnimation();
        resetWallRunAnimation();
        resetCrouchWalkAnimation();
//        resetFlipAnimation();
    }
    int getMAX_NUMBER_OF_JUMPS() {
        return maxNumberOfJumps;
    }
    int getCurrentJumps() {
        return currentJumps;
    }
    void setCurrentJumps(int num) {
        this.currentJumps = num;
        if (currentJumps > maxNumberOfJumps) {
            currentJumps = maxNumberOfJumps;
        }
        if (currentJumps < 0) {
            currentJumps = 0;
        }
    }
    boolean getIsCrouched() {
        return isCrouched;
    }
    void setIsCrouched(boolean b) {
        isCrouched = b;
    }
    BoundingBox getBox() {
        return box;
    }
    void setBoxLCollisionTrue() {
        this.box.setLeftCollision(true);
    }
    void setBoxRCollisionTrue() {
        this.box.setRightCollision(true);
    }
    void setBoxTCollisionTrue() {
        this.box.setTopCollision(true);
    }
    void setBoxBCollisionTrue() {
        this.box.setBottomCollision(true);
    }
    void setBoxCollisionsFalse() {
        this.box.setCollisionsFalse();
    }
    float getAccelerationX() {
        return 1;
    }
    void draw(Canvas canvas) {
        for (int i = 0; i<parts.size(); i++){
            parts.get(i).setWidth(boneWidth);
            parts.get(i).drawAll(canvas);
        }
        healthBar.draw(canvas);
    }
    void crouch(){
        for (int i = 0; i < parts.size(); i++){
            if(parts.get(i).getCurrentRotation() >= 180){
                if(crouchList.get(crouchKeyFrame)[i + 1] <= parts.get(i).getCurrentRotation() && crouchList.get(crouchKeyFrame)[i + 1] > (parts.get(i).getCurrentRotation() - 180)){
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((parts.get(i).getCurrentRotation() - crouchList.get(crouchKeyFrame)[i + 1])/(crouchList.get(crouchKeyFrame)[0] - crouchCount)));
                }
                else{
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((crouchList.get(crouchKeyFrame)[i + 1] - parts.get(i).getCurrentRotation())/(crouchList.get(crouchKeyFrame)[0] - crouchCount)));
                }
            }
            else{
                if(crouchList.get(crouchKeyFrame)[i + 1] >= parts.get(i).getCurrentRotation() && crouchList.get(crouchKeyFrame)[i + 1] < (parts.get(i).getCurrentRotation() + 180)){
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((crouchList.get(crouchKeyFrame)[i + 1] - parts.get(i).getCurrentRotation())/(crouchList.get(crouchKeyFrame)[0] - crouchCount)));
                }
                else{
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((parts.get(i).getCurrentRotation() - crouchList.get(crouchKeyFrame)[i + 1])/(crouchList.get(crouchKeyFrame)[0] - crouchCount)));
                }
            }
        }
        if(crouchCount < crouchList.get(crouchKeyFrame)[0]){
            crouchCount++;
            if(crouchCount >= crouchList.get(crouchKeyFrame)[0]){
                crouchCount = 0;
                if(crouchKeyFrame < (crouchList.size() - 1)){
                    crouchKeyFrame++;
                }
            }
        }
        connectJoints(parts);
        resetWalkAnimation();
        resetJumpAnimation();
        resetFallAnimation();
        resetWallRunAnimation();
        resetCrouchWalkAnimation();
        idleCount = 0;
    }
    void onWall(){
        for(int i = 0; i < parts.size(); i++){
            if(parts.get(i).getCurrentRotation() >= onWallKeyFrame[i + 1]){
                parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((parts.get(i).getCurrentRotation() - onWallKeyFrame[i + 1]) / onWallKeyFrame[0]));
            }
            else{
                parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((onWallKeyFrame[i + 1] - parts.get(i).getCurrentRotation()) / onWallKeyFrame[0]));
            }
        }
        if(onWallCount < (onWallKeyFrame[0])) {
            onWallCount++;
        }
        connectJoints(parts);
        resetWalkAnimation();
        resetJumpAnimation();
        resetFallAnimation();
        resetCrouchAnimation();
        resetWallRunAnimation();
        resetCrouchWalkAnimation();
        idleCount = 0;
    }
    void crouchWalk(){
        for (int i = 0; i < parts.size(); i++){
            if(parts.get(i).getCurrentRotation() >= 180){
                if(crouchWalkList.get(crouchWalkKeyFrame)[i + 1] <= parts.get(i).getCurrentRotation() && crouchWalkList.get(crouchWalkKeyFrame)[i + 1] > (parts.get(i).getCurrentRotation() - 180)){
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((parts.get(i).getCurrentRotation() - crouchWalkList.get(crouchWalkKeyFrame)[i + 1])/(crouchWalkList.get(crouchWalkKeyFrame)[0] - crouchWalkCount)));
                }
                else{
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((crouchWalkList.get(crouchWalkKeyFrame)[i + 1] - parts.get(i).getCurrentRotation())/(crouchWalkList.get(crouchWalkKeyFrame)[0] - crouchWalkCount)));
                }
            }
            else{
                if(crouchWalkList.get(crouchWalkKeyFrame)[i + 1] >= parts.get(i).getCurrentRotation() && crouchWalkList.get(crouchWalkKeyFrame)[i + 1] < (parts.get(i).getCurrentRotation() + 180)){
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((crouchWalkList.get(crouchWalkKeyFrame)[i + 1] - parts.get(i).getCurrentRotation())/(crouchWalkList.get(crouchWalkKeyFrame)[0] - crouchWalkCount)));
                }
                else{
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((parts.get(i).getCurrentRotation() - crouchWalkList.get(crouchWalkKeyFrame)[i + 1])/(crouchWalkList.get(crouchWalkKeyFrame)[0] - crouchWalkCount)));
                }
            }
        }
        connectJoints(parts);
        crouchWalkCount++;
        if(crouchWalkCount >= crouchWalkList.get(crouchWalkKeyFrame)[0]){
            crouchWalkCount = 0;
            crouchWalkKeyFrame++;
            if(crouchWalkKeyFrame >= crouchWalkList.size()){
                crouchWalkKeyFrame = 0;
            }
        }
        resetJumpAnimation();
//        resetFlipAnimation();
        resetFallAnimation();
        resetCrouchAnimation();
        resetWallRunAnimation();
        resetWalkAnimation();
        idleCount = 0;
    }
    void wallRun(){
        for (int i = 0; i < parts.size(); i++){
            if(parts.get(i).getCurrentRotation() >= 180){
                if(wallRunList.get(wallRunKeyFrame)[i + 1] <= parts.get(i).getCurrentRotation() && wallRunList.get(wallRunKeyFrame)[i + 1] > (parts.get(i).getCurrentRotation() - 180)){
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((parts.get(i).getCurrentRotation() - wallRunList.get(wallRunKeyFrame)[i + 1])/(wallRunList.get(wallRunKeyFrame)[0] - wallRunCount)));
                }
                else{
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((wallRunList.get(wallRunKeyFrame)[i + 1] - parts.get(i).getCurrentRotation())/(wallRunList.get(wallRunKeyFrame)[0] - wallRunCount)));
                }
            }
            else{
                if(wallRunList.get(wallRunKeyFrame)[i + 1] >= parts.get(i).getCurrentRotation() && wallRunList.get(wallRunKeyFrame)[i + 1] < (parts.get(i).getCurrentRotation() + 180)){
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() + ((wallRunList.get(wallRunKeyFrame)[i + 1] - parts.get(i).getCurrentRotation())/(wallRunList.get(wallRunKeyFrame)[0] - wallRunCount)));
                }
                else{
                    parts.get(i).setCurrentRotation(parts.get(i).getCurrentRotation() - ((parts.get(i).getCurrentRotation() - wallRunList.get(wallRunKeyFrame)[i + 1])/(wallRunList.get(wallRunKeyFrame)[0] - wallRunCount)));
                }
            }
        }
        connectJoints(parts);
        wallRunCount++;
        if(wallRunCount >= wallRunList.get(wallRunKeyFrame)[0]){
            wallRunCount = 0;
            wallRunKeyFrame++;
            if(wallRunKeyFrame >= wallRunList.size()){
                wallRunKeyFrame = 0;
            }
        }
        resetJumpAnimation();
//        resetFlipAnimation();
        resetFallAnimation();
        resetCrouchAnimation();
        resetWalkAnimation();
        resetCrouchWalkAnimation();
        idleCount = 0;
    }
//    private static int round(float floatNum){
//        int intNum = (int)(floatNum);
//        float decimal = floatNum - intNum;
//        if(decimal >= 0.5){
//            intNum++;
//        }
//        return intNum;
//    }
    private static int round(double doubleNum){
        int intNum = (int)(doubleNum);
        double decimal = (doubleNum - intNum);
        if(decimal >= 0.5){
            intNum++;
        }
        return intNum;
    }
}