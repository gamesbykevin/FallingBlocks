package com.gamesbykevin.fallingblocks.game;

import com.gamesbykevin.androidframework.resources.Disposable;

/**
 *
 * @author GOD
 */
public interface IGame extends Disposable
{
    /**
     * Logic to restart the game
     */
    public void reset() throws Exception;
    
    /**
     * Update game logic according to a motion event
     * @param action The action of the motion event "MOTION_UP", "MOTION_DOWN", etc..
     * @param x x-coordinate where motion event occurred
     * @param y y-coordinate where motion event occurred
     * @throws Exception
     */
    public void update(final int action, final float x, final float y) throws Exception;
}
