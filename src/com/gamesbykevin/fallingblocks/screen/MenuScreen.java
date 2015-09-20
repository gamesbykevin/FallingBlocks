package com.gamesbykevin.fallingblocks.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.fallingblocks.FallingBlocks;
import com.gamesbykevin.fallingblocks.assets.Assets;

/**
 * Our main menu
 * @author ABRAHAM
 */
public class MenuScreen implements Screen, Disposable
{
    //the logo
    private final Bitmap logo;
    
    //the buttons in our menu
    private Button start, exit, settings, instructions, more, rate;
    
    //our main screen reference
    private final MainScreen screen;
    
    public MenuScreen(final MainScreen screen)
    {
        //store reference to the logo
        this.logo = Images.getImage(Assets.ImageKey.Logo);
        
        //store our screen reference
        this.screen = screen;
        
        final int x = 230;
        int y = 350;
        
        final int addY = 200;
        
        //create buttons
        this.start = new Button(Images.getImage(Assets.ImageKey.MenuStart));
        this.start.setX(x);
        this.start.setY(y);
        this.start.updateBounds();
        
        y += addY;
        this.settings = new Button(Images.getImage(Assets.ImageKey.MenuSettings));
        this.settings.setX(x);
        this.settings.setY(y);
        this.settings.updateBounds();
        
        y += addY;
        this.instructions = new Button(Images.getImage(Assets.ImageKey.MenuInstructions));
        this.instructions.setX(x);
        this.instructions.setY(y);
        this.instructions.updateBounds();
        
        y += addY;
        this.rate = new Button(Images.getImage(Assets.ImageKey.MenuRate));
        this.rate.setX(x);
        this.rate.setY(y);
        this.rate.updateBounds();
        
        y += addY;
        this.more = new Button(Images.getImage(Assets.ImageKey.MenuMore));
        this.more.setX(x);
        this.more.setY(y);
        this.more.updateBounds();
        
        y += addY;
        this.exit = new Button(Images.getImage(Assets.ImageKey.MenuExit));
        this.exit.setX(x);
        this.exit.setY(y);
        this.exit.updateBounds();
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (start.contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioKey.SettingChange);
                
                //set running state
                screen.setState(MainScreen.State.Running);

                //create the game
                screen.createGame();
            }
            else if (settings.contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioKey.SettingChange);
                
                //set the state
                screen.setState(MainScreen.State.Options);
            }
            else if (instructions.contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioKey.SettingChange);
                
                //go to instructions
            }
            else if (rate.contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioKey.SettingChange);
                
                //go to web page
                this.screen.getPanel().getActivity().openWebpage(FallingBlocks.WEBPAGE_RATE_URL);
            }
            else if (more.contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioKey.SettingChange);
                
                //go to web page
                this.screen.getPanel().getActivity().openWebpage(FallingBlocks.WEBPAGE_MORE_GAMES_URL);
            }
            else if (exit.contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioKey.SettingChange);
                
                //exit game
                this.screen.getPanel().getActivity().finish();
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
        //draw main logo
        canvas.drawBitmap(logo, 104, 100, null);
        
        //draw the menu buttons
        start.render(canvas);
        settings.render(canvas);
        instructions.render(canvas);
        rate.render(canvas);
        more.render(canvas);
        exit.render(canvas);
    }
    
    @Override
    public void dispose()
    {
        if (start != null)
        {
            start.dispose();
            start = null;
        }
        
        if (exit != null)
        {
            exit.dispose();
            exit = null;
        }
        
        if (settings != null)
        {
            settings.dispose();
            settings = null;
        }
        
        if (instructions != null)
        {
            instructions.dispose();
            instructions = null;
        }
        
        if (more != null)
        {
            more.dispose();
            more = null;
        }
        
        if (rate != null)
        {
            rate.dispose();
            rate = null;
        }
    }
}