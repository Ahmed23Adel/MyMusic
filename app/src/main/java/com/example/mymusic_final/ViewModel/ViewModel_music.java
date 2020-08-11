package com.example.mymusic_final.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.example.mymusic_final.Pojo.Music_item;
import com.example.mymusic_final.util.Stored_music;

import java.util.List;

public class ViewModel_music extends ViewModel {

    public LiveData<PagedList<Music_item>> getMusic(Context context){
        return null/* Stored_music.getListOfSongs(context)*/;

    }
}
