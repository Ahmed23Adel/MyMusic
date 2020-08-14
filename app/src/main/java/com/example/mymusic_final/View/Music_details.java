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
import com.example.mymusic_final.play_cloud.Music_player;
import com.example.mymusic_final.play_cloud.Observable;
import com.example.mymusic_final.play_cloud.Observer;
import com.example.mymusic_final.util.SwipeDetector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class Music_details extends AppCompatActivity implements Observer {

    private ActivityMusicDetailsBinding binding;
    //position is get assigned from adapter
    public static Integer position;
    public static List<Music_item> listOfSongs;
    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicDetailsBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        listOfSongs = getListOfSongs();
        position = getPosition();
        initMusicInfo(listOfSongs, position);


        binding.includedMusic.playAndPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Music_player.isPlaying()) {
                    try {
                        Music_player.pause();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    binding.includedMusic.playAndPause.setImageResource(R.drawable.play_red);
                } else {
                    try {
                        Music_player.continuePlaying();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    binding.includedMusic.playAndPause.setImageResource(R.drawable.pause_red);
                }
            }
        });

        binding.includedMusic.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // old_Music_player.playNext();
                try {
                    Music_player.playNext();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.includedMusic.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///old_Music_player.playPrevious();
                try {
                    Music_player.playPrevious();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        Observable.subscribe(this);

    }


    public List<Music_item> getListOfSongs() {
        return Music_player.getListOfSongs();
    }

    public int getPosition() {
        return Music_player.position;
    }


    public void initMusicInfo(List<Music_item> listOfSongs, Integer position) {
        binding.includedMusic.seekBar.setMax(listOfSongs.get(getPosition()).getDurationMM());
        binding.includedMusic.musicTitle.setText(listOfSongs.get(position).getMusic_title());
        binding.includedMusic.musicArtistAlbum.setText(listOfSongs.get(position).getArtistAlbum());
        binding.includedMusic.duration.setText(listOfSongs.get(getPosition()).getDuration());
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

        if (Music_player.isPlaying()) {
            binding.includedMusic.playAndPause.setImageResource(R.drawable.pause_red);
        } else {
            binding.includedMusic.playAndPause.setImageResource(R.drawable.play_red);

        }

        binding.includedMusic.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    try {
                        Music_player.seekTo(progress);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        io.reactivex.rxjava3.core.Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .repeat()
                .subscribe(aLong -> {
                    if (Music_player.getCurrentState().mediaPlayer != null) {
                        int mCurrentPosition = Music_player.getCurrentState().mediaPlayer.getCurrentPosition();
                        binding.includedMusic.seekBar.setProgress(mCurrentPosition);
                        binding.includedMusic.durationPlayed.setText(Music_item.getDuration(mCurrentPosition));
                    }
                });


        binding.includedMusic.forward10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Music_player.getCurrentState().mediaPlayer.isPlaying()){
                    try {
                        binding.includedMusic.durationPlayed.setText(Music_item.getDuration(Music_player.getCurrentState().mediaPlayer.getCurrentPosition()+10000));
                        Music_player.seekTo(Music_player.getCurrentState().mediaPlayer.getCurrentPosition()+10000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        binding.includedMusic.durationPlayed.setText(Music_item.getDuration(Music_player.getCurrentState().mediaPlayer.getCurrentPosition()+10000));
                        Music_player.continuePlaying();
                        Music_player.seekTo(Music_player.getCurrentState().mediaPlayer.getCurrentPosition()+10000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        });
        binding.includedMusic.back10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (Music_player.getCurrentState().mediaPlayer.isPlaying()){
                   try {
                       binding.includedMusic.durationPlayed.setText(Music_item.getDuration(Music_player.getCurrentState().mediaPlayer.getCurrentPosition()-10000));
                       Music_player.seekTo(Music_player.getCurrentState().mediaPlayer.getCurrentPosition()-10000);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }else{
                   try {
                       binding.includedMusic.durationPlayed.setText(Music_item.getDuration(Music_player.getCurrentState().mediaPlayer.getCurrentPosition()-10000));
                       Music_player.continuePlaying();
                       Music_player.seekTo(Music_player.getCurrentState().mediaPlayer.getCurrentPosition()-10000);

                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }

            }
        });

        binding.includedMusic.shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Music_player.getCurrentStateRepeatAndFinish().isSHUFFLE()){
                    binding.includedMusic.shuffle.setImageResource(R.drawable.shuffle_off);
                    Music_player.setStateFinishAndRepeat(Music_player.getCurrentStateRepeatAndFinish().isREPEAT(),false,true);
                }else{
                    binding.includedMusic.shuffle.setImageResource(R.drawable.shuffle);
                    Music_player.setStateFinishAndRepeat(Music_player.getCurrentStateRepeatAndFinish().isREPEAT(),true,false);

                }
            }
        });
        binding.includedMusic.repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Music_player.getCurrentStateRepeatAndFinish().isREPEAT()){
                    binding.includedMusic.repeat.setImageResource(R.drawable.repeat_off);
                    Music_player.setStateFinishAndRepeat(false,Music_player.getCurrentStateRepeatAndFinish().isSHUFFLE(),Music_player.getCurrentStateRepeatAndFinish().isNoShuffle());
                }else{
                    binding.includedMusic.repeat.setImageResource(R.drawable.repeat_one);
                    Music_player.setStateFinishAndRepeat(true,Music_player.getCurrentStateRepeatAndFinish().isSHUFFLE(),Music_player.getCurrentStateRepeatAndFinish().isNoShuffle());

                }
            }
        });
        if (Music_player.getCurrentStateRepeatAndFinish().isREPEAT()){
            binding.includedMusic.repeat.setImageResource(R.drawable.repeat_one);
        }
        if (Music_player.getCurrentStateRepeatAndFinish().isSHUFFLE()){
            binding.includedMusic.shuffle.setImageResource(R.drawable.shuffle);
        }
        new SwipeDetector(binding.includedMusic.albumArtParent).setOnSwipeListener(new SwipeDetector.onSwipeEvent() {
            @Override
            public void SwipeEventDetected(View v, SwipeDetector.SwipeTypeEnum swipeType) {
                if(swipeType==SwipeDetector.SwipeTypeEnum.LEFT_TO_RIGHT){

                    try {
                        Music_player.playPrevious();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if (swipeType==SwipeDetector.SwipeTypeEnum.RIGHT_TO_LEFT){
                    try {
                        Music_player.playNext();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                    //getActivity().onBackPressed();
            }
        });

    }




    @Override
    public void updated(ArrayList<Music_item> listOfSongs, int position) {
        if (!isDestroyed()) {
            binding.includedMusic.musicTitle.setText(listOfSongs.get(position).getMusic_title());
            binding.includedMusic.musicArtistAlbum.setText(listOfSongs.get(position).getArtistAlbum());
            binding.includedMusic.duration.setText(listOfSongs.get(getPosition()).getDuration());
            binding.includedMusic.seekBar.setMax(listOfSongs.get(getPosition()).getDurationMM());
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

            if (Music_player.isPlaying()) {
                binding.includedMusic.playAndPause.setImageResource(R.drawable.pause_red);
            } else {
                binding.includedMusic.playAndPause.setImageResource(R.drawable.play_red);

            }
        }
    }
}