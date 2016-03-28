package com.gamesbykevin.fallingblocks.game.controller;

import com.gamesbykevin.androidframework.awt.Button;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Images;

import com.gamesbykevin.fallingblocks.assets.Assets;
import com.gamesbykevin.fallingblocks.game.Game;
import com.gamesbykevin.fallingblocks.player.Player;

import static com.gamesbykevin.fallingblocks.screen.OptionsScreen.SOUND_ENABLED;
import static com.gamesbykevin.fallingblocks.screen.OptionsScreen.SOUND_DISABLED;

import com.gamesbykevin.fallingblocks.screen.OptionsScreen.Key;
import static com.gamesbykevin.fallingblocks.screen.OptionsScreen.MODE_FREE;
import static com.gamesbykevin.fallingblocks.screen.OptionsScreen.MODE_VIEW_CPU;
import static com.gamesbykevin.fallingblocks.screen.OptionsScreen.MODE_VS_CPU;
import static com.gamesbykevin.fallingblocks.screen.OptionsScreen.MODE_CHALLENGE;

import com.gamesbykevin.fallingblocks.screen.ScreenManager;

import java.util.HashMap;

/**
 * This class will be our game controller
 * @author GOD
 */
public class Controller implements IController
{
    //all of the buttons for the player to control
    private HashMap<Assets.ImageGameKey, Button> buttons;
    
    //our game object reference
    private final Game game;
    
    /**
     * The starting location of our sound/pause/exit icons
     */
    private static final int MULTI_ICON_START_X = 25;
    private static final int MULTI_ICON_START_Y = 37;
    
    /**
     * The starting location of our sound/pause/exit icons
     */
    private static final int SINGLE_ICON_START_X = 352;
    private static final int SINGLE_ICON_START_Y = 200;
    
    private static final int CONTROLLER_START_X = 12;
    private static final int CONTROLLER_START_Y = 675;
    
    /**
     * Default size of the icon
     */
    private static final int ICON_DIMENSION = 75;

    private static final int BUTTON_HORIZONTAL_WIDTH = 87;
    private static final int BUTTON_HORIZONTAL_HEIGHT = 70;
    private static final int BUTTON_CIRCLE_WIDTH = 80;
    private static final int BUTTON_CIRCLE_HEIGHT = 80;
    
