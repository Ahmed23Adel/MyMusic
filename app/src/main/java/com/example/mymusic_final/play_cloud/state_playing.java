package com.example.mymusic_final.play_cloud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.example.mymusic_final.Pojo.Music_item;

import java.util.List;

public class state_playing extends Player_state {

    @SuppressLint("StaticFieldLeak")


    @Override
    public void playAtPosition(int position) {
        releaseMediaPlayer();
        int result=audioManager.requestAudioFocus(mAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);
        if (result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            Uri currentUri=Uri.parse(listOfSongs.get(position).getPath());
            mediaPlayer=MediaPlayer.create(mContext,currentUri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Music_player.getCurrentStateRepeatAndFinish().Next();
                }
            });
        }
    }

    @Override
    public void playNext() {
        releaseMediaPlayer();
        Music_player.position+=1;
        int result=audioManager.requestAudioFocus(mAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);
        if (result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            Uri currentUri=Uri.parse(listOfSongs.get(position).getPath());
            mediaPlayer=MediaPlayer.create(mContext,currentUri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Music_player.getCurrentStateRepeatAndFinish().Next();
                }
            });
        }
    }

    @Override
    public void playPrevious() {
        Music_player.position-=1;
        int result=audioManager.requestAudioFocus(mAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);
        if (result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            Uri currentUri=Uri.parse(listOfSongs.get(position).getPath());
            mediaPlayer=MediaPlayer.create(mContext,currentUri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Music_player.getCurrentStateRepeatAndFinish().Next();
                }
            });
        }
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
        Music_player.setCurrentState(Music_player.state_paused);
    }

    @Override
    public void stop() {
        mediaPlayer.stop();
        releaseMediaPlayer();
        Music_player.setCurrentState(Music_player.state_stopped);
    }

    @Override
    public boolean isPlaying() {
        return true;
    }

    @Override
    void continuePlaying() {
        Log.v("main","I'ts already playing");
    }


}
