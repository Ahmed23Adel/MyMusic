package com.example.mymusic_final.play_cloud;

public class state_idle extends Player_state {
    @Override
    public void playAtPosition(int position) {
        Music_player.setCurrentState(Music_player.state_playing);
        Music_player.getCurrentState().playAtPosition(position);
    }

    @Override
    public void playNext() throws Exception {
        throw  new IllegalStateException("we are in idle state, can't know the last position");
    }

    @Override
    public void playPrevious() {
        throw  new IllegalStateException("we are in idle state, can't know the last position");

    }

    @Override
    public void pause() {
        throw  new IllegalStateException("we are in idle state,you can't pause");

    }

    @Override
    public void stop() {
        throw  new IllegalStateException("we are in idle state, you can't stop");

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    void continuePlaying() {
        throw  new IllegalStateException("we are in idle state, you can't continue playing");

    }

    @Override
    void seekTo(int s) throws Exception {
        throw  new IllegalStateException("we are in idle state,you can't seek to");

    }
}
