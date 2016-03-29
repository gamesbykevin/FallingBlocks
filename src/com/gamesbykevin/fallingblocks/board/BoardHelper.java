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
import java.util.UUID;

/**
 * This class will contain helper methods
 * @author GOD
 */
public final class BoardHelper 
{
	/**
	 * The limit of challenge blocks for each row
	 */
	private static final int CHALLENGE_BLOCKS_PER_ROW = 6;
	
	/**
	 * The row where we start adding challenge blocks
	 */
	private static final int CHALLENGE_BLOCK_START_ROW = 4;
	
    /**
     * No height value
     */
    private static final int NO_HEIGHT = 0;
    
    /**
     * The location of the cleared block on the sprite sheet
     */
    private static final int CLEARED_BLOCK_COL = 4;
    
    /**
     * The location of the cleared block on the sprite sheet
     */
    private static final int CLEARED_BLOCK_ROW = 1;
    
    /**
     * The location of the challenge block on the sprite sheet
     */
    private static final int CHALLENGE_BLOCK_COL = 5;
    
    /**
     * The location of the challenge block on the sprite sheet
     */
    private static final int CHALLENGE_BLOCK_ROW = 1;
    
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
            if (Piece.ignoreType(type))
                continue;
            
            //pick random index
            final int index = GamePanel.RANDOM.nextInt(options.size());
            
            //calculate starting coordinates
            final int x = (int)options.get(index).getCol() * Block.DIMENSION_ANIMATION;
            final int y = (int)options.get(index).getRow() * Block.DIMENSION_ANIMATION;

            //create a new animation
            animation = new Animation(Images.getImage(Assets.ImageGameKey.Blocks), x, y, Block.DIMENSION_ANIMATION, Block.DIMENSION_ANIMATION);
            
            //add animation to sprite sheet
            board.getSpritesheet().add(type, animation);
            
