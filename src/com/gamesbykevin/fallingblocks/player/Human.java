package com.gamesbykevin.fallingblocks.player;

import com.gamesbykevin.fallingblocks.game.Game;

/**
 * The human controlled player
 * @author GOD
 */
public final class Human extends Player
{
    public Human(final Game.Mode mode) throws Exception
    {
        super(mode, true);
    }
}