    /**
     * Default Constructor
     * @param game Object game object reference
     */
    public Controller(final Game game) throws Exception
    {
        //assign object reference
        this.game = game;
        
        //create new list of buttons
        this.buttons = new HashMap<Assets.ImageGameKey, Button>();
        
        //add button controls
        this.buttons.put(Assets.ImageGameKey.Left, new Button(Images.getImage(Assets.ImageGameKey.Left)));
        this.buttons.put(Assets.ImageGameKey.Right, new Button(Images.getImage(Assets.ImageGameKey.Right)));
        this.buttons.put(Assets.ImageGameKey.Rotate, new Button(Images.getImage(Assets.ImageGameKey.Rotate)));
        this.buttons.put(Assets.ImageGameKey.Fall, new Button(Images.getImage(Assets.ImageGameKey.Fall)));
        this.buttons.put(Assets.ImageGameKey.Mute, new Button(Images.getImage(Assets.ImageGameKey.Mute)));
        this.buttons.put(Assets.ImageGameKey.Unmute, new Button(Images.getImage(Assets.ImageGameKey.Unmute)));
        this.buttons.put(Assets.ImageGameKey.Pause, new Button(Images.getImage(Assets.ImageGameKey.Pause)));
        this.buttons.put(Assets.ImageGameKey.Exit, new Button(Images.getImage(Assets.ImageGameKey.Exit)));
        
        //reset buttons
        reset();
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
    @Override
    public void update(final int action, final float x, final float y) throws Exception
    {
        //check if the touch screen was released
        if (action == MotionEvent.ACTION_UP)
        {
            //check if the player hit the pause
            if (buttons.get(Assets.ImageGameKey.Pause).contains(x, y))
            {
                //change the state to paused
                getGame().getScreen().setState(ScreenManager.State.Paused);

                //no need to continue
                return;
            }
            else if (buttons.get(Assets.ImageGameKey.Exit).contains(x, y))
            {
                //change to the exit confirm screen
                getGame().getScreen().setState(ScreenManager.State.Exit);

                //no need to continue
                return;
            }
            else if (buttons.get(Assets.ImageGameKey.Mute).contains(x, y) || buttons.get(Assets.ImageGameKey.Unmute).contains(x, y))
            {
                //flip the audio setting
                Audio.setAudioEnabled(!Audio.isAudioEnabled());

                //reset the controller
                reset();
                
                //make sure the correct button is showing
                if (Audio.isAudioEnabled())
                {
                    //play sound indicating sound is enabled
                    Audio.play(Assets.AudioMenuKey.Selection);
                    
                    //resume music
                    getGame().resumeMusic();
                }
                else
                {
                    //if audio is not enabled, stop all sound
                    Audio.stop();
                }

                //make sure the options screen is updated
                getGame().getScreen().getScreenOptions().setIndex(Key.Sound, Audio.isAudioEnabled() ? SOUND_ENABLED : SOUND_DISABLED);
                
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
            if (action == MotionEvent.ACTION_DOWN)
            {
                //if the player is pressing down, make the time expire to drop the piece
                if (buttons.get(Assets.ImageGameKey.Fall).isVisible() && buttons.get(Assets.ImageGameKey.Fall).contains(x, y))
                    player.setAction(Player.Action.MOVE_DOWN);
            }
            else if (action == MotionEvent.ACTION_MOVE)
            {
            	//if we move off the down button remove it
                if (!buttons.get(Assets.ImageGameKey.Fall).contains(x, y))
                {
                    //remove action
                    player.setAction(null);
                }
            }
            else if (action == MotionEvent.ACTION_UP)
            {
                //if the up control was released, we will rotate the piece
                if (buttons.get(Assets.ImageGameKey.Rotate).isVisible() && buttons.get(Assets.ImageGameKey.Rotate).contains(x, y))
                {
                    //set action
                    player.setAction(Player.Action.MOVE_ROTATE);
                    
                    //play sound effect
                    Audio.play(Assets.AudioGameKey.PieceRotate);
                }
                else if (buttons.get(Assets.ImageGameKey.Fall).isVisible() && buttons.get(Assets.ImageGameKey.Fall).contains(x, y))
                {
                    //remove action
                    player.setAction(null);
                }
                else if (buttons.get(Assets.ImageGameKey.Left).isVisible() && buttons.get(Assets.ImageGameKey.Left).contains(x, y))
                {
                    //set action
                    player.setAction(Player.Action.MOVE_LEFT);
                }
                else if (buttons.get(Assets.ImageGameKey.Right).isVisible() && buttons.get(Assets.ImageGameKey.Right).contains(x, y))
                {
                    //set action
                    player.setAction(Player.Action.MOVE_RIGHT);
                }
            }
        }
    }
    
    @Override
    public void reset() throws Exception
    {
    	if (buttons != null)
    	{
	        //determine which button is displayed
	        buttons.get(Assets.ImageGameKey.Unmute).setVisible(Audio.isAudioEnabled());
	        buttons.get(Assets.ImageGameKey.Mute).setVisible(!Audio.isAudioEnabled());
    	}
    	
    	int x = 0, y = 0;
    	
    	//get the mode index
    	final int modeIndex = game.getScreen().getScreenOptions().getIndex(Key.Mode); 

    	//setup the buttons depending on the game mode
    	switch (modeIndex)
    	{
	    	//single player
	    	case MODE_FREE:
	    	case MODE_VIEW_CPU:
	    	case MODE_CHALLENGE:
	    		
	        	//set start
	        	x = SINGLE_ICON_START_X;
	        	y = SINGLE_ICON_START_Y;
	        	
	            this.buttons.get(Assets.ImageGameKey.Mute).setX(x);
	            this.buttons.get(Assets.ImageGameKey.Mute).setY(y);
	            
	            this.buttons.get(Assets.ImageGameKey.Unmute).setX(x);
	            this.buttons.get(Assets.ImageGameKey.Unmute).setY(y);
	            
	            y += (ICON_DIMENSION * 1.5); 
	            
	            this.buttons.get(Assets.ImageGameKey.Pause).setX(x);
	            this.buttons.get(Assets.ImageGameKey.Pause).setY(y);
	            
	            y += (ICON_DIMENSION * 1.5); 
	            
	            this.buttons.get(Assets.ImageGameKey.Exit).setX(x);
	            this.buttons.get(Assets.ImageGameKey.Exit).setY(y);
	    		break;
	    		
	    	//multi-player
	    	case MODE_VS_CPU:
	    		
	        	//set start
	        	x = MULTI_ICON_START_X;
	        	y = MULTI_ICON_START_Y;
	        	
	            this.buttons.get(Assets.ImageGameKey.Mute).setX(x);
	            this.buttons.get(Assets.ImageGameKey.Mute).setY(y);
	            
	            this.buttons.get(Assets.ImageGameKey.Unmute).setX(x);
	            this.buttons.get(Assets.ImageGameKey.Unmute).setY(y);
	            
	            x += (ICON_DIMENSION * 1.25); 
	            
	            this.buttons.get(Assets.ImageGameKey.Pause).setX(x);
	            this.buttons.get(Assets.ImageGameKey.Pause).setY(y);
	            
	            x += (ICON_DIMENSION * 1.25); 
	            
	            this.buttons.get(Assets.ImageGameKey.Exit).setX(x);
	            this.buttons.get(Assets.ImageGameKey.Exit).setY(y);
	    		break;
	    		
    		default:
				throw new Exception("Game mode index not setup here: " + modeIndex);
    	}
        
        //assign the button locations
        this.buttons.get(Assets.ImageGameKey.Left).setX(CONTROLLER_START_X);
        this.buttons.get(Assets.ImageGameKey.Left).setY(CONTROLLER_START_Y);
        this.buttons.get(Assets.ImageGameKey.Right).setX(buttons.get(Assets.ImageGameKey.Left).getX() + (BUTTON_HORIZONTAL_WIDTH * 1.5));
        this.buttons.get(Assets.ImageGameKey.Right).setY(CONTROLLER_START_Y);
        this.buttons.get(Assets.ImageGameKey.Fall).setX(SINGLE_ICON_START_X - (BUTTON_CIRCLE_WIDTH * 1.15));
        this.buttons.get(Assets.ImageGameKey.Fall).setY(CONTROLLER_START_Y);
        this.buttons.get(Assets.ImageGameKey.Rotate).setX(buttons.get(Assets.ImageGameKey.Fall).getX() + (BUTTON_CIRCLE_WIDTH * 1.5));
        this.buttons.get(Assets.ImageGameKey.Rotate).setY(CONTROLLER_START_Y);
    	
        //set dimension
    	this.buttons.get(Assets.ImageGameKey.Left).setWidth(BUTTON_HORIZONTAL_WIDTH);
    	this.buttons.get(Assets.ImageGameKey.Left).setHeight(BUTTON_HORIZONTAL_HEIGHT);
    	this.buttons.get(Assets.ImageGameKey.Right).setWidth(BUTTON_HORIZONTAL_WIDTH);
    	this.buttons.get(Assets.ImageGameKey.Right).setHeight(BUTTON_HORIZONTAL_HEIGHT);
    	this.buttons.get(Assets.ImageGameKey.Rotate).setWidth(BUTTON_CIRCLE_WIDTH);
    	this.buttons.get(Assets.ImageGameKey.Rotate).setHeight(BUTTON_CIRCLE_HEIGHT);
    	this.buttons.get(Assets.ImageGameKey.Fall).setWidth(BUTTON_CIRCLE_WIDTH);
    	this.buttons.get(Assets.ImageGameKey.Fall).setHeight(BUTTON_CIRCLE_HEIGHT);
    	
        this.buttons.get(Assets.ImageGameKey.Mute).setWidth(ICON_DIMENSION);
        this.buttons.get(Assets.ImageGameKey.Mute).setHeight(ICON_DIMENSION);
        this.buttons.get(Assets.ImageGameKey.Unmute).setWidth(ICON_DIMENSION);
        this.buttons.get(Assets.ImageGameKey.Unmute).setHeight(ICON_DIMENSION);
        this.buttons.get(Assets.ImageGameKey.Pause).setWidth(ICON_DIMENSION);
        this.buttons.get(Assets.ImageGameKey.Pause).setHeight(ICON_DIMENSION);
        this.buttons.get(Assets.ImageGameKey.Exit).setWidth(ICON_DIMENSION);
        this.buttons.get(Assets.ImageGameKey.Exit).setHeight(ICON_DIMENSION);
        
        //update bounds for all buttons
        for (Button button : buttons.values())
        {
        	button.updateBounds();
        }
        
    	//is the cpu playing by itself
    	boolean cpuOnly = (modeIndex == MODE_VIEW_CPU);
        
        //if only the computer is playing we hide the controller buttons
        this.buttons.get(Assets.ImageGameKey.Left).setVisible(!cpuOnly);
        this.buttons.get(Assets.ImageGameKey.Right).setVisible(!cpuOnly);
        this.buttons.get(Assets.ImageGameKey.Rotate).setVisible(!cpuOnly);
        this.buttons.get(Assets.ImageGameKey.Fall).setVisible(!cpuOnly);
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
        	for (Button button : buttons.values())
        	{
        		if (button != null)
        			button.render(canvas);
        	}
        }
    }
}