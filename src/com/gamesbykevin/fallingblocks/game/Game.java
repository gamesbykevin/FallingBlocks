package com.gamesbykevin.fallingblocks.game;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.level.Select;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.fallingblocks.assets.Assets;
import com.gamesbykevin.fallingblocks.game.controller.Controller;
import com.gamesbykevin.fallingblocks.player.*;
import com.gamesbykevin.fallingblocks.screen.OptionsScreen;
import com.gamesbykevin.fallingblocks.storage.score.*;
import com.gamesbykevin.fallingblocks.thread.MainThread;
import com.gamesbykevin.fallingblocks.screen.ScreenManager;
import com.gamesbykevin.fallingblocks.screen.OptionsScreen.Key;

import java.util.ArrayList;
import java.util.List;

/**
 * The main game logic will happen here
 * @author ABRAHAM
 */
public class Game implements IGame
{
    //our main screen object reference
    private final ScreenManager screen;
    
    //the players in the game
    private List<Player> players;
    
    //the controller for our game
    private Controller controller;
    
    //our level select object
    private Select select;
    
    //the score card object
    private ScoreCard scorecard;
    
    //the duration we want to vibrate the phone for
    private static final long VIBRATION_DURATION = 200L;
    
    public Game(final ScreenManager screen) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
        
        //create new controller
        this.controller = new Controller(this);
        
        //create a our list of players
        this.players = new ArrayList<Player>();
        
        //create our level select object
        this.select = new Select();
        
        //create the level select screen
        this.select = new Select();
        
        //create score card object reference
        this.scorecard = new ScoreCard(this, screen.getPanel().getActivity(), MainThread.DEBUG);
        
        //setup the level select object
        GameHelper.setupLevelSelect(this);
    }
    
    /**
     * The the score card
     * @return Our score card object reference for tracking best score etc...
     */
    public ScoreCard getScorecard()
    {
    	return this.scorecard;
    }
    
    /**
     * Get the level select
     * @return The level select object
     */
    public Select getLevelSelect()
    {
    	return this.select;
    }
    
    /**
     * Get the main screen object reference
     * @return The main screen object reference
     */
    public ScreenManager getScreen()
    {
        return this.screen;
    }
    
    /**
     * Get the players
     * @return The list of players in play
     */
    public final List<Player> getPlayers()
    {
        return this.players;
    }
    
    /**
     * Get the human player
     * @return The human player, if not found null is returned
     */
    public Player getHuman()
    {
    	for (Player player : getPlayers())
    	{
    		if (player.isHuman())
    			return player;
    	}
    	
    	//no human players were found
    	return null;
    }
    
    @Override
    public void reset() throws Exception
    {
    	GameHelper.reset(this);
    }
    
    /**
     * Resume playing music
     */
    public void resumeMusic()
    {
    	switch (getScreen().getScreenOptions().getIndex(Key.Mode))
    	{
	    	case OptionsScreen.MODE_FREE:
	    	case OptionsScreen.MODE_VIEW_CPU:
	    	case OptionsScreen.MODE_ATTACK:
	    		Audio.play(Assets.AudioGameKey.MusicSingle, true);
	    		break;
	    		
	    	case OptionsScreen.MODE_VS_CPU:
	    		Audio.play(Assets.AudioGameKey.MusicVs, true);
	    		break;
	    		
	    	case OptionsScreen.MODE_CHALLENGE:
	    		Audio.play(Assets.AudioGameKey.MusicChallenge, true);
	    		break;
    	}
    }
    
    /**
     * Get the controller that the human player will control
     * @return The controller object reference
     */
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
    @Override
    public void update(final int action, final float x, final float y) throws Exception
    {
    	//if we don't have a selection
    	if (!getLevelSelect().hasSelection())
    	{
    		//if action up, check the location
    		if (action == MotionEvent.ACTION_UP)
    			getLevelSelect().setCheck((int)x, (int)y);
    		
    		//don't continue
    		return;
    	}
    	else
    	{
    		//we can now update the controller
    		getController().update(action, x, y);
    	}
    }
    
    /**
     * Update game
     * @throws Exception 
     */
    public void update() throws Exception
    {
    	GameHelper.update(this);
    }
    
    /**
     * Vibrate the phone if the setting is enabled
     */
    public void vibrate()
    {
		//make sure vibrate option is enabled
		if (getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Vibrate) == 0)
		{
    		//get our vibrate object
    		Vibrator v = (Vibrator) getScreen().getPanel().getActivity().getSystemService(Context.VIBRATOR_SERVICE);
    		 
			//vibrate for a specified amount of milliseconds
			v.vibrate(VIBRATION_DURATION);
		}
    }
    
    @Override
    public void dispose()
    {
        if (players != null)
        {
            for (Player player : getPlayers())
            {
                if (player != null)
                {
                    player.dispose();
                    player = null;
                }
            }
            
            players.clear();
            players = null;
        }
        
        if (controller != null)
        {
            controller.dispose();
            controller = null;
        }
        
        if (select != null)
        {
        	select.dispose();
        	select = null;
        }
        
        if (scorecard != null)
        {
        	scorecard.dispose();
        	scorecard = null;
        }
    }
    
    /**
     * Render game elements
     * @param canvas Where to write the pixel data
     * @throws Exception 
     */
    public void render(final Canvas canvas) throws Exception
    {
    	if (!getLevelSelect().hasSelection())
    	{
    		getLevelSelect().render(canvas, getScreen().getPaint());
    	}
    	else
    	{
	        //draw the player, etc....
	        if (getPlayers() != null)
	        {
	            for (Player player : getPlayers())
	            {
	                if (player != null)
	                    player.render(canvas);
	            }
	        }
	        
	        //draw the game controller
	        if (getController() != null)
	            getController().render(canvas);
    	}
    }
}