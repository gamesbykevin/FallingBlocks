package com.gamesbykevin.fallingblocks.board;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Entity;

import com.gamesbykevin.fallingblocks.board.piece.Block;
import com.gamesbykevin.fallingblocks.board.piece.Piece;

/**
 * The board
 * @author GOD
 */
public final class Board extends Entity implements IBoard
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
    
    //is the game over, board filled up
    private boolean gameover = false;
    
    /**
     * Create a new board
     * @param dimension The size of a single block to determine the size of the board
     */
    public Board(final int dimension)
    {
        super();
        
        //set default dimensions
        super.setWidth(dimension * COLS);
        super.setHeight(dimension * ROWS);
        
        //create new board array
        this.blocks = new Block[ROWS][COLS];
    }
    
    /**
     * Reset the board.<br>
     */
    public void reset()
    {
        for (int col = 0; col < COLS; col++)
        {
            for (int row = 0; row < ROWS; row++)
            {
                setBlock(col, row, null);
            }
        }
        
        //we don't have any completed lines
        setComplete(false);
        
        //the game is not over
        setGameover(false);
    }
    
    /**
     * Is the game over?
     * @return true = yes, false = no
     */
    public boolean hasGameover()
    {
        return this.gameover;
    }
    
    /**
     * Assign game over.
     * @param gameover true = yes, false = no
     */
    public void setGameover(final boolean gameover)
    {
        this.gameover = gameover;
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
     * Add the piece to the board temporarily
     * @param piece The piece we want to add
     */
    public void addTemp(final Piece piece)
    {
        //check each block in the piece
        for (Block block : piece.getBlocks())
        {
            //adjust the absolute position
            final int col = (int)(block.getCol() + piece.getCol());
            final int row = (int)(block.getRow() + piece.getRow());
            
            //add it to the board
            setBlock(col, row, block);
        }
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
                if (hasBlock(col, row))
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
                if (hasBlock(col, row) && piece.hasBlock(col, row))
                    return true;
            }
        }
        
        //no blocks exist here, return false
        return false;
    }
    
    /**
     * Does a block already occupy the specified location on the board?
     * @param col Column
     * @param row Row
     * @return true if a block exists at the specified location, otherwise false
     */
    public boolean hasBlock(final int col, final int row)
    {
        return (getBlock(col, row) != null);
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
        paint.setColor(Color.WHITE);
        canvas.drawRect(getDestination(), paint);    
    }
}