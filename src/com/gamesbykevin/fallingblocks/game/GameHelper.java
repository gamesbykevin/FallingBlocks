package com.gamesbykevin.fallingblocks.game;

import static com.gamesbykevin.fallingblocks.screen.OptionsScreen.MODE_CHALLENGE;
import static com.gamesbykevin.fallingblocks.screen.OptionsScreen.MODE_FREE;
import static com.gamesbykevin.fallingblocks.screen.OptionsScreen.MODE_VIEW_CPU;
import static com.gamesbykevin.fallingblocks.screen.OptionsScreen.MODE_VS_CPU;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.fallingblocks.assets.Assets;
import com.gamesbykevin.fallingblocks.board.Board;
import com.gamesbykevin.fallingblocks.board.BoardHelper;
import com.gamesbykevin.fallingblocks.panel.GamePanel;
import com.gamesbykevin.fallingblocks.player.Cpu;
import com.gamesbykevin.fallingblocks.player.Human;
import com.gamesbykevin.fallingblocks.player.Player;
import com.gamesbykevin.fallingblocks.player.stats.Stats;
import com.gamesbykevin.fallingblocks.screen.OptionsScreen;
import com.gamesbykevin.fallingblocks.screen.ScreenManager;
import com.gamesbykevin.fallingblocks.screen.OptionsScreen.Key;
import com.gamesbykevin.fallingblocks.storage.score.Score;

public final class GameHelper 
{
    //the ratio to determine the piece fall speed
    private static final float DIFFICULTY_RATIO_EASY = 1.0f;
    private static final float DIFFICULTY_RATIO_NORMAL = 0.5f;
    private static final float DIFFICULTY_RATIO_HARD = 0.25f;
    
    //the cpu pieces will fall faster
    private static final float DIFFICULTY_CPU_EXTRA = 0.5f;
	
    /**
     * The amount of health damage to apply to opponent
     */
    private static final int VS_MODE_DAMAGE = -3;
    
    /**
     * The amount of health to replenish to the player
     */
    private static final int VS_MODE_HEAL = 1;
    
    //level select information
    private static final int LEVEL_SELECT_COLS = 4;
    private static final int LEVEL_SELECT_ROWS = 4;
    private static final int LEVEL_SELECT_DIMENSION = 100;
    private static final int LEVEL_SELECT_PADDING = 25;
    private static final int LEVEL_SELECT_START_X = (GamePanel.WIDTH / 2) - (((LEVEL_SELECT_COLS * LEVEL_SELECT_DIMENSION) + ((LEVEL_SELECT_COLS - 1) * LEVEL_SELECT_PADDING)) / 2);
    private static final int LEVEL_SELECT_START_Y = 60;
    private static final int LEVEL_SELECT_TOTAL = 48;
    
    /**
     * Reset the game based on the current mode, difficulty, etc...
     * @param game The game reference object we want to reset
     * @throws Exception
     */
	protected static final void reset(final Game game) throws Exception
	{
    	//clear our players list
		game.getPlayers().clear();
    	
    	//update the level select
    	updateLevelSelect(game);
    	
        switch (game.getScreen().getScreenOptions().getIndex(Key.Mode))
        {
        	//free mode
	        case MODE_FREE:
	        	game.getPlayers().add(new Human(false));
	        	
	        	//flag selection
	        	game.getLevelSelect().setSelection(true);
	        	break;
	        
	        //view cpu mode
	        case MODE_VIEW_CPU:
	        	game.getPlayers().add(new Cpu(false));
	        	
	        	//flag selection
	        	game.getLevelSelect().setSelection(true);
	        	break;
	        	
	        //vs cpu mode
	        case MODE_VS_CPU:
	        	game.getPlayers().add(new Human(true));
	        	game.getPlayers().add(new Cpu(true));
	        	
	        	//flag selection
	        	game.getLevelSelect().setSelection(true);
	        	break;
	        	
	        case MODE_CHALLENGE:
	        	game.getPlayers().add(new Human(false));
	        	break;
        }
        
        //determine the piece drop rate
        for (Player player : game.getPlayers())
        {
        	switch (game.getScreen().getScreenOptions().getIndex(Key.Difficulty))
        	{
	        	//easy
	        	case 0:
	        		player.setDropDelay((long)(Board.COMPLETED_LINE_DELAY * DIFFICULTY_RATIO_EASY));
	        		break;
	        		
	        	//normal
	        	case 1:
	        		player.setDropDelay((long)(Board.COMPLETED_LINE_DELAY * DIFFICULTY_RATIO_NORMAL));
	        		break;
	        		
	        	//hard
	        	case 2:
	        		player.setDropDelay((long)(Board.COMPLETED_LINE_DELAY * DIFFICULTY_RATIO_HARD));
	        		break;
        	}
        	
            //the cpu pieces will fall slightly faster to make the challenge more competitive
            if (!player.isHuman())
                player.setDropDelay((int)(player.getDropDelay() * DIFFICULTY_CPU_EXTRA));
        }
        
        if (game.getPlayers() != null)
        {
            for (Player player : game.getPlayers())
            {
                if (player != null)
                    player.reset();
            }
        }
        
        //reset the controller
        if (game.getController() != null)
        	game.getController().reset();
        
        //if challenge mode add challenge blocks
        switch (game.getScreen().getScreenOptions().getIndex(Key.Mode))
        {
	        case OptionsScreen.MODE_CHALLENGE:
	        	//fill up board
	        	BoardHelper.populateChallenge(game.getHuman().getBoard(), game.getLevelSelect().getLevelIndex());
	        	
	        	//set text description
	        	game.getHuman().getStats().setStatDescription(Stats.CHALLENGE_STAT_DESCRIPTION);
	        	
    			//update stats
	        	game.getHuman().getStats().setLines(BoardHelper.getCountChallenge(game.getHuman().getBoard()));
	        	break;
        }
	}
	
