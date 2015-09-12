package com.gamesbykevin.fallingblocks.board;

import com.gamesbykevin.fallingblocks.board.piece.Block;

/**
 * This class will contain helper methods
 * @author GOD
 */
public final class BoardHelper 
{
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