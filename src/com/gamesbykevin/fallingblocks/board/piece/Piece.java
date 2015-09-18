package com.gamesbykevin.fallingblocks.board.piece;

import android.graphics.Canvas;
import com.gamesbykevin.androidframework.base.Cell;
import com.gamesbykevin.androidframework.resources.Disposable;

import com.gamesbykevin.fallingblocks.board.Board;
import com.gamesbykevin.fallingblocks.panel.GamePanel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A piece contains a list of blocks
 * @author GOD
 */
public final class Piece extends Cell implements Disposable
{
    /**
     * The number of different rotations per piece
     */
    public static final int TOTAL_ROTATIONS = 4;
    
    //the number of rotations
    private int rotations = 0;
    
    //list of blocks that create piece
    private List<Block> blocks;
    
    //all blocks will belong to this group
    private UUID group;
    
    /**
     * The different types of pieces
     */
    public enum Type
    {
        PieceOne, 
        PieceTwo,
        PieceThree,
        PieceFour,
        PieceFive,
        PieceSix,
        PieceSeven,
        Cleared
        ;
    }
    
    /**
     * Create a random piece
     * @param col Column
     * @param row Row
     * @param dimension (width/height) of 1 single block
     * @throws Exception 
     */
    public Piece(final int col, final int row, final int dimension) throws Exception
    {
        //call parent constructor
        super(col, row);
        
        //create unique id
        this.group = UUID.randomUUID();
        
        //create new list of blocks
        this.blocks = new ArrayList<Block>();
        
        final Type type = getRandomType();
        
        //create the piece of random type
        switch (type)
        {
            /**
             * Straight line
             */
            case PieceOne:
                add(0, 0, type);
                add(1, 0, type);
                add(2, 0, type);
                add(3, 0, type);
                break;
                
            /**
             * L
             */
            case PieceTwo:
                add(0, 0, type);
                add(1, 0, type);
                add(0, 1, type);
                add(0, 2, type);
                break;
            
            /**
             * J
             */
            case PieceThree:
                add(0,  0, type);
                add(1,  0, type);
                add(1,  1, type);
                add(1, 2, type);
                break;
                
            /**
             * Square
             */
            case PieceFour:
                add(0,  0, type);
                add(1,  0, type);
                add(1,  1, type);
                add(0,  1, type);
                break;
                
            /**
             * S
             */
            case PieceFive:
                add(0,  0, type);
                add(1,  0, type);
                add(0,  1, type);
                add(-1,  1, type);
                break;
            
            /**
             * half-plus
             */
            case PieceSix:
                add(0,  0, type);
                add(0,  1, type);
                add(1,  1, type);
                add(-1, 1, type);
                break;
                
            /**
             * Z
             */
            case PieceSeven:
                add(0,  0, type);
                add(-1, 0, type);
                add(0,  1, type);
                add(1,  1, type);
                break;
                
            case Cleared:
                throw new Exception("A new piece can't be created of this type.");
                
            default:
                throw new Exception("Piece type not setup here " + type.toString());
        }
        
        //assign the dimensions
        for (Block block : getBlocks())
        {
            block.setWidth(dimension);
            block.setHeight(dimension);
        }
    }
    
    /**
     * Get random piece type appropriate for a new piece
     * @return Random piece type
     */
    private static Type getRandomType()
    {
        //create our list
        List<Type> types = new ArrayList<Type>();
        
        //check each type
        for (Type type : Type.values())
        {
            //this is not a valid piece type, for a new piece
            if (type == Type.Cleared)
                continue;
            
            //add to list
            types.add(type);
        }
        
        //return random result
        return types.get(GamePanel.RANDOM.nextInt(types.size()));
    }
    
    /**
     * Set the rotation
     * @param rotations The number rotation we are on will range from 0 - 3
     */
    public void setRotations(final int rotations)
    {
        //assign number
        this.rotations = rotations;
        
        //if out of range reset to 0
        if (rotations < 0 || rotations >= TOTAL_ROTATIONS)
            this.rotations = 0;
    }
    
    /**
     * Get the number of rotations
     * @return The number of rotations ranging from 0 - 3
     */
    public int getRotation()
    {
        return this.rotations;
    }
    
    /**
     * Rotate the piece clockwise
     */
    public void rotateClockwise()
    {
        for (Block block : getBlocks())
        {
            //get the current location
            final int col = (int)block.getCol();
            final int row = (int)block.getRow();
            
            //rotate
            block.setCol(row);
            block.setRow(-col);
        }
        
        //keep track of rotation
        setRotations(getRotation() + 1);
    }
    
    /**
     * Rotate the piece counter clockwise
     */
    public void rotateCounterClockwise()
    {
        for (Block block : getBlocks())
        {
            //get the current location
            final int col = (int)block.getCol();
            final int row = (int)block.getRow();
            
            //rotate
            block.setCol(-row);
            block.setRow(col);
        }
        
        //keep track of rotation
        setRotations(getRotation() - 1);
    }
    
