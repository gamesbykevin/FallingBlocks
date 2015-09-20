package com.gamesbykevin.fallingblocks.player;

import android.graphics.Canvas;

import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.fallingblocks.assets.Assets;

import com.gamesbykevin.fallingblocks.board.Board;
import com.gamesbykevin.fallingblocks.board.BoardHelper;
import com.gamesbykevin.fallingblocks.board.piece.Block;
import com.gamesbykevin.fallingblocks.board.piece.Piece;
import com.gamesbykevin.fallingblocks.game.Game;
import com.gamesbykevin.fallingblocks.player.stats.Stats;

/**
 * The player in the game.<br>
 * Each player has their own board
 * @author GOD
 */
public abstract class Player extends PlayerHelper implements IPlayer, Disposable
{
    //the board where pieces will fall
    private Board board;
    
    //the current and next piece
    private Piece current, next;
    
    //our stats object
    private Stats stats;
    
    //the game mode
    private final Game.Mode mode;
    
    /**
     * Create a new player
     * @param mode The desired game mode
     * @param human Is this player human?
     * @throws Exception
     */
    protected Player(final Game.Mode mode, final boolean human) throws Exception
    {
        super(human);
        
        //store the game mode
        this.mode = mode;
        
        //create new stats object
        this.stats = new Stats((getMode() == Game.Mode.TwoPlayerVsCpu), isHuman() ? "Player" : "Cpu");
        
        //determine the dimensions/location of the board and stat window
        switch (getMode())
        {
            case SinglePlayerCpu:
            case SinglePlayerHuman:
                
                setBlockDimension(Block.DIMENSION_LARGE);
                
                //create a new board
                this.board = new Board(getBlockDimension());
                
                //position board
                getBoard().setX(START_X);
                getBoard().setY(START_Y);

                //position stats
                getStats().setX(getBoard().getX() + getBoard().getWidth() + START_X);
                getStats().setY(getBoard());
                break;
                
            case TwoPlayerVsCpu:
                
                //determine size by human or not
                setBlockDimension((human) ? Block.DIMENSION_REGULAR : Block.DIMENSION_SMALL);
                
                //create a new board
                this.board = new Board(getBlockDimension());
                
                //the human and cpu will be placed accordingly
                if (isHuman())
                {
                    //position board
                    getBoard().setX(START_X);
                    getBoard().setY(1300 - START_Y - getBoard().getHeight());

                    //position stats
                    getStats().setX(getBoard().getX() + getBoard().getWidth() + START_X);
                    getStats().setY(getBoard().getY() + getBoard().getHeight() - getStats().getHeight());
                }
                else
                {
                    //position board
                    getBoard().setX((Block.DIMENSION_REGULAR * Board.COLS) + (START_X * 15));
                    getBoard().setY(getStats().getHeight() + (START_Y * 2));

                    //position stats
                    getStats().setX(getBoard());
                    getStats().setY(START_Y);
                }
                break;
                
            default:
                throw new Exception("Mode not setup here " + getMode().toString());
        }
        
        //setup animations now the board has been created
        BoardHelper.assignAnimations(getBoard());
        
        //store the previous piece drop time
        resetTime();
    }
    
    @Override
    public void reset()
    {
        getBoard().reset();
        getStats().reset();
        setAction(null);
        resetTime();
        this.current = null;
        this.next = null;
    }
    
    /**
     * Get the game mode
     * @return The assigned game mode
     */
    private Game.Mode getMode()
    {
        return this.mode;
    }
    
    /**
     * Get our stats object
     * @return Object with player stats
     */
    public final Stats getStats()
    {
        return this.stats;
    }
    
    /**
     * Rotate the current piece
     * @return true if the rotation was successful, false if the rotation caused block collision or out of bounds
     */
    public boolean rotate()
    {
        //make sure the current piece exists
        if (getCurrent() != null)
        {
            //rotate clockwise
            getCurrent().rotateClockwise();
            
            //if not in bounds, rotate backwards
            if (!getCurrent().hasBounds() || getBoard().hasBlock(getCurrent()))
            {
                //rotate back
                getCurrent().rotateCounterClockwise();
                
                //not successful
                return false;
            }
            
            //rotation was successfult
            return true;
        }
        
        //no rotation occurred
        return false;
    }
    
    
    /**
     * Get the player's board
     * @return The players board
     */
    public final Board getBoard()
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
                //set default start
                getNext().setCol(Board.START_COL);
                getNext().setRow(Board.START_ROW);
                
