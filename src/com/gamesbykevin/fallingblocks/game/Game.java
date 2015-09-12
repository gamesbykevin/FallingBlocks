package com.gamesbykevin.fallingblocks.game;

import android.view.MotionEvent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Disposable;

import com.gamesbykevin.fallingblocks.assets.Assets;
import com.gamesbykevin.fallingblocks.game.controller.Controller;

import com.gamesbykevin.fallingblocks.player.*;
import com.gamesbykevin.fallingblocks.screen.MainScreen;

/**
 * The main game logic will happen here
 * @author ABRAHAM
 */
public class Game implements Disposable
{
    //object to draw text
    private Paint paint;
    
    //our main screen object reference
    private final MainScreen screen;
    
    //the human player
    private Player player;
    
    //the background image
    private Entity background;
    
    //the controller for our game
    private Controller controller;
    
    public Game(final MainScreen screen)
    {
        //our main screen object reference
        this.screen = screen;
        
        //create the text paint object
        this.paint = new Paint();
        this.paint.setColor(Color.WHITE);
        this.paint.setTextSize(48f);
        this.paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        
        //create a new player
        this.player = new Human();
        
        //create a new background
        this.background = new Entity();
        
        //assign position, size
        this.background.setX(0);
        this.background.setY(0);
        this.background.setWidth(Assets.getImage(Assets.ImageKey.Background).getWidth());
        this.background.setHeight(Assets.getImage(Assets.ImageKey.Background).getHeight());
        
        //add animation to spritesheet
        this.background.getSpritesheet().add(Assets.ImageKey.Background, new Animation(Assets.getImage(Assets.ImageKey.Background)));
        
        //create new controller
        this.controller = new Controller(this);
    }
    
    public Player getPlayer()
    {
        return this.player;
    }
    
    public Controller getController()
    {
        return this.controller;
    }
    
    /**
     * Update the game based on the motion event
     * @param event Motion Event
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     */
    public void updateMotionEvent(final MotionEvent event, final float x, final float y)
    {
        getController().updateMotionEvent(event, x, y);
    }
    
    /**
     * Update game
     * @throws Exception 
     */
    public void update() throws Exception
    {
        if (getPlayer() != null)
            getPlayer().update();
    }
    
    @Override
    public void dispose()
    {
        //recycle these objects
        if (player != null)
        {
            player.dispose();
            player = null;
        }
        
        if (background != null)
        {
            background.dispose();
            background = null;
        }
        
        if (controller != null)
        {
            controller.dispose();
            controller = null;
        }
    }
    
    /**
     * Render game elements
     * @param canvas Where to write the pixel data
     * @throws Exception 
     */
    public void render(final Canvas canvas) throws Exception
    {
        //draw the background first
        if (background != null)
            background.render(canvas);
        
        //draw the player, etc....
        if (getPlayer() != null)
            getPlayer().render(canvas);
        
        //draw the game controller
        if (getController() != null)
            getController().render(canvas);
    }
}