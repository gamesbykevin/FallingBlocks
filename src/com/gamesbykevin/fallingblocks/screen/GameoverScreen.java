package com.gamesbykevin.fallingblocks.screen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Font;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.fallingblocks.assets.Assets;
import com.gamesbykevin.fallingblocks.panel.GamePanel;

/**
 * The game over screen
 * @author GOD
 */
public class GameoverScreen implements Screen, Disposable
{
    //our main screen reference
    private final MainScreen screen;
    
    //object to paint background
    private Paint paint;
    
    //the message to display
    private String message = "";
    
    //the dimensions of the text message
    private int pixelW;
    
    //buttons
    private Button restart, mainmenu, exitgame;
    
    public GameoverScreen(final MainScreen screen)
    {
        //store our parent reference
        this.screen = screen;
        
        //create paint text object
        this.paint = new Paint();
        this.paint.setColor(Color.WHITE);
        this.paint.setTextSize(48f);
        this.paint.setTypeface(Font.getFont(Assets.FontKey.Default));
        
        final int x = 230;
        int y = 375;
        final int addY = 200;
        
        //create our buttons
        this.restart = new Button(Images.getImage(Assets.ImageKey.GameoverRestart));
        this.restart.setX(x);
        this.restart.setY(y);
        this.restart.updateBounds();
        
        y += addY;
        this.mainmenu = new Button(Images.getImage(Assets.ImageKey.GameoverMainmenu));
        this.mainmenu.setX(x);
        this.mainmenu.setY(y);
        this.mainmenu.updateBounds();
        
        y += addY;
        this.exitgame = new Button(Images.getImage(Assets.ImageKey.GameoverExit));
        this.exitgame.setX(x);
        this.exitgame.setY(y);
        this.exitgame.updateBounds();
    }
    
    /**
     * Assign the message
     * @param message The message we want displayed
     */
    public void setMessage(final String message)
    {
        //assign the message
        this.message = message;
        
        //create temporary rectangle
        Rect tmp = new Rect();
        
        //get the rectangle around the message
        paint.getTextBounds(message, 0, message.length(), tmp);
        
        //store the dimensions
        pixelW = tmp.width();
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (restart.contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioKey.SettingChange);
                
                //move back to the game
                screen.setState(MainScreen.State.Running);
                
                //restart game
                screen.getGame().reset();
            }
            else if (mainmenu.contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioKey.SettingChange);
                
                //move to the main menu
                screen.setState(MainScreen.State.Ready);
            }
            else if (exitgame.contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioKey.SettingChange);
                
                //exit game
                screen.getPanel().getActivity().finish();
            }
            return true;
        }
        
        //no action was taken here
        return false;
    }
    
    @Override
    public void update() throws Exception
    {
        //nothing needed to update here
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        if (paint != null)
        {
            //calculate middle
            final int x = (GamePanel.WIDTH / 2) - (pixelW / 2);
            final int y = (int)(GamePanel.HEIGHT * .15);
             
            //draw text
            canvas.drawText(this.message, x, y, paint);
        }
        
        //render buttons
        restart.render(canvas);
        mainmenu.render(canvas);
        exitgame.render(canvas);
    }
    
    @Override
    public void dispose()
    {
        if (paint != null)
            paint = null;
        
        if (restart != null)
        {
            restart.dispose();
            restart = null;
        }
        
        if (mainmenu != null)
        {
            mainmenu.dispose();
            mainmenu = null;
        }
        
        if (exitgame != null)
        {
            exitgame.dispose();
            exitgame = null;
        }
    }
}