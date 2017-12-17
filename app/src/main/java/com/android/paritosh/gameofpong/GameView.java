package com.android.paritosh.gameofpong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;

import static com.android.paritosh.gameofpong.R.id.scoreDisplay;

/**
 * Created by PARITOSH on 9/25/2017.
 */

    // Notice we implement runnable so we have
    // A thread and can override the run method.
    public class GameView  extends SurfaceView implements Runnable {
        // This is our thread
        Thread mGameThread = null;
        // This is new. We need a SurfaceHolder
        // When we use Paint and Canvas in a thread
        // We will see it in action in the draw method soon.
        SurfaceHolder mOurHolder;
        // A boolean which we will set and unset
        // when the game is running- or not
        volatile boolean mPlaying;
        // Game is mPaused at the start
        boolean mPaused = true;
        // A Canvas and a Paint object
        Canvas mCanvas;
        Paint mPaint;
        // This variable tracks the game frame rate
        long mFPS;
        // The size of the screen in pixels
        int mScreenX;
        int mScreenY;
        // The players mPaddle
        Paddle mPaddle1;
        // A mBall
        Ball mBall;
        // For sound FX
        SoundPool sp;

        int loseLifeID = -1;
    int explodeID = -1;
        // The mScore
        int mScore = 0;
        // Lives
        int mLives = 3;


    Vibrator vibe;

    SharedPreferences setting = this.getContext().getSharedPreferences("LEVEL_DATA", Context.MODE_PRIVATE);
    SharedPreferences vibration = this.getContext().getSharedPreferences("VIBRATION_DATA", Context.MODE_PRIVATE);
    SharedPreferences audio = this.getContext().getSharedPreferences("AUDIO_DATA", Context.MODE_PRIVATE);

    int lv = setting.getInt("LEVEL", 0);
    boolean v = vibration.getBoolean("Vibrate", true);
    boolean a = audio.getBoolean("aud", true);
    /*
    When we call new() on gameView
    This custom constructor runs
    */
    public GameView(Context context, int x, int y) {

    /*
    The next line of code asks the
    SurfaceView class to set up our object.
    */
        super(context);
        // Set the screen width and height
        mScreenX = x;
        mScreenY = y;
        // Initialize mOurHolder and mPaint objects
        mOurHolder = getHolder();
        mPaint = new Paint();
        // A new mPaddle
        mPaddle1 = new Paddle(mScreenX, mScreenY);
        // Create a mBall
        mBall = new Ball(mScreenX, mScreenY);
        vibe = (Vibrator) this.getContext().getSystemService(Context.VIBRATOR_SERVICE);
 /*
 Instantiate our sound pool
 dependent upon which version
 of Android is present
 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes =
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build();
            sp = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else {
            sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }
        try{
            // Create objects of the 2 required classes
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;
            // Load our fx in memory ready for use
            descriptor = assetManager.openFd("loseLife.ogg");
            loseLifeID = sp.load(descriptor, 0);
            descriptor = assetManager.openFd("explode.ogg");
            explodeID = sp.load(descriptor, 0);
        }catch(IOException e){
            // Print an error message to the console
            Log.e("error", "failed to load sound files");
        }
        setupAndRestart();
    }

    public void setupAndRestart(){
        // Put the mBall back to the start
        mBall.reset(mScreenX, mScreenY);
        mPaddle1.resetPaddle(mScreenX,mScreenY);
        // if game over reset scores and mLives
        if(mLives == 0) {

            //GameActivity g = new GameActivity();
            //g.start(mScore);

            //vibe.vibrate(200);
            mScore = 0;
            mLives = 3;

        }
    }

    public void gameOver(Context c) {

        if (mLives == 0) {

            //GameActivity g = new GameActivity();
            //g.start(mScore);


            Intent i = new Intent(c, ScoreActivity.class);
            i.putExtra("score", mScore);
            c.startActivity(i);
            ((Activity) (c)).finish();
            //getActivity().finish();


            mScore = 0;
            mLives = 3;

        }
    }

    public void vibrationSetter(int length) {
        if (v == true) {
            vibe.vibrate(length);
        }
    }


    @Override
    public void run() {
        while (mPlaying) {
            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();
            // Update the frame
            // Update the frame
            if(!mPaused){
                update();
            }
            // Draw the frame
            draw();
    /*
    Calculate the FPS this frame
    We can then use the result to
    time animations in the update methods.
    */
            long timeThisFrame = System.currentTimeMillis()
                    - startFrameTime;
            if (timeThisFrame >= 1) {
                mFPS = 1000 / timeThisFrame;
            }
        }
    }

    // Everything that needs to be updated goes in here