                //assign to the current
                this.current = getNext();
                
                //remove the next piece reference
                removeNext();
            }
            else
            {
                //create a new piece
                this.current = new Piece(Board.START_COL, Board.START_ROW, getBlockDimension());
            }
            
            //reset timer
            resetTime();
        }
        
        //render the next piece, if exists
        if (getNext() == null)
            this.next = new Piece(0, 0, getBlockDimension());
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
     * Update common game elements
     * @throws Exception 
     */
    public void update() throws Exception
    {
        //if there are no completed rows
        if (!getBoard().hasComplete())
        {
            //if the game is over, don't continue
            if (getBoard().hasGameover())
                return;
            
            //if we are out of health, the game is over
            if (getStats().getHealth() <= Stats.HEALTH_MIN)
            {
                //flag the game over
                getBoard().setGameover(true);
                
                //no need to continue
                return;
            }
            
            //make sure we have pieces
            if (getCurrent() == null || getNext() == null)
            {
                //create the piece
                createPiece();
            }
            else
            {
                //check to see if it is time to drop a piece
                if (hasAction(Action.MOVE_DOWN) || System.nanoTime() - getTime() >= getDropDelay())
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
                        
                        //if the piece is still in collision with the board, we have gameover
                        if (getBoard().hasBlock(getCurrent()))
                        {
                            //flag game over
                            getBoard().setGameover(true);
                            
                            //no need to continue
                            return;
                        }
                        
                        //add piece to board
                        getBoard().add(getCurrent());
                        
                        //if there is at least 1 completed row, flag complete
                        if (BoardHelper.getCompletedRowCount(getBoard()) > 0)
                        {
                            //mark blocks completed
                            BoardHelper.markCompletedRows(getBoard());
                            
                            //set the board as complete
                            getBoard().setComplete(true);
                            
                            //play sound effect
                            Audio.play(Assets.AudioKey.CompletedLine);
                        }
                        else
                        {
                            //no completed line, play place piece sound effect
                            Audio.play(Assets.AudioKey.PiecePlace);
                        }
                        
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
                        
                        //rotate and if successful
                        if (rotate())
                        {
                            //only play sound effect if human
                            if (isHuman())
                            {
                                //play sound effect
                                Audio.play(Assets.AudioKey.PieceRotate);
                            }
                        }
                    }
                }
            }
        }
        else
        {
            //check to see if the completed line timer is finished
            if (System.nanoTime() - getBoard().getTime() >= Board.COMPLETED_LINE_DELAY)
            {
                //update the completed lines count
                getStats().setLines(getStats().getLines() + BoardHelper.getCompletedRowCount(getBoard()));
                
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
        
        if (stats != null)
        {
            stats.dispose();
            stats = null;
        }
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
                    final int x = (int)(getBoard().getX() + (block.getCol() * block.getWidth()));
                    final int y = (int)(getBoard().getY() + (block.getRow() * block.getHeight()));
                    
                    //render block
                    block.render(canvas, getBoard().getSpritesheet().get(block.getType()).getImage(), x, y);
                }
            }
            
            //render the current piece, if exists
            if (getCurrent() != null)
                getCurrent().render(canvas, getBoard());
            
            //render the player stats
            if (getStats() != null)
                getStats().render(canvas);
            
            /**
             * If the next piece exists, render inside the stat window
             */
            if (getNext() != null)
            {
                //reference to scale the piece
                float scale;
                
                //determine how to scale according to game mode and human status
                switch (getMode())
                {
                    case SinglePlayerHuman:
                    case SinglePlayerCpu:
                        scale = 0.5f;
                        break;
                        
                    case TwoPlayerVsCpu:
                        scale = (isHuman()) ? 0.5f : 1.0f;
                        break;
                        
                    default:
                        throw new Exception("Mode is not setup here: " + getMode().toString());
                }
                
                getNext().render(canvas, getBoard(), scale, getStats().getOffsetX(), getStats().getOffsetY());
            }
        }
    }
}