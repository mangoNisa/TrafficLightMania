package com.nisanabi.mygame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

/**
 * Created by hello on 11/12/2015.
 */
public class Arrow extends GameObject{

   private Bitmap image;

    public Arrow(Bitmap res, int x, int y){
        super.x = x;
        super.y = y;
        image = res;

    }

    public void update(){
        x += 30;
    }

    public void draw(Canvas canvas){
        Paint paint = new Paint();

        paint.setColor(Color.MAGENTA);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawBitmap(image , x , y, null);
    }


}
