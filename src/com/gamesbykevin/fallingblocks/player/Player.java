package com.gamesbykevin.fallingblocks.player;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gamesbykevin.androidframework.resources.Disposable;

import com.gamesbykevin.fallingblocks.board.Board;
import com.gamesbykevin.fallingblocks.board.BoardHelper;
import com.gamesbykevin.fallingblocks.board.piece.Block;
import com.gamesbykevin.fallingblocks.board.piece.Piece;
import com.gamesbykevin.fallingblocks.panel.GamePanel;
import com.gamesbykevin.fallingblocks.thread.MainThread;

/**
 * The player in the game.<br>
 * Each player has their own board
 * @author GOD
 */
public abstract class Player implements IPlayer, Disposable
{
    //the board where pieces will fall
    private Board board;
    
    //the time of the previous piece drop
    private long time;
    
    //the current and next piece
    private Piece current, next;
    
    //paint object to render blocks, etc...
    private Paint paint;
    
    /**
     * Different action's the player can make
     */
    public enum Action
    {
        MOVE_DOWN, MOVE_RIGHT, MOVE_LEFT, MOVE_ROTATE
    }
    
    //the desired action
    private Action action;
    
    /**
     * Create a new player
     */
    protected Player()
    {
        //create a new board
        this.board = new Board();
        
        getBoard().setX(50);
        getBoard().setY(50);
        getBoard().setWidth(Block.DIMENSION * Board.COLS);
        getBoard().setHeight(Block.DIMENSION * Board.ROWS);
        
        //store the previous piece drop time
        resetTime();
        
        //create new paint object
        this.paint = new Paint();
    }
    
    /**
     * Does the user have the assigned action
     * @param action Action to check
     * @return true = yes, otherwise false
     */
    private boolean hasAction(final Action action)
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
     * Rotate the current piece
     */
    public void rotate()
    {
        //make sure the current piece exists
        if (getCurrent() != null)
        {
            //rotate clockwise
            getCurrent().rotateClockwise();
            
            //if not in bounds, rotate backwards
            if (!getCurrent().hasBounds() || getBoard().hasBlock(getCurrent()))
                getCurrent().rotateCounterClockwise();
        }
    }
    
    /**
     * Get the paint object used to render blocks, etc....
     * @return 
     */
    private Paint getPaint()
    {
        return this.paint;
    }
    
    /**
     * Get the player's board
     * @return The players board
     */
    protected final Board getBoard()
    {
        return this.board;
    }
    
    /**
     * Create new pieces if they do not exist
     * @throws Exception 
     */
    private void createPiece() throws Exception
    {
        if (getCurrent() == null)
        {
            if (getNext() != null)
            {
                //assign to the current
                this.current = getNext();
                
                //remove the next piece reference
                removeNext();
            }
            else
            {
                //create a new piece
                this.current = new Piece(GamePanel.RANDOM, Board.START_COL, Board.START_ROW);
            }
            
            //reset timer
            resetTime();
        }
        
        if (getNext() == null)
            this.next = new Piece(GamePanel.RANDOM, Board.START_COL, Board.START_ROW);
    }
    
    /**
     * Get the current piece
     * @return The current piece in play
     */
    protected Piece getCurrent()
    {
        return this.current;
    }
    
    /**
     * Get the next piece
     * @return The next piece coming up
     */
    protected Piece getNext()
    {
        return this.next;
    }
    
    /**
     * Remove the current piece
     */
    private void removeCurrent()
    {
        this.current = null;
    }
    
    /**
     * Remove the next piece
     */
    private void removeNext()
    {
        this.next = null;
    }
    
    /**
     * Reset the timer, from the previous piece drop
     */
    private void resetTime()
    {
        setTime(System.nanoTime());
    }
    
    /**
     * Assign the previous drop time.
     * @param time The time of the previous piece drop (nanoseconds)
     */
    private void setTime(final long time)
    {
        this.time = time;
    }
    
    /**
     * The start time of the previous piece drop
     * @return The time of the previous piece drop (nanoseconds)
     */
    private long getTime()
    {
        return this.time;
    }
    
