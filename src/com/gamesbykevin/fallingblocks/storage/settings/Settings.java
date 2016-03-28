package com.gamesbykevin.fallingblocks.storage.settings;

import android.app.Activity;
import com.gamesbykevin.androidframework.io.storage.Internal;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.fallingblocks.screen.OptionsScreen;
import static com.gamesbykevin.fallingblocks.screen.OptionsScreen.SOUND_ENABLED;
import com.gamesbykevin.fallingblocks.screen.OptionsScreen.Key;

/**
 * Save the settings to the internal storage
 * @author GOD
 */
public final class Settings extends Internal
{
    //our options screen reference object
    private final OptionsScreen screen;
    
    /**
     * This string will separate each setting
     */
    private static final String SEPARATOR = ";";
    
    public Settings(final OptionsScreen screen, final Activity activity)
    {
        super("Settings", activity);
        
        //store our screen reference object
        this.screen = screen;
        
        //if content exists load it
        if (super.getContent().toString().trim().length() > 0)
        {
            try
            {
                //split the content into an array
                final String[] data = super.getContent().toString().split(SEPARATOR);

                //get the # of the options in the settings
                final int length = Key.values().length;
                
                //load each option
                for (int position = 0; position < length; position++)
                {
                	//make sure we stay in bounds
                	if (position >= length || position >= data.length)
                		break;
                	
                	//locate the option key
                	final Key key = Key.values()[position];
                	
                	//parse the index value from this array element
                	final int value = Integer.parseInt(data[position]);
                	
                	//restore settings
                	getScreen().setIndex(key, value);
                	
                	//if the sound option, we need to flag the audio enabled/disabled
                	if (key == Key.Sound)
                		Audio.setAudioEnabled(value == SOUND_ENABLED);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        //make sure the text in the buttons are aligned
        getScreen().reset();
    }
    
    /**
     * Get the options screen
     * @return The options screen object reference
     */
    private OptionsScreen getScreen()
    {
    	return this.screen;
    }
    
    /**
     * Save the settings to the internal storage
     */
    @Override
    public void save()
    {
        try
        {
            //remove all existing content
            super.getContent().delete(0, super.getContent().length());

            //save every option we have in our options screen
            for (Key key : Key.values())
            {
            	//add the data to our string builder
            	super.getContent().append(getScreen().getButtons().get(key).getIndex());
            	
            	//add delimiter to separate each option index value
        		super.getContent().append(SEPARATOR);
            }
            
            //remove the last character since there won't be any additional settings
            super.getContent().deleteCharAt(super.getContent().length() - 1);

            //save data
            super.save();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
}