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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.mymusic_final.Fragments.Details_of_song;
import com.example.mymusic_final.Observing.Observable_Stored_music;
import com.example.mymusic_final.Pojo.Album_item;
import com.example.mymusic_final.Pojo.Details_music_item;
import com.example.mymusic_final.Pojo.Music_item;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Logger;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Stored_music implements Observable_Stored_music {

    public static MutableLiveData<List<Music_item>> music = new MutableLiveData<List<Music_item>>();
    private static MutableLiveData<Details_music_item> details_music_itemMutableLiveData = new MutableLiveData<Details_music_item>();
    public static MutableLiveData<List<Music_item>> search_music = new MutableLiveData<List<Music_item>>();
    public static MutableLiveData<List<Album_item>> albums_music = new MutableLiveData<List<Album_item>>();

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

    public static MutableLiveData getDetailsAtId(final Context context, int id) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.COMPOSER,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.DATE_MODIFIED,
                MediaStore.Audio.Media.IS_ALARM,
                MediaStore.Audio.Media.IS_RINGTONE
        };
        String selection = MediaStore.Audio.Media._ID + "=" + id;

        Cursor cursor = context.getContentResolver().query(uri, projection, selection, null, null);
        while (cursor.moveToNext()) {

            String size = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String composer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.COMPOSER));
            String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
            String dateModified = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED));
            String isAlarm = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.IS_ALARM));
            String isRingTone = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.IS_RINGTONE));

            Details_music_item detailsOfSong = new Details_music_item(size, composer, dateAdded, dateModified, isAlarm.equals("1"), isRingTone.equals("1"));
            details_music_itemMutableLiveData.setValue(detailsOfSong);

        }
        cursor.close();

        return details_music_itemMutableLiveData;

    }

    public static void updateAtId(final Context context, int id, ContentValues contentValues) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String where = MediaStore.Audio.Media._ID + "=" + id;
        context.getContentResolver().update(uri, contentValues, where, null);
        Observable_Stored_music.notifyObservers();
    }

    public static void share(Context context, Uri uri) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("audio/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(share, "Share My Music"));
        //search(context,"ahmed","ahmed","ahmed");
        getAlbums(context);
    }

    public static MutableLiveData search(final Context context, final String title,final String album,final String artist) {
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
                };
                Log.v("main","10"+title);
                Log.v("main","11"+album);
                Log.v("main","12"+artist);

                //String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0 AND (" + MediaStore.Audio.Media.TITLE + " LIKE ?" + " || " +
                        //MediaStore.Audio.Media.ALBUM + " LIKE ?" + " || "+MediaStore.Audio.Media.ARTIST + " LIKE ?" + ")";
                //Log.v("main",selection);

                String selection=MediaStore.Audio.Media.IS_MUSIC+" !=0 AND "+MediaStore.Audio.Media.ARTIST+" = ahmed ";


                        /*+
                        "AND ( "
                        +MediaStore.Audio.Media.TITLE+" = ? "
                        +" || "
                        +MediaStore.Audio.Media.ALBUM+" = ? "
                        +" || "
                        +MediaStore.Audio.Media.ARTIST+" = ? "
                        +" ) "*/;
                String [] selectionArgs=new String[]{title};
                Cursor cursor = context.getContentResolver().query(uri, projection, null,null, MediaStore.Audio.Media.TITLE);
                Log.v("main","9"+cursor.getCount());

                ArrayList<Music_item> listOfSongs = new ArrayList<>();
                final Uri sArtworkUri = Uri
                        .parse("content://media/external/audio/albumart");
                while (cursor.moveToNext()) {
                    Log.v("main","999999999");

                    Music_item music = new Music_item();
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    Integer id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    Log.v("main",title);

                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));



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
                Log.v("main","8"+listOfSongs.size());

                emitter.onNext(listOfSongs);

            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {
            search_music.setValue((List) o);
        });
        return search_music;
    }



    public static MutableLiveData searchRX(final Context context, final String title,final String album,final String artist){
        ArrayList<Music_item> arrayList= new ArrayList<>();
        music.observe((LifecycleOwner) context, music_items -> {
            for(Music_item mi: music_items){
                if ((title!=null&&mi.getMusic_title().toLowerCase().contains(title.toLowerCase()))
                        ||(album!=null&&mi.getAlbumName().toLowerCase().contains(album.toLowerCase()))
                        ||(artist!=null&&mi.getArtist().toLowerCase().contains(artist.toLowerCase()))){
                    arrayList.add(mi);
                }
            }
            search_music.postValue(arrayList);

        });
        return search_music;
    }

    //: TODO doesnt work update it to likenHashSet
    public static MutableLiveData getAlbums(final Context context){
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] projections=new String[]{
                        MediaStore.Audio.Media.ALBUM_ID,
                        //MediaStore.Audio.Media.ALBUM,

                };

                String selection=MediaStore.Audio.Media.IS_MUSIC+" !=0";
                Cursor cursor= context.getContentResolver().query(uri,projections,selection,null,MediaStore.Audio.Media.TITLE);

                //I added them to linkedHashSet to remove any duplicates IDs
                LinkedHashSet<Integer> linkedHashSet= new LinkedHashSet<Integer>();
                while(cursor.moveToNext()){
                    linkedHashSet.add(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                }
                cursor.close();

                String[] projectionArray=new String[]{
                        MediaStore.Audio.Media.ALBUM,

                };
                String selectionArray= selection+" AND "+MediaStore.Audio.Media.ALBUM_ID+" = ?";
                //I added them all to arrayList
                ArrayList<Album_item>  albumArray= new ArrayList<Album_item>();
                Uri sArtworkUri = Uri
                        .parse("content://media/external/audio/albumart");
                for (Integer n:linkedHashSet){
                    Album_item albumItem=new Album_item();
                    albumItem.set_ID(n);
                    String[] selectionArgs = new String[]{String.valueOf(n)};
                    Cursor cursorArray= context.getContentResolver().query(uri,projectionArray,selectionArray,selectionArgs,null);
                    while(cursorArray.moveToNext()){
                        albumItem.setAlbumName(cursorArray.getString(cursorArray.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                    }

                    Uri uri2 = ContentUris.withAppendedId(sArtworkUri, n);
                    albumItem.setAlbumUri(uri2);
                    albumArray.add(albumItem);
                }
                linkedHashSet.clear();
                //albums_music.setValue(albumArray);
                emitter.onNext(albumArray);
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(o->{ albums_music.setValue((List)o);});
        return albums_music;
    }

    public static ArrayList<Music_item> getMusicAtAlbumID(final Context context, int albumID){
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media._ID,
                       };

        String selection=MediaStore.Audio.Media.IS_MUSIC + "!=0" +" AND "+ MediaStore.Audio.Media.ALBUM_ID+" = ?";
        String [] selectionArgs= new String[]{String.valueOf(albumID)};
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, MediaStore.Audio.Media.TITLE);
        ArrayList<Music_item> listOfSongs= new ArrayList<>();;
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
        return listOfSongs;
    }


}
