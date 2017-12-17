package com.android.paritosh.gameofpong;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class OptionsActivity extends AppCompatActivity {

    SharedPreferences level, vibration, audio;
    private int mDifficulty;
    private TextView levelDisplay, vibrationDisplay, audioDisplay;
    private boolean mVibrationFlag, mAudioFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_options);
        FullScreencall();
        levelDisplay = (TextView) findViewById(R.id.level);
        vibrationDisplay = (TextView) findViewById(R.id.vSwitchView);
        audioDisplay = (TextView) findViewById(R.id.audio);


        level = getSharedPreferences("LEVEL_DATA", Context.MODE_PRIVATE);
        vibration = getSharedPreferences("VIBRATION_DATA", Context.MODE_PRIVATE);
        audio = getSharedPreferences("AUDIO_DATA", Context.MODE_PRIVATE);
        int hs = levelGetter();
        if (hs < 1) {
            mDifficulty = 1;
            levelSetter(mDifficulty);
            levelDisplay.setText("" + levelGetter());

        }
        if (hs > 3) {
            mDifficulty = 3;
            levelSetter(mDifficulty);
            levelDisplay.setText("" + levelGetter());
        }
        if (hs >= 1 && hs <= 3) {
            levelDisplay.setText("" + levelGetter());
        }


        mVibrationFlag = vibrationGetter();
        vibS();

        mAudioFlag = audioGetter();
        aubS();


        Log.i("pref", "the level is " + levelGetter());
        Log.i("start", "vib mode " + vibrationGetter());
    }

    public void ext(View view) {
        Intent i = new Intent(OptionsActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void FullScreencall() {
        if (Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for higher api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }

    }

    //=========================================================================================
    //=========================================================================================

        /*
          SharedPreferences setting = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int hs = setting.getInt("HIGH_SCORE", 0);

        if (score > hs) {
            highSL.setText("High Score: " + score);

            SharedPreferences.Editor editor = setting.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();
        } else {
            highSL.setText("High Score: " + hs);
        }
        */
    //=========================================================================================
    //=========================================================================================


    public void levelSetter(int lev) {
        SharedPreferences.Editor editor = level.edit();
        editor.putInt("LEVEL", lev);
        editor.commit();
    }

    public int levelGetter() {
        return level.getInt("LEVEL", 0);
    }

    public void vibrationSetter(boolean v) {
        SharedPreferences.Editor editor = vibration.edit();
        editor.putBoolean("Vibrate", v);
        editor.commit();
    }

    public boolean vibrationGetter() {
        return vibration.getBoolean("Vibrate", true);
    }

    public void increment(View view) {
        mDifficulty++;
        if (mDifficulty > 3) {
            mDifficulty = 3;
        }
        levelDisplay.setText("" + mDifficulty);
        levelSetter(mDifficulty);
        Log.i("incre", "the level is " + levelGetter());
    }

    public void decrement(View view) {
        mDifficulty--;
        if (mDifficulty < 1) {
            mDifficulty = 1;
        }
        levelDisplay.setText("" + mDifficulty);
        levelSetter(mDifficulty);
        Log.i("decr", "the level is " + levelGetter());
    }

    public void vibS() {
        if (mVibrationFlag == true) {
            vibrationDisplay.setText("ON");
        } else if (mVibrationFlag == false) {
            vibrationDisplay.setText("OFF");
        }
    }

    public void vibSwitch(View view) {
        mVibrationFlag = vibrationGetter();
        if (mVibrationFlag == true) {
            mVibrationFlag = false;
        } else {
            mVibrationFlag = true;
        }
        vibS();
        vibrationSetter(mVibrationFlag);

        Log.i("vibs", "vib mode " + vibrationGetter());

    }

    public void aubS() {
        if (mAudioFlag == true) {
            audioDisplay.setText("ON");
        } else if (mAudioFlag == false) {
            audioDisplay.setText("OFF");
        }
    }

    public void audioSwitch(View view) {
        mAudioFlag = audioGetter();
        if (mAudioFlag == true) {
            mAudioFlag = false;
        } else {
            mAudioFlag = true;
        }
        aubS();
        audioSetter(mAudioFlag);
        Log.i("aud", "aud mode " + audioGetter());
    }

    public void audioSetter(boolean v) {
        SharedPreferences.Editor editor = audio.edit();
        editor.putBoolean("aud", v);
        editor.commit();
    }

    public boolean audioGetter() {
        return audio.getBoolean("aud", true);
    }
}
