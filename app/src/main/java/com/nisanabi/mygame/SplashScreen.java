package com.nisanabi.mygame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    public static SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        pref = this.getSharedPreferences("cargame", Context.MODE_PRIVATE);

        Thread openMenu = new Thread(){

            @Override
            public void run(){
                try {

                    Intent i = new Intent(getApplicationContext(), Game.class );



                    sleep(3000);
                    startActivity(i);

                } catch (InterruptedException e) {

                }

                finish();
            }
        };
        openMenu.start();
    }

    public static void setHighScore(int s){
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("highscore", s);
        editor.commit();
    }

    public static int getHighScore(){
        int i = pref.getInt("highscore", 0);
        return i;
    }
}
