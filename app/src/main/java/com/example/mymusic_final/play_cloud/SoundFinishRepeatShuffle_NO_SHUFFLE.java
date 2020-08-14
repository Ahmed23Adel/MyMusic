package com.example.mymusic_final.play_cloud;

import android.util.Log;

public class SoundFinishRepeatShuffle_NO_SHUFFLE extends  SoundFinishRepeatShuffle_state{

    @Override
    public void Next() {
        Music_player.setPosition(Music_player.getPosition()+1);
        Music_player.playAtPosition(Music_player.getPosition());

        //musicPlayer.getCurrentState().playAtPosition(musicPlayer.position+1);
    }
}
