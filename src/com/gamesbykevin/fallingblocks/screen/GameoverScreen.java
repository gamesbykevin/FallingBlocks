package com.gamesbykevin.fallingblocks.screen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.HashMap;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Font;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.fallingblocks.MainActivity;
import com.gamesbykevin.fallingblocks.assets.Assets;
import com.gamesbykevin.fallingblocks.panel.GamePanel;
import com.gamesbykevin.fallingblocks.player.stats.Stats;

/**
 * The game over screen
 * @author GOD
 */
public class GameoverScreen implements Screen, Disposable
{
    //our main screen reference
    private final ScreenManager screen;
    
    //object to paint message
    private Paint paint;
    
    //the message to display
    private String message = "";
    
    //where we draw the image
    private int messageX = 0, messageY = 0;
    
    //time we have displayed text
    private long time;
    
    /**
     * The amount of time to wait until we render the game over menu
     */
    private static final long DELAY_MENU_DISPLAY = 750L;
    
    //do we display the menu
    private boolean display = false;
    
    /**
     * The text to display for the new game
     */
    private static final String BUTTON_TEXT_NEW_GAME = "New";
    
    /**
     * The text to display for the next level
     */
    private static final String BUTTON_TEXT_NEXT_LEVEL = "Next";
    
    /**
     * The text to display for to retry the level
     */
    private static final String BUTTON_TEXT_RETRY_LEVEL = "Retry";
    
    /**
     * The text to display for the menu
     */
    private static final String BUTTON_TEXT_MENU = "Menu";
    
    //list of buttons
    private HashMap<Key, Button> buttons;
    
    //buttons to access each button list
    private enum Key
    {
    	Action, Menu, Rate
    }
    
    public GameoverScreen(final ScreenManager screen)
    {
        //store our parent reference
        this.screen = screen;
        
        //create buttons hash map
        this.buttons = new HashMap<Key, Button>();
        
        //the start location of the button
        int y = ScreenManager.BUTTON_Y;
        int x = ScreenManager.BUTTON_X;

        //create our buttons
        addButton(x, y, Key.Action, BUTTON_TEXT_NEW_GAME);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButton(x, y, Key.Menu, BUTTON_TEXT_MENU);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButton(x, y, Key.Rate, MenuScreen.BUTTON_TEXT_RATE_APP);
    }
    
    /**
     * Add button to our list
     * @param x desired x-coordinate
     * @param y desired y-coordinate
     * @param key Unique key to access button
     * @param description The text description to add
     */
    private void addButton(final int x, final int y, final Key key, final String description)
    {
    	//create new button
    	Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
    	
    	//position the button
    	button.setX(x);
    	button.setY(y);
    	
    	//assign the dimensions
    	button.setWidth(MenuScreen.BUTTON_WIDTH);
    	button.setHeight(MenuScreen.BUTTON_HEIGHT);
    	button.updateBounds();
    	
    	//add the text description
    	button.addDescription(description);
    	button.positionText(getScreen().getPaint());
    	
    	//add button to the list
    	this.buttons.put(key, button);
    }
    
    private ScreenManager getScreen()
    {
    	return this.screen;
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        //reset timer
        time = System.currentTimeMillis();
        
        //do we display the menu
        setDisplay(false);
    }
    
    /**
     * Assign the message
     * @param message The message we want displayed
     * @param success Did we win (true) or lose (false)?
     */
    public void setMessage(final String message, final boolean success)
    {
        //assign the message
        this.message = message;
        
        //create temporary rectangle
        Rect tmp = new Rect();
        
        //create paint text object for the message
        if (paint == null)
        {
	        //assign metrics
        	paint = new Paint();
        	paint.setColor(Color.WHITE);
        	paint.setTextSize(Stats.STAT_FONT_SIZE);
	        paint.setTypeface(Font.getFont(Assets.FontGameKey.Default));
        }
        
        //get the rectangle around the message
        paint.getTextBounds(message, 0, message.length(), tmp);
        
        //calculate the position of the message
        messageX = (GamePanel.WIDTH / 2) - (tmp.width() / 2);
        messageY = (int)(GamePanel.HEIGHT * .12);
        
        //determine the buttons depending on the mode selected
        switch (getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Mode))
        {
	        case OptionsScreen.MODE_CHALLENGE:
	        	buttons.get(Key.Action).setDescription(0, (success) ? BUTTON_TEXT_NEXT_LEVEL : BUTTON_TEXT_RETRY_LEVEL);
	        	break;
	        	
        	default:
        		buttons.get(Key.Action).setDescription(0, BUTTON_TEXT_NEW_GAME);
        		break;
        }
        
