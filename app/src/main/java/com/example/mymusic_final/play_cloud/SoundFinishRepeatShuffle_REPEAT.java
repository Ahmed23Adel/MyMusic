package com.example.mymusic_final.play_cloud;

import android.util.Log;

public class SoundFinishRepeatShuffle_REPEAT extends SoundFinishRepeatShuffle_state{

    @Override
    public void Next() {
        Music_player.playAtPosition(Music_player.getPosition());

        //musicPlayer.getCurrentState().playAtPosition(musicPlayer.position);
    }
}
