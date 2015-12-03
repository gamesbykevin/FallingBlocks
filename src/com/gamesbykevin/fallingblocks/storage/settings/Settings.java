package com.gamesbykevin.fallingblocks.storage.settings;

import android.app.Activity;
import com.gamesbykevin.androidframework.io.storage.Internal;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.fallingblocks.screen.OptionsScreen;

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

                for (int key = 0; key < data.length; key++)
                {
                	//get the index value in this array element
                	int index = Integer.parseInt(data[key]);
                	
                	//restore settings
                	screen.setIndex(key, index);
                	
                	//if the sound option, we need to flag the audio enabled/disabled
                	if (key == OptionsScreen.INDEX_BUTTON_SOUND)
                		Audio.setAudioEnabled(index == 0);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        //make sure the text in the buttons are aligned
        screen.reset();
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
            for (int key = 0; key < screen.getButtons().size(); key++)
            {
            	//add the data to our string builder
            	super.getContent().append(screen.getButtons().get(key).getIndex());
            	
            	//if not at the last option add delimiter
            	if (key < screen.getButtons().size() - 1)
            		super.getContent().append(SEPARATOR);
            }

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