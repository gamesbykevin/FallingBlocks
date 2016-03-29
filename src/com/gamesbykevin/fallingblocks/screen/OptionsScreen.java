package com.gamesbykevin.fallingblocks.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.HashMap;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.fallingblocks.assets.Assets;
import com.gamesbykevin.fallingblocks.panel.GamePanel;
import com.gamesbykevin.fallingblocks.storage.settings.Settings;
import com.gamesbykevin.fallingblocks.MainActivity;

/**
 * This screen will contain the game options
 * @author GOD
 */
public class OptionsScreen implements Screen, Disposable
{
    //our logo reference
    private final Bitmap logo;
    
    //list of buttons
    private HashMap<Key, Button> buttons;
    
    //our main screen reference
    private final ScreenManager screen;
    
    //our storage settings object
    private Settings settings;
    
    //our paint object for this screen
    private Paint paint;

    //buttons to access each button in the list
    public enum Key
    {
    	Back, Sound, Vibrate, Difficulty, Mode, Instructions, Facebook, Twitter
    }
    
    /**
     * The value when the sound is enabled
     */
    public static final int SOUND_ENABLED = 0;
    
    /**
     * The value when the sound is disabled
     */
    public static final int SOUND_DISABLED = 1;
    
    /**
     * Free Mode
     */
    public static final int MODE_FREE = 0;
    
    /**
     * View Cpu Mode
     */
    public static final int MODE_VIEW_CPU = 1;
    
    /**
     * Vs Cpu Mode
     */
    public static final int MODE_VS_CPU = 2;
    
    /**
     * Challenge mode 
     */
    public static final int MODE_CHALLENGE = 3;
    
    /**
     * Attack mode 
     */
    public static final int MODE_ATTACK = 4;
    
    public OptionsScreen(final ScreenManager screen)
    {
        //our logo reference
        this.logo = Images.getImage(Assets.ImageMenuKey.Logo);

        //create buttons hash map
        this.buttons = new HashMap<Key, Button>();

        //store our screen reference
        this.screen = screen;
        
        //create our paint object for this menu
        this.paint = new Paint(screen.getPaint());
        
        //change font size
        this.paint.setTextSize(ScreenManager.DEFAULT_FONT_SIZE);
        
        //start coordinates
        int y = ScreenManager.BUTTON_Y;
        int x = ScreenManager.BUTTON_X;
        
        //add sound option
        addButtonSound(x, y);
        
        //add vibrate option
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonVibrate(x, y);
        
        //add mode option
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonMode(x, y);
        
        //add mode option
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonDifficulty(x, y);
        
        //the back button
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonBack(x, y);
        
        //add social media icons finally
        addIcons();
        
        //setup each button dimensions and boundary
        for (Key key : Key.values())
        {
        	switch (key)
        	{
	        	case Twitter:
	        	case Facebook:
	        	case Instructions:
	        		buttons.get(key).setWidth(MenuScreen.ICON_DIMENSION);
	        		buttons.get(key).setHeight(MenuScreen.ICON_DIMENSION);
                	buttons.get(key).updateBounds();
	        		break;
        		
        		default:
                	buttons.get(key).setWidth(MenuScreen.BUTTON_WIDTH);
                	buttons.get(key).setHeight(MenuScreen.BUTTON_HEIGHT);
                	buttons.get(key).updateBounds();
                	buttons.get(key).positionText(paint);
                	break;
        	}
        }
        
        //create our settings object last, which will load the previous settings
        this.settings = new Settings(this, screen.getPanel().getActivity());
    }
    
    /**
     * Get the list of buttons.<br>
     * We typically use this list to help load/set the settings based on the index of each button.
     * @return The list of buttons on the options screen
     */
    public HashMap<Key, Button> getButtons()
    {
    	return this.buttons;
    }
    