            //remove option from list
            options.remove(index);
        }
        
        //here is where the cleared animation is
        int x = (CLEARED_BLOCK_COL * Block.DIMENSION_ANIMATION);
        int y = (CLEARED_BLOCK_ROW * Block.DIMENSION_ANIMATION);
        
        //add the cleared animation finally
        board.getSpritesheet().add(Piece.Type.Cleared, new Animation(Images.getImage(Assets.ImageGameKey.Blocks), x, y, Block.DIMENSION_ANIMATION, Block.DIMENSION_ANIMATION));
        
        //here is where the challenge animation is
        x = (CHALLENGE_BLOCK_COL * Block.DIMENSION_ANIMATION);
        y = (CHALLENGE_BLOCK_ROW * Block.DIMENSION_ANIMATION);
        
        //add the challenge animation finally
        board.getSpritesheet().add(Piece.Type.Challenge, new Animation(Images.getImage(Assets.ImageGameKey.Blocks), x, y, Block.DIMENSION_ANIMATION, Block.DIMENSION_ANIMATION));
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
        	//make sure block exists and we aren't supposed to ignore
            if (board.getBlock(col, row) != null && !Piece.ignoreType(board.getBlock(col, row).getType()))
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
                //if the current row is not empty and the one below it is and the one below doesn't have any challenge blocks
                if (!hasEmptyRow(board, row) && hasEmptyRow(board, row + 1) && getCountChallenge(board, row + 1) < 1)
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
     * Is the specified row empty?
     * @param row The row we want to check
     * @return true if all columns are empty for the given row, false otherwise
     */
    private static boolean hasEmptyRow(final Board board, final int row)
    {
        for (int col = 0; col < board.getBlocks()[0].length; col++)
        {
            //if a block exists the row is not empty
            if (board.getBlock(col, row) != null && !Piece.ignoreType(board.getBlock(col, row).getType()))
                return false;
        }
        
        //all blocks are empty return true
        return true;
    }
    
    /**
     * Populate the board for the challenge mode
     * @param board The board we want to change
     * @param levelIndex The current assigned level
     */
    public static final void populateChallenge(final Board board, final int levelIndex)
    {
    	//the number of challenge blocks allowed
    	final int limit = (levelIndex + 1) * 2;
    	
    	//the maximum amount of challenge blocks possible
    	final int max = (Board.ROWS - CHALLENGE_BLOCK_START_ROW) * CHALLENGE_BLOCKS_PER_ROW;
    	
    	//create list of available rows where we can spawn challenge blocks
    	List<Integer> options = new ArrayList<Integer>();
    	
    	//what is our limit
    	int end = Board.ROWS - ((int)(limit / CHALLENGE_BLOCKS_PER_ROW) + 2);
    	
    	//make sure we stay within bounds
    	if (end < CHALLENGE_BLOCK_START_ROW)
    		end = CHALLENGE_BLOCK_START_ROW;
    	
    	//populate list of possible rows
    	for (int row = Board.ROWS - 1; row >= end; row--)
    	{
    		options.add(row);
    	}
    	
    	//continue until we met our limit or the max allowed or we run out of rows to place blocks
    	while (getCountChallenge(board) < limit && getCountChallenge(board) < max && !options.isEmpty())
    	{
    		//pick a random index
    		final int index = GamePanel.RANDOM.nextInt(options.size());
    		
    		//get that row
    		final int row = options.get(index);
    		
    		//if a single row has reached the limit
    		if (getCountChallenge(board, row) >= CHALLENGE_BLOCKS_PER_ROW)
    		{
    			//remove that row
    			options.remove(index);
    		}
    		else
    		{
    			//possible columns to choose from
    			final List<Integer> columns = new ArrayList<Integer>();
    			
    			for (int col = 0; col < Board.COLS; col++)
    			{
    				//if there is no block it is a possibility
    				if (board.getBlocks()[row][col] == null)
    					columns.add(col);
    			}
    			
    			//pick random column
    			final int column = columns.get(GamePanel.RANDOM.nextInt(columns.size()));
    			
    			//create our challenge block
    			Block block = new Block(column, row, UUID.randomUUID(), Piece.Type.Challenge);
    			
    			//set the block dimension(s)
    			block.setWidth(Block.DIMENSION_LARGE);
    			block.setHeight(Block.DIMENSION_LARGE);
    			
    			//place challenge block at our chosen location
    			board.setBlock(column, row, block);
    		}
    	}
    }
    
    /**
     * Count the challenge blocks
     * @param board The board we want to check
     * @return The total number of challenge blocks
     */
    public static final int getCountChallenge(final Board board)
    {
    	//track the count
    	int count = 0;
    	
    	for (int row = 0; row < board.getBlocks().length; row++)
    	{
    		count += getCountChallenge(board, row);
    	}
    	
    	//return result
    	return count;
    }
    
    /**
     * Count the challenge blocks
     * @param board The board we want to check
     * @param row The row we want to count
     * @return The total number of challenge blocks
     */
    public static final int getCountChallenge(final Board board, final int row)
    {
    	//track the count
    	int count = 0;
    	
    	//check each column
		for (int col = 0; col < board.getBlocks()[0].length; col++)
		{
			//if block exists and is a challenge add to our count
			if (board.getBlock(col, row) != null && 
				board.getBlock(col, row).getType() == Piece.Type.Challenge)
				count++;
		}
    	
    	//return result
    	return count;
    }
    
    /**
     * Add penalty to the board.<br>
     * Here we will push up all blocks by 1 row and add a row of random blocks at the bottom
     * @param board Board we want to add penalty to
     * @param human Is this player human? needed to determine block dimensions
     * @param penalty The number of lines penalized
     */
    public static final void addPenalty(final Board board, final boolean human, int penalty)
    {
    	while (penalty > 0)
    	{
    		//if there are already blocks at the first row, the game will be over
    		if (!hasEmptyRow(board, 0))
    			board.setGameover(true);
    		
	    	//check each row
	    	for (int row = 1; row < board.getBlocks().length; row++)
	    	{
	        	//check each column
	    		for (int col = 0; col < board.getBlocks()[0].length; col++)
	    		{
	                //get the block at the current location
	                Block block = board.getBlock(col, row);
	                
	                //make sure the block exists before updating
	                if (block != null)
	                {
		                //make sure the new row location is updated
		                block.setRow(row - 1);
		    			
						//now assign block to the correct row
						board.setBlock(col, row - 1, block);
						
						//finally remove it from the current
						board.setBlock(col, row, null);
	                }
	    		}
	    	}
	    	
			//possible columns to choose from
			final List<Integer> columns = new ArrayList<Integer>();
			
			//add columns to list that we will pick from
			for (int col = 0; col < Board.COLS; col++)
			{
				columns.add(col);
			}
	    	
			//continue until we have met the blocks per row requirement
			while (columns.size() > Board.COLS - CHALLENGE_BLOCKS_PER_ROW)
			{
				//pick random index
				final int index = GamePanel.RANDOM.nextInt(columns.size());
				
				//pick random column
				final int column = columns.get(index);
				
				//block will be added to the last row
				final int row = Board.ROWS - 1;
				
				//create our challenge block
				Block block = new Block(column, row, UUID.randomUUID(), Piece.Type.Challenge);
				
				//dimensions will be different if human
				if (human)
				{
					block.setWidth(Block.DIMENSION_REGULAR);
					block.setHeight(Block.DIMENSION_REGULAR);
				}
				else
				{
					block.setWidth(Block.DIMENSION_SMALL);
					block.setHeight(Block.DIMENSION_SMALL);
				}
				
				//place challenge block at our chosen location
				board.setBlock(column, row, block);
				
				//remove column from our list
				columns.remove(index);
		 	}
			
			//take away 1 penalty
			penalty--;
    	}
   }
}