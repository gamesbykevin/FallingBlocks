package com.gamesbykevin.fallingblocks.player;

import android.graphics.Canvas;
import com.gamesbykevin.androidframework.resources.Disposable;

/**
 * Methods required to implement for each player
 * @author GOD
 */
public interface IPlayer extends Disposable
{
    /**
     * Method to update common elements
     * @throws Exception 
     */
    public void update() throws Exception;
    
    /**
     * Method to render the player
     * @param canvas Canvas to write pixels to
     * @throws Exception
     */
    public void render(final Canvas canvas) throws Exception;
    
    /**
     * Logic to reset game
     */
    public void reset();
}