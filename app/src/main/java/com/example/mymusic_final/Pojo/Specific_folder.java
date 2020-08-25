package com.example.mymusic_final.Pojo;

import android.net.Uri;

public abstract class Specific_folder {

    private int id;
    private String name;
    private Uri pic_uri;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getPic_uri() {
        return pic_uri;
    }

    public void setPic_uri(Uri pic_uri) {
        this.pic_uri = pic_uri;
    }
}
