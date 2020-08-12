package com.example.mymusic_final.play_cloud;

public class SoundFinishRepeatShuffle_NO_SHUFFLE extends  SoundFinishRepeatShuffle_state{

    @Override
    public void Next() {
        musicPlayer.getCurrentState().playAtPosition(musicPlayer.position+1);
    }
}
