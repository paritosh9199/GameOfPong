package com.android.paritosh.gameofpong;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView scoreL = (TextView) findViewById(R.id.scoreLabel);
        TextView highSL = (TextView) findViewById(R.id.highScoreLabel);
        FullScreencall();

        int score = getIntent().getIntExtra("score", 0);
        scoreL.setText(score + "");

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

    public void game(View view) {
        Intent i = new Intent(ScoreActivity.this, GameActivity.class);
        startActivity(i);
        finish();
    }

    public void mainMenu(View view) {
        Intent i = new Intent(ScoreActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }


}
