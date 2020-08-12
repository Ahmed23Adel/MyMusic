package com.example.mymusic_final.play_cloud;

import java.util.Random;

public class SoundFinishRepeatShuffle_SHUFFLE extends SoundFinishRepeatShuffle_state {

    @Override
    public void Next() {
        int max=musicPlayer.listOfSongs.size()-1;
        int min=0;
        Random random= new Random();
        int rand=random.nextInt((max-min)+1)+min;
        musicPlayer.getCurrentState().playAtPosition(rand);
    }
}
