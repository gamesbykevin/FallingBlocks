package com.gamesbykevin.fallingblocks.screen;

import android.graphics.Canvas;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.fallingblocks.game.Game;

/**
 * The game screen that contains the game
 * @author GOD
 */
public class GameScreen implements Screen, Disposable
{
    //our object containing the main game functionality
    private Game game;
    
    //our main screen reference
    private final ScreenManager screen;
    
    public GameScreen(final ScreenManager screen)
    {
        this.screen = screen;
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
        if (getGame() == null)
            this.game = new Game(screen);
        
        //flag level selection as false
        getGame().getLevelSelect().setSelection(false);
        
        //reset the game
        getGame().reset();
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        //anything need to be reset here
    }
    
    @Override
    public boolean update(final int action, final float x, final float y) throws Exception
    {
        if (getGame() != null)
            getGame().update(action, x, y);
        
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        if (getGame() != null)
            getGame().update();
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //render game if exists
        if (getGame() != null)
            getGame().render(canvas);
    }
    
    @Override
    public void dispose()
    {
        if (game != null)
        {
            game.dispose();
            game = null;
        }
    }
}