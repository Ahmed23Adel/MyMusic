package com.example.mymusic_final.play_cloud;

import java.util.Random;

public class SoundFinishRepeatShuffle_NULL extends SoundFinishRepeatShuffle_state {

    //if Null it will work as shuffle

    @Override
    public void Next() {
        int max=musicPlayer.listOfSongs.size()-1;
        int min=0;
        Random random= new Random();
        int rand=random.nextInt((max-min)+1)+min;
        musicPlayer.getCurrentState().playAtPosition(rand);

    }
}
