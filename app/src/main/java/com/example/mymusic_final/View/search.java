package com.example.mymusic_final.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.example.mymusic_final.Adapter.adapter_music;
import com.example.mymusic_final.Fragments.SettingsFragment;
import com.example.mymusic_final.R;
import com.example.mymusic_final.databinding.ActivitySearchBinding;
import com.example.mymusic_final.util.Stored_music;
import com.google.android.material.snackbar.Snackbar;
import com.turingtechnologies.materialscrollbar.MaterialScrollBar;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class search extends AppCompatActivity {

    private ActivitySearchBinding binding;
    private final int requestCode_readExternalStorage = 1;
    private adapter_music adapterMusic;
    private boolean isSettingsVisible = false;
    private final SettingsFragment settingsFragment = new SettingsFragment();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        adapterMusic = new adapter_music();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.musicSearchRecyclerView.setLayoutManager(linearLayoutManager);
        binding.musicSearchRecyclerView.setHasFixedSize(true);

        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                binding.searchField.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() > 0) {
                            emitter.onNext(s.toString());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(n -> {
                    searchFromEditText();
                });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSettingsVisible) {
                    isSettingsVisible=true;
                    binding.searchSetting.setVisibility(View.VISIBLE);
                    binding.settingWholeBackground.setVisibility(View.VISIBLE);
                    FragmentManager fragmentManage = getSupportFragmentManager();
                    fragmentManage.beginTransaction()
                            .add(R.id.search_setting, settingsFragment)
                            .commit();
                } else {
                    GONEeverything();
                }
            }
        });
        binding.settingWholeBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSettingsVisible) {
                    GONEeverything();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (isSettingsVisible) {
            GONEeverything();
        } else {
            super.onBackPressed();
        }
    }

    public void GONEeverything() {
        binding.searchSetting.setVisibility(View.GONE);
        binding.settingWholeBackground.setVisibility(View.GONE);
        isSettingsVisible = false;
        FragmentManager fragmentManage = getSupportFragmentManager();
        fragmentManage.beginTransaction()
                .remove(settingsFragment)
                .commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void searchFromEditText() {
        String searchable = binding.searchField.getText().toString();
        Context context;
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isTitle=sharedPreferences.getBoolean(getString(R.string.key_title),false);
        boolean isAlbum=sharedPreferences.getBoolean(getString(R.string.key_album),false);
        boolean isArtist=sharedPreferences.getBoolean(getString(R.string.key_artist),false);

        searchAbout(isTitle?searchable:null,isAlbum? searchable:null,isArtist? searchable:null);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void searchAbout(String title, String album, String artist) {
        if (Stored_music.isExternalReadGranted(this)) {
            showMusic(title, album, artist);

        } else {
            requestPermissionForExternalStorage();
        }


    }

    private void showMusic(String title, String album, String artist) {
        Stored_music.searchRX(this, title, album, artist).observe(this, music_items -> {
            setRecyclerView((ArrayList) music_items);
        });
    }

    public void requestPermissionForExternalStorage() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode_readExternalStorage);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestCode_readExternalStorage) {
            if (grantResults[0] == PERMISSION_GRANTED) {
                searchFromEditText();
            } else {
                Snackbar.make(binding.getRoot(), "Permission not granted. We can't work without it", Snackbar.LENGTH_LONG).setAction("Grant it", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestPermissionForExternalStorage();
                    }
                }).show();
            }
        }
    }

    void setRecyclerView(ArrayList music_items) {
        binding.musicSearchRecyclerView.setAdapter(null);
        adapterMusic = new adapter_music().setListOfSongs(music_items).setContext(this);
        binding.musicSearchRecyclerView.setAdapter(adapterMusic);

        //for sidebar scroll alphabetically
        MaterialScrollBar materialScrollBar = new MaterialScrollBar(this, binding.musicSearchRecyclerView);
        materialScrollBar.addSectionIndicator(this);
        materialScrollBar.setAutoHide(true);
        materialScrollBar.setTextColour(R.color.black);
    }
}