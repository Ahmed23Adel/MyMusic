package com.example.mymusic_final.View;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mymusic_final.Pojo.Music_item;
import com.example.mymusic_final.R;
import com.example.mymusic_final.databinding.ActivityMusicDetailsBinding;
import com.example.mymusic_final.util.Constants;
import com.example.mymusic_final.util.Stored_music;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;

import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class Music_details extends AppCompatActivity {

    private ActivityMusicDetailsBinding binding;
    private  Integer position;
    private List<Music_item> listOfSongs;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMusicDetailsBinding.inflate(getLayoutInflater());
        View root=binding.getRoot();
        setContentView(root);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //binding.includedMusic.
        position=getPosition();
        listOfSongs=getListOfSongs();
        initMusicInfo(listOfSongs,position);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        Glide.with(this)
                .load(R.drawable.backgrouns_13)
                .apply(bitmapTransform(new BlurTransformation(50, 5)))
                .into(new CustomTarget<Drawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        binding.includedMusic.wholeBackground.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public int getPosition(){
        return getIntent().getIntExtra(Constants.Music.MUSIC_POSITION,-1);
    }

    public  List<Music_item> getListOfSongs(){
        return Stored_music.getListOfSongs();
    }

    public void initSong(){

    }

    public Uri getUri(List<Music_item> listOfSongs,Integer position){
        return Uri.parse(listOfSongs.get(position).getPath());
    }

    public void initMusicInfo(List<Music_item> listOfSongs,Integer position){
        binding.includedMusic.musicTitle.setText(listOfSongs.get(position).getMusic_title());
        binding.includedMusic.musicArtistAlbum.setText(listOfSongs.get(position).getArtistAlbum());
        Glide.with(this).load(listOfSongs.get(position).getAlbumArt()).error(R.drawable.audio_track).placeholder(R.drawable.audio_track)
                .into(binding.includedMusic.albumArt);

        Glide.with(this)
                .load(listOfSongs.get(position).getAlbumArt())
                .apply(bitmapTransform(new BlurTransformation(50, 5)))
                .into(new CustomTarget<Drawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        binding.includedMusic.wholeBackground.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}