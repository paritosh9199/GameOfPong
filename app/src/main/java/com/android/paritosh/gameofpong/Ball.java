package com.android.paritosh.gameofpong;

import android.graphics.RectF;

import java.util.Random;

/**
 * Created by PARITOSH on 9/25/2017.
 */

public class Ball {
    private RectF mRect;
    private float mXVelocity;
    private float mYVelocity;
    private float mBallWidth;
    private float mBallHeight;

    public Ball(int screenX, int screenY) {
        // Make the mBall size relative to the screen resolution
        //changed from 100 to 80
        mBallWidth = screenX / 80;
        mBallHeight = mBallWidth;
        /*
        Start the ball travelling straight up
        at a quarter of the screen height per second
        */
        // started wid 4
        mYVelocity = screenY / 2;
        mXVelocity = mYVelocity;
        // Initialize the Rect that represents the mBall
        mRect = new RectF();
    }

    // Give access to the Rect
    public RectF getRect(){
        return mRect;
    }

    // Change the position each frame
    public void update(long fps){
        mRect.left = mRect.left + (mXVelocity / fps);
        mRect.top = mRect.top + (mYVelocity / fps);
        mRect.right = mRect.left + mBallWidth;
        mRect.bottom = mRect.top - mBallHeight;
    }

    // Reverse the vertical heading
    public void reverseYVelocity(){
        mYVelocity = -mYVelocity;
    }
    // Reverse the horizontal heading
    public void reverseXVelocity() {
        mXVelocity = -mXVelocity;
    }
    public void setRandomXVelocity(){
        Random generator = new Random();
        int answer = generator.nextInt(2);
        if(answer == 0){
            reverseXVelocity();
        }
    }
    // Speed up by 10%
    // A score of 25 is damn tough on this setting
    // initial setting to be low

    /*
    create a function flag type to adjust the speed
    to do that got to work with increaseVelocity()
     */
    public void increaseVelocity(){
        mXVelocity = mXVelocity + mXVelocity / 100;
        mYVelocity = mYVelocity + mYVelocity / 110;
    }

    public void clearObstacleY(float y){
        mRect.bottom = y;
        mRect.top = y - mBallHeight;
    }
    public void clearObstacleYtop(float y){
        mRect.top = y;
        mRect.bottom = y - mBallHeight;
    }
    public void clearObstacleX(float x){
        mRect.left = x;
        mRect.right = x + mBallWidth;
    }
    public void reset(int x, int y){
        mRect.left = x / 2 - mBallWidth/2;
        mRect.top = y - 20;
        mRect.right = x / 2 + mBallWidth - mBallWidth/2;
        mRect.bottom = y - 20 - mBallHeight;
    }

}
