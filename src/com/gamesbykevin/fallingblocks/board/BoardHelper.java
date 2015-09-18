package com.gamesbykevin.fallingblocks.board;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Cell;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.fallingblocks.assets.Assets;
import com.gamesbykevin.fallingblocks.board.piece.Block;
import com.gamesbykevin.fallingblocks.board.piece.Piece;
import com.gamesbykevin.fallingblocks.panel.GamePanel;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will contain helper methods
 * @author GOD
 */
public final class BoardHelper 
{
    /**
     * No height value
     */
    private static final int NO_HEIGHT = 0;
    
    /**
     * Calculate the aggregate height
     * @param board The board we want to check
     * @return The total height of each column on the board
     */
    public static final int getAggregateHeight(final Board board)
    {
        //start with 0 height
        int height = NO_HEIGHT;
        
        //calculate the height of each column
        for (int col = 0; col < Board.COLS; col++)
        {
            height += getColumnHeight(board, col);
        }
        
        //return result
        return height;
    }
    
    private static int getColumnHeight(final Board board, final int col)
    {
        for (int row = 0; row < Board.ROWS; row++)
        {
            //if there is a block add the height
            if (board.hasBlock(col, row))
            {
                //return height
                return (Board.ROWS - row);
            }
        }
        
        //return no height
        return NO_HEIGHT;
    }
    
    /**
     * Count the number of holes on the board.<br>
     * A hole is an empty space on the board that has a filled block above it
     * @param board The board we want to check
     * @return The total number of empty blocks that are below a filled block
     */
    public static final int getHoleCount(final Board board)
    {
        int count = 0;
        
        for (int col = 0; col < Board.COLS; col++)
        {
            //did we hit a block yet
            boolean hitBlock = false;
            
            for (int row = 0; row < Board.ROWS; row++)
            {
                //if we found a block, flag it
                if (board.hasBlock(col, row))
                {
                    hitBlock = true;
                }
                else
                {
                    //if this is not a block and we already found one, then count the hole
                    if (hitBlock)
                        count++;
                }
            }
        }
        
        //return the total number of holes
        return count;
    }
    
    /**
     * Get the total height difference between each neighboring column
     * @param board The board we want to check
     * @return The total bumpiness between columns
     */
    public static final int getTotalBumpiness(final Board board)
    {
        int total = 0;
        
        for (int col = 0; col < Board.COLS - 1; col++)
        {
            //get the height of the 2 neighboring columns
            final int height1 = getColumnHeight(board, col);
            final int height2 = getColumnHeight(board, col + 1);
            
            //calculate bumpiness and add to total
            if (height1 > height2)
            {
                total += (height1 - height2);
            }
            else
            {
                total += (height2 - height1);
            }
        }
        
        //return total bumpiness
        return total;
    }
    
    /**
     * Assign animations
     * @param board The board to assign animations to
     */
    public static final void assignAnimations(final Board board)
    {
        //create list of locations
        List<Cell> options = new ArrayList<Cell>();
        options.add(new Cell(0,0));
        options.add(new Cell(1,0));
        options.add(new Cell(2,0));
        options.add(new Cell(3,0));
        options.add(new Cell(4,0));
        options.add(new Cell(0,1));
        options.add(new Cell(1,1));
        options.add(new Cell(2,1));
        options.add(new Cell(3,1));
        
        //animation object
        Animation animation;
        
        //pick a random animation for each piece type
        for (Piece.Type type : Piece.Type.values())
        {
            //we will ignore the cleared type for now
            if (type == Piece.Type.Cleared)
                continue;
            
            //pick random index
            final int index = GamePanel.RANDOM.nextInt(options.size());
            
            //calculate starting coordinates
            final int x = (int)options.get(index).getCol() * Block.DIMENSION_ANIMATION;
            final int y = (int)options.get(index).getRow() * Block.DIMENSION_ANIMATION;

            //create a new animation
            animation = new Animation(Images.getImage(Assets.ImageKey.Blocks), x, y, Block.DIMENSION_ANIMATION, Block.DIMENSION_ANIMATION);
            
            //add animation to sprite sheet
            board.getSpritesheet().add(type, animation);
            
            //remove option from list
            options.remove(index);
        }
        
        //here is where the cleared animation is
        final int x = (4 * Block.DIMENSION_ANIMATION);
        final int y = (1 * Block.DIMENSION_ANIMATION);
        
        //add the cleared animation last
        board.getSpritesheet().add(Piece.Type.Cleared, new Animation(Images.getImage(Assets.ImageKey.Blocks), x, y, Block.DIMENSION_ANIMATION, Block.DIMENSION_ANIMATION));
    }
    
