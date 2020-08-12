package com.example.mymusic_final.play_cloud;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mymusic_final.Pojo.Music_item;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Music_player extends Service {

    private static Music_player self=null;
    public static Music_player getObject(){
        return self;
    }

    public static MediaPlayer mediaPlayer;
    public static AudioManager audioManager;
    public static int position;
    public static ArrayList<Music_item> listOfSongs;
    public static Context mContext;

    public static Player_state state_idle = new state_idle();
    public static Player_state state_paused = new state_paused();
    public static Player_state state_playing = new state_playing();
    public static Player_state state_stopped = new state_stopped();
    public static Player_state currentState=state_idle;


    public static void setPosition(int position) {
        Music_player.position = position;
    }

    public static void setListOfSongs(ArrayList<Music_item> listOfSongs) {
        Music_player.listOfSongs = listOfSongs;
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



    static AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener;

    public Music_player() {
        //super("PlayerService");
    }

    /*@Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mContext = this;
    }*/

    public static void init(){

    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("main","1");
        //currentState = state_idle;
        setStateFinishAndRepeat(false, false, false);
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);


        mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
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

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        mContext = this;
        Log.v("main","2");

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mContext = this;
        Log.v("main","3");

        return null;
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        mContext = this;
        super.onStart(intent, startId);
    }

    public static void playAtPosition(int position) {
        Log.v("main","4");

        currentState.playAtPosition(position);
    }

    public static void playNext() throws Exception {
        currentState.playNext();
    }

    public static void playPrevious() throws Exception {
        currentState.playPrevious();
    }

    public static void pause() throws Exception {
        currentState.pause();
    }

    public static void stop() {
        currentState.stop();
    }

    public static boolean isPlaying() {
        return currentState.isPlaying();
    }

    public static void continuePlaying() throws Exception {
        currentState.continuePlaying();
    }

}
