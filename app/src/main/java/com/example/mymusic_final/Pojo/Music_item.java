package com.example.mymusic_final.Pojo;

import android.graphics.Bitmap;
import android.net.Uri;

public class Music_item {
    private String picUri;
    private boolean isFav;
    private String music_title;
    private String artist;
    private String duration;
    private Uri AlbumArt;
    private String path;
    private String albumName;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Music_item() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Uri getAlbumArt() {
        return AlbumArt;
    }

    public void setAlbumArt(Uri bitmap) {
        this.AlbumArt = bitmap;
    }

    public String getDuration() {
        int du=Integer.valueOf(duration);
        du/=1000;
        int secods= du%60;
        int minuts=(du-secods)/60;
        return minuts+":"+secods;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPicUri() {
        return picUri;
    }

    public boolean isFav() {
        return isFav;
    }

    public String getMusic_title() {
        return music_title;
    }

    public String getArtist() {
        return artist;
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public void setMusic_title(String music_name) {
        music_title = music_name;
    }

    public void setArtist(String music_write) {
        artist = music_write;
    }

    public String getArtistAlbum(){
        if (getAlbumName()!=null){
            if (!getArtist().equals("<unknown>")){
                return getArtist()+"- "+getAlbumName();
            }else{
                return getAlbumName();
            }
        }else{
            if (!getArtist().equals("<unknown>")){
                return getArtist();
            }else if (getArtist().equals("<unknown>")){
                return "";
            }
        }
        return "";
    }
}
