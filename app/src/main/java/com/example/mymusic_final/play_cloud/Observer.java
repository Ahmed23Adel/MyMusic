package com.example.mymusic_final.play_cloud;

import com.example.mymusic_final.Pojo.Music_item;

import java.util.ArrayList;

public interface Observer {

    void updated(ArrayList<Music_item> listOfSongs, int position);
}
