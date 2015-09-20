package com.gamesbykevin.fallingblocks.game;

import android.view.MotionEvent;
import android.graphics.Canvas;

import com.gamesbykevin.androidframework.resources.Audio;

import com.gamesbykevin.fallingblocks.assets.Assets;
import com.gamesbykevin.fallingblocks.board.Board;
import com.gamesbykevin.fallingblocks.game.controller.Controller;
import com.gamesbykevin.fallingblocks.panel.GamePanel;

import com.gamesbykevin.fallingblocks.player.*;
import com.gamesbykevin.fallingblocks.screen.MainScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * The main game logic will happen here
 * @author ABRAHAM
 */
public class Game implements IGame
{
    //our main screen object reference
    private final MainScreen screen;
    
    //the players in the game
    private List<Player> players;
    
    //the controller for our game
    private Controller controller;
    
    /**
     * The different game modes
     */
    public enum Mode
    {
        SinglePlayerHuman,
        SinglePlayerCpu,
        TwoPlayerVsCpu
    }

    //store the music reference
    private Assets.AudioKey musicKey;
    
    /**
     * The amount of health damage to apply to opponent
     */
    private static final int VS_MODE_DAMAGE = -3;
    
    /**
     * The amount of health to replenish to the player
     */
    private static final int VS_MODE_HEAL = 1;
    
    //the ratio to determine the piece fall speed
    private static final float DIFFICULTY_RATIO_EASY = 1.0f;
    private static final float DIFFICULTY_RATIO_NORMAL = 0.5f;
    private static final float DIFFICULTY_RATIO_HARD = 0.25f;
    
    //the cpu pieces will fall faster
    private static final float DIFFICULTY_CPU_EXTRA = 0.5f;
    
    /**
     * The game difficulty
     */
    public enum Difficulty
    {
        Normal,
        Hard,
        Easy
    }
    
    public Game(final MainScreen screen, final Mode mode, final Difficulty difficulty) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
        
        //create a new player
        this.players = new ArrayList<Player>();
        
        //setup the game mode
        switch (mode)
        {
            case SinglePlayerHuman:
                this.players.add(new Human(mode));
                break;
                
            case SinglePlayerCpu:
                this.players.add(new Cpu(mode));
                break;
                
            case TwoPlayerVsCpu:
                this.players.add(new Human(mode));
                this.players.add(new Cpu(mode));
                break;
        }
        
        //determine the piece drop rate
        for (Player player : getPlayers())
        {
            //setup the drop delay
            switch (difficulty)
            {
                case Easy:
                    player.setDropDelay((long)(Board.COMPLETED_LINE_DELAY * DIFFICULTY_RATIO_EASY));
                    break;
                    
                case Normal:
                    player.setDropDelay((long)(Board.COMPLETED_LINE_DELAY * DIFFICULTY_RATIO_NORMAL));
                    break;
                    
                case Hard:
                    player.setDropDelay((long)(Board.COMPLETED_LINE_DELAY * DIFFICULTY_RATIO_HARD));
                    break;
            }
            
            //the cpu pieces will fall slightly faster to make the challenge more competitive
            if (!player.isHuman())
                player.setDropDelay((int)(player.getDropDelay() * DIFFICULTY_CPU_EXTRA));
        }
        
        //create new controller
        this.controller = new Controller(this);
    }
    
    /**
     * Get the main screen object reference
     * @return The main screen object reference
     */
    public MainScreen getMainScreen()
    {
        return this.screen;
    }
    
    /**
     * Get the players
     * @return The list of players in play
     */
    public final List<Player> getPlayers()
    {
        return this.players;
    }
    
    @Override
    public void reset()
    {
        if (getPlayers() != null)
        {
            for (Player player : getPlayers())
            {
                if (player != null)
                    player.reset();
            }
            
            //make sure no existing audio
            Audio.stop();
            
            //pick random music to play
            this.musicKey = GamePanel.RANDOM.nextBoolean() ? Assets.AudioKey.Music0 : Assets.AudioKey.Music1;
            
            //play random song
            Audio.play(getMusicKey(), true);
        }
    }
    
    private Assets.AudioKey getMusicKey()
    {
        return this.musicKey;
    }
    
    /**
     * Resume playing music
     */
    public void resumeMusic()
    {
        if (getMusicKey() != null)
            Audio.play(getMusicKey(), true);
    }
    
    /**
     * Get our opponent
     * @param player The current player searching for an opponent
     * @return The player who does not have a matching id, indicating it is the other player (opponent)
     */
    public Player getOpponent(final Player player)
    {
        for (Player opponent : getPlayers())
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
    
    /**
     * 
     * @return 
     */
    public Controller getController()
    {
        return this.controller;
    }
    
    /**
     * Update the game based on the motion event
     * @param event Motion Event
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     */
    public void updateMotionEvent(final MotionEvent event, final float x, final float y)
    {
        getController().updateMotionEvent(event, x, y);
    }
    
    /**
     * Update game
     * @throws Exception 
     */
    public void update() throws Exception
    {
        for (Player player : getPlayers())
        {
            if (player != null)
            {
                //if the player has game over
                if (player.getBoard().hasGameover())
                {
                    //set the state to game over
                    screen.setState(MainScreen.State.GameOver);
                    
                    //default message
                    screen.getGameoverScreen().setMessage("Game Over");
                    
                    //if there is more than 1 player
                    if (getPlayers().size() > 1)
                    {
                        if (player.isHuman())
                        {
                            //set message
                            screen.getGameoverScreen().setMessage("Game Over, You Lose");

                            //play song
                            Audio.play(Assets.AudioKey.GameoverLose);
                        }
                        else
                        {
                            //set message
                            screen.getGameoverScreen().setMessage("Game Over, You Win");
                            
                            //play song
                            Audio.play(Assets.AudioKey.GameoverWin);
                        }
                    }
                    else
                    {
                        //play song
                        Audio.play(Assets.AudioKey.GameoverLose);
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
                    //get the opponent
                    final Player opponent = getOpponent(player);
                    
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
                    }
                }
            }
        }
    }
    
    @Override
    public void dispose()
    {
        if (players != null)
        {
            for (Player player : getPlayers())
            {
                if (player != null)
                {
                    player.dispose();
                    player = null;
                }
            }
            
            players.clear();
            players = null;
        }
        
        if (controller != null)
        {
            controller.dispose();
            controller = null;
        }
    }
    
    /**
     * Render game elements
     * @param canvas Where to write the pixel data
     * @throws Exception 
     */
    public void render(final Canvas canvas) throws Exception
    {
        //draw the player, etc....
        if (getPlayers() != null)
        {
            for (Player player : getPlayers())
            {
                if (player != null)
                    player.render(canvas);
            }
        }
        
        //draw the game controller
        if (getController() != null)
            getController().render(canvas);
    }
}