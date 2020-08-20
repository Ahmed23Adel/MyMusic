package com.example.mymusic_final.View;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mymusic_final.Fragments.fragment_music;
import com.example.mymusic_final.Pojo.Music_item;
import com.example.mymusic_final.R;
import com.example.mymusic_final.databinding.ActivityAlbumDetailsBinding;
import com.example.mymusic_final.util.Constants;
import com.example.mymusic_final.util.Stored_music;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class album_details extends AppCompatActivity {

    private int albumID;
    private ArrayList<Music_item> listOfMusic;
    private ActivityAlbumDetailsBinding binding;
    private fragment_music fragmentMusic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Adele");
        albumID=getIntent().getExtras().getInt(Constants.Music.ALBUM_ID);
        listOfMusic= Stored_music.getMusicAtAlbumID(this,albumID);
        setTitle(listOfMusic.get(0).getAlbumName());
        binding=ActivityAlbumDetailsBinding.inflate(getLayoutInflater());
        View root= binding.getRoot();
        setContentView(root);

        fragmentMusic= fragment_music.newInstance(fragment_music.ACTIONS_ALBUM_ID,null);
        setRecyclerView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }

    private void setRecyclerView() {
        fragmentMusic.setAlbum_id(albumID);
        FragmentManager fragmentManage = getSupportFragmentManager();
        fragmentManage.beginTransaction()
                .add(R.id.frameLayout, fragmentMusic)
                .commit();
        Glide.with(this)
                .load(listOfMusic.get(0).getAlbumArt())
                .into(new CustomTarget<Drawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        binding.toolbarLayout.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

    }
}