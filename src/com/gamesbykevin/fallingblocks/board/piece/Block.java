package com.gamesbykevin.fallingblocks.board.piece;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import java.util.UUID;

import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Disposable;

/**
 * A bunch of blocks create a piece
 * @author GOD
 */
public final class Block extends Entity implements Disposable
{
    //all blocks that create a piece are part of the same group
    private final UUID group;
    
    /**
     * The pixel width/height of 1 block animation
     */
    public static final int DIMENSION_ANIMATION = 32;
    
    /**
     * The pixel dimension of a regular size block
     */
    public static final int DIMENSION_REGULAR = 24;
    
    /**
     * The pixel dimension of a regular size block
     */
    public static final int DIMENSION_SMALL = 12;
    
    /**
     * The dimension of a large size block
     */
    public static final int DIMENSION_LARGE = 32;
    
    //the type of piece this block belongs to
    private Piece.Type type;
    
    /**
     * Create a block at the specified location, color, and group
     * @param col Column
     * @param row Row
     * @param group Group this block belongs to
     * @param type The type of piece this block belongs to
     */
    public Block(final int col, final int row, final UUID group, final Piece.Type type)
    {
        //save the location
        super.setCol(col);
        super.setRow(row);
        
        //assign the group
        this.group = group;
        
        //store the piece type
        setType(type);
    }
    
    /**
     * Assign the piece type.<br>
     * This will have an effect on the image render.
     * @param type The desired piece type
     */
    public final void setType(final Piece.Type type)
    {
        this.type = type;
    }
    
    /**
     * Get the type
     * @return The type of piece this block belongs to
     */
    public Piece.Type getType()
    {
        return this.type;
    }
    
    /**
     * Get the group id
     * @return The id where all blocks with the same group create a piece
     */
    public UUID getGroup()
    {
        return this.group;
    }
    
    /**
     * Does the specified group match
     * @param group The group to check for match
     * @return true = yes, false = no
     */
    public boolean hasGroup(final UUID group)
    {
        return getGroup().equals(group);
    }
    
    /**
     * Assign the column
     * @param col Column
     */
    public void setCol(final int col)
    {
        super.setCol(col);
    }
    
    /**
     * Assign the row
     * @param row Row
     */
    public void setRow(final int row)
    {
        super.setRow(row);
    }
    
    @Override
    public void dispose()
    {
        //recycle objects here
        super.dispose();
    }
    
    /**
     * Render the block
     * @param canvas Canvas to write pixel data
     * @param image The image of the block
     * @param x x-coordinate
     * @param y y-coordinate
     * @throws Exception 
     */
    public void render(final Canvas canvas, final Bitmap image, final int x, final int y) throws Exception
    {
        //update coordinates
        super.setX(x);
        super.setY(y);
        
        //render the specified image
        super.render(canvas, image);
    }
}