        //make sure each button is positioned correctly
        for (Button button : buttons.values())
        {
        	button.positionText(getScreen().getPaint());
        }
    }
    
    /**
     * Flag display
     * @param display true if we want to display the buttons, false otherwise
     */
    private void setDisplay(final boolean display)
    {
    	this.display = display;
    }
    
    /**
     * Do we display the buttons?
     * @return true = yes, false = no
     */
    private boolean hasDisplay()
    {
    	return this.display;
    }
    
    @Override
    public boolean update(final int action, final float x, final float y) throws Exception
    {
        //if we aren't displaying the menu, return false
        if (!hasDisplay())
            return false;
        
        if (action == MotionEvent.ACTION_UP)
        {
        	for (Key key : Key.values())
        	{
        		//get the current button
        		Button button = buttons.get(key);
        		
        		//if we did not click this button skip to the next
        		if (!button.contains(x, y))
        			continue;
        		
                //remove message
                setMessage("", false);
                
        		//handle each button different
        		switch (key)
        		{
	        		case Action:
	                    
	                    //reset with the current settings
	                    getScreen().getScreenGame().getGame().reset();
	                    
	                    //move back to the game
	                    getScreen().setState(ScreenManager.State.Running);
	                    
	                    //play sound effect
	                    Audio.play(Assets.AudioMenuKey.Selection);
	                    
	                    //we don't request additional motion events
	                    return false;

	        		case Menu:
	                    
	                    //determine if anything needs to be done depending on the game mode
	                    switch (getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Mode))
	                    {
	            	        case OptionsScreen.MODE_CHALLENGE:
	            	        	getScreen().getScreenGame().getGame().getLevelSelect().setSelection(false);
	            	        	break;
	            	        	
	                    	default:
	                    		break;
	                    }
	        			
	                    //move to the main menu
	                    getScreen().setState(ScreenManager.State.Ready);
	                    
	                    //play sound effect
	                    Audio.play(Assets.AudioMenuKey.Selection);
	                    
	                    //we don't request additional motion events
	                    return false;
	        			
	        		case Rate:
	                    
	                    //play sound effect
	                    Audio.play(Assets.AudioMenuKey.Selection);
	                    
	                    //go to rate game page
	                    getScreen().getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_RATE_URL);
	                    
	                    //we don't request additional motion events
	                    return false;
	        			
        			default:
        				throw new Exception("Key not setup here: " + key);
        		}
        	}
        }
        
        //no action was taken here and we may need additional events
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        //if not displaying the menu, track timer
        if (!hasDisplay())
        {
            //if time has passed display menu
            if (System.currentTimeMillis() - time >= DELAY_MENU_DISPLAY)
            {
            	//display the menu
            	setDisplay(true);

                //anything else to do here
            }
        }
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        if (hasDisplay())
        {
            //only darken the background when the menu is displayed
            ScreenManager.darkenBackground(canvas);
            
            //if message exists, draw the text
            if (paint != null)
                canvas.drawText(this.message, messageX, messageY, paint);
        
            //render the buttons
            for (Key key : Key.values())
            {
            	buttons.get(key).render(canvas, getScreen().getPaint());
            }
        }
    }
    
    @Override
    public void dispose()
    {
        if (paint != null)
        	paint = null;
        
        if (buttons != null)
        {
	        for (Key key : Key.values())
	        {
	        	if (buttons.get(key) != null)
	        	{
	        		buttons.get(key).dispose();
	        		buttons.put(key, null);
	        	}
	        }
	        
	        buttons.clear();
	        buttons = null;
        }
    }
}