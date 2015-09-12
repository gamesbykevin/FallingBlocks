package com.gamesbykevin.fallingblocks.player;

import android.graphics.Canvas;

/**
 * Methods required to implement for each player
 * @author GOD
 */
public interface IPlayer 
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
}
