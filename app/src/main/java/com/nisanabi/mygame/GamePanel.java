package com.nisanabi.mygame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
    int countbird = 0;
    private long smokeStartTime;
    private long missileStartTime;
    private long missile2StartTime;
    private long rainbowStartTime;
    private long rainbow2StartTime;
    private int lastscore;
    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<SmokePuff> smoke;
    private ArrayList<Missile> missiles;
    private ArrayList<GreenLight> missiles2;
    private ArrayList<GreenLight> missiles3;


    float scaleFactorX ;
     float scaleFactorY ;

    Rect clipBounds;
    Rect shoot;
    private ArrayList<Integer> birds;

    int x1;
    int x2;
    int y1;
    int y2;

    private Random rand = new Random();

    private int maxBorderHeight;
    private int minBorderHeight;
    private boolean topDown = true;
    private boolean botDown = true;
    private boolean newGameCreated;
    private boolean newHighest = false;
    private boolean startShooting = false;
    //increase to slow down difficulty progression, decrease to speed up difficulty progression
    private int progressDenom = 20;

    private Explosion explosion;
    private long startReset;
    private boolean reset;
    private boolean dissapear;
    private boolean started;
    private boolean greenOnly = false;
    private int best;
    private int greenCount = 0;
    HighScore highscore;

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
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.car), 111, 70, 3);
        smoke = new ArrayList<SmokePuff>();
        missiles = new ArrayList<Missile>();
        missiles2 = new ArrayList<GreenLight>();
        missiles3 = new ArrayList<GreenLight>();

        birds = new ArrayList<Integer>();
        birds.add(R.drawable.bird);
        birds.add(R.drawable.bird2);
        birds.add(R.drawable.bird3);
        birds.add(R.drawable.bird4);
        birds.add(R.drawable.bird5);

        smokeStartTime=  System.nanoTime();
        missileStartTime = System.nanoTime();
        missile2StartTime = System.nanoTime();
        rainbowStartTime = System.nanoTime();
        rainbow2StartTime = System.nanoTime();

        thread = new MainThread(getHolder(), this);
        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            x1 = (int)(event.getX()/ scaleFactorX) + clipBounds.left;
            y1= (int)(event.getY()/scaleFactorY) + clipBounds.top;

            if(!player.getPlaying()&& newGameCreated && reset)
            {

                player.setPlaying(true);
                player.setUp(true);

            }
            if(player.getPlaying()){

                if(started){
                    if(shoot.contains(x1,y1)) {
                        startShooting = true;
                    }
                }
                if (!started) started = true;


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

            //add missiles on timer
            long missileElapsed = (System.nanoTime()-missileStartTime)/1000000;
            long missile2Elapsed = (System.nanoTime()-missile2StartTime)/1000000;
            long rainbowElapsed = (System.nanoTime()-rainbowStartTime)/1000000;
            long rainbowElapsed2 = (System.nanoTime()-rainbow2StartTime)/1000000;

            if(startShooting){
                smoke.add(new SmokePuff(player.getX() + player.getWidth(), player.getY()+ (player.getHeight()/2)));
                startShooting = false;
            }
           /* if(greenOnly && rainbowElapsed > 800) {


                missiles2.add(new GreenLight(BitmapFactory.decodeResource(getResources(), R.drawable.rainbow),
                        WIDTH + 500, (int) (rand.nextDouble() * (HEIGHT - (100)) + 30), 60, 60, player.getScore(), 1));

                greenCount++;
                rainbowStartTime = System.nanoTime();
                }
                if(greenCount>=20){
                    greenCount = 0;
                    greenOnly = false;
                }*/
           // }else if(!greenOnly){

                if (missileElapsed > (800) && !greenOnly) {

                    if(countbird > birds.size()-1){
                        countbird = 0;
                    }

                    if(missiles.size()==0)
                    {
                        missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), birds.get(countbird)),
                                WIDTH + 500, HEIGHT/2, 44, 41, player.getScore(), 8));
                    }else {
                        int position = (int) (rand.nextDouble() * (HEIGHT - (100)) + 30);
                        missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), birds.get(countbird)),
                                WIDTH + 500, position , 44, 41, player.getScore(), 8));
                        countbird++;
                    }
                    //reset timer
                    missileStartTime = System.nanoTime();
                }
                /*if (missile2Elapsed > (3000) && !greenOnly) {


                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.bird),
                            WIDTH + 500, (int) (rand.nextDouble() * (HEIGHT - (100)) + 30), 59, 50, player.getScore(), 8));

                    //reset timer
                    missile2StartTime = System.nanoTime();
                }*/

                if (rainbowElapsed > 5000 && !greenOnly) {
                    missiles2.add(new GreenLight(BitmapFactory.decodeResource(getResources(), R.drawable.rainbow),
                            WIDTH + 500, (int) (rand.nextDouble() * (HEIGHT - (100)) + 30), 60, 60, player.getScore(), 1));

                    rainbowStartTime = System.nanoTime();
                }

                /*if (rainbowElapsed2 > 1000 && !greenOnly) {
                    missiles3.add(new GreenLight(BitmapFactory.decodeResource(getResources(), R.drawable.rainbow),
                            WIDTH + 500, (int) (rand.nextDouble() * (HEIGHT - (100)) + 30), 60, 60, player.getScore(), 7));

                    rainbow2StartTime = System.nanoTime();
                }*/
            //}

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

            //loop through every missile and check collision and remove
            for(int i = 0; i<missiles2.size();i++)
            {
                //update missile
                missiles2.get(i).update();

                if(collision(missiles2.get(i),player))
                {
                    missiles2.remove(i);
                    player.setScore(1);
                    break;
                }
                //remove missile if it is way off the screen
                if(missiles2.get(i).getX()<-100)
                {
                    missiles2.remove(i);
                    break;
                }
            }

            //loop through every missile and check collision and remove
            for(int i = 0; i<missiles3.size();i++)
            {
                //update missile
                missiles3.get(i).update();

                if(collision(missiles3.get(i),player))
                {
                    missiles3.remove(i);
                    greenOnly = true;
                    missiles.clear();
                    missiles2.clear();
                    break;
                }
                //remove missile if it is way off the screen
                if(missiles3.get(i).getX()<-100)
                {
                    missiles3.remove(i);
                    break;
                }
            }


            for(int i = 0; i<smoke.size();i++)
            {
                smoke.get(i).update();
                if(smoke.get(i).getX()<-10)
                {
                    smoke.remove(i);
                }


            }
        } else{

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


        if(canvas!=null) {

            scaleFactorX = getWidth()/(WIDTH*1.f);
            scaleFactorY = getHeight()/(HEIGHT*1.f);

            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            clipBounds = canvas.getClipBounds();
            bg.draw(canvas);

            if(!dissapear && started) {
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
            for(GreenLight m: missiles2)
            {
                m.draw(canvas);
            }

            for(GreenLight m: missiles3)
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
        missiles2.clear();
        missiles3.clear();
        smoke.clear();
        player.setPlaying(false);
        smoke.clear();
        greenOnly = false;
        greenCount = 0;
        minBorderHeight = 5;
        maxBorderHeight = 30;

        player.resetDY();
        started = false;
        lastscore = player.getScore();
        player.resetScore();
        player.setY(HEIGHT / 2);

        System.out.println("SCOOOOOOOOOOOOOOOOOORE " + player.getScore());
        if(lastscore > SplashScreen.getHighScore()){
            newHighest = true;
            SplashScreen.setHighScore(lastscore);
        }
        //create initial borders
        newGameCreated = true;
    }

    public void drawText(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);

        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        if(player.getPlaying()){
            canvas.drawText("" + (player.getScore()), (WIDTH / 2) - 30, 60, paint);

            shoot = new Rect(WIDTH-100, HEIGHT-100, WIDTH-50, HEIGHT-50);
            paint.setColor(Color.RED);
            canvas.drawRect(shoot, paint);
        }

        if(!player.getPlaying() && newGameCreated && reset) {

            Rect rect = new Rect();
            rect.set(60, 30, WIDTH - 60, HEIGHT);
            paint.setColor(Color.WHITE);


            canvas.drawRoundRect(100, 80, WIDTH - 100, 260, 20, 20, paint);
            paint.setColor(Color.MAGENTA);
            paint.setTextSize(40);

            if(newHighest) {
                paint.setTextSize(60);
                newHighest = false;
            }

            paint.setColor(Color.BLACK);
            RectF bounds = new RectF(rect);
            String score = "Score";
            bounds.right = paint.measureText(score, 0, score.length());
            bounds.left += (rect.width() - bounds.right)/2.0f;
            canvas.drawText(score, bounds.left, 120, paint);

            bounds = new RectF(rect);
            String lscore = "" + lastscore;
            bounds.right = paint.measureText(lscore, 0, lscore.length());
            bounds.left += (rect.width() - bounds.right)/2.0f;
            canvas.drawText(lscore, bounds.left, 160, paint);

            String best = "Best"  ;
            bounds = new RectF(rect);
            bounds.right = paint.measureText(best, 0, best.length());
            bounds.left += (rect.width() - bounds.right)/2.0f;
            canvas.drawText(best, bounds.left, 200, paint);

            String best1 = "" + SplashScreen.getHighScore();
            bounds = new RectF(rect);
            bounds.right = paint.measureText(best1, 0, best1.length());
            bounds.left += (rect.width() - bounds.right) / 2.0f;
            canvas.drawText(best1, bounds.left, 240, paint);
 //////////////////////////////////////////////////////////////////////////////////////////

            paint.setColor(Color.WHITE);
            canvas.drawRoundRect(70, 330, WIDTH - 70, 530, 20, 20, paint);
            paint.setTextSize(35);

            paint.setColor(Color.BLACK);
            bounds = new RectF(rect);
            String playit = "PRESS TO PLAY";
            bounds.right = paint.measureText(playit, 0, playit.length());
            bounds.left += (rect.width() - bounds.right)/2.0f;
            canvas.drawText(playit, bounds.left, 380, paint);

            paint.setTextSize(20);
            bounds = new RectF(rect);
            String h = "Press and hold to go up" ;
            bounds.right = paint.measureText(h, 0, h.length());
            bounds.left += (rect.width() - bounds.right)/2.0f;
            canvas.drawText(h, bounds.left, 420, paint);

            String d = "Release to go down" ;
            bounds = new RectF(rect);
            bounds.right = paint.measureText(d, 0, d.length());
            bounds.left += (rect.width() - bounds.right)/2.0f;
            canvas.drawText(d, bounds.left, 460, paint);

            String i = "Avoid the birds & collect the rainbows";
            bounds = new RectF(rect);
            bounds.right = paint.measureText(i, 0, i.length());
            bounds.left += (rect.width() - bounds.right) / 2.0f;
            canvas.drawText(i, bounds.left, 500, paint);

            Rect rr = new Rect(x1, y1, x1+10, y1+10);
            paint.setColor(Color.BLUE);
            canvas.drawRect(rr, paint);
        }
    }
}