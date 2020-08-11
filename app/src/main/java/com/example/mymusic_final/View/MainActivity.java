package com.example.mymusic_final.View;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.mymusic_final.Music_details;
import com.example.mymusic_final.R;
import com.example.mymusic_final.util.Stored_music;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mymusic_final.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    LinearLayout bottom_player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        bottom_player= findViewById(R.id.bottom_player);
        bottom_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, Music_details.class);
                startActivity(intent);
            }
        });


    }


}