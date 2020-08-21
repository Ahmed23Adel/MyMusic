package com.example.mymusic_final.Pojo;

import android.net.Uri;

import lombok.Data;

public @Data class Artist_item {
    private int artistId;
    private String artistName;
    private Uri artist_pic;

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Uri getArtist_pic() {
        return artist_pic;
    }

    public void setArtist_pic(Uri artist_pic) {
        this.artist_pic = artist_pic;
    }
}
