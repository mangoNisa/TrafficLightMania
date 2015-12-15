package com.nisanabi.mygame;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HighScore extends AppCompatActivity {

    SharedPreferences bestScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public HighScore(){
        bestScore = this.getSharedPreferences("flyingcar", Context.MODE_PRIVATE);
    }
    public void setHighScore(int s){
        bestScore = this.getSharedPreferences("flyingcar", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = bestScore.edit();
        editor.putInt("highscore", s);
        editor.commit();

    }

    public int getHighScore(){
        System.out.println("****************************************");

        bestScore = this.getSharedPreferences("flyingcar", Context.MODE_PRIVATE);
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

        int score = bestScore.getInt("highscore", 0);
        System.out.println("SCOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOORE" + score);

        return 1;
    }
}