    /**
     * Update common game elements
     * @throws Exception 
     */
    public void update() throws Exception
    {
        //if there are no completed rows
        if (!getBoard().hasComplete())
        {
            //make sure we have pieces
            if (getCurrent() == null || getNext() == null)
            {
                //create the piece
                createPiece();
            }
            else
            {
                //check to see if it is time to drop a piece
                if (hasAction(Action.MOVE_DOWN) || System.nanoTime() - getTime() >= Board.COMPLETED_LINE_DELAY)
                {
                    //move the row down 1
                    getCurrent().increaseRow();
                    
                    //reset timer
                    resetTime();
                    
                    /**
                     * If the piece is not in bounds or in collision with another block
                     */
                    if (!getCurrent().hasBounds() || getBoard().hasBlock(getCurrent()))
                    {
                        //move back up 1 row
                        getCurrent().decreaseRow();
                        
                        //add piece to board
                        getBoard().add(getCurrent());
                        
                        //if there is at least 1 completed row, flag complete
                        if (BoardHelper.getCompletedRowCount(getBoard()) > 0)
                            getBoard().setComplete(true);
                        
                        //remove the current piece
                        removeCurrent();
                    }
                }
                else
                {
                    //check if we are moving
                    if (hasAction(Action.MOVE_RIGHT))
                    {
                        //remove action
                        setAction(null);
                        
                        //move the piece
                        getCurrent().increaseCol();

                        //If the piece is not in bounds or in collision with another block
                        if (!getCurrent().hasBounds() || getBoard().hasBlock(getCurrent()))
                            getCurrent().decreaseCol();
                    }
                    else if (hasAction(Action.MOVE_LEFT))
                    {
                        //remove action
                        setAction(null);
                        
                        //move the piece
                        getCurrent().decreaseCol();

                        //If the piece is not in bounds or in collision with another block
                        if (!getCurrent().hasBounds() || getBoard().hasBlock(getCurrent()))
                            getCurrent().increaseCol();
                    }
                    else if (hasAction(Action.MOVE_ROTATE))
                    {
                        //remove action
                        setAction(null);
                        
                        //rotate piece
                        rotate();
                    }
                }
            }
        }
        else
        {
            //check to see if the completed line timer is finished
            if (System.nanoTime() - getBoard().getTime() >= Board.COMPLETED_LINE_DELAY)
            {
                //get the completed lines count
                final int count = BoardHelper.getCompletedRowCount(getBoard());
                
                //if debug, print result
                if (MainThread.DEBUG)
                {
                    //print result
                    System.out.println("Completed lines: " + count);
                }
                
                //clear the rows
                BoardHelper.clearRows(getBoard());
                
                //drop the pieces (if exists)
                BoardHelper.dropBlocks(getBoard());
                
                //remove the completed line(s) flag
                getBoard().setComplete(false);
            }
        }
    }
    
    @Override
    public void dispose()
    {
        if (board != null)
        {
            board.dispose();
            board = null;
        }
        
        if (current != null)
        {
            current.dispose();
            current = null;
        }
        
        if (next != null)
        {
            next.dispose();
            next = null;
        }
        
        if (paint != null)
            paint = null;
    }
    
    
    /**
     * Render the player, including the board etc....
     * @param canvas Canvas to write pixel data to
     * @throws Exception
     */
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //make sure the board exists
        if (getBoard() != null)
        {
            //draw the board background
            getBoard().renderBackground(canvas);
            
            for (int row = 0; row < Board.ROWS; row++)
            {
                for (int col = 0; col < Board.COLS; col++)
                {
                    //get the current block
                    final Block block = getBoard().getBlock(col, row);
                    
                    //if there is no block, there is nothing to render
                    if (block == null)
                        continue;
                    
                    //calculate coordinate
                    final int x = (int)(getBoard().getX() + (block.getCol() * Block.DIMENSION));
                    final int y = (int)(getBoard().getY() + (block.getRow() * Block.DIMENSION));
                    
                    //render block
                    block.render(canvas, getBoard().getSpritesheet().get(block.getType()).getImage(), x, y);
                }
            }
            
            //make sure the current piece exists
            if (getCurrent() != null)
            {
                //calculate starting coordinate
                final int startX = (int)(getBoard().getX() + (getCurrent().getCol() * Block.DIMENSION));
                final int startY = (int)(getBoard().getY() + (getCurrent().getRow() * Block.DIMENSION));
                
                //render piece
                getCurrent().render(canvas, getBoard().getSpritesheet().get(getCurrent().getBlocks().get(0).getType()).getImage(), startX, startY);
            }
        }
    }
}