package com.gamesbykevin.fallingblocks.storage.score;

import java.util.ArrayList;
import java.util.List;

import com.gamesbykevin.androidframework.io.storage.Internal;
import com.gamesbykevin.fallingblocks.game.Game;
import com.gamesbykevin.fallingblocks.screen.OptionsScreen;

import android.app.Activity;

public class ScoreCard extends Internal 
{
    //list of scores
    private List<Score> scores;
    
    /**
     * New score separator string
     */
    private static final String NEW_SCORE = ";";
    
    /**
     * This string will separate the data for a score
     */
    private static final String SEPARATOR = "-";
    
    /**
     * The name of the score card file name where we will store the best scores
     */
    private static final String FILE_NAME = "ScoreCard";

    //our game object reference
    private final Game game;
    
	public ScoreCard(final Game game, final Activity activity, final boolean debug) 
	{
		//call to parent
        super(FILE_NAME, activity, debug);
        
        //store game object reference
        this.game = game;
        
        //create new score
        this.scores = new ArrayList<Score>();
        
        //make sure content exists before we try to load it
        if (super.getContent().toString().trim().length() > 0)
        {
            //load file with each level on a new line
            final String[] scores = super.getContent().toString().split(NEW_SCORE);

            //load each level into our array
            for (int index = 0; index < scores.length; index++)
            {
                //split level data
                String[] data = scores[index].split(SEPARATOR);

                //get the information
                final int level = Integer.parseInt(data[0]);
                final int mode = Integer.parseInt(data[1]);

                //load the score to our list
                update(level, mode);
            }
        }
    }
    
    /**
     * Do we have the score?<br>
     * We want the score of the specified level and colors
     * @param level The level index
     * @param colors The colors index
     * @return true if the specified score exists, false otherwise
     */
    public boolean hasScore(final int level, final int colors)
    {
    	return (getScore(level, colors) != null);
    }
    
    /**
     * Do we have the score?<br>
     * We will get the score of the current level and mode and see if exists
     * @return true if the specified score exists, false otherwise
     */
    public boolean hasScore()
    {
    	//use the current level index, and mode
    	return hasScore(game.getLevelSelect().getLevelIndex(), game.getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Mode));
    }
    
    /**
     * Get the score object.<br>
     * We will get the score of the current mode and level
     * @return The score object of the current level and mode, if not found null is returned
     */
    public Score getScore()
    {
    	return getScore(game.getLevelSelect().getLevelIndex(), game.getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Mode));
    }
    
    /**
     * Get the score object of the specified level and mode
     * @param level The level index
     * @param mode Mode index
     * @return The score object of the specified level and mode, if not found null is returned
     */
    public Score getScore(final int level, final int mode)
    {
    	//check our list to see if the specified score is better
    	for (Score score : scores)
    	{
    		//if the level and colors match return the score
    		if (score.getLevel() == level && score.getMode() == mode)
    			return score;
    	}
    	
    	//score was not found
    	return null;
    }
    
    /**
     * Update the score with the specified level and mode.<br>
     * If the score does not exist, it will be added.<br>
     * @param level The specified level
     * @param mode The mode index
     * @return true if updating the score was successful, false otherwise
     */
    public boolean update(final int level, final int mode)
    {
    	//if the score does not exist, add it
    	if (getScore(level, mode) == null)
    	{
    		//score does not exist, so add it
    		scores.add(new Score(level, mode));
    	}
    	else
    	{
    		//nothing was updated
    		return false;
    	}
    	
    	//save the score
    	save();
    	
    	//score was updated
    	return true;
    }
    
    /**
     * Save the scores to the internal storage
     */
    @Override
    public void save()
    {
        //remove all existing content
        super.getContent().delete(0, super.getContent().length());
        
        for (Score score : scores)
        {
            //if content exists, add delimiter to separate each score
            if (super.getContent().length() > 0)
                super.getContent().append(NEW_SCORE);
            
            //write level, size, and time
            super.getContent().append(score.getLevel());
            super.getContent().append(SEPARATOR);
            super.getContent().append(score.getMode());
        }
        
        //save the content to physical internal storage location
        super.save();
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        if (scores != null)
        {
            scores.clear();
            scores = null;
        }
    }
}