	/**
	 * Setup the level select object
	 * @param game Game reference object
	 */
	protected static final void setupLevelSelect(final Game game)
	{
        game.getLevelSelect().setButtonNext(new Button(Images.getImage(Assets.ImageGameKey.PageNext)));
        game.getLevelSelect().setButtonOpen(new Button(Images.getImage(Assets.ImageGameKey.LevelOpen)));
        game.getLevelSelect().setButtonLocked(new Button(Images.getImage(Assets.ImageGameKey.LevelLocked)));
        game.getLevelSelect().setButtonPrevious(new Button(Images.getImage(Assets.ImageGameKey.PagePrevious)));
        game.getLevelSelect().setButtonSolved(new Button(Images.getImage(Assets.ImageGameKey.LevelComplete)));
        game.getLevelSelect().setCols(LEVEL_SELECT_COLS);
        game.getLevelSelect().setRows(LEVEL_SELECT_ROWS);
        game.getLevelSelect().setDimension(LEVEL_SELECT_DIMENSION);
        game.getLevelSelect().setPadding(LEVEL_SELECT_PADDING);
        game.getLevelSelect().setStartX(LEVEL_SELECT_START_X);
        game.getLevelSelect().setStartY(LEVEL_SELECT_START_Y);
        game.getLevelSelect().setTotal(LEVEL_SELECT_TOTAL);
	}
	
    /**
     * Update the level select object to flag completed levels and locked levels
     */
    protected static final void updateLevelSelect(final Game game)
    {
        //load the saved data
        for (int levelIndex = game.getLevelSelect().getTotal() - 1; levelIndex >= 0; levelIndex--)
        {
        	//get the score for the specified level and colors
        	Score score = game.getScorecard().getScore(levelIndex, game.getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Mode));
        	
        	//if a score exists
        	if (score != null)
        	{
        		//mark this level as completed
        		game.getLevelSelect().setCompleted(levelIndex, true);
        		
        		//mark this level as not locked
        		game.getLevelSelect().setLocked(levelIndex, false);
        		
        		//also make sure the next level is not locked as well
        		if (levelIndex < game.getLevelSelect().getTotal() - 1)
        			game.getLevelSelect().setLocked(levelIndex + 1, false);
        	}
        	else
        	{
        		//mark this level as locked
        		//game.getLevelSelect().setLocked(levelIndex, true);
        		game.getLevelSelect().setLocked(levelIndex, false);
        		
        		//mark this level as not completed
        		game.getLevelSelect().setCompleted(levelIndex, false);
        	}
        }
        
