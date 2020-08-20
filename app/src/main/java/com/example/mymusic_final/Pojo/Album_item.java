package com.example.mymusic_final.Pojo;

import android.net.Uri;

import lombok.Data;

public @Data class Album_item {
    private String albumName;
    private Uri albumUri;
    private int _ID;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Uri getAlbumUri() {
        return albumUri;
    }

    public void setAlbumUri(Uri albumUri) {
        this.albumUri = albumUri;
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }
}
