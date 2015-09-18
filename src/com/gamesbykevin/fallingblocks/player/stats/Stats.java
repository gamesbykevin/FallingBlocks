package com.gamesbykevin.fallingblocks.player.stats;

import com.gamesbykevin.androidframework.base.Entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * The object representing the player's statistics
 * @author GOD
 */
public final class Stats extends Entity implements IStats
{
    //number of completed lines
    private int lines = 0;
    
    //the health
    private int health = HEALTH_MAX;
    
    /**
     * How much health to start with
     */
    private static final int HEALTH_MAX = 100;
    
    /**
     * The least amount of health possible (dead)
     */
    public static final int HEALTH_MIN = 0;
    
    //paint object to render text
    private Paint paint;
    
    //text description
    private String description;
    
    /**
     * Default dimension of stat window
     */
    public static final int WIDTH = 270;
    
    /**
     * Default dimension of stat window
     */
    public static final int HEIGHT_NORMAL = 327;
    
    /**
     * Dimension of stat window to include the health bar
     */
    public static final int HEIGHT_EXTRA = 350;
    
    /**
     * When rendering the next piece it will be inside the stat window and offset the coordinates
     */
    private static final int PIECE_OFFSET_X = 50;
    
    /**
     * When rendering the next piece it will be inside the stat window and offset the coordinates
     */
    private static final int PIECE_OFFSET_Y = 150;
    
    /**
     * When rendering the health bar it will be inside the stat window
     */
    private static final int HEALTH_OFFSET_X = 10;
    
    /**
     * When rendering the health bar it will be inside the stat window
     */
    private static final int HEALTH_OFFSET_Y = 300;
    
    /**
     * Dimension of the health bar
     */
    private static final double HEALTH_WIDTH = 250;
    
    /**
     * Dimension of the health bar
     */
    private static final double HEALTH_HEIGHT = 40;
    
    //do we extend the size of the window
    private final boolean extendWindow;
    
    /**
     * Create new stats window to track the player's stats
     * @param extendWindow Do we need to make the window larger (for vs mode)
     * @param description The text description to display in the stat window
     */
    public Stats(final boolean extendWindow, final String description) 
    {
        super();
        
        //assign the text description
        setDescription(description);
        
        //assign decision
        this.extendWindow = extendWindow;
        
        if (this.extendWindow)
        {
            //add extra height for the health bar
            super.setWidth(WIDTH);
            super.setHeight(HEIGHT_EXTRA);
        }
        else
        {
            //assign default dimension
            super.setWidth(WIDTH);
            super.setHeight(HEIGHT_NORMAL);
        }
        
        //create new paint object
        this.paint = new Paint();
    }
    
    @Override
    public void reset()
    {
        setLines(HEALTH_MIN);
        setHealth(HEALTH_MAX);
    }
    
    /**
     * The location to render the next piece
     * @return The x-coordinate
     */
    public int getOffsetX()
    {
        return (int)this.getX() + PIECE_OFFSET_X;
    }
    
    /**
     * The location to render the next piece
     * @return The y-coordinate
     */
    public int getOffsetY()
    {
        return (int)this.getY() + PIECE_OFFSET_Y;
    }
    
    /**
     * 
     * @param description 
     */
    public final void setDescription(final String description)
    {
        this.description = description;
    }
    
    /**
     * Get the text description
     * @return 
     */
    private String getDescription()
    {
        return this.description;
    }
    
    /**
     * Get the player health
     * @return The player's health (will be between 0 - 100)
     */
    public int getHealth()
    {
        return this.health;
    }
    
    /**
     * Assign the health
     * @param health The desired health (will be between 0 - 100)
     */
    public void setHealth(final int health)
    {
        //assign the health
        this.health = health;
        
        //make sure we don't go below the minimum or above the maximum
        if (getHealth() < HEALTH_MIN)
            setHealth(HEALTH_MIN);
        if (getHealth() > HEALTH_MAX)
            setHealth(HEALTH_MAX);
    }
    
    /**
     * Get the paint object used to render blocks, etc....
     * @return 
     */
    public Paint getPaint()
    {
        return this.paint;
    }
    
    /**
     * Get the number of completed lines
     * @return The total number of completed lines
     */
    public int getLines()
    {
        return this.lines;
    }
    
    /**
     * Assign the number of completed lines
     * @param lines The total number of completed lines
     */
    public void setLines(final int lines)
    {
        this.lines = lines;
    }
    
    @Override
    public void dispose()
    {
        if (paint != null)
            paint = null;
    }
    
    /**
     * Render the statistics
     * @param canvas Canvas where we write pixel data to
     */
    @Override
    public void render(final Canvas canvas)
    {
        //fill background
        getPaint().setStyle(Paint.Style.FILL);
        getPaint().setColor(Color.BLACK);
        canvas.drawRect(getDestination(), getPaint());
        
        //draw border
        getPaint().setStyle(Paint.Style.STROKE);
        getPaint().setColor(Color.WHITE);
        canvas.drawRect(getDestination(), getPaint());
        
        //set font size
        getPaint().setTextSize(48f);
        
        //draw lines stat
        canvas.drawText(getDescription(), (int)getX() + 25, (int)getY() + 60, getPaint());
        
        //draw next
        canvas.drawText("Next", (int)getX() + 25, (int)getY() + 120, getPaint());
        
        //do we render the health bar
        if (this.extendWindow)
        {
            //draw health info
            canvas.drawText("Health: ", (int)getX() + 25, (int)getY() + 290, getPaint());
            
            //determine color by health
            if (getHealth() > 75)
            {
                getPaint().setColor(Color.GREEN);
            }
            else if (getHealth() > 50)
            {
                getPaint().setColor(Color.YELLOW);
            }
            else if (getHealth() > 25)
            {
                getPaint().setARGB(255, 255, 165, 0);
            }
            else
            {
                getPaint().setColor(Color.RED);
            }
            
            //fill the health bar
            getPaint().setStyle(Paint.Style.FILL);
            
            //determine the width of the health
            final int x1 = (int)(getX() + HEALTH_OFFSET_X);
            final int y1 = (int)(getY() + HEALTH_OFFSET_Y);
            final int x2 = (int)(x1 + (HEALTH_WIDTH * (getHealth() / (double)HEALTH_MAX)));
            final int y2 = (int)(y1 + HEALTH_HEIGHT);
            
            //draw the health bar
            canvas.drawRect(x1, y1, x2, y2, getPaint());
            
            //setup the outline
            getPaint().setStyle(Paint.Style.STROKE);
            getPaint().setColor(Color.WHITE);
            
            //draw the outline
            canvas.drawRect(x1, y1, (int)(x1 + HEALTH_WIDTH), (int)(y1 + HEALTH_HEIGHT), getPaint());
        }
        else
        {
            //draw lines stat
            canvas.drawText("Lines: " + getLines(), (int)getX() + 25, (int)getY() + 290, getPaint());
        }
    }
}