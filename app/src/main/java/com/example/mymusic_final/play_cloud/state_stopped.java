package com.example.mymusic_final.play_cloud;

import android.util.Log;

public class state_stopped extends Player_state {


    @Override
    public void playAtPosition(int position) {
        Music_player.setCurrentState(Music_player.state_playing);
        Music_player.getCurrentState().playAtPosition(position);
    }

    @Override
    public void playNext() throws Exception {
        throw  new IllegalStateException("it's stopped, we don't know last position");
    }

    @Override
    public void playPrevious() throws Exception{
        throw  new IllegalStateException("it's stopped, we don't know last position");

    }

    @Override
    public void pause() throws Exception{
        throw  new IllegalStateException("it's stopped");

    }

    @Override
    public void stop() {
        Log.v("main","already stopped");
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    void continuePlaying() throws Exception{
        throw  new IllegalStateException("it's stopped, nothing is selected");

    }

    @Override
    void seekTo(int s) throws Exception {
        throw new IllegalStateException("it's stopped, you can't seek to");
    }
}
