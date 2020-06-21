package com.example.World2D;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, GameObject {
    private MainThread thread;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    int pointerCount;
    int cappedPointerCount;
    int actionIndex;
    int action;
    int touchX;
    int touchY;
    int id;
    int jumpId;
    int leftId;
    int rightId;
    int crouchId;
    int meleeId;
    int fireId;
    int healId;
    int respawnId;
    int heals = 3;
    int mostDrawn = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    int maxVelocity = 12;
    int animationId = 0;
    int fallDamageCount = 0;
    int fallDamageDivisor = 47;
    int regenCount = 0;

    Path path = new Path();
    Utilities util = new Utilities();
    ArrayList <PointF> pointers = new ArrayList<>();
    int buttonSize = util.screenHeight() / 720 * 80;
    private RoundButton leftButton = new RoundButton(round(util.screenWidth() / 1280 * 120), util.screenHeight() / 720 * 600, buttonSize, new Arrow(180, buttonSize / 4 * 3, util.screenWidth() / 1280 * 120, util.screenHeight() / 720 * 600));
    private RoundButton rightButton = new RoundButton(round(util.screenWidth() / 1280 * 300), util.screenHeight() / 720 * 600, buttonSize, new Arrow(0, buttonSize / 4 * 3, util.screenWidth() / 1280 * 300, util.screenHeight() / 720 * 600));
    private RoundButton jumpButton = new RoundButton(round(util.screenWidth() / 1280 * 1180), util.screenHeight() / 720 * 540, buttonSize, new Arrow(270, buttonSize / 4 * 3, util.screenWidth() / 1280 * 1180, util.screenHeight() / 720 * 540));
    private RoundButton crouchButton = new RoundButton(round(util.screenWidth() / 1280 * 1010), util.screenHeight() / 720 * 600, buttonSize, new Arrow(90, buttonSize / 4 * 3, util.screenWidth() / 1280 * 1010, util.screenHeight() / 720 * 600));
    //    private RoundButton testButton = new RoundButton(round(util.screenWidth() / 1280 * 1000), round(util.screenHeight() / 720 * 420), buttonSize);
    private RoundButton meleeButton = new RoundButton(util.screenWidth() / 1280 * 1000, util.screenHeight() / 720 * 420, buttonSize);
    private RoundButton fireButton = new RoundButton(round(util.screenWidth() / 1280f) * 1170, util.screenHeight() / 720 * 330, buttonSize);
    private RoundButton healButton = new RoundButton(round(util.screenWidth() / 1280 * 40), round(util.screenHeight() / 720 * 140), round(util.screenHeight() / 720 * 40));
    private RoundButton respawnButton = new RoundButton(round(util.screenWidth() / 1280 * 40), round(util.screenHeight() / 720 * 40), round(util.screenHeight() / 720 * 40));
    Player player = new Player();
    Environment e = new Environment(0, 200);
    public GamePanel(Context context){
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        paint.setMaskFilter(null);
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(retry){
            try {
                thread.setRunning(false);
                thread.join();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            retry = false;
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event){
        pointerCount = event.getPointerCount();
        cappedPointerCount = pointerCount > 5 ? 5 : pointerCount;
        actionIndex = event.getActionIndex();
        action = event.getAction() & MotionEvent.ACTION_MASK;
        if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN && event.getPointerCount() <= 5){
            if(action == MotionEvent.ACTION_DOWN){
                touchX = round(event.getX());
                touchY = round(event.getY());
                id =event.getPointerId(0);
                pointers.add(new PointF(event.getX(0), event.getY(0)));
                if(!rightButton.getActive()){
                    leftButton.handleTouchDown(pointers.get(0).x, pointers.get(0).y);
                    if(leftButton.getActive()){
                        leftId = 0;
                    }
                }
                if(!leftButton.getActive()){
                    rightButton.handleTouchDown(pointers.get(0).x, pointers.get(0).y);
                    if(rightButton.getActive()){
                        rightId = 0;
                    }
                }
                if(!jumpButton.getActive()){
                    crouchButton.handleTouchDown(pointers.get(0).x, pointers.get(0).y);
                    if(crouchButton.getActive()){
                        crouchId = 0;
                    }
                }
                if(!crouchButton.getActive()){
                    jumpButton.handleTouchDown(pointers.get(0).x, pointers.get(0).y);
                    if(jumpButton.getActive() && !player.getIsCrouched()) {
                        jumpId = 0;
                        if (player.getHealth() > 0) {
                            if (player.getCurrentJumps() > 0) {
                                if (!player.getBox().getLeftCollision() && !player.getBox().getLeftCollision() && player.getCurrentJumps() > 0) {
                                    player.resetJumpAnimation();
                                }
                                player.setCurrentJumps(player.getCurrentJumps() - 1);
                                yVelocity = 20;
                            }
                            if (player.getBox().getLeftCollision() && !player.getBox().getBottomCollision()) {
                                xVelocity = -maxVelocity;
                            }
                            if (player.getBox().getRightCollision() && !player.getBox().getBottomCollision()) {
                                xVelocity = maxVelocity;
                            }
                        }
                    }
                }
                if(!meleeButton.getActive()){
                    meleeButton.handleTouchDown(pointers.get(0).x, pointers.get(0).y);
                    if(meleeButton.getActive()){
                        meleeId = 0;
                        player.setPlayAnimation(true);
                    }
                }
                if(!fireButton.getActive()){
                    fireButton.handleTouchDown(pointers.get(0).x, pointers.get(0).y);
                    if(fireButton.getActive()){
                        fireId = 0;



                    }
                }
                if(!healButton.getActive()){
                    healButton.handleTouchDown(pointers.get(0).x, pointers.get(0).y);
                    if(healButton.getActive()){
                        healId = 0;
                        if((player.healthBar.getMaxHealth() - player.getHealth()) - regenCount > 0 && heals > 0 && player.getHealth() > 0){
                            regenCount += player.healthBar.getMaxHealth() / 2;
                            heals--;
                        }
                    }
                }
                if(!respawnButton.getActive()){
                    respawnButton.handleTouchDown(pointers.get(0).x, pointers.get(0).y);
                    if(respawnButton.getActive()){
                        respawnId = 0;
                        e.setDisplacement(0, 200);
                        xVelocity = 0;
                        yVelocity = 0;
                        fallDamageCount = 0;
                        player.resetDeathAnimation();
                        player.setInstantIdle();
                        heals = 3;
                        player.setHealth(player.healthBar.getMaxHealth());
                    }
                }
            }
            else if(action == MotionEvent.ACTION_POINTER_DOWN){
                this.id = event.getPointerId(getIndex(event));
                pointers.add(new PointF(event.getX(event.getActionIndex()), event.getY(event.getActionIndex())));
                if(!leftButton.getActive()){
                    leftButton.handleTouchDown(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()));
                    if (leftButton.getActive()){
                        leftId = event.getPointerId(event.getActionIndex());
                    }
                }
                if(!rightButton.getActive()){
                    rightButton.handleTouchDown(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()));
                    if (rightButton.getActive()){
                        rightId = event.getPointerId(event.getActionIndex());
                    }
                }
                if(!jumpButton.getActive()){
                    jumpButton.handleTouchDown(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()));
                    if (jumpButton.getActive()) {
                        jumpId = event.getPointerId(event.getActionIndex());
                        if (player.getHealth() > 0) {
                            if (player.getCurrentJumps() > 0 && !player.getIsCrouched()) {
                                if (!player.getBox().getLeftCollision() && !player.getBox().getLeftCollision() && player.getCurrentJumps() > 0) {
                                    player.resetJumpAnimation();
                                }
                                player.setCurrentJumps(player.getCurrentJumps() - 1);
                                yVelocity = 20;
                                if (player.getBox().getLeftCollision() && !player.getBox().getBottomCollision()) {
                                    xVelocity = -maxVelocity;
                                }
                                if (player.getBox().getRightCollision() && !player.getBox().getBottomCollision()) {
                                    xVelocity = maxVelocity;
                                }
                            }
                        }
                    }
                }
                if(!crouchButton.getActive()){
                    crouchButton.handleTouchDown(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()));
                    if (crouchButton.getActive()){
                        crouchId = event.getPointerId(event.getActionIndex());
                    }
                }
                if(!meleeButton.getActive()){
                    meleeButton.handleTouchDown(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()));
                    if(meleeButton.getActive()){
                        meleeId = event.getPointerId(event.getActionIndex());
                        player.setPlayAnimation(true);
                    }
                }
                if(!fireButton.getActive()){
                    fireButton.handleTouchDown(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()));
                    if(fireButton.getActive()){
                        fireId = event.getPointerId(event.getActionIndex());
                    }
                }
                if(!healButton.getActive()){
                    healButton.handleTouchDown(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()));
                    if(healButton.getActive()){
                        healId = event.getPointerId(event.getActionIndex());
                        if((player.healthBar.getMaxHealth() - player.getHealth()) - regenCount > 0 && heals > 0 && player.getHealth() > 0){
                            regenCount += player.healthBar.getMaxHealth() / 2;
                            heals--;
                        }
                    }
                }
                if(!respawnButton.getActive()){
                    respawnButton.handleTouchDown(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()));
                    if(respawnButton.getActive()){
                        respawnId = event.getPointerId(event.getActionIndex());
                        e.setDisplacement(-e.getXDisplacement(), -(e.getYDisplacement() + util.screenHeight()));
                        xVelocity = 0;
                        yVelocity = 0;
                        fallDamageCount = 0;
                        player.resetDeathAnimation();
                        player.setInstantIdle();
                        heals = 3;
                        player.setHealth(player.healthBar.getMaxHealth());
                    }
                }
            }
        }
        else if(action == MotionEvent.ACTION_MOVE && event.getPointerCount() <= 5){
            for (int i = 0; i < event.getPointerCount(); i++){
                this.id = event.getPointerId(i);
                pointers.get(i).set(event.getX(i), event.getY(i));
                if(leftId == event.getPointerId(i)){
                    leftButton.handleTouchDown(pointers.get(i).x, pointers.get(i).y);
                }
                if(rightId == event.getPointerId(i)){
                    rightButton.handleTouchDown(pointers.get(i).x, pointers.get(i).y);
                }
                if(jumpId == event.getPointerId(i)){
                    jumpButton.handleTouchDown(pointers.get(i).x, pointers.get(i).y);
                }
                if(crouchId == event.getPointerId(i)){
                    crouchButton.handleTouchDown(pointers.get(i).x, pointers.get(i).y);
                }
                if(meleeId == event.getPointerId(i)){
                    meleeButton.handleTouchDown(pointers.get(i).x, pointers.get(i).y);
                }
                if(fireId == event.getPointerId(i)){
                    fireButton.handleTouchDown(pointers.get(i).x, pointers.get(i).y);
                }
                if(healId == event.getPointerId(i)){
                    healButton.handleTouchDown(pointers.get(i).x, pointers.get(i).y);
                }
                if(respawnId == event.getPointerId(i)){
                    respawnButton.handleTouchDown(pointers.get(i).x, pointers.get(i).y);
                }
            }
        }
        else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP){
            if(action == MotionEvent.ACTION_UP){
                this.id = event.getPointerId(0);
                pointers.remove(0);
                if(leftId == 0){
                    leftButton.setInactive();
                }
                if(rightId == 0){
                    rightButton.setInactive();
                }
                if(jumpId == 0){
                    jumpButton.setInactive();
                }
                if(crouchId == 0){
                    crouchButton.setInactive();
                }
                if(meleeId == 0){
                    meleeButton.setInactive();
                }
                if(fireId == 0){
                    fireButton.setInactive();
                }
                if(healId == 0){
                    healButton.setInactive();
                }
                if(respawnId == 0){
                    respawnButton.setInactive();
                }
            }
            if(action == MotionEvent.ACTION_POINTER_UP){
                this.id = event.getPointerId(getIndex(event));
                pointers.remove(getIndex(event));
                if(leftId == event.getActionIndex()){
                    leftButton.setInactive();
                }
                if(rightId == event.getActionIndex()){
                    rightButton.setInactive();
                }
                if(jumpId == event.getActionIndex()){
                    jumpButton.setInactive();
                }
                if(crouchId == event.getActionIndex()){
                    crouchButton.setInactive();
                }
                if(meleeId == event.getActionIndex()){
                    meleeButton.setInactive();
                }
                if(fireId == event.getActionIndex()){
                    fireButton.setInactive();
                }
                if(healId == event.getActionIndex()){
                    healButton.setInactive();
                }
                if(respawnId == event.getActionIndex()){
                    respawnButton.setInactive();
                }
            }
        }
        pointerCount = pointers.size();
        if(pointers.size() == 0){
            leftButton.setInactive();
            rightButton.setInactive();
            jumpButton.setInactive();
            crouchButton.setInactive();
            meleeButton.setInactive();
            fireButton.setInactive();
            healButton.setInactive();
            respawnButton.setInactive();
        }
        return true;
    }
    private int getIndex(MotionEvent event){
        return (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) & MotionEvent.ACTION_POINTER_INDEX_SHIFT;
    }
    boolean isColliding = false;
    boolean check = false;
    @Override
    public void update(){
        leftButton.setColour(0, 0, 0);
        rightButton.setColour(0, 0, 0);
        jumpButton.setColour(0, 0, 0);
        crouchButton.setColour(0, 0, 0);
        meleeButton.setColour(0, 0, 0);
        fireButton.setColour(0, 0, 0);
        healButton.setColour(0, 0, 0);
        respawnButton.setColour(0, 0, 0);
        //*****************************************************************************************
        if(xVelocity != 0 && player.getBox().getBottomCollision() && !player.getIsCrouched() && player.getHealth() > 0){
            player.walk();
            animationId = 1;
            if(fallDamageCount > util.screenHeight()){
                player.setHealth(player.getHealth() - ((fallDamageCount - util.screenHeight()) / fallDamageDivisor));
            }
            fallDamageCount = 0;
        }
        else if(xVelocity == 0 && yVelocity == 0 && !player.getIsCrouched() && player.getBox().getBottomCollision() && player.getHealth() > 0){
            player.setIdle();
            animationId = 2;
            if(fallDamageCount > util.screenHeight()){
                player.setHealth(player.getHealth() - ((fallDamageCount - util.screenHeight()) / fallDamageDivisor));
            }
            fallDamageCount = 0;
        }
        else if(yVelocity > 0 && !player.getBox().getBottomCollision() && player.getCurrentJumps() <= player.getMAX_NUMBER_OF_JUMPS() - 1 && player.getCurrentJumps() >= 0 && player.getHealth() > 0){
            player.jump();
            animationId = 3;
            fallDamageCount = 0;
        }
//        else if(yVelocity > 0 && !player.getBox().getBottomCollision() && player.getCurrentJumps() < player.getMAX_NUMBER_OF_JUMPS() - 1){
//            player.flip();
//        }
        else if(yVelocity <= 0 && !player.getBox().getBottomCollision() && !player.getBox().getRightCollision() && !player.getBox().getLeftCollision() && player.getHealth() > 0){
            player.fall();
            animationId = 4;
            fallDamageCount += Math.abs(yVelocity);
        }
        else if((player.getBox().getLeftCollision() || player.getBox().getRightCollision()) && yVelocity <= 0 && !player.getBox().getBottomCollision() && player.getHealth() > 0){
            player.onWall();
            animationId = 5;
            fallDamageCount = 0;
        }
        else if((player.getBox().getLeftCollision() || player.getBox().getRightCollision()) && yVelocity > 0 && !player.getBox().getBottomCollision() && xVelocity == 0 && player.getHealth() > 0){
            player.wallRun();
            animationId = 6;
            fallDamageCount = 0;
        }
        else if(xVelocity == 0 && player.getIsCrouched() && player.getHealth() > 0){
            player.crouch();
            animationId = 7;
            if(fallDamageCount > util.screenHeight()){
                player.setHealth(player.getHealth() - ((fallDamageCount - util.screenHeight()) / fallDamageDivisor));
            }
            fallDamageCount = 0;
        }
        else if(player.getIsCrouched() && xVelocity != 0 && player.getBox().getBottomCollision() && player.getHealth() > 0){
            player.crouchWalk();
            animationId = 8;
            if(fallDamageCount > util.screenHeight()){
                player.setHealth(player.getHealth() - ((fallDamageCount - util.screenHeight()) / fallDamageDivisor));
            }
            fallDamageCount = 0;
        }
        else if(player.getHealth() <= 0 && player.getBox().getBottomCollision()){
            player.death();
        }
        else{
            player.setIdle();
            animationId = 9;
            fallDamageCount = 0;
        }
        //*****************************************************************************************
        for(int i = 0; i<e.mainList.size(); i++){
            e.mainList.get(i).setColour();
        }
        if(rightButton.getActive() && player.getHealth() > 0){
            if(player.getBox().getBottomCollision() || player.getBox().getLeftCollision() || player.getBox().getRightCollision()) {
                xVelocity -= player.getAccelerationX();
                if (xVelocity < -maxVelocity && !player.getIsCrouched()) {
                    xVelocity = -maxVelocity;
                }
                else if(xVelocity < -4 && player.getIsCrouched()){
                    xVelocity = -4;
                }
            }
            else if(!player.getBox().getBottomCollision() && !player.getBox().getTopCollision() && !player.getBox().getRightCollision() && !player.getBox().getLeftCollision()){
                xVelocity -= 0.4;
                if(xVelocity < -maxVelocity){
                    xVelocity = -maxVelocity;
                }
            }
        }
        if(leftButton.getActive() && player.getHealth() > 0){
            if (player.getBox().getBottomCollision() || player.getBox().getLeftCollision() || player.getBox().getRightCollision()) {
                xVelocity += player.getAccelerationX();
                if (xVelocity > maxVelocity && !player.getIsCrouched()) {
                    xVelocity = maxVelocity;
                }
                else if(xVelocity > 4 && player.getIsCrouched()){
                    xVelocity = 4;
                }
            }
            else if(!player.getBox().getBottomCollision() && !player.getBox().getTopCollision() && !player.getBox().getRightCollision() && !player.getBox().getLeftCollision()){
                xVelocity += 0.4;
                if(xVelocity > maxVelocity){
                    xVelocity = maxVelocity;
                }
            }
        }
        player.setIsCrouched(false);
        if((crouchButton.getActive() && player.getBox().getBottomCollision() || (player.getBox().getTopCollision() && player.getBox().getBottomCollision())) && player.getHealth() > 0){
            int bottomBound = player.getBox().getY() + player.getBox().getHeight() / 2;
            player.getBox().setBounds(player.getBox().getWidth(), player.getBox().getWidth());
            player.getBox().setPosition(player.getBox().getX(), bottomBound - (player.getBox().getWidth() / 2));
            player.setIsCrouched(true);
        }
        else{
            player.getBox().setBounds(player.getBox().getWidth(), util.screenHeight() / 6);
            player.getBox().setPosition(player.getBox().getX(), round(util.screenHeight() / 1.6));
            ArrayList<Platform>collisionList = e.getCanDrawList();
            player.setBoxCollisionsFalse();
            for(int i = 0; i < collisionList.size(); i++){
                if(collisionList.get(i).checkCollision(player.getBox())) {
                    isColliding = true;
                    int bLeft = player.getBox().getX() - (player.getBox().getWidth() / 2);
                    int bRight = player.getBox().getX() + (player.getBox().getWidth() / 2);
                    int bTop = player.getBox().getY() - (player.getBox().getHeight() / 2);
                    int bBottom = player.getBox().getY() + (player.getBox().getHeight() / 2);
                    int topDist = round(Math.abs(bBottom - ((collisionList.get(i).getY()) - (collisionList.get(i).getHeight() / 2))));
                    int leftDist = round(Math.abs(bRight - ((collisionList.get(i).getX()) - (collisionList.get(i).getWidth() / 2))));
                    int rightDist = round(Math.abs(bLeft - ((collisionList.get(i).getX()) + (collisionList.get(i).getWidth() / 2))));
                    int bottomDist = round(Math.abs(bTop - ((collisionList.get(i).getY()) + (collisionList.get(i).getHeight() / 2))));
                    if(topDist <= leftDist && topDist <= rightDist && topDist <= bottomDist){
                        player.getBox().setTopCollision(true);
                    }
                    else if(bottomDist < topDist && bottomDist <= leftDist && bottomDist <= rightDist){
                        player.getBox().setBottomCollision(true);
                    }
                    if(leftDist < topDist && leftDist < rightDist && leftDist < bottomDist){
                        player.getBox().setLeftCollision(true);
                    }
                    else if(rightDist < topDist && rightDist < leftDist && rightDist < bottomDist){
                        player.getBox().setRightCollision(true);
                    }
                }
            }
            if(player.getBox().getTopCollision() && player.getBox().getBottomCollision()){
                check = true;
                int bottomBound = player.getBox().getY() + player.getBox().getHeight() / 2;
                player.getBox().setBounds(player.getBox().getWidth(), player.getBox().getWidth());
                player.getBox().setPosition(player.getBox().getX(), bottomBound - (player.getBox().getWidth() / 2));
                player.setIsCrouched(true);
            }
        }
        if(!leftButton.getActive() && !rightButton.getActive() && (player.getBox().getLeftCollision() || player.getBox().getRightCollision() || player.getBox().getTopCollision()||player.getBox().getBottomCollision())) {
            if (xVelocity > player.getAccelerationX()) {
                xVelocity -= player.getAccelerationX();
            }
            if (xVelocity < -player.getAccelerationX()) {
                xVelocity += player.getAccelerationX();
            }
            if(xVelocity <= player.getAccelerationX() && xVelocity >= -player.getAccelerationX()) {
                xVelocity = 0;
            }
        }
        if((player.getBox().getRightCollision() || player.getBox().getLeftCollision())) {
            yVelocity -=(float)(0.55);
            if(yVelocity < -4){
                yVelocity = -4;
            }
        }
        else{
            yVelocity -= 0.8;
            if (yVelocity < -30) {
                yVelocity = -30;
            }
        }
        e.setDisplacement(round(xVelocity), round(yVelocity));
        if(xVelocity > 0){
            player.setFacingLeft(true);
        }
        else if(xVelocity < 0){
            player.setFacingLeft(false);
        }
        ArrayList<Platform>collisionList = e.getCanDrawList();
        player.setBoxCollisionsFalse();
        for(int i = 0; i < collisionList.size(); i++){
            if(collisionList.get(i).checkCollision(player.getBox())){
                isColliding = true;
                int bLeft = player.getBox().getX() - (player.getBox().getWidth() / 2);
                int bRight = player.getBox().getX() + (player.getBox().getWidth() / 2);
                int bTop = player.getBox().getY() - (player.getBox().getHeight() / 2);
                int bBottom = player.getBox().getY() + (player.getBox().getHeight() / 2);
                int topDist = round(Math.abs(bBottom - ((collisionList.get(i).getY()) - (collisionList.get(i).getHeight() / 2))));
                int leftDist = round(Math.abs(bRight - ((collisionList.get(i).getX()) - (collisionList.get(i).getWidth() / 2))));
                int rightDist = round(Math.abs(bLeft - ((collisionList.get(i).getX()) + (collisionList.get(i).getWidth() / 2))));
                int bottomDist = round(Math.abs(bTop - ((collisionList.get(i).getY()) + (collisionList.get(i).getHeight() / 2))));
                int pTop = round(collisionList.get(i).getY()) - collisionList.get(i).getHeight() / 2;
                int pLeft = round(collisionList.get(i).getX()) - collisionList.get(i).getWidth() / 2;
                int pRight = round(collisionList.get(i).getX()) + collisionList.get(i).getWidth() / 2;
                int pBottom = round(collisionList.get(i).getY()) + collisionList.get(i).getHeight() / 2;
                if(topDist <= leftDist && topDist <= rightDist && topDist <= bottomDist){
                    //player on top of platform
                    int backtrack = 0;
                    while(bBottom > pTop + backtrack){
                        backtrack++;
                    }
                    e.setDisplacement(0, backtrack);
                    yVelocity = 0;
                    player.setBoxBCollisionTrue();
                    player.setCurrentJumps(player.getMAX_NUMBER_OF_JUMPS());
                }
                else if(bottomDist < topDist && bottomDist <= leftDist && bottomDist <= rightDist){
                    //player on bottom of platform
                    int backtrack = 0;
                    while(bTop < pBottom + backtrack){
                        backtrack--;
                    }
                    e.setDisplacement(0, backtrack);
                    yVelocity = (float)(-0.8);
                    player.setBoxTCollisionTrue();
                }
                if(leftDist < topDist && leftDist < rightDist && leftDist < bottomDist){
                    //player on left of platform
                    int backtrack = 0;
                    while(bRight > pLeft + backtrack){
                        backtrack++;
                    }
                    if(!(player.getBox().getBottomCollision() && player.getBox().getTopCollision())) {
                        e.setDisplacement(backtrack, 0);
                    }
                    xVelocity = 0;
                    player.setBoxRCollisionTrue();
                    player.setFacingLeft(false);
                    player.setCurrentJumps(player.getMAX_NUMBER_OF_JUMPS());
                }
                else if(rightDist < topDist && rightDist < leftDist && rightDist < bottomDist){
                    //player on right of platform
                    int backtrack = 0;
                    while(bLeft < pRight + backtrack){
                        backtrack--;
                    }
                    if(!(player.getBox().getBottomCollision() && player.getBox().getTopCollision())) {
                        e.setDisplacement(backtrack, 0);
                    }
                    if(xVelocity > 0.5) {
                        xVelocity = 0;
                    }
                    player.setBoxLCollisionTrue();
                    player.setFacingLeft(true);
                    player.setCurrentJumps(player.getMAX_NUMBER_OF_JUMPS());
                }
            }
        }
        if(player.getPlayAnimation()){
            player.runAnimation();
        }
    }
    @Override
    public void draw(Canvas canvas){
        paint.setTextSize(40);
        paint .setStrokeWidth(4);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        super.draw(canvas);
        canvas.drawColor(Color.rgb(255, 255, 255));

        e.draw(canvas);

        canvas.drawText("FPS: " + MainThread.getFPS(), 90, 60, paint);
        canvas.drawText("x: " + -e.mainList.get(0).getXDisplacement() + ", y: " + e.mainList.get(0).getYDisplacement(), 90, 100, paint);
        canvas.drawText("animationCount: " + player.getAnimationCount() + ", playAnimation: " + player.getPlayAnimation(), 90, 140, paint);
        if(e.getCanDrawList().size() > mostDrawn){
            mostDrawn = e.getCanDrawList().size();
        }

//        canvas.drawText("# drawn platforms: " + e.getCanDrawList().size() + ", most Drawn: " + mostDrawn, 10, 140, paint);
//        if(animationId == 1){
//            canvas.drawText("walking. count: " + player.getWalkCount() + ", keyframe: " + player.getWalkKeyFrame() , 10, 180, paint);
//        }
//        else if(animationId == 2){
//            canvas.drawText("idle", 10, 180, paint);
//        }
//        else if(animationId == 3){
//            canvas.drawText("jumping", 10, 180, paint);
//        }
//        else if(animationId == 4){
//            canvas.drawText("falling", 10, 180, paint);
//        }
//        else if(animationId == 5){
//            canvas.drawText("sliding down wall", 10, 180, paint);
//        }
//        else if(animationId == 6){
//            canvas.drawText("wall running", 10, 180, paint);
//        }
//        else if(animationId == 7){
//            canvas.drawText("crouching", 10, 180, paint);
//        }
//        else if(animationId == 8){
//            canvas.drawText("crouch walking", 10, 180, paint);
//        }
//        else {
//            canvas.drawText("idling", 10, 180, paint);
//        }
        player.getBox().setCanDraw(true);
        if(player.getHealth() < player.healthBar.getMaxHealth() && player.getHealth() > 0 && regenCount > 0){
            regenCount--;
            player.setHealth(player.getHealth() + round(player.healthBar.getMaxHealth() / 100));
        }
        else{
            regenCount = 0;
        }
        if(player.healthBar.getCurrentHealth() < player.healthBar.getMaxHealth() / 2){
            paint.setColor(Color.argb(round((float)(255) - ((float)(player.healthBar.getCurrentHealth()) / (float)(player.healthBar.getMaxHealth() / 2) * (float) (255))),150, 0, 0));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(round( (float)(100) - (float)(player.healthBar.getCurrentHealth()) / (float)(player.healthBar.getMaxHealth() / 2) * (float) (100)));
            paint.setMaskFilter(new BlurMaskFilter(paint.getStrokeWidth() / 2, BlurMaskFilter.Blur.NORMAL));
            canvas.drawRect(0, 0, util.screenWidth(), util.screenHeight(), paint);
        }
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
        paint.setMaskFilter(null);
        player.draw(canvas);
        leftButton.draw(canvas);
        rightButton.draw(canvas);
        jumpButton.draw(canvas);
        crouchButton.draw(canvas);
        meleeButton.draw(canvas);
        fireButton.draw(canvas);
        healButton.draw(canvas);
        respawnButton.draw(canvas);
        canvas.drawText("" + heals, healButton.getX() - paint.getTextSize() / 4, healButton.getY() + paint.getTextSize() / 3, paint);
        paint.setMaskFilter(null);
        path.addArc(respawnButton.getX() - respawnButton.getRadius() / 2, respawnButton.getY() - respawnButton.getRadius() / 2, respawnButton.getX() + respawnButton.getRadius() / 2, respawnButton.getY() + respawnButton.getRadius() / 2, -45, 300);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
        path.reset();
        paint.setStyle(Paint.Style.FILL);
        path.moveTo(respawnButton.getX() + 5, respawnButton.getY() - 21);
        path.lineTo(respawnButton.getX() + 5, respawnButton.getY() - 1);
        path.lineTo(respawnButton.getX() + 25, respawnButton.getY() - 21);
        path.setFillType(Path.FillType.EVEN_ODD);
        canvas.drawPath(path, paint);
        path.reset();
    }
    private static int round(float floatNum){
        int intNum = (int)(floatNum);
        float decimal = floatNum - intNum;
        if(decimal >= 0.5){
            intNum++;
        }
        return intNum;
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
