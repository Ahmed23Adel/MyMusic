package com.example.mymusic_final.play_cloud;

import java.util.Random;

public class SoundFinishRepeatShuffle_SHUFFLE extends SoundFinishRepeatShuffle_state {

    @Override
    public void Next() {
        int max=Music_player.getListOfSongs().size()-1;
        int min=0;
        Random random= new Random();
        int rand=random.nextInt((max-min)+1)+min;
        Music_player.setPosition(rand);
        Music_player.playAtPosition(Music_player.getPosition());
//        Music_player.getCurrentState().playAtPosition(rand);
    }
}