    /**
     * Clear all blocks in the specified row
     * @param board The board we want to manipulate
     * @param row The row where we want to remove all blocks
     */
    private static void clearRow(final Board board, final int row)
    {
        //check each column
        for (int col = 0; col < Board.COLS; col++)
        {
            //remove the block at this location
            board.setBlock(col, row, null);
        }
    }
    
    /**
     * Clear the completed rows from the board
     * @param board The board we want to remove the completed rows from
     */
    public static final void clearRows(final Board board)
    {
        //check each row
        for (int row = 0; row < Board.ROWS; row++)
        {
            //if the current row is complete
            if (hasCompletedRow(board, row))
            {
                //clear the blocks in this row
                clearRow(board, row);
            }
        }
    }
    
    /**
     * Is the specified row complete?
     * @param board The board we are checking
     * @param row The row we want to check
     * @return true if every column in this row is an existing block, false otherwise
     */
    private static boolean hasCompletedRow(final Board board, final int row)
    {
        for (int col = 0; col < Board.COLS; col++)
        {
            //if a block does not exist, we don't have a completed row
            if (board.getBlock(col, row) == null)
                return false;
        }
        
        //all blocks exist in this row, return true
        return true;
    }
    
    /**
     * For each row that is completed.<br>
     * Each block in that row will have the Piece.Type.Cleared
     * @param board The board we are changing
     */
    public static final void markCompletedRows(final Board board)
    {
        //check each row
        for (int row = 0; row < Board.ROWS; row++)
        {
            //if the current row is complete, change piece type for a blocks in the row
            if (hasCompletedRow(board, row))
            {
                //mark all columns cleared
                for (int col = 0; col < Board.COLS; col++)
                {
                    board.getBlock(col, row).setType(Piece.Type.Cleared);
                }
            }
        }
    }
    
    /**
     * Get the count of completed rows
     * @param board The board we want to check
     * @return The number of rows that have a block in every column
     */
    public static final int getCompletedRowCount(final Board board)
    {
        //keep track of the # of rows
        int count = 0;
        
        //check each row
        for (int row = 0; row < Board.ROWS; row++)
        {
            //if the current row is complete, increase count
            if (hasCompletedRow(board, row))
                count++;
        }
        
        //return the result
        return count;
    }
    
    /**
     * Move all blocks in the specified row to the row below
     * @param row The row containing blocks we want to move south
     */
    private static void dropRow(final Board board, final int row)
    {
        for (int col = 0; col < board.getBlocks()[0].length; col++)
        {
            if (board.getBlock(col, row) != null)
            {
                //get the block at this location
                Block block = board.getBlock(col, row);
                
                //make sure the row location is updated
                block.setRow(row + 1);
                
                //move the block to the row below
                board.setBlock(col, row + 1, block);
                
                //remove the previous block
                board.setBlock(col, row, null);
            }
        }
    }
    
    /**
     * Drop the blocks of all rows that have blocks but have 0 blocks underneath
     * @param board The board we want to check
     */
    public static final void dropBlocks(final Board board)
    {
        //do we check all rows
        boolean check = true;
        
        while(check)
        {
            //no longer check all rows
            check = false;
            
            //start at the bottom and move north
            for (int row = board.getBlocks().length - 2; row >= 0; row--)
            {
                //if the current row is not empty and the one below it is
                if (!hasEmptyRow(board, row) && hasEmptyRow(board, row + 1))
                {
                    //move the blocks in the current row to the row below
                    dropRow(board, row);
                    
                    //we need to check the rows again
                    check = true;
                }
            }
        }
    }
    
    /**
     * Is the specified row empty
     * @param row The row we want to check
     * @return true if all columns are empty for the given row, false otherwise
     */
    private static boolean hasEmptyRow(final Board board, final int row)
    {
        for (int col = 0; col < board.getBlocks()[0].length; col++)
        {
            //if a block exists the row is not empty
            if (board.getBlock(col, row) != null)
                return false;
        }
        
        //all blocks are empty return true
        return true;
    }
}