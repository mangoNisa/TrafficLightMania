package com.nisanabi.mygame;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    public static final int WIDTH = 500;
    public static final int HEIGHT = 800;
    public static final int MOVESPEED = -5;
    private long smokeStartTime;
    private long missileStartTime;
    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<SmokePuff> smoke;
    private ArrayList<Missile> missiles;
    private Random rand = new Random();
    private int maxBorderHeight;
    private int minBorderHeight;
    private boolean topDown = true;
    private boolean botDown = true;
    private boolean newGameCreated;
    //increase to slow down difficulty progression, decrease to speed up difficulty progression
    private int progressDenom = 20;

    private Explosion explosion;
    private long startReset;
    private boolean reset;
    private boolean dissapear;
    private boolean started;
    private int best;

    public GamePanel(Context context)
    {
        super(context);


        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter<1000)
        {
            counter++;
            try{thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;
            }catch(InterruptedException e){e.printStackTrace();}

        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.car), 128, 80, 3);
        smoke = new ArrayList<SmokePuff>();
        missiles = new ArrayList<Missile>();

        smokeStartTime=  System.nanoTime();
        missileStartTime = System.nanoTime();

        thread = new MainThread(getHolder(), this);
        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(!player.getPlaying() && newGameCreated && reset)
            {
                player.setPlaying(true);
                player.setUp(true);
            }
            if(player.getPlaying()){
                if(!started) started = true;
                reset = false;
                player.setUp(true);
            }
            return true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP)
        {
            player.setUp(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void update()

    {
        if(player.getPlaying()) {



            bg.update();
            player.update();

            //calculate the threshold of height the border can have based on the score
            //max and min border heart are updated, and the border switched direction when either max or
            //min is met

            maxBorderHeight = 30+player.getScore()/progressDenom;
            //cap max border height so that borders can only take up a total of 1/2 the screen
            if(maxBorderHeight > HEIGHT/4)maxBorderHeight = HEIGHT/4;
            minBorderHeight = 5+player.getScore()/progressDenom;

            //add missiles on timer
            long missileElapsed = (System.nanoTime()-missileStartTime)/1000000;
            if(missileElapsed >(2000 - player.getScore()/4)){


                //first missile always goes down the middle
                if(missiles.size()==0)
                {
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),R.drawable.
                            missile),WIDTH + 10, HEIGHT/2, 45, 15, player.getScore(), 13));
                }
                else
                {

                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),R.drawable.missile),
                            WIDTH+10, (int)(rand.nextDouble()*(HEIGHT - (maxBorderHeight * 2))+maxBorderHeight),45,15, player.getScore(),13));
                }

                //reset timer
                missileStartTime = System.nanoTime();
            }
            //loop through every missile and check collision and remove
            for(int i = 0; i<missiles.size();i++)
            {
                //update missile
                missiles.get(i).update();

                if(collision(missiles.get(i),player))
                {
                    missiles.remove(i);
                    player.setPlaying(false);
                    break;
                }
                //remove missile if it is way off the screen
                if(missiles.get(i).getX()<-100)
                {
                    missiles.remove(i);
                    break;
                }
            }


            //add smoke puffs on timer
            long elapsed = (System.nanoTime() - smokeStartTime)/1000000;
            if(elapsed > 120){
                smoke.add(new SmokePuff(player.getX(), player.getY()+10));
                smokeStartTime = System.nanoTime();
            }

            for(int i = 0; i<smoke.size();i++)
            {
                smoke.get(i).update();
                if(smoke.get(i).getX()<-10)
                {
                    smoke.remove(i);
                }
            }
        }
        else{

            player.resetDY();

            //not reset yet and player is not playing
            if(!reset){
                newGameCreated = false;
                startReset = System.nanoTime();
                reset = true;
                dissapear = true;
                explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion)
                ,player.getX(), player.getY()-30, 140, 140, 35);
            }

            explosion.update();

            //time it waits before player can start game again
            long resetElapsed = (System.nanoTime() - startReset)/1000000;

            if(resetElapsed>2500 && !newGameCreated){
                newGame();
            }
        }
    }
    public boolean collision(GameObject a, GameObject b)
    {
        if(Rect.intersects(a.getRectangle(), b.getRectangle()))
        {
            return true;
        }
        return false;
    }
    @Override
    public void draw(Canvas canvas)
    {
        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);

        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);

            if(!dissapear) {
                player.draw(canvas);
            }
            //draw smokepuffs
            for(SmokePuff sp: smoke)
            {
                sp.draw(canvas);
            }
            //draw missiles
            for(Missile m: missiles)
            {
                m.draw(canvas);
            }


            //draw explostion
            if(started){
                explosion.draw(canvas);
            }

            drawText(canvas);

            canvas.restoreToCount(savedState);


        }
    }


    public void newGame()
    {
        dissapear = false;

        missiles.clear();
        smoke.clear();

        minBorderHeight = 5;
        maxBorderHeight = 30;

        player.resetDY();
        player.resetScore();
        player.setY(HEIGHT / 2);

        if(player.getScore()>best){
            best = player.getScore();
        }
        //create initial borders
        newGameCreated = true;


    }

    public void drawText(Canvas canvas){

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("" + (player.getScore()), (WIDTH/2)-20, 30, paint);

        if(!player.getPlaying() && newGameCreated && reset){

            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS TO START", WIDTH / 2 - 200, HEIGHT / 2+130, paint1);

            paint1.setTextSize(20);
            canvas.drawText("PRESS AND HOLD TO GO UP", WIDTH / 2, HEIGHT / 2 + 300, paint1);
            canvas.drawText("RELEASE TO GO DOWN", WIDTH/2-50, HEIGHT/2+340, paint1);

        }
    }


}