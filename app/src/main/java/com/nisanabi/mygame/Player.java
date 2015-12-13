package com.nisanabi.mygame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by hello on 11/12/2015.
 */
public class Player extends GameObject{

    private Bitmap spritesheet;
    private int score;
    private boolean up;
    private boolean playing;
    private Animation animation = new Animation();
    private long startTime;
    private int max, min;

    public Player(Bitmap res, int w, int h, int numFrames){
        x = 100;
        y = GamePanel.HEIGHT/2;
        dy = 0;
        score = 0;
        height = h;
        width = w;
        max = 7;
        min = -7;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for(int i = 0; i<image.length; i++){
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }


        animation.setFrames(image);
        animation.setDelay(10);
        startTime = System.nanoTime();
    }

    public void setUp(boolean b){
        up = b;
    }

    public void update(){
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed>100){
            //score++;
            startTime = System.nanoTime();
        }
        animation.update();

        if(up){
            dy -=  1;
        }else{
            dy += 1;
        }

        if(dy>max) dy = max;
        if(dy<min) dy = min;

        y+= dy*2;
    }

    public void draw(Canvas canvas){

        if(y< 0 ) y = 0;
        else if(y > GamePanel.HEIGHT - height ) y = GamePanel.HEIGHT - height;
        canvas.drawBitmap(animation.getImage(), x,y,null );

    }

    public int getScore(){
        return score;
    }

    public void setScore(int s){
        score +=s;
    }

    public boolean getPlaying(){
        return playing;
    }

    public void setPlaying(boolean b){
        playing = b;
    }

    public void resetDY(){
        dy = 0;
        max = 7;
        min = -7;
    }

    public void setMaxMin(int max, int min){
        this.max = max;
        this.min = min;
    }
    public void resetScore(){
        score = 0;
    }
}

