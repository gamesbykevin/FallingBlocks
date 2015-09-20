package com.gamesbykevin.fallingblocks.screen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Audio;

import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;

import com.gamesbykevin.fallingblocks.assets.Assets;
import com.gamesbykevin.fallingblocks.game.Game;
import com.gamesbykevin.fallingblocks.panel.GamePanel;

/**
 * This class will contain the game screens
 * @author ABRAHAM
 */
public final class MainScreen implements Screen, Disposable
{
    //the background image
    private Entity background;
    
    /**
     * These are the different states in our game
     */
    public enum State 
    {
        Ready, Running, Paused, Options, Exit, GameOver
    }
    
    //the current state of the game
    private State state;
    
    //our game panel
    private final GamePanel panel;
    
    //our object containing the main game functionality
    private Game game;
    
    //the menu
    private MenuScreen menuScreen;
    
    //the pause screen
    private PauseScreen pauseScreen;
    
    //the confirm exit screen
    private ExitScreen exitScreen;
    
    //options screen
    private OptionsScreen optionsScreen;
    
    //the gameover screen
    private GameoverScreen gameoverScreen;
    
    public MainScreen(final GamePanel panel)
    {
        //create a new background
        this.background = new Entity();
        
        //assign position, size
        this.background.setX(0);
        this.background.setY(0);
        this.background.setWidth(GamePanel.WIDTH);
        this.background.setHeight(GamePanel.HEIGHT);
        
        //add animation to spritesheet
        this.background.getSpritesheet().add(Assets.ImageKey.Background, new Animation(Images.getImage(Assets.ImageKey.Background)));
        
        //store our game panel reference
        this.panel = panel;
        
        //default to the ready state
        this.state = State.Ready;
        
        //create the menu screens
        this.menuScreen = new MenuScreen(this);
        this.pauseScreen = new PauseScreen(this);
        this.exitScreen = new ExitScreen(this);
        this.optionsScreen = new OptionsScreen(this);
        this.gameoverScreen = new GameoverScreen(this);
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
                pauseScreen.update(event, x, y);
                break;
                
            case Options:
                optionsScreen.update(event, x, y);
                break;
                
            case Exit:
                exitScreen.update(event, x, y);
                break;
                
            case GameOver:
                gameoverScreen.update(event, x, y);
                break;
            
            //this shouldn't happen
            default:
                throw new Exception("Undefined state " + state.toString());
        }
        
        //return true for now
        return true;
    }
    
    /**
     * Get the game over screen
     * @return The game over screen reference
     */
    public GameoverScreen getGameoverScreen()
    {
        return this.gameoverScreen;
    }
    
    /**
     * Get the options screen
     * @return The options screen reference
     */
    public OptionsScreen getOptionsScreen()
    {
        return this.optionsScreen;
    }
    
    /**
     * Update runtime logic here (if needed)
     * @throws Exception 
     */
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
                
            case Options:
                break;
                
            case Exit:
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
    
    /**
     * Create game object
     * @throws Exception
     */
    public void createGame() throws Exception
    {
        this.game = new Game(
            this, 
            Game.Mode.values()[optionsScreen.getIndexMode()],
            Game.Difficulty.values()[optionsScreen.getIndexDifficulty()]
        );
        
        //reset game
        getGame().reset();
    }
    
    protected GamePanel getPanel()
    {
        return this.panel;
    }
    
    public State getState()
    {
        return this.state;
    }
    
    public void setState(final State state)
    {
        //if pausing store the previous state
        if (state == State.Paused)
            pauseScreen.setStatePrevious(getState());
        
        //if not the running state, stop all existing sound
        if (state != State.Running)
        {
            Audio.stop();
        }
        else
        {
            if (getGame() != null)
                getGame().resumeMusic();
        }
        
        
        //assign the state
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
            
            //draw the background
            background.render(canvas);
            
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

                    //if running, only render the game
                case Running:
                    if (getGame() != null)
                        getGame().render(canvas);
                    break;

                case Paused:
                    
                    switch (pauseScreen.getStatePrevious())
                    {
                        case Ready:
                            //draw menu
                            if (menuScreen != null)
                                menuScreen.render(canvas);
                            break;
                            
                        case Running:
                            if (getGame() != null)
                                getGame().render(canvas);
                            break;
                            
                        case Options:
                            if (optionsScreen != null)
                                optionsScreen.render(canvas);
                            break;
                            
                        case Exit:
                            if (exitScreen != null)
                                exitScreen.render(canvas);
                            break;
                            
                        case GameOver:
                            break;
                    }
                    
                    //darken background
                    canvas.drawARGB(175, 0, 0, 0);
                    
                    if (pauseScreen != null)
                        pauseScreen.render(canvas);
                    break;

                case Options:
                    if (getGame() != null)
                        getGame().render(canvas);
                    
                    //darken background
                    canvas.drawARGB(175, 0, 0, 0);
                    
                    if (optionsScreen != null)
                        optionsScreen.render(canvas);
                    break;
                    
                case Exit:
                    if (getGame() != null)
                        getGame().render(canvas);
                    
                    //darken background
                    canvas.drawARGB(175, 0, 0, 0);
                    
                    if (exitScreen != null)
                        exitScreen.render(canvas);
                    break;
                    
                case GameOver:
                    if (getGame() != null)
                        getGame().render(canvas);
                    
                    //darken background
                    canvas.drawARGB(175, 0, 0, 0);
                    
                    //render game over info
                    gameoverScreen.render(canvas);
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
        
        if (exitScreen != null)
        {
            exitScreen.dispose();
            exitScreen = null;
        }
        
        if (optionsScreen != null)
        {
            optionsScreen.dispose();
            optionsScreen = null;
        }
        
        if (gameoverScreen != null)
        {
            gameoverScreen.dispose();
            gameoverScreen = null;
        }
    }
}