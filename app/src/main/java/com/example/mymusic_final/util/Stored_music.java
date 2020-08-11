package com.example.mymusic_final.util;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;
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

    static MutableLiveData<List<Music_item>> music=new MutableLiveData<List<Music_item>>();

    public static  MutableLiveData<List<Music_item>> getListOfSongs(final Context context){
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                Cursor cursor=context.getContentResolver().query(uri,null,MediaStore.Audio.Media.IS_MUSIC+"!=0",null,null);
                ArrayList<Music_item> listOfSongs=new ArrayList<>();
                while (cursor.moveToNext()){
                    Music_item music= new Music_item();
                    String title=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String duration=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

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
                    listOfSongs.add(music);
                }
                cursor.close();
                emitter.onNext(listOfSongs);

            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(o->{
            music.setValue((List)o);
        });
        return  music;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isExternalReadGranted(Context context){
        return context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
    }


}
