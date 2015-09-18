package com.gamesbykevin.fallingblocks.player;

import com.gamesbykevin.fallingblocks.board.Board;
import java.util.UUID;

/**
 * The player helper class will contain common logic for the player
 * @author GOD
 */
public abstract class PlayerHelper
{
    //the time of the previous piece drop
    private long time;
    
    /**
     * Different action's the player can make
     */
    public enum Action
    {
        MOVE_DOWN, MOVE_RIGHT, MOVE_LEFT, MOVE_ROTATE
    }
    
    //the desired action
    private Action action;
    
    //default start location
    protected static final int START_X = 10;
    protected static final int START_Y = 10;
    
    //the time to wait before dropping the piece
    private long dropDelay = Board.COMPLETED_LINE_DELAY;
    
    //the default dimension of a single block
    private int dimension;
    
    //is the player human
    private final boolean human;
    
    //the unique id for this entity
    private final UUID id;
    
    /**
     * Create a new instance
     * @param human Is this player human?
     */
    protected PlayerHelper(final boolean human)
    {
        //assign human status
        this.human = human;
        
        //assign a random id
        this.id = UUID.randomUUID();
    }
    
    /**
     * Get the id
     * @return The unique id that identifies this entity
     */
    public UUID getId()
    {
        return this.id;
    }
    
    /**
     * Do we have this id?
     * @param id The unique key we want to check
     * @return true if the id's match, false otherwise
     */
    public boolean hasId(final UUID id)
    {
        return (getId().equals(id));
    }
    
    
    /**
     * Assign the block dimension
     * @param dimension The default dimension (width/height) of a single block
     */
    protected final void setBlockDimension(final int dimension)
    {
        this.dimension = dimension;
    }
    
    /**
     * Get the block dimension
     * @return The default dimension (width/height) of a single block
     */
    protected final int getBlockDimension()
    {
        return this.dimension;
    }
    
    /**
     * Set the drop delay
     * @param dropDelay The time to wait until we apply gravity
     */
    public void setDropDelay(final long dropDelay)
    {
        this.dropDelay = dropDelay;
    }
    
    /**
     * Get the drop delay
     * @return The time to wait until we apply gravity
     */
    public long getDropDelay()
    {
        return this.dropDelay;
    }
    
    /**
     * Reset the timer, from the previous piece drop
     */
    protected void resetTime()
    {
        setTime(System.nanoTime());
    }
    
    /**
     * Assign the previous drop time.
     * @param time The time of the previous piece drop (nanoseconds)
     */
    protected void setTime(final long time)
    {
        this.time = time;
    }
    
    /**
     * The start time of the previous piece drop
     * @return The time of the previous piece drop (nanoseconds)
     */
    protected long getTime()
    {
        return this.time;
    }
    
    /**
     * Does the user have the assigned action
     * @param action Action to check
     * @return true = yes, otherwise false
     */
    protected boolean hasAction(final Action action)
    {
        //if the action does not exist, return false
        if (this.action == null)
            return false;
        
        //check if the action is set
        return (this.action == action);
    }
    
    /**
     * Assign the action
     * @param action The desired player action
     */
    public void setAction(final Action action)
    {
        this.action = action;
    }
    
    /**
     * Is this player human?
     * @return true = yes, false = no
     */
    public final boolean isHuman()
    {
        return this.human;
    }
}