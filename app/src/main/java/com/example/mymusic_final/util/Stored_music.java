package com.example.mymusic_final.util;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mymusic_final.Pojo.Music_item;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Stored_music {

    public static MutableLiveData<List<Music_item>> music = new MutableLiveData<List<Music_item>>();
    private static onDataChanged listener;

    public static MutableLiveData<List<Music_item>> getListOfSongs(final Context context) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                Log.v("main", String.valueOf(uri));
                String[] projection = {
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media._ID

                };

                Cursor cursor = context.getContentResolver().query(uri, projection, MediaStore.Audio.Media.IS_MUSIC + "!=0", null, null);
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
        if (listener != null) {
            listener.dataChanged();
        }
    }


    public interface onDataChanged {
        void dataChanged();
    }

    public static void setListener(onDataChanged listener2) {
        listener = listener2;
    }

}
