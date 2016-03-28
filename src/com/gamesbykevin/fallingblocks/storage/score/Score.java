package com.gamesbykevin.fallingblocks.storage.score;

/**
 * The score for a level
 * @author GOD
 */
public final class Score 
{
    //the level this score is for
    private final int level;
    
    //the mode this score is for
    private final int mode;
    
    protected Score(final int level, final int mode)
    {
    	//assign default values
    	this.level = level;
    	this.mode = mode;
    }
    
    /**
     * Get the level index
     * @return The level this score is for
     */
    public int getLevel()
    {
        return this.level;
    }
    
    /**
     * Get the mode
     * @return The mode for this score
     */
    public int getMode()
    {
        return this.mode;
    }
}