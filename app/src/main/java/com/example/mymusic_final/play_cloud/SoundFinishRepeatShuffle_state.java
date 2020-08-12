package com.example.mymusic_final.play_cloud;

public abstract class SoundFinishRepeatShuffle_state {

    static Music_player musicPlayer;

    public static final boolean REPEAT = false;
    public static final boolean SHUFFLE = false;
    public static final boolean NO_SHUFFLE = false;

    public static SoundFinishRepeatShuffle_state SoundFinishRepeatShuffle_NULL = new SoundFinishRepeatShuffle_NULL();
    public static SoundFinishRepeatShuffle_state SoundFinishRepeatShuffle_REPEAT = new SoundFinishRepeatShuffle_REPEAT();
    public static SoundFinishRepeatShuffle_state SoundFinishRepeatShuffle_SHUFFLE = new SoundFinishRepeatShuffle_SHUFFLE();
    public static SoundFinishRepeatShuffle_state SoundFinishRepeatShuffle_NO_SHUFFLE = new SoundFinishRepeatShuffle_NO_SHUFFLE();

    public static SoundFinishRepeatShuffle_state currentState;

    public void Next() {
        currentState.Next();
    }

    public static void setState(boolean repeat, boolean shuffle, boolean noShuffle) {
        if (repeat == true) {
            currentState = SoundFinishRepeatShuffle_REPEAT;
        } else if (shuffle == true && noShuffle == false) {
            currentState = SoundFinishRepeatShuffle_SHUFFLE;
        } else if (noShuffle == true && shuffle == false) {
            currentState = SoundFinishRepeatShuffle_NO_SHUFFLE;
        } else {
            currentState = SoundFinishRepeatShuffle_NULL;

        }
    }
}