    /**
     * Add icons, including links to social media
     */
    private void addIcons()
    {
        Button tmp = new Button(Images.getImage(Assets.ImageMenuKey.Instructions));
        tmp.setX(GamePanel.WIDTH - (MenuScreen.ICON_DIMENSION * 4.5));
        tmp.setY(GamePanel.HEIGHT - (MenuScreen.ICON_DIMENSION * 1.25));
        this.buttons.put(Key.Instructions, tmp);
        
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Facebook));
        tmp.setX(GamePanel.WIDTH - (MenuScreen.ICON_DIMENSION * 3));
        tmp.setY(GamePanel.HEIGHT - (MenuScreen.ICON_DIMENSION * 1.25));
        this.buttons.put(Key.Facebook, tmp);
        
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Twitter));
        tmp.setX(GamePanel.WIDTH - (MenuScreen.ICON_DIMENSION * 1.5));
        tmp.setY(GamePanel.HEIGHT - (MenuScreen.ICON_DIMENSION * 1.25));
        this.buttons.put(Key.Twitter, tmp);
    }
    
    private void addButtonMode(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Mode - Free");
        button.addDescription("Mode - View Cpu");
        button.addDescription("Mode - vs Cpu");
        button.addDescription("Mode - Challenge");
        button.addDescription("Mode - Attack");
        button.setX(x);
        button.setY(y);
        this.buttons.put(Key.Mode, button);
    }
    
    private void addButtonDifficulty(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Skill - Easy");
        button.addDescription("Skill - Normal");
        button.addDescription("Skill - Hard");
        button.setX(x);
        button.setY(y);
        this.buttons.put(Key.Difficulty, button);
    }
    
    private void addButtonBack(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Back");
        button.setX(x);
        button.setY(y);
        this.buttons.put(Key.Back, button);
    }
    
    private void addButtonSound(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Sound - On");
        button.addDescription("Sound - Off");
        button.setX(x);
        button.setY(y);
        this.buttons.put(Key.Sound, button);
    }
    
    private void addButtonVibrate(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Vibrate - On");
        button.addDescription("Vibrate - Off");
        button.setX(x);
        button.setY(y);
    	this.buttons.put(Key.Vibrate, button);
    }
    
    /**
     * Assign the index.
     * @param key The key of the button we want to change
     * @param index The desired index
     */
    public void setIndex(final Key key, final int index)
    {
    	buttons.get(key).setIndex(index);
    }
    
    /**
     * Get the index selection of the specified button
     * @param key The key of the button we want to check
     * @return The current selection for the specified button key
     */
    public int getIndex(final Key key)
    {
    	return buttons.get(key).getIndex();
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        if (buttons != null)
        {
        	for (Key key : Key.values())
        	{
        		//get the current button
        		Button button = buttons.get(key);
        		
        		try
        		{
	        		switch (key)
	        		{
						case Back:
						case Sound:
						case Difficulty:
						case Mode:
						case Vibrate:
							button.positionText(paint);
							break;
							
						//do nothing for these
						case Instructions:
						case Facebook:
						case Twitter:
							break;
							
						default:
							throw new Exception("Key not handled here: " + key);
	        		}
        		}
        		catch (Exception e)
        		{
        			e.printStackTrace();
        		}
        	}
        }
    }
    
    @Override
    public boolean update(final int action, final float x, final float y) throws Exception
    {
    	//we only want motion event up
    	if (action != MotionEvent.ACTION_UP)
    		return true;
    	
        if (buttons != null)
        {
        	for (Key key : Key.values())
        	{
        		//get the current button
        		Button button = buttons.get(key);
        		
        		//if the button does not exist skip to the next
        		if (button == null)
        			continue;
        		
    			//if we did not select this button, skip to the next
    			if (!button.contains(x, y))
    				continue;
				
				//determine which button
				switch (key)
				{
    				case Back:
    					
    					//change index
    					button.setIndex(button.getIndex() + 1);
    					
    	                //store our settings
    	                settings.save();
    	                
    	                //set ready state
    	                screen.setState(ScreenManager.State.Ready);
    	                
    	                //play sound effect
    	                Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //no need to continue
    	                return false;
    	                
    				case Sound:
    	    			
    					//change index
    					button.setIndex(button.getIndex() + 1);
    					
    					//position the text
    			        button.positionText(paint);
    			        
                        //flip setting
                        Audio.setAudioEnabled(!Audio.isAudioEnabled());
                        
                        //we also want to update the audio button in the controller so the correct is displayed
                        if (screen.getScreenGame() != null && screen.getScreenGame().getGame() != null)
                        {
                        	//make sure the controller exists
                    		if (screen.getScreenGame().getGame().getController() != null)
                    			screen.getScreenGame().getGame().getController().reset();
                        }
                        
                        //play sound effect
                        Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //exit loop
                        return false;
                        
					case Difficulty:
					case Mode:
					case Vibrate:
    					
    					//change index
    					button.setIndex(button.getIndex() + 1);
    					
    					//position the text
    			        button.positionText(paint);
    					
                        //play sound effect
                        Audio.play(Assets.AudioMenuKey.Selection);
    					return false;
                        
    				case Instructions:
    					
    	                //play sound effect
    	                Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //go to instructions
    	                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_GAME_INSTRUCTIONS_URL);
    	                
    	                //we do not request any additional events
    	                return false;
    					
    				case Facebook:
    					
    	                //play sound effect
    	                Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //go to instructions
    	                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_FACEBOOK_URL);
    	                
    	                //we do not request any additional events
    	                return false;
    					
    				case Twitter:
    					
    	                //play sound effect
    	                Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //go to instructions
    	                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_TWITTER_URL);
    	                
    	                //we do not request any additional events
    	                return false;
    				
    				default:
                    	throw new Exception("Key not setup here: " + key);
				}
        	}
        }
    	
        //return true
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        //no updates needed here
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw our main logo
        canvas.drawBitmap(logo, ScreenManager.LOGO_X, ScreenManager.LOGO_Y, null);
        
        //draw the menu buttons
    	for (Key key : Key.values())
    	{
    		if (buttons.get(key) != null)
    		{
    			switch (key)
    			{
	    			case Back:
	    			case Sound:
	    			case Mode:
					case Vibrate:
	    			case Difficulty:
	    				buttons.get(key).render(canvas, paint);
	    				break;
	    				
	    			case Instructions:
	    			case Facebook:
	    			case Twitter:
	    				buttons.get(key).render(canvas);
	    				break;
	    				
	    			default:
	    				throw new Exception("Button with key not setup here: " + key);
    			}
    		}
    			
    	}
    }
    
    @Override
    public void dispose()
    {
        if (paint != null)
        	paint = null;
    	
        if (settings != null)
        {
            settings.dispose();
            settings = null;
        }
        
        if (buttons != null)
        {
        	for (Key key :Key.values())
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