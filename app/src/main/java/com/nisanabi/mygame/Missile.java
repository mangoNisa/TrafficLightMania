package com.nisanabi.mygame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by hello on 11/12/2015.
 */
public class Missile extends GameObject{

    private int score;
    private int speed;
    private Random rand = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet;
    private boolean drop;

    public Missile(Bitmap res, int x, int y, int w, int h, int s, int numFrames){
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        score = s;
        drop = false;

        speed = 10 + (int) (rand.nextDouble()*score);

        //cap missle speed
        if(speed >= 16){
            speed = 16;
        }

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        //loop through the images assignment element of array to a missile animation
        for(int i = 0; i<image.length; i++){
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        animation.setFrames(image);
        //if missle is faster, then delay will decreate so animation will be faster
        animation.setDelay(20);
    }

    public void update(){
        if(!drop) {
            x -= speed;
        }else{
            y += 30;
        }
        animation.update();
    }

    public void draw(Canvas canvas){
        try{
            canvas.drawBitmap(animation.getImage(), x,y,null);
        }catch(Exception e){}
    }

    @Override
    public int getWidth(){

        //offset a bit for more realisitic collision detection
        return width-10;
    }

    public void drop(boolean b){
        drop = b;
    }

    public boolean getDrop() { return this.drop; }
}
