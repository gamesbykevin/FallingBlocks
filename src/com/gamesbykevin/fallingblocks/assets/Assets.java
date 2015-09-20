package com.gamesbykevin.fallingblocks.assets;

import android.app.Activity;

import com.gamesbykevin.androidframework.resources.*;

/**
 * This class will contain our game assets
 * @author ABRAHAM
 */
public class Assets
{
    /**
     * The directory where audio sound effect resources are kept
     */
    private static final String DIRECTORY_AUDIO = "audio";
    
    /**
     * The directory where image resources are kept
     */
    private static final String DIRECTORY_IMAGE = "image";
    
    /**
     * The directory where font resources are kept
     */
    private static final String DIRECTORY_FONT = "font";
    
    /**
     * The different fonts used in our game.<br>
     * Order these according to the file name in the "font" assets folder.
     */
    public enum FontKey
    {
        Default
    }
    
    /**
     * The different images in our game.<br>
     * Order these according to the file name in the "image" assets folder.
     */
    public enum ImageKey
    {
        SettingsBack,
        Background, 
        SettingsDifficulty0, 
        SettingsDifficulty1, 
        SettingsDifficulty2, 
        Control_Down,
        Control_Exit,
        ExitCancel,
        ExitConfirm,
        MenuExit,
        GameoverExit,
        Blocks,
        MenuInstructions,
        Control_Left,
        Logo,
        GameoverMainmenu, 
        SettingsMode0, 
        SettingsMode1, 
        SettingsMode2, 
        MenuMore,
        Control_Mute,
        Control_Pause,
        MenuRate,
        GameoverRestart,
        Control_Right,
        MenuSettings,
        SettingsSoundOff,
        SettingsSoundOn,
        MenuStart,
        Control_UnMute,
        Control_Up,
    }
    
    /**
     * The key of each sound in our game.<br>
     * Order these according to the file name in the "audio" assets folder.
     */
    public enum AudioKey
    {
        CompletedLine,
        GameoverLose, 
        GameoverWin,
        MusicVs,
        MusicSingle,
        PiecePlace,
        PieceRotate,
        SettingChange
    }
    
    /**
     * Load all assets
     * @param activity Object containing AssetManager needed to load assets
     * @throws Exception 
     */
    public static final void load(final Activity activity) throws Exception
    {
        //load all images
        Images.load(activity, ImageKey.values(), DIRECTORY_IMAGE);
        
        //load all fonts
        Font.load(activity, FontKey.values(), DIRECTORY_FONT);
        
        //load all audio
        Audio.load(activity, AudioKey.values(), DIRECTORY_AUDIO);
    }
    
    /**
     * Recycle assets
     */
    public static void recycle()
    {
        Images.dispose();
        Font.dispose();
        Audio.dispose();
    }
}