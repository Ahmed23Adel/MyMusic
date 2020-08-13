package com.example.mymusic_final.View;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mymusic_final.Adapter.adapter_music;
import com.example.mymusic_final.Pojo.Music_item;
import com.example.mymusic_final.R;
import com.example.mymusic_final.Services.old_Music_player;
import com.example.mymusic_final.databinding.ActivityMainBinding;
import com.example.mymusic_final.play_cloud.Music_player;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.LinearLayout;

import com.example.mymusic_final.ui.main.SectionsPagerAdapter;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class MainActivity extends AppCompatActivity {

    LinearLayout bottom_player;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View root= binding.getRoot();
        setContentView(root);

        Intent MusicServiceIntent= new Intent(this, Music_player.class);
        startService(MusicServiceIntent);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        adapter_music.setListener(new adapter_music.OnItemClickListener() {
            @Override
            public void onItemClick(List<Music_item> listOfMusic,int position) {
                Music_item currentMusic= listOfMusic.get(position);
                binding.titleHome.setText(currentMusic.getMusic_title());
                binding.artistHome.setText(currentMusic.getArtistAlbum());
                binding.playAndPauseHome.setImageResource(R.drawable.pause_red);
                Glide.with(MainActivity.this).load(currentMusic.getAlbumArt()).error(R.drawable.audio_track).placeholder(R.drawable.audio_track)
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
        });

        View.OnClickListener onClickListener= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, Music_details.class);
                startActivity(intent);
            }
        };
        if (old_Music_player.isPlaying()){
            binding.playAndPauseHome.setImageResource(R.drawable.pause_red);
        }else{
            binding.playAndPauseHome.setImageResource(R.drawable.play_red);

        }

        bottom_player= findViewById(R.id.bottom_player);
        bottom_player.setOnClickListener(onClickListener);
        binding.titleHome.setOnClickListener(onClickListener);
        binding.artistHome.setOnClickListener(onClickListener);
        binding.albumArtHome.setOnClickListener(onClickListener);
        binding.playAndPauseHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (old_Music_player.isPlaying()){
                    binding.playAndPauseHome.setImageResource(R.drawable.play_red);
                }else{
                    binding.playAndPauseHome.setImageResource(R.drawable.pause_red);

                }
                old_Music_player.changeState();
                //Intent intent= new Intent(MainActivity.this, Music_details.class);
                //startActivity(intent);
            }
        });




    }


}