package com.gamesbykevin.fallingblocks.board;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.fallingblocks.assets.Assets;

import com.gamesbykevin.fallingblocks.board.piece.Block;
import com.gamesbykevin.fallingblocks.board.piece.Piece;
import com.gamesbykevin.fallingblocks.panel.GamePanel;

import java.util.ArrayList;
import java.util.List;

/**
 * The board
 * @author GOD
 */
public final class Board extends Entity implements Disposable
{
    //the table containing the blocks
    private Block[][] blocks;
    
    /**
     * The total number of rows on the board
     */
    public static final int ROWS = 20;
    
    /**
     * The total number of columns on the board
     */
    public static final int COLS = 10;
    
    //the default place to start the piece
    public static final int START_COL = (COLS / 2);
    public static final int START_ROW = 0;
    
    //is there at least 1 line complete
    private boolean complete = false;
    
    //the time the completed line started
    private long time;
    
    /**
     * The amount of time to wait when at least 1 line has been completed
     */
    public static final long COMPLETED_LINE_DELAY = Animation.NANO_SECONDS_PER_SECOND;
    
    //our paint object
    private Paint paint;
    
    /**
     * Create a new board
     */
    public Board()
    {
        //create new board array
        this.blocks = new Block[ROWS][COLS];
        
        //list of different piece types
        List<Piece.Type> pieceTypes = new ArrayList<Piece.Type>();
        
        //add each to a list
        for (Piece.Type type : Piece.Type.values())
        {
            pieceTypes.add(type);
        }
        
        for (int col = 0; col < 5; col++)
        {
            for (int row = 0; row < 2; row++)
            {
                if (pieceTypes.isEmpty())
                    continue;
                
                //calculate starting coordinates
                final int x = col * Block.DIMENSION;
                final int y = row * Block.DIMENSION;
                
                //create a new animation
                Animation animation = new Animation(Assets.getImage(Assets.ImageKey.Blocks), x, y, Block.DIMENSION, Block.DIMENSION);
                        
                //pick random type
                final int index = GamePanel.RANDOM.nextInt(pieceTypes.size());
                
                //add animation to sprite sheet
                super.getSpritesheet().add(pieceTypes.get(index), animation);
                
                //remove random choice so it won't be chosen again
                pieceTypes.remove(index);
            }
        }
    }
    
    /**
     * flag a line completed
     * @param complete true if there is a completed line, false otherwise
     */
    public void setComplete(final boolean complete)
    {
        //assign the complete value
        this.complete = complete;
        
        //if there is a completed line, store the start time
        if (hasComplete())
            this.time = System.nanoTime();
    }
    
    /**
     * Get the time (nanoseconds)
     * @return The start time the board was completed
     */
    public long getTime()
    {
        return this.time;
    }
    
    /**
     * Do we have at least 1 completed line?
     * @return true = yes, false = no
     */
    public boolean hasComplete()
    {
        return this.complete;
    }
    
    /**
     * Add the piece to the board
     * @param piece The piece we want to add
     */
    public void add(final Piece piece)
    {
        //check each block in the piece
        for (Block block : piece.getBlocks())
        {
            //adjust the absolute position
            final int col = (int)(block.getCol() + piece.getCol());
            final int row = (int)(block.getRow() + piece.getRow());
            
            //update the position
            block.setCol(col);
            block.setRow(row);
            
            //add it to the board
            setBlock(col, row, block);
        }
    }
    
    /**
     * Remove the piece from the board.<br>
     * All blocks that belong to the same group are part of the same piece.
     * @param piece The piece we want to remove.
     */
    public void remove(final Piece piece)
    {
        for (int col = 0; col < getBlocks()[0].length; col++)
        {
            for (int row = 0; row < getBlocks().length; row++)
            {
                //make sure the block exists
                if (getBlock(col, row) != null)
                {
                    //if the block is part of the piece, remove it
                    if (getBlock(col, row).hasGroup(piece.getGroup()))
                        setBlock(col, row, null);
                }
            }
        }
    }
    
    /**
     * Does a block already occupy the where the piece is on the board?
     * @param piece The piece we want to check
     * @return true if a block already exists where the piece is located, false otherwise
     */
    public boolean hasBlock(final Piece piece)
    {
        for (int col = 0; col < getBlocks()[0].length; col++)
        {
            for (int row = 0; row < getBlocks().length; row++)
            {
                //if a block exists here, and the block is at the same location as the piece
                if (getBlock(col, row) != null && piece.hasBlock(col, row))
                    return true;
            }
        }
        
        //no blocks exist here, return false
        return false;
    }
    
    /**
     * Assign the block at the specified location
     * @param col Column
     * @param row Row
     * @param block The block we want to assign
     */
    protected void setBlock(final int col, final int row, final Block block)
    {
        getBlocks()[row][col] = block;
    }
    
    /**
     * Get the block at the specified location
     * @param col Column
     * @param row Row
     * @return The block at the location
     */
    public Block getBlock(final int col, final int row)
    {
        return getBlocks()[row][col];
    }
    
    /**
     * Get the list of blocks that make up the board
     * @return The list of blocks
     */
    public Block[][] getBlocks()
    {
        return this.blocks;
    }
    
    /**
     * Recycle objects
     */
    @Override
    public void dispose()
    {
        super.dispose();
        
        if (this.paint != null)
        {
            this.paint.reset();
            this.paint = null;
        }
        
        if (this.blocks != null)
        {
            for (int row = 0; row < ROWS; row++)
            {
                for (int col = 0; col < COLS; col++)
                {
                    if (this.blocks[row][col] != null)
                    {
                        this.blocks[row][col].dispose();
                        this.blocks[row][col] = null;
                    }
                }
            }
            
            this.blocks = null;
        }
    }
    
    /**
     * Draw the background of the board
     * @param canvas 
     */
    public void renderBackground(final Canvas canvas)
    {
        //create new paint object if not exists
        if (paint == null)
            paint = new Paint();
        
        //fill background
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawRect(getDestination(), paint);
        
        //draw border
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(getDestination(), paint);    
    }
}