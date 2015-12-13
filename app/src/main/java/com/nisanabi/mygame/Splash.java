package com.nisanabi.mygame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Thread splashThread = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(3000);
                    Intent i = new Intent(getApplicationContext(), Game.class);
                    startActivity(i);
                    finish();
                } catch (InterruptedException e) {}
            }
        };

        splashThread.start();
    }
}
