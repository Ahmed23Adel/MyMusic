package com.example.mymusic_final.play_cloud;

public class SoundFinishRepeatShuffle_REPEAT extends SoundFinishRepeatShuffle_state{

    @Override
    public void Next() {
        Music_player.playAtPosition(Music_player.getPosition());

        //musicPlayer.getCurrentState().playAtPosition(musicPlayer.position);
    }
}
