package com.example.mymusic_final.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mymusic_final.Observing.Observer_Stored_music;
import com.example.mymusic_final.Pojo.Music_item;
import com.example.mymusic_final.R;
import com.example.mymusic_final.databinding.ActivityMainBinding;
import com.example.mymusic_final.play_cloud.Music_player;
import com.example.mymusic_final.play_cloud.Observable;
import com.example.mymusic_final.play_cloud.Observer;
import com.example.mymusic_final.util.Stored_music;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mymusic_final.ui.main.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class MainActivity extends AppCompatActivity implements Observer,Observer_Stored_music  {

    LinearLayout bottom_player;
    private ActivityMainBinding binding;
    public static String title;
    private boolean isFromPref=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View root= binding.getRoot();
        setContentView(root);

        binding.titleHome.setSelected(true);

        Intent MusicServiceIntent= new Intent(this, Music_player.class);
        startService(MusicServiceIntent);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);



        View.OnClickListener onClickListener= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, Music_details.class);
                startActivity(intent);
            }
        };
        bottom_player= findViewById(R.id.bottom_player);
        bottom_player.setOnClickListener(onClickListener);
        binding.titleHome.setOnClickListener(onClickListener);
        binding.artistHome.setOnClickListener(onClickListener);
        binding.albumArtHome.setOnClickListener(onClickListener);

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        int position=Integer.parseInt(sharedPreferences.getString(getString(R.string.key_music_position),"-1"));
        String  type=sharedPreferences.getString(getString(R.string.key_type),null);
        int specific_folder_id=Integer.parseInt(sharedPreferences.getString(getString(R.string.key_specific_folder_id),"-1"));


        Observable.subscribe(this);
        binding.playAndPauseHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFromPref){
                    Music_player.playAtPosition(position);
                }
                if (Music_player.isPlaying()){
                    try {
                        Music_player.pause();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    binding.playAndPauseHome.setImageResource(R.drawable.play_red);
                }else{
                    try {
                        Music_player.continuePlaying();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    binding.playAndPauseHome.setImageResource(R.drawable.pause_red);
                }
            }
        });

        binding.goSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, search.class);
                startActivity(intent);
            }
        });



        if (type!=null){
           if (type.equals(getString(R.string.type_MUSIC))){
               Stored_music.getListOfSongs(this).observe(this, new androidx.lifecycle.Observer<List<Music_item>>() {
                   @Override
                   public void onChanged(List<Music_item> music_items) {
                       Log.v("main","f22");
                       Music_player.setListOfSongs((ArrayList) music_items);
                       Music_player.setPosition(position);
                       initInfo((ArrayList<Music_item>) music_items,position);

                   }
               });
           }else {
               if (type.equals(getString(R.string.type_ALBUMS))){
                   ArrayList arrayList= Stored_music.getMusicAtAlbumID(this,specific_folder_id);
                   Music_player.setListOfSongs(arrayList);
                   Music_player.setPosition(position);
                   initInfo(arrayList,specific_folder_id);
               }else{
                   ArrayList arrayList= Stored_music.getMusicAtArtistId(this,specific_folder_id);
                   Music_player.setListOfSongs(arrayList);
                   Music_player.setPosition(position);
                   initInfo(arrayList,specific_folder_id);

               }
           }
        }
    }

    @Override
    protected void onDestroy() {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        Editor editor= sharedPreferences.edit();
        editor.putString(getString(R.string.key_type),getString(R.string.type_MUSIC));
        editor.putString(getString(R.string.key_specific_folder_id),"-1");
        editor.putString(getString(R.string.key_music_position),String.valueOf(Music_player.getPosition()));
        editor.apply();
        Log.v("main","f1");
        super.onDestroy();
    }

    @Override
    public void updated(ArrayList<Music_item> listOfSongs, int position) {
        isFromPref=false;
       initInfo(listOfSongs,position);


    }

    private void initInfo(ArrayList<Music_item> listOfSongs, int position){
        binding.bottomPlayer.setVisibility(View.VISIBLE);
        Music_item currentMusic= listOfSongs.get(position);
        binding.titleHome.setText(currentMusic.getMusic_title());
        binding.artistHome.setText(currentMusic.getArtistAlbum());
        binding.playAndPauseHome.setImageResource(R.drawable.pause_red);
        if (Music_player.isPlaying()){
            binding.playAndPauseHome.setImageResource(R.drawable.pause_red);
        }else{
            binding.playAndPauseHome.setImageResource(R.drawable.play_red);

        }
        Glide.with(MainActivity.this).load(currentMusic.getAlbumArt()).circleCrop().error(R.drawable.audio_track).placeholder(R.drawable.audio_track)
                .into(binding.albumArtHome);


        Glide.with(MainActivity.this)
                .load(currentMusic.getAlbumArt())
                .apply(bitmapTransform(new BlurTransformation(50, 5)))
                .into(new CustomTarget<Drawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        bottom_player.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    public void updated() {
        if(!isDestroyed()){
            Stored_music.getListOfSongs(this).observe(this, new androidx.lifecycle.Observer<List<Music_item>>() {
                @Override
                public void onChanged(List<Music_item> music_items) {
                    Music_player.setListOfSongs((ArrayList)music_items);
                    initInfo(Music_player.getListOfSongs(),Music_player.getPosition());

                }
            });
        }
    }
}