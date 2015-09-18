package com.gamesbykevin.fallingblocks.player.stats;

import android.graphics.Canvas;

import com.gamesbykevin.androidframework.resources.Disposable;

/**
 * The stats need to implement this method
 * @author GOD
 */
public interface IStats extends Disposable
{
    /**
     * Render the stats
     * @param canvas Canvas where we are writing pixels
     */
    public void render(final Canvas canvas);
    
    /**
     * Logic to reset
     */
    public void reset();
}
