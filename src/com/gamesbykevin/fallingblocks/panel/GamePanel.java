package com.gamesbykevin.fallingblocks.panel;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gamesbykevin.androidframework.resources.Disposable;

import com.gamesbykevin.fallingblocks.screen.MainScreen;
import com.gamesbykevin.fallingblocks.R;
import com.gamesbykevin.fallingblocks.FallingBlocks;
import com.gamesbykevin.fallingblocks.assets.Assets;
import com.gamesbykevin.fallingblocks.thread.MainThread;

import java.util.Random;

/**
 * Game Panel class
 * @author ABRAHAM
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, Disposable
{
    /**
     * Our random object used to make random decisions
     */
    public static Random RANDOM = new Random(System.nanoTime());
    
    //default dimensions of window for this game
    public static final int WIDTH = 960;
    public static final int HEIGHT = 1600;
    
    //the reference to our activity
    private final FallingBlocks activity;
    
    //the object containing our game screens
    private MainScreen screen;
    
    //our main game thread
    private MainThread thread;
    
    /**
     * Create a new game panel
     * @param activity Our main activity 
     */
    public GamePanel(final FallingBlocks activity)
    {
        //call to parent constructor
        super(activity);
        
        //store context
        this.activity = activity;
        
        //make game panel focusable = true so it can handle events
        super.setFocusable(true);
        
        //load game resources
        loadAssets();
    }
    
    @Override
    public void dispose()
    {
        //it could take several attempts to stop the thread
        boolean retry = true;
        
        //count number of attempts to complete thread
        int count = 0;
        
        while (retry && count <= MainThread.COMPLETE_THREAD_ATTEMPTS)
        {
            try
            {
                //increase count
                count++;
                
                if (thread != null)
                {
                    //set running false, to stop the infinite loop
                    thread.setRunning(false);

                    //wait for thread to finish
                    thread.join();
                }
                
                //if we made it here, we were successful
                retry = false;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        //make thread null
        this.thread = null;
        
        //assign null
        RANDOM = null;
        
        if (screen != null)
        {
            screen.dispose();
            screen = null;
        }
        
        //recycle asset objects
        Assets.recycle();
    }
    
    /**
     * Get the activity
     * @return The activity reference
     */
    public final FallingBlocks getActivity()
    {
        return this.activity;
    }
    
    /**
     * Load game resources, if a resource is already loaded nothing will happen
     */
    private void loadAssets()
    {
        //load images
        Assets.assignImage(Assets.ImageKey.Background,      BitmapFactory.decodeResource(getResources(), R.drawable.background));
        Assets.assignImage(Assets.ImageKey.Control_Left,    BitmapFactory.decodeResource(getResources(), R.drawable.left));
        Assets.assignImage(Assets.ImageKey.Control_Right,   BitmapFactory.decodeResource(getResources(), R.drawable.right));
        Assets.assignImage(Assets.ImageKey.Control_Up,      BitmapFactory.decodeResource(getResources(), R.drawable.up));
        Assets.assignImage(Assets.ImageKey.Control_Down,    BitmapFactory.decodeResource(getResources(), R.drawable.down));
        Assets.assignImage(Assets.ImageKey.Control_Mute,    BitmapFactory.decodeResource(getResources(), R.drawable.mute));
        Assets.assignImage(Assets.ImageKey.Control_UnMute,  BitmapFactory.decodeResource(getResources(), R.drawable.unmute));
        Assets.assignImage(Assets.ImageKey.Control_Pause,   BitmapFactory.decodeResource(getResources(), R.drawable.pause));
        Assets.assignImage(Assets.ImageKey.Blocks,          BitmapFactory.decodeResource(getResources(), R.drawable.fallingblocks));
        
        //load audio
        //Assets.assignAudio(Assets.AudioKey.Win, MediaPlayer.create(getActivity(), R.raw.sound_win));
    }
    
    /**
     * Now that the surface has been created we can create our game objects
     * @param holder 
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        try
        {
            //create new random object
            RANDOM = new Random(System.nanoTime());
            
            //load game resources
            loadAssets();
            
            //make sure the screen is created first before the thread starts
            if (this.screen == null)
                this.screen = new MainScreen(this);

            //if the thread does not exist, create it
            if (this.thread == null)
                this.thread = new MainThread(getHolder(), this);

            //if the thread hasn't been started yet
            if (!this.thread.isRunning())
            {
                //start the thread
                this.thread.setRunning(true);
                this.thread.start();
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent event)
    {
        try
        {
            if (this.screen != null)
            {
                //calculate the coordinate offset
                final float scaleFactorX = (float)WIDTH / getWidth();
                final float scaleFactorY = (float)HEIGHT / getHeight();

                //adjust the coordinates
                final float x = event.getRawX() * scaleFactorX;
                final float y = event.getRawY() * scaleFactorY;

                //update the events
                return this.screen.update(event, x, y);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return super.onTouchEvent(event);
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        //pause the game
        if (screen != null)
            screen.setState(MainScreen.State.Paused);
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        //does anything need to be done here?
    }
    
    /**
     * Update the game state
     */
    public void update()
    {
        try
        {
            if (screen != null)
                screen.update();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onDraw(Canvas canvas)
    {
        if (canvas != null)
        {
            //store the canvas state
            final int savedState = canvas.save();
            
            try
            {
                //make sure the screen object exists
                if (screen != null)
                    screen.render(canvas);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
            //restore previous canvas state
            canvas.restoreToCount(savedState);
        }
    }
}