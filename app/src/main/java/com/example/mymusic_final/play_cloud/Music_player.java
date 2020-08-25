package com.example.mymusic_final.play_cloud;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.example.mymusic_final.Notifications.NotificationGenerator;
import androidx.annotation.Nullable;

import com.example.mymusic_final.Pojo.Music_item;
import com.example.mymusic_final.util.Constants;

import java.util.ArrayList;


public class Music_player extends Service implements Observable{


    private static Music_player self = null;

    public static Music_player getObject() {
        return self;
    }

    public static MediaPlayer mediaPlayer;
    public static AudioManager audioManager;
    public static int position;
    public static ArrayList<Music_item> listOfSongs;
    public static Context mContext;

    static AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                mediaPlayer.pause();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {

                mediaPlayer.pause();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mediaPlayer.pause();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mediaPlayer.start();
            }


        }
    };
    ;

    public static Player_state state_idle;
    public static Player_state state_paused;
    public static Player_state state_playing;
    public static Player_state state_stopped;
    public static Player_state currentState;


    public static void setPosition(int position) {
        Music_player.position = position;
    }

    public static int getPosition() {
        return position;
    }

    public static void setListOfSongs(ArrayList<Music_item> listOfSongs) {
        Music_player.listOfSongs = listOfSongs;
    }
    public static ArrayList<Music_item> getListOfSongs() {
        return listOfSongs;
    }


    public static SoundFinishRepeatShuffle_state currentStateRepeatAndFinish;

    public static void setStateFinishAndRepeat(boolean repeat, boolean shuffle, boolean noShuffle) {
        currentStateRepeatAndFinish.setState(repeat, shuffle, noShuffle);
    }
    public static SoundFinishRepeatShuffle_state getCurrentStateRepeatAndFinish() {
        return currentStateRepeatAndFinish;
    }

    public static Player_state getCurrentState() {
        return currentState;
    }

    public static void setCurrentState(Player_state currentState) {
        Music_player.currentState = currentState;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        setStateFinishAndRepeat(false, false, false);
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        mContext = this;
        state_idle = new state_idle();
        state_paused = new state_paused();
        state_playing = new state_playing();
        state_stopped = new state_stopped();
        currentState = state_idle;
        currentStateRepeatAndFinish=new SoundFinishRepeatShuffle_state();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static void playAtPosition(int position) {
        Log.v("main","4");

        Intent intent= new Intent(mContext,NotificationGenerator.class);
        intent.setAction(Constants.Notifications.SHOW_NOTIFICATION);
        mContext.startService(intent);
        currentState.playAtPosition(position);
        Music_changed();
    }

    public static void playNext() throws Exception {
        currentState.playNext();
        Music_changed();
    }

    public static void playPrevious() throws Exception {
        currentState.playPrevious();
        Music_changed();
    }

    public static void pause() throws Exception {
        currentState.pause();
        Music_changed();
    }

    public static void stop() {
        currentState.stop();
        Music_changed();
    }
    public static void seekTo(int s)throws Exception{
        currentState.seekTo(s);
        Music_changed();
    }

    public static boolean isPlaying() {
        return currentState.isPlaying();
    }

    public static void continuePlaying() throws Exception {
        currentState.continuePlaying();
        Music_changed();
    }


    public static void Music_changed(){
        Observable.notifyObservers(getListOfSongs(),position);
    }
}
