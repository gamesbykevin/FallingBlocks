package com.gamesbykevin.fallingblocks.screen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.fallingblocks.game.Game;
import com.gamesbykevin.fallingblocks.panel.GamePanel;

/**
 * This class will contain the game screens
 * @author ABRAHAM
 */
public final class MainScreen implements Screen, Disposable
{
    /**
     * These are the different states in our game
     */
    public enum State 
    {
        Ready, Running, Paused, GameOver
    }
    
    //the current state of the game
    private State state;
    
    //object to paint background
    private Paint paint;
    
    //our game panel
    private final GamePanel panel;
    
    //our object containing the main game functionality
    private Game game;
    
    //the menu
    private MenuScreen menuScreen;
    
    //the pause screen
    private PauseScreen pauseScreen;
    
    public MainScreen(final GamePanel panel)
    {
        //store our game panel reference
        this.panel = panel;
        
        //default to the ready state
        this.state = State.Ready;
        
        //create paint text object
        this.paint = new Paint();
        this.paint.setColor(Color.WHITE);
        this.paint.setTextSize(48f);
        this.paint.setAntiAlias(true);
        this.paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        
        //create the menu screens
        this.menuScreen = new MenuScreen(this);
        this.pauseScreen = new PauseScreen(this);
    }
    
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        switch (getState())
        {
            case Ready:
                menuScreen.update(event, x, y);
                break;
                
            case Running:
                if (getGame() != null)
                    getGame().updateMotionEvent(event, x, y);
                break;
                
            case Paused:
                this.pauseScreen.update(event, x, y);
                break;
                
            case GameOver:
                break;
            
            //this shouldn't happen
            default:
                throw new Exception("Undefined state " + state.toString());
        }
        
        //return true for now
        return true;
    }
    
    public void update() throws Exception
    {
        switch (getState())
        {
            case Ready:
                break;
                
            case Running:
                if (getGame() != null)
                    getGame().update();
                break;
                
            case Paused:
                break;
                
            case GameOver:
                break;
            
            //this shouldn't happen
            default:
                throw new Exception("Undefined state " + state.toString());
        }
    }
    
    protected Game getGame()
    {
        return this.game;
    }
    
    public void createGame()
    {
        //if game doesn't exist
        if (getGame() == null)
            this.game = new Game(this);
        
        //assign the game mode?
    }
    
    protected GamePanel getPanel()
    {
        return this.panel;
    }
    
    private State getState()
    {
        return this.state;
    }
    
    public void setState(final State state)
    {
        this.state = state;
    }
    
    public void render(final Canvas canvas) throws Exception
    {
        if (canvas != null)
        {
            //calculate the screen ratio
            final float scaleFactorX = getPanel().getWidth() / (float)GamePanel.WIDTH;
            final float scaleFactorY = getPanel().getHeight() / (float)GamePanel.HEIGHT;
        
            //scale to the screen size
            canvas.scale(scaleFactorX, scaleFactorY);
            
            //fill background
            canvas.drawColor(Color.BLACK);
            
            //render the appropriate screen
            switch (getState())
            {
                case Ready:
                    if (getGame() != null)
                        getGame().render(canvas);
                    
                    //darken background
                    canvas.drawARGB(175, 0, 0, 0);
                    
                    //draw menu
                    if (menuScreen != null)
                        menuScreen.render(canvas);
                    break;

                case Running:
                    if (getGame() != null)
                        getGame().render(canvas);
                    break;

                case Paused:
                    if (getGame() != null)
                        getGame().render(canvas);
                    
                    //darken background
                    canvas.drawARGB(175, 0, 0, 0);
                    
                    if (pauseScreen != null)
                        pauseScreen.render(canvas);
                    break;

                case GameOver:
                    break;

                //this shouldn't happen
                default:
                    throw new Exception("Undefined state " + state.toString());
            }
        }
    }
    
    @Override
    public void dispose()
    {
        this.paint = null;
        
        if (game != null)
        {
            game.dispose();
            game = null;
        }
        
        if (pauseScreen != null)
        {
            pauseScreen.dispose();
            pauseScreen = null;
        }
        
        if (menuScreen != null)
        {
            menuScreen.dispose();
            menuScreen = null;
        }
    }
}