package com.nisanabi.mygame;

import android.graphics.Rect;

/**
 * Created by hello on 11/12/2015.
 */
public abstract class GameObject {

    protected int x;
    protected int y;
    protected  int dy;
    protected int dx;
    protected int width;
    protected int height;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Rect getRectangle(){
        return new Rect(x, y, x+width, y+height);
    }
}
