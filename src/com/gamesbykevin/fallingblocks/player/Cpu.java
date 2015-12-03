package com.gamesbykevin.fallingblocks.player;

import com.gamesbykevin.fallingblocks.board.Board;
import com.gamesbykevin.fallingblocks.board.BoardHelper;
import com.gamesbykevin.fallingblocks.board.piece.Piece;

/**
 * A computer controlled player
 * @author GOD
 */
public final class Cpu extends Player
{
    //do he have the destination
    private boolean destination = false;
    
    //the target column we want the ai to move to
    private int column;
    
    //the target number of rotations
    private int roationCount;
    
    //aggregate height score weight
    private static final double WEIGHT_AGGREGATE_HEIGHT = -0.66569;
    
    //completed lines score weight
    private static final double WEIGHT_COMPLETED_LINES = 0.99275;
    
    //holes score weight
    private static final double WEIGHT_HOLES = -0.46544;
    
    //bumpiness score weight
    private static final double WEIGHT_BUMPINESS = -0.24077;
    
    public Cpu(final boolean multiplayer) throws Exception
    {
        super(multiplayer, false);
    }
    
    @Override
    public void update() throws Exception
    {
        //update common elements
        super.update();
        
        //if the current piece does not exist, we don't have a destination
        if (getCurrent() == null)
        {
            setDestination(false);
        }
        else
        {
            //do we have the destination for the current piece
            if (!hasDestination())
            {
                //locate the best position
                calculateDestination();
            }
            else
            {
                //if we are not at our target rotation yet
                if (getCurrent().getRotation() != getTargetRotation())
                {
                    //rotate piece
                    setAction(Action.MOVE_ROTATE);
                }
                else if (getCurrent().getCol() < getTargetColumn())
                {
                    //if we are short of our destination move east
                    setAction(Action.MOVE_RIGHT);
                }
                else if (getCurrent().getCol() > getTargetColumn())
                {
                    //if we are past our destination move west
                    setAction(Action.MOVE_LEFT);
                }
                else
                {
                    /**
                     * The piece will drop here, don't need to do anything.<br>
                     * We can force the piece to drop if single player cpu to speed up the game
                     */
                    if (!super.isMultiPlayer())
                        setAction(Action.MOVE_DOWN);
                }
            }
        }
    }
    
    /**
     * Here we determine the best place to put the block piece.<br>
     * We will implement a scoring algorithm.<br>
     * This will locate the position with the highest score as our destination
     */
    private void calculateDestination() throws Exception
    {
        //get current location
        final int originalCol = (int)getCurrent().getCol();
        final int originalRow = (int)getCurrent().getRow();
        
        //store the score to beat
        double score = 0;
        
        //in cases the score may be below 0 make sure we at least assign for the first score
        boolean initialScore = true;
        
        //check each rotation
        for (int count = 0; count < Piece.TOTAL_ROTATIONS; count++)
        {
            //rotate piece clockwise each time
            getCurrent().rotateClockwise();
            
            //check each location
            for (int col = 0; col < Board.COLS; col++)
            {
                for (int row = 0; row < Board.ROWS; row++)
                {
                    //set the starting point
                    getCurrent().setCol(col);
                    getCurrent().setRow(row);
                    
                    //if the piece is not in bounds here, continue
                    if (!getCurrent().hasBounds())
                        continue;
                    
                    //check until we hit floor or another block
                    if (getCurrent().hasRow(Board.ROWS - 1) || getBoard().hasBlock(getCurrent()))
                    {
                        //if we hit a block move up 1 row
                        if (getBoard().hasBlock(getCurrent()))
                            getCurrent().decreaseRow();
                        
                        //if the piece is located above the top of the board
                        if (getCurrent().hasRow(-1))
                            continue;
                        
                        //if we still don't have bounds, continue
                        if (!getCurrent().hasBounds())
                            continue;
                        
                        //if a block already exists at the location of the place
                        if (getBoard().hasBlock(getCurrent()))
                            continue;
                        
                        //add piece to board
                        getBoard().addTemp(getCurrent());
                        
                        //what is the score for placing the piece here
                        double tmpScore = 0;
                        
                        //calculate/add aggregate height score
                        tmpScore += (WEIGHT_AGGREGATE_HEIGHT * BoardHelper.getAggregateHeight(getBoard()));
                        
                        //calculate/add completed row(s) score
                        tmpScore += (WEIGHT_COMPLETED_LINES * BoardHelper.getCompletedRowCount(getBoard()));
                        
                        //calculate/add hole count score
                        tmpScore += (WEIGHT_HOLES * BoardHelper.getHoleCount(getBoard()));
                        
                        //calculate/add bumpiness score
                        tmpScore += (WEIGHT_BUMPINESS * BoardHelper.getTotalBumpiness(getBoard()));

                        //if this score is better than our high score, or we need to set the score to beat
                        if (tmpScore > score || initialScore)
                        {
                            //we now have the initial score set
                            initialScore = false;
                            
                            //set the new high score
                            score = tmpScore;
                            
                            //set our target rotation
                            setTargetRotation(getCurrent().getRotation());
                            
                            //set the column we want to place the piece
                            setTargetColumn(col);
                        }
                        
                        //now that we are done scoring we can remove the piece from the board
                        getBoard().remove(getCurrent());
                        
                        
                        
                        //no need to check any further rows, exit loop to check the next column
                        break;
                    }
                }
            }
        }
        
        //now restore location back
        getCurrent().setCol(originalCol);
        getCurrent().setRow(originalRow);
        
        //flag that we have a destination
        setDestination(true);
    }
    
    /**
     * Set the number of rotations for placing our piece
     * @param rotations The total number of rotations needed to get to our goal
     */
    private void setTargetRotation(final int roationCount)
    {
        this.roationCount = roationCount;
    }
    
    /**
     * Get the number of rotations for placing our piece
     * @return The total number of rotations needed to get to our goal
     */
    private int getTargetRotation()
    {
        return this.roationCount;
    }
    
    /**
     * Set the target column for placing our piece
     * @param column The column we want to put our piece
     */
    private void setTargetColumn(final int column)
    {
        this.column = column;
    }
    
    /**
     * Get the target column for placing our piece
     * @return The column we want to put our piece
     */
    private int getTargetColumn()
    {
        return this.column;
    }
            
    /**
     * Do we have a destination?
     * @return true if we have the location of the piece we want to place, false otherwise
     */
    private boolean hasDestination()
    {
        return this.destination;
    }
    
    /**
     * Set flag if we have a destination
     * @param destination true if we have found a destination, false otherwise
     */
    private void setDestination(final boolean destination)
    {
        this.destination = destination;
    }
}