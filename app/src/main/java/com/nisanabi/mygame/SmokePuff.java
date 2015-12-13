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

        r = 10;
        super.x = x;
        super.y = y;
    }

    public void update(){
        x -= 20;
    }

    public void draw(Canvas canvas){
        Paint paint = new Paint();

        paint.setColor(Color.MAGENTA);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(x - r, y - r, r, paint);
        canvas.drawCircle(x-r+4, y-r-4, r, paint);
        canvas.drawCircle(x-r+8, y-r+2, r, paint);

    }
}
