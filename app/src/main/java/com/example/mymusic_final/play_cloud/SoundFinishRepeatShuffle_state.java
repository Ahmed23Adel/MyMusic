package com.example.mymusic_final.play_cloud;

public class SoundFinishRepeatShuffle_state {

    static Music_player musicPlayer;

    public static  boolean REPEAT = false;
    public static  boolean SHUFFLE = false;
    public static  boolean NO_SHUFFLE = false;

    public static SoundFinishRepeatShuffle_state SoundFinishRepeatShuffle_NULL = new SoundFinishRepeatShuffle_NULL();
    public static SoundFinishRepeatShuffle_state SoundFinishRepeatShuffle_REPEAT = new SoundFinishRepeatShuffle_REPEAT();
    public static SoundFinishRepeatShuffle_state SoundFinishRepeatShuffle_SHUFFLE = new SoundFinishRepeatShuffle_SHUFFLE();
    public static SoundFinishRepeatShuffle_state SoundFinishRepeatShuffle_NO_SHUFFLE = new SoundFinishRepeatShuffle_NO_SHUFFLE();

    public static SoundFinishRepeatShuffle_state currentState;

    public void Next() {
        currentState.Next();
    }

    public static void setState(boolean repeat, boolean shuffle, boolean noShuffle) {
        REPEAT=repeat;
        SHUFFLE=shuffle;
        NO_SHUFFLE=noShuffle;
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

    public static boolean isREPEAT() {
        return REPEAT;
    }

    public static void setREPEAT(boolean REPEAT) {
        SoundFinishRepeatShuffle_state.REPEAT = REPEAT;
    }

    public static boolean isSHUFFLE() {
        return SHUFFLE;
    }

    public static void setSHUFFLE(boolean SHUFFLE) {
        SoundFinishRepeatShuffle_state.SHUFFLE = SHUFFLE;
    }

    public static boolean isNoShuffle() {
        return NO_SHUFFLE;
    }

    public static void setNoShuffle(boolean noShuffle) {
        NO_SHUFFLE = noShuffle;
    }
}
