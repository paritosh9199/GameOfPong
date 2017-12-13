package com.android.paritosh.gameofpong;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import static android.os.Build.VERSION_CODES.M;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FullScreencall();
        setContentView(R.layout.activity_main);
    }

    /*public void heyyo(View view){
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }*/
    public void game(View view){
        Intent i = new Intent(MainActivity.this, GameActivity.class);
        startActivity(i);
    }
    public void options(View view){
        Intent i = new Intent(MainActivity.this, OptionsActivity.class);
        startActivity(i);
    }
    public void info(View view){
        Intent i = new Intent(MainActivity.this, InfoActivity.class);
        startActivity(i);
    }

    public void cred(View view){
        Intent i = new Intent(MainActivity.this, CreditaActivity.class);
        startActivity(i);
    }

    public void FullScreencall() {
        if(Build.VERSION.SDK_INT < 19){
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
}
