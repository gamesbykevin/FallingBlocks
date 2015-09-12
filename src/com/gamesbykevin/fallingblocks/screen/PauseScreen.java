package com.gamesbykevin.fallingblocks.screen;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.fallingblocks.assets.Assets;

/**
 * The pause screen
 * @author ABRAHAM
 */
public class PauseScreen implements Screen, Disposable
{
    //our main screen reference
    private final MainScreen screen;
    
    //the buttons on this screen
    private Button resumeGame;
    
    public PauseScreen(final MainScreen screen)
    {
        //store our parent reference
        this.screen = screen;
        
        /*
        //create the resume game button
        this.resumeGame = new Button(Assets.getImage(Assets.ImageKey.Button_ResumeGame));
        this.resumeGame.setX(180);
        this.resumeGame.setY(555);
        */
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (screen.getGame() == null)
            {
                //if the game doesn't exist yet, go to main menu
                screen.setState(MainScreen.State.Ready);
            }
            else
            {
                //resume game
                screen.setState(MainScreen.State.Running);
                /*
                if (screen.getGame().getBoard().hasGameover())
                {
                    //if the game is over, go back to the main menu
                    screen.setState(MainScreen.State.Ready);
                }
                else
                {
                    //set the state back to running
                    screen.setState(MainScreen.State.Running);
                }
                */
            }
        }
        
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //resume button
        //this.resumeGame.draw(canvas);
    }
    
    @Override
    public void dispose()
    {
        if (this.resumeGame != null)
            this.resumeGame.dispose();
    }
}