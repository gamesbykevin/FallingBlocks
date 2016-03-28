package com.gamesbykevin.fallingblocks.game.controller;

import android.graphics.Canvas;
import com.gamesbykevin.androidframework.resources.Disposable;

/**
 * Each controller needs to have these methods
 * @author GOD
 */
public interface IController extends Disposable
{
	/**
	 * Logic to reset
	 */
	public void reset() throws Exception;
	
    /**
     * Update logic when motion event occurs
     * @param event Motion Event
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void update(final int action, final float x, final float y) throws Exception;
    
    /**
     * Render our controller
     * @param canvas Object to write pixels to
     * @throws Exception
     */
    public void render(final Canvas canvas) throws Exception;
}