    	//the first level can never be locked
        game.getLevelSelect().setLocked(0, false);
    }
    
    /**
     * Update the game
     * @param game The game object reference
     * @throws Exception
     */
    protected static final void update(final Game game) throws Exception
    {
    	if (!game.getLevelSelect().hasSelection())
    	{
    		//update the object
    		game.getLevelSelect().update();
    		
    		//if we have a selection now, reset the board
    		if (game.getLevelSelect().hasSelection())
    		{
    			//make sure the level is not locked, if it is locked play sound effect
    			if (game.getLevelSelect().isLocked(game.getLevelSelect().getLevelIndex()))
    			{
    				//flag selection as false
    				game.getLevelSelect().setSelection(false);
    				
    				//play sound effect
    				Audio.play(Assets.AudioGameKey.InvalidLevelSelect);
    			}
    			else
    			{
    				//reset the board for the next level
    				reset(game);
    				
    				//flag selection as true
    				game.getLevelSelect().setSelection(true);
    			}
    		}
    		
    		//no need to continue
    		return;
    	}
    	else
    	{
	        for (Player player : game.getPlayers())
	        {
	            if (player != null)
	            {
	                //if the player has game over
	                if (player.getBoard().hasGameover())
	                {
	                	//vibrate phone
	                	game.vibrate();
	                	
	                    //set the state to game over
	                	game.getScreen().setState(ScreenManager.State.GameOver);
	                    
	                    //default message
	                	game.getScreen().getScreenGameover().setMessage("Game Over", false);
	                    
	                    //if there is more than 1 player
	                    if (game.getPlayers().size() > 1)
	                    {
	                        if (player.isHuman())
	                        {
	                            //set message
	                        	game.getScreen().getScreenGameover().setMessage("Game Over, You Lose", false);
	
	                            //play song
	                            Audio.play(Assets.AudioGameKey.GameoverLose);
	                        }
	                        else
	                        {
	                            //set message
	                        	game.getScreen().getScreenGameover().setMessage("Game Over, You Win", true);
	                            
	                            //play song
	                            Audio.play(Assets.AudioGameKey.GameoverWin);
	                        }
	                    }
	                    else
	                    {
	                    	//check game mode to determine what to do
	                    	switch (game.getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Mode))
	                    	{
	                    		case MODE_CHALLENGE:
	                    			if (BoardHelper.getCountChallenge(game.getHuman().getBoard()) <= 0)
	                    			{
	                    				//save level to our score card
	                    				game.getScorecard().update(game.getLevelSelect().getLevelIndex(), MODE_CHALLENGE);
	                    				
	                    				//move to the next level
	                    				game.getLevelSelect().setLevelIndex(game.getLevelSelect().getLevelIndex() + 1);
	                    				
	    	                            //set message
	    	                        	game.getScreen().getScreenGameover().setMessage("Game Over, You Win", true);
	    	                            
	    	                            //play song
	    	                            Audio.play(Assets.AudioGameKey.GameoverWin);
	                    			}
	                    			else
	                    			{
		    	                        //play song
		    	                        Audio.play(Assets.AudioGameKey.GameoverLose);
	                    			}
	                    			break;
	                    		
	                    		default:
	    	                        //play song
	    	                        Audio.play(Assets.AudioGameKey.GameoverLose);
	                    			break;
	                    	}
	                    }
	                    break;
	                }
	                
	                //store the number of lines the player has completed
	                final int lines = player.getStats().getLines();
	                
	                //update the player
	                player.update();
	                
	                //if the player has completed at least 1 line, update
	                if (lines != player.getStats().getLines())
	                {
                    	//check game mode to determine what to do
                    	switch (game.getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Mode))
                    	{
                    		case MODE_CHALLENGE:
                    			//check if all challenge blocks are removed
                    			if (BoardHelper.getCountChallenge(player.getBoard()) <= 0)
                    				player.getBoard().setGameover(true);
                    			
                    			//update stats
                    			player.getStats().setLines(BoardHelper.getCountChallenge(player.getBoard()));
                    			break;
                    			
                    		case MODE_VS_CPU:
        	                    //get the opponent
        	                    final Player opponent = getOpponent(game, player);
        	                    
        	                    //if the opponent exists
        	                    if (opponent != null)
        	                    {
        	                        //find the number of lines completed
        	                        final int difference = player.getStats().getLines() - lines;
        	                        
        	                        //calculate the total heal/damage
        	                        final int damage = difference * VS_MODE_DAMAGE;
        	                        final int heal = difference * VS_MODE_HEAL;
        	                        
        	                        //update the players
        	                        player.getStats().setHealth(player.getStats().getHealth() + heal);
        	                        opponent.getStats().setHealth(opponent.getStats().getHealth() + damage);
        	                        
        	                        //if the opponent received damage, vibrate phone
        	                        if (opponent.isHuman())
        	                        	game.vibrate();
        	                    }
                    			break;
                    	}
	                }
	            }
	        }
    	}
    }
    
    /**
     * Get our opponent
     * @param game Game reference object
     * @param player The current player searching for an opponent
     * @return The player who does not have a matching id, indicating it is the other player (opponent)
     */
    protected static final Player getOpponent(final Game game, final Player player)
    {
        for (Player opponent : game.getPlayers())
        {
            //if the id's match it is the same player, skip
            if (player.hasId(opponent.getId()))
                continue;
            
            //the id's do not match, we found our opponent
            return opponent;
        }
        
        //this should not happen
        return null;
    }    
}