package com.gamesbykevin.fallingblocks.screen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Font;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.fallingblocks.assets.Assets;
import com.gamesbykevin.fallingblocks.panel.GamePanel;

/**
 * The exit screen, when the player wants to go back to the menu
 * @author GOD
 */
public class ExitScreen implements Screen, Disposable
{
    /**
     * Custom message displayed on screen
     */
    private static final String MESSAGE = "Go back to menu?";
    
    //the dimensions of the text message above
    private final int pixelW, pixelH;
    
    //our main screen reference
    private final MainScreen screen;
    
    //object to paint background
    private Paint paint;
    
    //the button to confirm or cancel
    private Button exitConfirm, exitCancel;
    
    public ExitScreen(final MainScreen screen)
    {
        //store our parent reference
        this.screen = screen;
        
        //create paint text object
        this.paint = new Paint();
        this.paint.setColor(Color.WHITE);
        this.paint.setTextSize(48f);
        this.paint.setTypeface(Font.getFont(Assets.FontKey.Default));
        
        //create temporary rectangle
        Rect tmp = new Rect();
        
        //get the rectangle around the message
        paint.getTextBounds(MESSAGE, 0, MESSAGE.length(), tmp);
        
        //store the dimensions
        pixelW = tmp.width();
        pixelH = tmp.height();
        
        //create buttons
        this.exitCancel  = new Button(Images.getImage(Assets.ImageKey.ExitCancel));
        this.exitConfirm = new Button(Images.getImage(Assets.ImageKey.ExitConfirm));
        
        //position buttons
        this.exitCancel.setX(670);
        this.exitCancel.setY(875);
        this.exitCancel.updateBounds();
        this.exitConfirm.setX(190);
        this.exitConfirm.setY(875);
        this.exitConfirm.updateBounds();
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (this.exitCancel.contains(x, y))
            {
                //if cancel, go back to game
                screen.setState(MainScreen.State.Running);
                
                //play sound effect
                Assets.play(Assets.AudioKey.SettingChange);
            }
            else if (this.exitConfirm.contains(x, y))
            {
                //if confirm, go back to menu
                screen.setState(MainScreen.State.Ready);
                
                //play sound effect
                Assets.play(Assets.AudioKey.SettingChange);
            }
            
            return true;
        }
        
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
            final int y = (GamePanel.HEIGHT / 2) - (pixelH / 2);
             
            //draw text
            canvas.drawText(MESSAGE, x, y, paint);
        }
        
        //render buttons
        this.exitConfirm.render(canvas);
        this.exitCancel.render(canvas);
    }
    
    @Override
    public void dispose()
    {
        if (exitConfirm != null)
        {
            exitConfirm.dispose();
            exitConfirm = null;
        }
        
        if (exitCancel != null)
        {
            exitCancel.dispose();
            exitCancel = null;
        }
        
        if (paint != null)
            paint = null;
    }
}