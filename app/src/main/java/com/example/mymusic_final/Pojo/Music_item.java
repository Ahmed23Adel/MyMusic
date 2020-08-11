package com.example.mymusic_final.Pojo;

public class Music_item {
    private String picUri;
    private boolean isFav;
    private String music_title;
    private String artist;
    private String duration;

    public Music_item() {
    }

    public Music_item(String music_title, String artist, String picUri, boolean isFav,String duration) {
        this.picUri = picUri;
        this.isFav = isFav;
        this.music_title = music_title;
        this.artist = artist;
        this.duration=duration;
    }

    public String getDuration() {
        return duration;
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
}
