package com.nisanabi.mygame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by hello on 12/12/2015.
 */
public class YellowLight extends GameObject{

    private int score;
    private int speed;
    private Random rand = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet;

    public YellowLight(Bitmap res, int x, int y, int w, int h, int s, int numFrames){
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        score = s;

        speed = 7 + (int) (rand.nextDouble()*score);

        //cap missle speed
        if(speed%2 ==0 ){
            speed = 8;
        }else if(speed >= 20){
            speed = 20;
        }

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        //loop through the images assignment element of array to a missile animation
        for(int i = 0; i<image.length; i++){
            image[i] = Bitmap.createBitmap(spritesheet, 0, i*height, width, height);
        }

        animation.setFrames(image);
        //if missle is faster, then delay will decreate so animation will be faster
        animation.setDelay(100-speed);
    }

    public void update(){
        x-=speed;
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
}
