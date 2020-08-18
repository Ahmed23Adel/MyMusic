package com.example.mymusic_final.util;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mymusic_final.Fragments.Details_of_song;
import com.example.mymusic_final.Observing.Observable_Stored_music;
import com.example.mymusic_final.Pojo.Details_music_item;
import com.example.mymusic_final.Pojo.Music_item;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Stored_music implements Observable_Stored_music {

    public static MutableLiveData<List<Music_item>> music = new MutableLiveData<List<Music_item>>();
    private static MutableLiveData<Details_music_item> details_music_itemMutableLiveData= new MutableLiveData<Details_music_item>();

    public static MutableLiveData<List<Music_item>> getListOfSongs(final Context context) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] projection = {
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media._ID,
                        //MediaStore.Audio.Media.COMPOSER
                        //MediaStore.Audio.Media.DATE_ADDED
                        //MediaStore.Audio.Media.DATE_MODIFIED
                        //MediaStore.Audio.Media.SIZE
                        //MediaStore.Audio.Media.IS_ALARM
                        //MediaStore.Audio.Media.IS_RINGTONE


                };

                Cursor cursor = context.getContentResolver().query(uri, projection, MediaStore.Audio.Media.IS_MUSIC + "!=0", null, MediaStore.Audio.Media.TITLE);
                ArrayList<Music_item> listOfSongs = new ArrayList<>();
                while (cursor.moveToNext()) {
                    Music_item music = new Music_item();
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    Integer id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));

                    //Log.v("main","i ");
                    //Log.v("main","i "+cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    Uri sArtworkUri = Uri
                            .parse("content://media/external/audio/albumart");


                    Uri uri2 = ContentUris.withAppendedId(sArtworkUri,
                            cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                    music.setAlbumArt(uri2);

                    music.setMusic_title(title);
                    music.setArtist(artist);
                    music.setFav(false);
                    music.setDuration(duration);
                    music.setPicUri(null);
                    music.setPath(path);
                    music.setAlbumName(album);
                    music.set_ID(id);
                    listOfSongs.add(music);
                }
                cursor.close();
                emitter.onNext(listOfSongs);

            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {
            music.setValue((List) o);
        });
        return music;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isExternalReadGranted(Context context) {
        return context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static List<Music_item> getListOfSongs() {
        return music.getValue();
    }

    public static void deleteSongAtID(Context context, int id) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        context.getContentResolver().delete(uri, MediaStore.Audio.Media._ID + "=" + id, null);
        Observable_Stored_music.notifyObservers();
    }

    public static MutableLiveData getDetailsAtId(final Context context,int id ){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.COMPOSER,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.DATE_MODIFIED,
                MediaStore.Audio.Media.IS_ALARM,
                MediaStore.Audio.Media.IS_RINGTONE
        };
        String selection=MediaStore.Audio.Media._ID+"="+id;

        Cursor cursor=context.getContentResolver().query(uri,projection,selection,null,null);
        while (cursor.moveToNext()){

            String size=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String composer=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.COMPOSER));
            String dateAdded=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
            String dateModified=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED));
            String isAlarm=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.IS_ALARM));
            String isRingTone=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.IS_RINGTONE));

            Details_music_item detailsOfSong= new Details_music_item(size,composer,dateAdded,dateModified, isAlarm.equals("1"), isRingTone.equals("1"));
            details_music_itemMutableLiveData.setValue(detailsOfSong);

        }
            cursor.close();

        return details_music_itemMutableLiveData;

    }

    public static void updateAtId(final Context context, int id, ContentValues contentValues){
        Uri uri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String where=MediaStore.Audio.Media._ID+"="+id;
        context.getContentResolver().update(uri,contentValues,where,null);
        Observable_Stored_music.notifyObservers();
    }

    public static void share(Context context,Uri uri){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("audio/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(share, "Share My Music"));
    }





}