    /**
     * Increase the current column
     */
    public void increaseCol()
    {
        super.setCol(getCol() + 1);
    }
    
    /**
     * Decrease the current column
     */
    public void decreaseCol()
    {
        super.setCol(getCol() - 1);
    }
    
    /**
     * Increase the current row
     */
    public void increaseRow()
    {
        super.setRow(getRow() + 1);
    }
    
    /**
     * Decrease the current row
     */
    public void decreaseRow()
    {
        super.setRow(getRow() - 1);
    }
    
    /**
     * Get the assigned group.<br>
     * All blocks that make this piece are part of the same group
     * @return The assigned group
     */
    public UUID getGroup()
    {
        return this.group;
    }
    
    /**
     * Does any block in this piece have the specified row?
     * @param row The row we are checking for
     * @return true if the row matches, false otherwise
     */
    public boolean hasRow(final int row)
    {
        for (Block block : getBlocks())
        {
            //if the row matches, return true
            if (getRow() + block.getRow() == row)
                return true;
        }
        
        //none of the blocks were on the specified row
        return false;
    }
    
    /**
     * Get the blocks.
     * @return The list of blocks that make up this piece
     */
    public List<Block> getBlocks()
    {
        return this.blocks;
    }
    
    /**
     * Does this piece has a block at the specified location?
     * @param col Column
     * @param row Row
     * @return true if the location is part of one of the blocks in this piece, false otherwise
     */
    public boolean hasBlock(final int col, final int row)
    {
        //check each block
        for (Block block : getBlocks())
        {
            //adjust location to check for collision
            if (col == getCol() + block.getCol() && row == getRow() + block.getRow())
                return true;
        }
        
        //return false
        return false;
    }
    
    /**
     * Is this piece in bounds of the game board?
     * @return true if in bounds of the game board, false otherwise
     */
    public boolean hasBounds()
    {
        for (Block block : getBlocks())
        {
            final int col = (int)(getCol() + block.getCol());
            final int row = (int)(getRow() + block.getRow());
            
            //if the column is out of bounds
            if (col < 0 || col >= Board.COLS)
                return false;
            
            //if the row is out of bounds
            if (row >= Board.ROWS)
                return false;
        }
        
        //piece is within board boundary, return true
        return true;
    }
    
    /**
     * Add block to the list
     * @param col Column
     * @param row Row
     * @param type The type of piece the blocks are
     */
    private void add(final int col, final int row, final Type type)
    {
        this.blocks.add(new Block(col, row, getGroup(), type));
    }
    
    @Override
    public void dispose()
    {
        if (blocks != null)
        {
            for (Block block : blocks)
            {
                if (block != null)
                {
                    block.dispose();
                    block = null;
                }
            }
            
            blocks.clear();
            blocks = null;
        }
    }
    
    /**
     * Render the block on the board
     * @param canvas Canvas to write pixel data
     * @param board The board we are rendering on
     * @throws Exception
     */
    public void render(final Canvas canvas, final Board board) throws Exception
    {
        render(canvas, board, 1f);
    }
    
    /**
     * Render the block on the board
     * @param canvas Canvas to write pixel data
     * @param board The board we are rendering on
     * @param scale The ratio to resize the piece
     * @throws Exception
     */
    public void render(final Canvas canvas, final Board board, final float scale) throws Exception
    {
        render(canvas, board, scale, (int)board.getX(), (int)board.getY());
    }
    
    /**
     * Render the block on the board at the specified starting location
     * @param canvas Canvas to write pixel data
     * @param board The board we are rendering on
     * @param scale The ratio to resize the piece
     * @param startX The desired starting x-coordinate of the piece
     * @param startY The desired starting y-coordinate of the piece
     * @throws Exception
     */
    public void render(final Canvas canvas, final Board board, final float scale, final int startX, final int startY) throws Exception
    {
        for (Block block : getBlocks())
        {
            //store the dimensions
            final double w = block.getWidth();
            final double h = block.getHeight();
            
            //adjust dimensions
            block.setWidth(w * scale);
            block.setHeight(h * scale);
            
            //calculate starting coordinate of the piece
            final int x = (int)(startX + (getCol() * block.getWidth()));
            final int y = (int)(startY + (getRow() * block.getHeight()));
            
            //adjust location for the current block
            final int blockX = x + (int)(block.getCol() * block.getWidth());
            final int blockY = y + (int)(block.getRow() * block.getHeight());
            
            //render the block
            block.render(canvas, board.getSpritesheet().get(block.getType()).getImage(), blockX, blockY);
            
            //restore dimensions
            block.setWidth(w);
            block.setHeight(h);
        }
    }
}