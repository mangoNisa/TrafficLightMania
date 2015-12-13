package com.nisanabi.mygame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by hello on 11/12/2015.
 */
public class Explosion {

    private int x;
    private int y;
    private  int width;
    private int height;
    private int row;
    private Animation animation = new Animation();
    private Bitmap spritesheet;

    public Explosion(Bitmap res, int x, int y, int w, int h, int numFrame){
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;

        Bitmap[] image = new Bitmap[numFrame];

        spritesheet = res;

        for(int i = 0; i<image.length; i++){
            if(i%5 == 0 && i>0)row++;
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(100);
    }

    public void update(){
        if(!animation.playedOnce()){
            animation.update();
        }
    }

    public void draw(Canvas canvas){
        if(!animation.playedOnce()){
            canvas.drawBitmap(animation.getImage(), x, y, null);
        }
    }

    public int getHeight(){
        return height;
    }
}
