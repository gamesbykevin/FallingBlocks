package com.gamesbykevin.fallingblocks.board.piece;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import java.util.UUID;

import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Disposable;

/**
 * A bunch of blocks create a piece
 * @author GOD
 */
public final class Block extends Entity implements Disposable
{
    //assigned color for the block
    private final int color;
    
    //all blocks that create a piece are part of the same group
    private final UUID group;
    
    /**
     * The pixel width/height of 1 block
     */
    public static final int DIMENSION = 32;
    
    //the type of piece this block belongs to
    private final Piece.Type type;
    
    /**
     * Create a block at the specified location, color, and group
     * @param col Column
     * @param row Row
     * @param color Color
     * @param group Group this block belongs to
     * @param type The type of piece this block belongs to
     */
    public Block(final int col, final int row, final int color, final UUID group, final Piece.Type type)
    {
        //save the location
        super.setCol(col);
        super.setRow(row);
        
        //store the color
        this.color = color;
        
        //assign the group
        this.group = group;
        
        //store the piece type
        this.type = type;
        
        //assign dimensions
        super.setWidth(DIMENSION);
        super.setHeight(DIMENSION);
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
    
    /**
     * Get the assigned color
     * @return The color of the block when rendered
     */
    public int getColor()
    {
        return this.color;
    }
    
    @Override
    public void dispose()
    {
        //recycle objects here
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