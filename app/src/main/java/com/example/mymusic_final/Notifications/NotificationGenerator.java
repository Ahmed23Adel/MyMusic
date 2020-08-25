package com.example.mymusic_final.Notifications;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.mymusic_final.Observing.Observable_Stored_music;
import com.example.mymusic_final.Observing.Observer_Stored_music;
import com.example.mymusic_final.Pojo.Music_item;
import com.example.mymusic_final.R;
import com.example.mymusic_final.View.MainActivity;
import com.example.mymusic_final.play_cloud.Music_player;
import com.example.mymusic_final.play_cloud.Observable;
import com.example.mymusic_final.play_cloud.Observer;
import com.example.mymusic_final.util.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.transform.Source;

public class NotificationGenerator extends Service implements Observer, Observer_Stored_music {

    private static Music_item currentMusicItem;
    NotificationManager notificationManager;
    private static boolean isShown=false;
    MediaSessionCompat mediaSessionCompat;
    private PendingIntent pendingIntentPlay;
    private PendingIntent pendingIntentNext;
    private PendingIntent pendingIntentPrevious;
    private PendingIntent pendingIntentForward_10;
    private PendingIntent pendingIntentBack_10;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("main", "5");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mediaSessionCompat= new MediaSessionCompat(this, "media tag");
        mediaSessionCompat.setActive(true);
        Observable.subscribe(this);
        Observable_Stored_music.subscribe(this);
        pendingIntentPlay=getPendingIntent(this, Constants.Notifications.ACTION_PLAY_OR_PAUSE);
        pendingIntentNext=getPendingIntent(this, Constants.Notifications.ACTION_NEXT);
        pendingIntentPrevious=getPendingIntent(this, Constants.Notifications.ACTION_BACK);
        pendingIntentForward_10=getPendingIntent(this, Constants.Notifications.ACTION_FORWARD_10);
        pendingIntentBack_10=getPendingIntent(this, Constants.Notifications.ACTION_BACK_10);
        Log.v("main", "1");

        if (intent.getAction().equals(Constants.Notifications.SHOW_NOTIFICATION)) {
            Log.v("main", "2");
            currentMusicItem = Music_player.getListOfSongs().get(Music_player.getPosition());
            showNotifications(this);

        } else if (intent.getAction().equals(Constants.Notifications.ACTION_PLAY_OR_PAUSE)) {

        }
        if (intent.getAction().equals(Constants.Notifications.ACTION_NEXT)) {

        }
        if (intent.getAction().equals(Constants.Notifications.ACTION_BACK)) {

        }
        Observable.subscribe(this);
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotifications(Context context) {
        if (!isShown) {
            Music_item musicItem = Music_player.getListOfSongs().get(Music_player.getPosition());

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(Constants.Notifications.NOTIFICATIONS_CHANNEL
                        , context.getString(R.string.norification_channel_name), NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("for MyMusic app");
                notificationManager.createNotificationChannel(notificationChannel);


            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, Constants.Notifications.NOTIFICATIONS_CHANNEL)
                    .setSmallIcon(R.drawable.audio_track)
                    //.setCustomBigContentView(remoteViews)
                    .setLargeIcon(getDefaultAlbumArt(musicItem))
                    .setContentTitle(currentMusicItem.getMusic_title())
                    .setContentText(currentMusicItem.getArtistAlbum())
                    .setContentIntent(getPendingIntent(this))//TODO: HAS PROBLEM
                    .setCategory(NotificationCompat.CATEGORY_SERVICE)
                    .addAction(R.drawable.back_10_red, "Back 10", pendingIntentBack_10)
                    .addAction(R.drawable.back, "Back",pendingIntentPrevious)
                    .addAction(Music_player.isPlaying() ? R.drawable.audio_pause : R.drawable.audio_play, "playOrPause",pendingIntentPlay)
                    .addAction(R.drawable.next, "Next", pendingIntentNext)
                    .addAction(R.drawable.forward_10, "Forward 10",pendingIntentForward_10)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1, 2,3,4).setMediaSession(mediaSessionCompat.getSessionToken()))
                    .setAutoCancel(true);

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
                notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            }
            notificationManager.notify(Constants.Notifications.NOTIFICATIONS_MANAGER_ID, notificationBuilder.build());
            isShown= true;
        }else{
            updateNotification(context,Music_player.getListOfSongs().get(Music_player.getPosition()));
        }
    }



    private void updateNotification(Context context,Music_item musicItem) {



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, Constants.Notifications.NOTIFICATIONS_CHANNEL)
                .setSmallIcon(R.drawable.audio_track)
                //.setCustomBigContentView(remoteViews)
                .setLargeIcon(getDefaultAlbumArt(musicItem))
                .setContentTitle(musicItem.getMusic_title())
                .setContentText(musicItem.getArtistAlbum())
                .setContentIntent(getPendingIntent(this))
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .addAction(R.drawable.back_10_red, "Back 10", getPendingIntent(context,Constants.Notifications.ACTION_BACK_10))
                .addAction(R.drawable.back, "Back", getPendingIntent(context,Constants.Notifications.ACTION_BACK))
                .addAction(Music_player.isPlaying() ? R.drawable.audio_pause : R.drawable.audio_play, "playOrPause", getPendingIntent(context,Constants.Notifications.ACTION_PLAY_OR_PAUSE))
                .addAction(R.drawable.next, "Next", getPendingIntent(context,Constants.Notifications.ACTION_NEXT))
                .addAction(R.drawable.forward_10, "Forward 10", getPendingIntent(context,Constants.Notifications.ACTION_FORWARD_10))
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2,3,4).setMediaSession(mediaSessionCompat.getSessionToken()))
                .setNotificationSilent()
                .setOngoing(true);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(Constants.Notifications.NOTIFICATIONS_MANAGER_ID, notificationBuilder.build());
    }

    //for play cloud
    @Override
    public void updated(ArrayList<Music_item> listOfSongs, int position) {
            updateNotification(getApplicationContext(),listOfSongs.get(position));
    }

    //for stored music
    @Override
    public void updated() {

    }

    public static PendingIntent getPendingIntent(Context context,String ID){
        Intent intent= new Intent(context,NotificationBroadCast.class);
        intent.setAction(ID);
        return PendingIntent.getBroadcast(context, Constants.Notifications.REQUEST_CODE_PENDING_INTENT, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    public static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, Constants.Notifications.REQUEST_CODE_PENDING_INTENT, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.backgrouns_13, options);
        } catch (Exception e) {
        }
        return bm;
    }

    public Bitmap getDefaultAlbumArt(Music_item music_item) {
       Uri uri=music_item.getAlbumArt();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}