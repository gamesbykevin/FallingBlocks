package com.gamesbykevin.fallingblocks.screen;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.fallingblocks.FallingBlocks;
import com.gamesbykevin.fallingblocks.assets.Assets;
import com.gamesbykevin.fallingblocks.game.Game;

/**
 * Our main menu
 * @author ABRAHAM
 */
public class MenuScreen implements Screen, Disposable
{
    //the buttons in our menu
    private Button exitGame, singlePlayer, vsCpu, moreGames;
    
    //our main screen reference
    private final MainScreen screen;
    
    public MenuScreen(final MainScreen screen)
    {
        this.screen = screen;
        
        /*
        if (this.exitGame == null)
        {
            this.exitGame = new Button(Assets.getImage(Assets.ImageKey.Button_ExitGame));
            this.exitGame.setX(180);
            this.exitGame.setY(600);
        }
        */
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            screen.setState(MainScreen.State.Running);
            
            screen.createGame();
            
            /*
            if (exitGame.hasBoundary(x, y))
            {
                screen.getPanel().getActivity().finish();
            }
            else if (newGame1Player.hasBoundary(x, y))
            {
                //set the state to running
                //screen.setState(MainScreen.State.Running);

                //create the game
                //screen.createGame(Game.Mode.SinglePlayer);
            }
            else if (moreGames.hasBoundary(x, y))
            {
                //open web page
                screen.getPanel().getActivity().openWebpage(TicTacToe.WEBPAGE_URL);
            }
            else if (instructions.hasBoundary(x, y))
            {
                //open web page
                screen.getPanel().getActivity().openWebpage(TicTacToe.INSTRUCTIONS_URL);
            }
            */
        }
        
        //return true
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw the menu buttons
        //exitGame.draw(canvas);
        //moreGames.draw(canvas);
    }
    
    @Override
    public void dispose()
    {
        this.exitGame = null;
    }
}