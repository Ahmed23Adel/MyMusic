package com.example.mymusic_final.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mymusic_final.Pojo.Music_item;
import com.example.mymusic_final.View.Music_details;

import java.util.List;

public class Music_player extends IntentService {


    private static Integer position;
    private static List<Music_item> listOfSongs;
    private static AudioManager audioManager;
    private static MediaPlayer mediaPlayer;

    final AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener= new AudioManager.OnAudioFocusChangeListener() {
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

    public Music_player() {
        super("PlayerService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        position= Music_details.position;
        listOfSongs= Music_details.listOfSongs;

        Log.v("main","1");
       audioManager= (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        int result=audioManager.requestAudioFocus(mAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);

        if (result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            Log.v("main","2");
            mediaPlayer=MediaPlayer.create(this, Uri.parse(listOfSongs.get(position).getPath()));
            mediaPlayer.start();
        }
    }

    public static void changeState(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }else {
            mediaPlayer.start();
        }
    }
    public static boolean isPlaying(){
        if (mediaPlayer==null){
            return false;
        }
        return mediaPlayer.isPlaying();
    }



}
