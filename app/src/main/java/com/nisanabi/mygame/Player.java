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
        x = 20;
        y = GamePanel.HEIGHT/2;
        dy = 0;
        score = 0;
        height = h;
        width = w;
        max = 10;
        min = -14;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for(int i = 0; i<image.length; i++){
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }


        animation.setFrames(image);
        animation.setDelay(15);
        startTime = System.nanoTime();
    }

    public void setUp(boolean b){
        up = b;
    }

    public void update(){
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed>1000){
            //score++;
            startTime = System.nanoTime();
        }
        animation.update();

        if(up){
            dy -=  1;
            y -=15;
        }else{
            dy += 1;
            y+= dy*2;

        }

        if(dy>max) dy = max;
        if(dy<min) dy = min;

    }

    public void draw(Canvas canvas){

        if(y > GamePanel.HEIGHT - (height + 25) || y < 25){
            setPlaying(false);
        }
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
        max = 10;
        min = -14;
    }

    public void setMaxMin(int max, int min){
        this.max = max;
        this.min = min;
    }
    public void resetScore(){
        score = 0;
    }
}

