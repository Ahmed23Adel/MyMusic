package com.example.mymusic_final.View;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mymusic_final.Adapter.adapter_music;
import com.example.mymusic_final.Pojo.Music_item;
import com.example.mymusic_final.R;
import com.example.mymusic_final.Services.old_Music_player;
import com.example.mymusic_final.databinding.ActivityMusicDetailsBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class Music_details extends AppCompatActivity {

    private ActivityMusicDetailsBinding binding;
    //position is get assigned from adapter
    public static Integer position;
    public static List<Music_item> listOfSongs;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMusicDetailsBinding.inflate(getLayoutInflater());
        View root=binding.getRoot();
        setContentView(root);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        listOfSongs=getListOfSongs();
        position=getPosition();
        initMusicInfo(listOfSongs,position);



        if (old_Music_player.isPlaying()){
            binding.includedMusic.playAndPause.setImageResource(R.drawable.pause_red);
        }else{
            binding.includedMusic.playAndPause.setImageResource(R.drawable.play_red);

        }
        binding.includedMusic.playAndPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (old_Music_player.isPlaying()){
                    binding.includedMusic.playAndPause.setImageResource(R.drawable.play_red);
                }else{
                    binding.includedMusic.playAndPause.setImageResource(R.drawable.pause_red);

                }
                old_Music_player.changeState();
            }
        });

        binding.includedMusic.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                old_Music_player.playNext();
            }
        });

        binding.includedMusic.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                old_Music_player.playPrevious();
            }
        });

        old_Music_player.setOnPlayChanged(new old_Music_player.OnPlayChanged() {
            @Override
            public void updatedTo(int position2) {
                initMusicInfo(listOfSongs,position2);
            }
        });

    }


    public  List<Music_item> getListOfSongs(){
        return old_Music_player.getListOfSongs();
    }

    public int getPosition(){
        return old_Music_player.getPosition();
    }



    public Uri getUri(List<Music_item> listOfSongs,Integer position){
        return Uri.parse(listOfSongs.get(position).getPath());
    }

    public void initMusicInfo(List<Music_item> listOfSongs,Integer position){
        adapter_music.getListener().onItemClick(listOfSongs,position);
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