package com.nisanabi.mygame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by hello on 11/12/2015.
 */
public class SmokePuff extends GameObject{

    public int r;

    public SmokePuff(int x, int y){

        super.x = x;
        super.y = y;
    }

    public void update(){
        x += 30;
    }

    public void draw(Canvas canvas){
        Paint paint = new Paint();

        paint.setColor(Color.MAGENTA);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(x+40 , y-5 , x+60, y+5, paint);
    }
}