// Movement, collision detection etc.
    public void update() {
        // Move the mPaddle if required
        mPaddle1.update(mFPS);
        mBall.update(mFPS);
        // Check for mBall colliding with mPaddle
        if(RectF.intersects(mPaddle1.getRect(), mBall.getRect())) {
            mBall.setRandomXVelocity();
            mBall.reverseYVelocity();
            mBall.clearObstacleY(mPaddle1.getRect().top - 2);
            mScore++;
            mBall.increaseVelocity(lv);
            if (a == true) {
                sp.play(loseLifeID, 1, 1, 0, 0, 1);
            }
            vibrationSetter(30);
        }
        // Bounce the mBall back when it hits the bottom of screen
        if(mBall.getRect().bottom > mScreenY){
            mBall.reverseYVelocity();
            mBall.clearObstacleY(mScreenY - 2);
            // Lose a life
            mLives--;

            mBall.increaseVelocity(lv);

            if (a == true) {
                sp.play(explodeID, 1, 1, 0, 0, 1);
            }
            vibrationSetter(100);
            if(mLives == 0){
                //mPaused = true;
                gameOver(getContext());

            }
        }
        // Bounce the mBall back when it hits the top of screen
        if(mBall.getRect().top < 0){
            mBall.reverseYVelocity();

            //mBall.increaseVelocity();

            mBall.clearObstacleYtop(30);
            if (a == true) {
                sp.play(loseLifeID, 1, 1, 0, 0, 1);
            }

            vibrationSetter(30);
        }
        // If the mBall hits left wall bounce
        if(mBall.getRect().left < 0){
            mBall.reverseXVelocity();

            //mBall.increaseVelocity();

            mBall.clearObstacleX(2);
            if (a == true) {
                sp.play(loseLifeID, 1, 1, 0, 0, 1);
            }
            vibrationSetter(30);
        }
        // If the mBall hits right wall bounce
        if(mBall.getRect().right > mScreenX){
            mBall.reverseXVelocity();
            mBall.clearObstacleX(mScreenX - 22);
            if (a == true) {
                sp.play(loseLifeID, 1, 1, 0, 0, 1);
            }
            vibrationSetter(30);
        }
    }

    // Draw the newly updated scene
    public void draw() {
        // Make sure our drawing surface is valid or we crash
        if (mOurHolder.getSurface().isValid()) {
            // Draw everything here
            // Lock the mCanvas ready to draw
            mCanvas = mOurHolder.lockCanvas();
            // Draw the background color

            /*
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.bgmain);
            mCanvas.drawBitmap(bitmap, 0, 0, mPaint);
            this is used to add image as a background
            rendering is slow here use with caution
            */


            mCanvas.drawColor(Color.argb(255, 0, 0, 0));
            // Choose the brush color for drawing
            mPaint.setColor(Color.argb(255, 255, 255, 255));


            // Draw the mPaddle
            mCanvas.drawRect(mPaddle1.getRect(), mPaint);
            // Draw the mBall in oval shape
            mCanvas.drawOval(mBall.getRect(), mPaint);

            // Choose the brush color for drawing
            mPaint.setColor(Color.argb(255, 255, 255, 255));
            // Draw the mScore
            mPaint.setTextSize(40);
            /*
            gotta modify the draw text to text view and define a setter function
             */
            mCanvas.drawText("Score: " + mScore +
                    " Lives: " + mLives, 10, 50, mPaint);

            // Draw everything to the screen
            mOurHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    // If the Activity is paused/stopped
    // shutdown our thread.
    public void pause() {
        mPlaying = false;
        try {
            mGameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    // If the Activity starts/restarts
    // start our thread.
    public void resume() {
        mPlaying = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }

    // The SurfaceView class implements onTouchListener
    // So we can override this method and detect screen touches.
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                mPaused = false;
                // Is the touch on the right or left?
                if(motionEvent.getX() > mScreenX / 2){
                    mPaddle1.setMovementState(mPaddle1.RIGHT);
                }
                else{
                    mPaddle1.setMovementState(mPaddle1.LEFT);
                }
                break;
            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:
                mPaddle1.setMovementState(mPaddle1.STOPPED);
                break;
        }
        return true;
    }


}
