package com.example.mymusic_final.play_cloud;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

public class state_paused extends Player_state {


    @Override
    public void playAtPosition(int position) {
        releaseMediaPlayer();
        int result=audioManager.requestAudioFocus(mAudioFocusChangeListener, AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);
        if (result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            Uri currentUri=Uri.parse(Music_player.getListOfSongs().get(position).getPath());
            mediaPlayer= MediaPlayer.create(mContext,currentUri);
            mediaPlayer.start();
            Music_player.setCurrentState(Music_player.state_playing);
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
        Music_player.setPosition(Music_player.getPosition()+1);
        int result = audioManager.requestAudioFocus(mAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Uri currentUri = Uri.parse(Music_player.getListOfSongs().get(Music_player.getPosition()).getPath());
            mediaPlayer = MediaPlayer.create(mContext, currentUri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Music_player.getCurrentStateRepeatAndFinish().Next();
                }
            });
        }
        Music_player.setCurrentState(Music_player.state_playing);
    }

    @Override
    public void playPrevious() {
        releaseMediaPlayer();
        Music_player.setPosition(Music_player.getPosition()-1);
        int result = audioManager.requestAudioFocus(mAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Uri currentUri = Uri.parse(Music_player.getListOfSongs().get(Music_player.getPosition()).getPath());
            mediaPlayer = MediaPlayer.create(mContext, currentUri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Music_player.getCurrentStateRepeatAndFinish().Next();
                }
            });
        }
        Music_player.setCurrentState(Music_player.state_playing);

    }

    @Override
    public void pause() {
        Log.v("main","it's already paused");
    }

    @Override
    public void stop() {
        Music_player.mediaPlayer.stop();
        releaseMediaPlayer();
        Music_player.setCurrentState(Music_player.state_stopped);
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    void continuePlaying() {
        mediaPlayer.start();
        Music_player.setCurrentState(Music_player.state_playing);
    }

    @Override
    void seekTo(int s) throws Exception {
        mediaPlayer.seekTo(s);
    }
}
