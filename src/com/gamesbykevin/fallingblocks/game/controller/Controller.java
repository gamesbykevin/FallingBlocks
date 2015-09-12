package com.gamesbykevin.fallingblocks.game.controller;

import com.gamesbykevin.androidframework.awt.Button;

import com.gamesbykevin.fallingblocks.assets.Assets;
import com.gamesbykevin.fallingblocks.game.Game;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.gamesbykevin.fallingblocks.player.Player;

import java.util.HashMap;

/**
 * This class will be our game controller
 * @author GOD
 */
public class Controller implements IController
{
    //all of the buttons for the player to control
    private HashMap<Assets.ImageKey, Button> buttons;
    
    //our game object reference
    private final Game game;
    
    /**
     * Default Constructor
     * @param game Object game object reference
     */
    public Controller(final Game game)
    {
        //assign object reference
        this.game = game;
        
        //create new list of buttons
        this.buttons = new HashMap<Assets.ImageKey, Button>();
        
        //add button controls
        this.buttons.put(Assets.ImageKey.Control_Left, new Button(Assets.getImage(Assets.ImageKey.Control_Left)));
        this.buttons.put(Assets.ImageKey.Control_Right, new Button(Assets.getImage(Assets.ImageKey.Control_Right)));
        this.buttons.put(Assets.ImageKey.Control_Up, new Button(Assets.getImage(Assets.ImageKey.Control_Up)));
        this.buttons.put(Assets.ImageKey.Control_Down, new Button(Assets.getImage(Assets.ImageKey.Control_Down)));
        this.buttons.put(Assets.ImageKey.Control_Mute, new Button(Assets.getImage(Assets.ImageKey.Control_Mute)));
        this.buttons.put(Assets.ImageKey.Control_UnMute, new Button(Assets.getImage(Assets.ImageKey.Control_UnMute)));
        this.buttons.put(Assets.ImageKey.Control_Pause, new Button(Assets.getImage(Assets.ImageKey.Control_Pause)));
        
        final int x = 100;
        final int y = 1200;
        
        //assign the button locations
        this.buttons.get(Assets.ImageKey.Control_Left).setX(x);
        this.buttons.get(Assets.ImageKey.Control_Left).setY(y + 95);
        this.buttons.get(Assets.ImageKey.Control_Right).setX(x + 165);
        this.buttons.get(Assets.ImageKey.Control_Right).setY(y + 95);
        this.buttons.get(Assets.ImageKey.Control_Up).setX(x + 95);
        this.buttons.get(Assets.ImageKey.Control_Up).setY(y);        
        this.buttons.get(Assets.ImageKey.Control_Down).setX(x + 95);
        this.buttons.get(Assets.ImageKey.Control_Down).setY(y + 155);
        
        this.buttons.get(Assets.ImageKey.Control_Mute).setX(x + 400);
        this.buttons.get(Assets.ImageKey.Control_Mute).setY(y + 20);
        this.buttons.get(Assets.ImageKey.Control_UnMute).setX(x + 400);
        this.buttons.get(Assets.ImageKey.Control_UnMute).setY(y + 20);
        this.buttons.get(Assets.ImageKey.Control_Pause).setX(x + 400);
        this.buttons.get(Assets.ImageKey.Control_Pause).setY(y + 170);
        
        //assign collision boundary
        setBounds(buttons.get(Assets.ImageKey.Control_Down), 0, 50, 100, 75);
        setBounds(buttons.get(Assets.ImageKey.Control_Up), 0, 0, 100, 75);
        setBounds(buttons.get(Assets.ImageKey.Control_Left), 0, 0, 75, 100);
        setBounds(buttons.get(Assets.ImageKey.Control_Right), 50, 0, 75, 100);
        this.buttons.get(Assets.ImageKey.Control_Mute).updateBounds();
        this.buttons.get(Assets.ImageKey.Control_UnMute).updateBounds();
        this.buttons.get(Assets.ImageKey.Control_Pause).updateBounds();
    }
    
    /**
     * Set the bounds of the specified button
     * @param button
     * @param offsetX
     * @param offsetY
     * @param width
     * @param height 
     */
    private void setBounds(final Button button, final int offsetX, final int offsetY, final int width, final int height)
    {
        button.setBounds((int)button.getX() + offsetX, (int)button.getY() + offsetY, width, height);
    }
    
    /**
     * Get our game object reference
     * @return Our game object reference
     */
    private Game getGame()
    {
        return this.game;
    }
    
    /**
     * Update the controller based on the motion event
     * @param event Motion Event
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     */
    public void updateMotionEvent(final MotionEvent event, final float x, final float y)
    {
        //check if the touch screen is pressed down
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (buttons.get(Assets.ImageKey.Control_Left).contains(x, y))
            {
                
            }
            
            if (buttons.get(Assets.ImageKey.Control_Right).contains(x, y))
            {
                
            }
            
            //if the player is pressing down, make the time expire to drop the piece
            if (buttons.get(Assets.ImageKey.Control_Down).contains(x, y))
            {
                getGame().getPlayer().setAction(Player.Action.MOVE_DOWN);
            }
        }
        
        //check if the touch screen was released
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            //if the up control was released, we will rotate the piece
            if (buttons.get(Assets.ImageKey.Control_Up).contains(x, y))
            {
                getGame().getPlayer().setAction(Player.Action.MOVE_ROTATE);
            }
            else if (buttons.get(Assets.ImageKey.Control_Down).contains(x, y))
            {
                getGame().getPlayer().setAction(null);
            }
            else if (buttons.get(Assets.ImageKey.Control_Left).contains(x, y))
            {
                getGame().getPlayer().setAction(Player.Action.MOVE_LEFT);
            }
            else if (buttons.get(Assets.ImageKey.Control_Right).contains(x, y))
            {
                getGame().getPlayer().setAction(Player.Action.MOVE_RIGHT);
            }
        }
    }
    
    /**
     * Recycle objects
     */
    @Override
    public void dispose()
    {
        if (buttons != null)
        {
            for (Button button : buttons.values())
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            buttons.clear();
            buttons = null;
        }
    }
    
    /**
     * Render the controller
     * @param canvas Write pixel data to this canvas
     * @throws Exception 
     */
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw the buttons
        if (buttons != null)
        {
            buttons.get(Assets.ImageKey.Control_Left).render(canvas);
            buttons.get(Assets.ImageKey.Control_Right).render(canvas);
            buttons.get(Assets.ImageKey.Control_Down).render(canvas);
            buttons.get(Assets.ImageKey.Control_Up).render(canvas);
            buttons.get(Assets.ImageKey.Control_UnMute).render(canvas);
            buttons.get(Assets.ImageKey.Control_Pause).render(canvas);
        }
    }
}