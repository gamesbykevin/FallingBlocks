package com.gamesbykevin.fallingblocks.game.controller;

import com.gamesbykevin.androidframework.awt.Button;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Images;

import com.gamesbykevin.fallingblocks.assets.Assets;
import com.gamesbykevin.fallingblocks.game.Game;
import com.gamesbykevin.fallingblocks.panel.GamePanel;
import com.gamesbykevin.fallingblocks.player.Player;
import com.gamesbykevin.fallingblocks.screen.MainScreen;

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
        this.buttons.put(Assets.ImageKey.Control_Left, new Button(Images.getImage(Assets.ImageKey.Control_Left)));
        this.buttons.put(Assets.ImageKey.Control_Right, new Button(Images.getImage(Assets.ImageKey.Control_Right)));
        this.buttons.put(Assets.ImageKey.Control_Up, new Button(Images.getImage(Assets.ImageKey.Control_Up)));
        this.buttons.put(Assets.ImageKey.Control_Down, new Button(Images.getImage(Assets.ImageKey.Control_Down)));
        this.buttons.put(Assets.ImageKey.Control_Mute, new Button(Images.getImage(Assets.ImageKey.Control_Mute)));
        this.buttons.put(Assets.ImageKey.Control_UnMute, new Button(Images.getImage(Assets.ImageKey.Control_UnMute)));
        this.buttons.put(Assets.ImageKey.Control_Pause, new Button(Images.getImage(Assets.ImageKey.Control_Pause)));
        this.buttons.put(Assets.ImageKey.Control_Exit, new Button(Images.getImage(Assets.ImageKey.Control_Exit)));
        
        //the start location of the controller buttons
        final int x = 100;
        final int y = 1313;
        
        //assign the button locations
        this.buttons.get(Assets.ImageKey.Control_Left).setX(x);
        this.buttons.get(Assets.ImageKey.Control_Left).setY(y + 95);
        this.buttons.get(Assets.ImageKey.Control_Right).setX(x + 165);
        this.buttons.get(Assets.ImageKey.Control_Right).setY(y + 95);
        this.buttons.get(Assets.ImageKey.Control_Up).setX(x + 95);
        this.buttons.get(Assets.ImageKey.Control_Up).setY(y);        
        this.buttons.get(Assets.ImageKey.Control_Down).setX(x + 95);
        this.buttons.get(Assets.ImageKey.Control_Down).setY(y + 155);
        
        this.buttons.get(Assets.ImageKey.Control_Mute).setX(x + 375);
        this.buttons.get(Assets.ImageKey.Control_Mute).setY(y + 97);
        this.buttons.get(Assets.ImageKey.Control_UnMute).setX(x + 375);
        this.buttons.get(Assets.ImageKey.Control_UnMute).setY(y + 97);
        this.buttons.get(Assets.ImageKey.Control_Pause).setX(x + 500);
        this.buttons.get(Assets.ImageKey.Control_Pause).setY(y + 97);
        this.buttons.get(Assets.ImageKey.Control_Exit).setX(x + 625);
        this.buttons.get(Assets.ImageKey.Control_Exit).setY(y + 97);
        
        //assign collision boundary
        setBounds(buttons.get(Assets.ImageKey.Control_Down), 0, 50, 100, 75);
        setBounds(buttons.get(Assets.ImageKey.Control_Up), 0, 0, 100, 75);
        setBounds(buttons.get(Assets.ImageKey.Control_Left), 0, 0, 75, 100);
        setBounds(buttons.get(Assets.ImageKey.Control_Right), 50, 0, 75, 100);
        
        this.buttons.get(Assets.ImageKey.Control_Mute).updateBounds();
        this.buttons.get(Assets.ImageKey.Control_UnMute).updateBounds();
        this.buttons.get(Assets.ImageKey.Control_Pause).updateBounds();
        this.buttons.get(Assets.ImageKey.Control_Exit).updateBounds();
        
        if (Audio.isAudioEnabled())
        {
            this.buttons.get(Assets.ImageKey.Control_Mute).setVisible(false);
            this.buttons.get(Assets.ImageKey.Control_UnMute).setVisible(true);
        }
        
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
        //check if the touch screen was released
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            //check if the player hit the pause
            if (buttons.get(Assets.ImageKey.Control_Pause).contains(x, y))
            {
                //change the state to paused
                getGame().getMainScreen().setState(MainScreen.State.Paused);

                //no need to continue
                return;
            }
            else if (buttons.get(Assets.ImageKey.Control_Exit).contains(x, y))
            {
                //change to the exit confirm screen
                getGame().getMainScreen().setState(MainScreen.State.Exit);

                //no need to continue
                return;
            }
            else if (buttons.get(Assets.ImageKey.Control_Mute).contains(x, y) || buttons.get(Assets.ImageKey.Control_UnMute).contains(x, y))
            {
                //flip the audio setting
                Audio.setAudioEnabled(!Audio.isAudioEnabled());

                //make sure the correct button is showing
                if (Audio.isAudioEnabled())
                {
                    //play sound indicating sound is enabled
                    Audio.play(Assets.AudioKey.SettingChange);
                    
                    //resume music
                    getGame().resumeMusic();
                }
                else
                {
                    //if audio is not enabled, stop all sound
                    Audio.stop();
                }

                //no need to continue
                return;
            }
        }
            
        for (Player player : getGame().getPlayers())
        {
            //if not human, this player can't be controlled
            if (!player.isHuman())
                continue;
            
            //check if the touch screen is pressed down
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                //if the player is pressing down, make the time expire to drop the piece
                if (buttons.get(Assets.ImageKey.Control_Down).contains(x, y))
                    player.setAction(Player.Action.MOVE_DOWN);
            }

            //check if the touch screen was released
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                //if the up control was released, we will rotate the piece
                if (buttons.get(Assets.ImageKey.Control_Up).contains(x, y))
                {
                    //set action
                    player.setAction(Player.Action.MOVE_ROTATE);
                    
                    //play sound effect
                    Audio.play(Assets.AudioKey.PieceRotate);
                }
                else if (buttons.get(Assets.ImageKey.Control_Down).contains(x, y))
                {
                    //remove action
                    player.setAction(null);
                }
                else if (buttons.get(Assets.ImageKey.Control_Left).contains(x, y))
                {
                    //set action
                    player.setAction(Player.Action.MOVE_LEFT);
                }
                else if (buttons.get(Assets.ImageKey.Control_Right).contains(x, y))
                {
                    //set action
                    player.setAction(Player.Action.MOVE_RIGHT);
                }
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
            buttons.get(Audio.isAudioEnabled() ? Assets.ImageKey.Control_UnMute : Assets.ImageKey.Control_Mute).render(canvas);
            buttons.get(Assets.ImageKey.Control_Pause).render(canvas);
            buttons.get(Assets.ImageKey.Control_Exit).render(canvas);
        }
    }
}