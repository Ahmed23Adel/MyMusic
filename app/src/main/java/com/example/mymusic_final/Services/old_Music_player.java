package com.example.mymusic_final.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mymusic_final.Adapter.adapter_music;
import com.example.mymusic_final.Pojo.Music_item;
import com.example.mymusic_final.View.Music_details;

import java.util.List;

@Deprecated
public class old_Music_player extends IntentService {


    public static Integer position;
    public static List<Music_item> listOfSongs;
    private static AudioManager audioManager;
    private static MediaPlayer mediaPlayer;
    private static Context mContext;
    public static  OnPlayChanged onPlayChanged;

    final static AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener= new AudioManager.OnAudioFocusChangeListener() {
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

    public old_Music_player() {
        super("PlayerService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        audioManager= (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        mContext= this;
        playMusicAtPosition(position);

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

    public static void playNext(){
        if (position==listOfSongs.size()-1){
            position=0;
            playMusicAtPosition(position);
            onPlayChanged.updatedTo(position);
        }else{
            position=position+1;
            playMusicAtPosition(position);
            onPlayChanged.updatedTo(position);
        }

    }

    public static void playPrevious(){
        if (position==0){
            position=listOfSongs.size()-1;
            playMusicAtPosition(position);
            onPlayChanged.updatedTo(position);
        }else{
            position=position-1;
            playMusicAtPosition(position);
            onPlayChanged.updatedTo(position);
        }

    }

    private static void playMusicAtPosition(int position){
        if (mediaPlayer!=null){
            mediaPlayer.release();
        }
        int result=audioManager.requestAudioFocus(mAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);

        if (result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            mediaPlayer=MediaPlayer.create(mContext, Uri.parse(listOfSongs.get(position).getPath()));
            mediaPlayer.start();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                releaseMediaPlayer();
            }
        });
      //  adapter_music.getListener().onItemClick(listOfSongs,position);
    }

    private static void releaseMediaPlayer() {
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
    public interface OnPlayChanged{
        void updatedTo(int position);
    }

    public static void setOnPlayChanged(OnPlayChanged onPlayChanged2){
        onPlayChanged=onPlayChanged2;
    }

    public static Integer getPosition() {
        return position;
    }

    public static List<Music_item> getListOfSongs() {
        return listOfSongs;
    }
}
