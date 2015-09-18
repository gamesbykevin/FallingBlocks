package com.gamesbykevin.fallingblocks.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.fallingblocks.assets.Assets;

import java.util.ArrayList;
import java.util.List;

/**
 * This screen will contain the game options
 * @author GOD
 */
public class OptionsScreen implements Screen, Disposable
{
    //our logo reference
    private final Bitmap logo;
    
    //list of difficulty buttons
    private List<Button> difficulties;
    
    //list of mode buttons
    private List<Button> modes;
    
    //store the index
    private int indexDifficulty = 0;
    private int indexMode = 0;
    
    //the go back button
    private Button back;
    
    //our main screen reference
    private final MainScreen screen;
    
    public OptionsScreen(final MainScreen screen)
    {
        //our logo reference
        this.logo = Images.getImage(Assets.ImageKey.Logo);
        
        //store our screen reference
        this.screen = screen;
        
        //start coordinates
        final int x = 230;
        int y = 350;
        
        //add buttons
        this.difficulties = new ArrayList<Button>();
        this.difficulties.add(new Button(Images.getImage(Assets.ImageKey.SettingsDifficulty0)));
        this.difficulties.add(new Button(Images.getImage(Assets.ImageKey.SettingsDifficulty1)));
        this.difficulties.add(new Button(Images.getImage(Assets.ImageKey.SettingsDifficulty2)));
        
        for (Button button : difficulties)
        {
            button.setX(x);
            button.setY(y);
            button.updateBounds();
        }
        
        //add buttons
        this.modes = new ArrayList<Button>();
        this.modes.add(new Button(Images.getImage(Assets.ImageKey.SettingsMode0)));
        this.modes.add(new Button(Images.getImage(Assets.ImageKey.SettingsMode1)));
        this.modes.add(new Button(Images.getImage(Assets.ImageKey.SettingsMode2)));
        
        y += 200;
        for (Button button : modes)
        {
            button.setX(x);
            button.setY(y);
            button.updateBounds();
        }
        
        y += 200;
        //the back button
        this.back = new Button(Images.getImage(Assets.ImageKey.SettingsBack));
        this.back.setX(x);
        this.back.setY(y);
        this.back.updateBounds();
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (back.contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioKey.SettingChange);
                
                //set ready state
                screen.setState(MainScreen.State.Ready);
                
                //no need to continue
                return true;
            }
            
            for (Button button : modes)
            {
                if (button.contains(x, y))
                {
                    //play sound effect
                    Audio.play(Assets.AudioKey.SettingChange);
                    
                    //increase index
                    this.indexMode++;
                    
                    if (this.indexMode >= modes.size())
                        this.indexMode = 0;
                    
                    //no need to continue
                    return true;
                }
            }
            
            for (Button button : difficulties)
            {
                if (button.contains(x, y))
                {
                    //play sound effect
                    Audio.play(Assets.AudioKey.SettingChange);
                    
                    //increase index
                    this.indexDifficulty++;
                    
                    if (this.indexDifficulty >= difficulties.size())
                        this.indexDifficulty = 0;
                    
                    //no need to continue
                    return true;
                }
            }
        }
        
        //return false
        return false;
    }
    
    @Override
    public void update() throws Exception
    {
        //no updates needed here
    }
    
    /**
     * Get the mode index
     * @return The user selected mode
     */
    public int getIndexMode()
    {
        return this.indexMode;
    }
    
    /**
     * Get the difficulty index
     * @return The user selected difficulty
     */
    public int getIndexDifficulty()
    {
        return this.indexDifficulty;
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw main logo
        canvas.drawBitmap(logo, 104, 100, null);
        
        //draw the menu buttons
        difficulties.get(this.indexDifficulty).render(canvas);
        modes.get(this.indexMode).render(canvas);
        back.render(canvas);
    }
    
    @Override
    public void dispose()
    {
        if (difficulties != null)
        {
            for (Button button : difficulties)
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            difficulties.clear();
            difficulties = null;
        }
        
        if (modes != null)
        {
            for (Button button : modes)
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            modes.clear();
            modes = null;
        }
    }    
}