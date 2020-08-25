package com.example.mymusic_final.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mymusic_final.play_cloud.Music_player;
import com.example.mymusic_final.util.Constants;

public class NotificationBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constants.Notifications.ACTION_PLAY_OR_PAUSE)){

            Log.v("main","play or pause");
            if(Music_player.isPlaying()){
                try {
                    Music_player.pause();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Music_player.continuePlaying();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if (intent.getAction().equals(Constants.Notifications.ACTION_NEXT)){

            Log.v("main","next");
            try {
                Music_player.playNext();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if (intent.getAction().equals(Constants.Notifications.ACTION_BACK)){
            Log.v("main","back");
            try {
                Music_player.playPrevious();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if (intent.getAction().equals(Constants.Notifications.ACTION_FORWARD_10)){
            try {
                Music_player.seekTo(Music_player.getCurrentState().mediaPlayer.getCurrentPosition() + 10000);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }else if (intent.getAction().equals(Constants.Notifications.ACTION_BACK_10)){
            try {
                Music_player.seekTo(Music_player.getCurrentState().mediaPlayer.getCurrentPosition() - 10000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
