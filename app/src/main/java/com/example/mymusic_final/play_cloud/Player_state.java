package com.example.mymusic_final.play_cloud;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;

import com.example.mymusic_final.Pojo.Music_item;

import java.util.List;

public abstract class Player_state {

    public static MediaPlayer mediaPlayer;
    static AudioManager audioManager=Music_player.audioManager;
    static  AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener =Music_player.mAudioFocusChangeListener;
    static Context mContext=Music_player.mContext;
    static List<Music_item> listOfSongs=Music_player.listOfSongs;
    static int position =Music_player.position;

    static int currentMMLeft=0;

    public static int getCurrentMMLeft() {
        return currentMMLeft;
    }

    public static void setCurrentMMLeft(int currentMMLeft) {
        Player_state.currentMMLeft = currentMMLeft;
    }

    abstract void playAtPosition(int position);
    abstract void playNext() throws Exception;
    abstract void playPrevious() throws Exception;
    abstract void pause() throws Exception;
    abstract void stop();
    abstract boolean isPlaying();
    abstract void continuePlaying() throws Exception;
    abstract void seekTo(int s) throws Exception;

    public  void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int i=audioManager.abandonAudioFocus(mAudioFocusChangeListener);
            }
        }
    }
}
