package com.example.mymusic_final.Adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymusic_final.Pojo.Music_item;
import com.example.mymusic_final.R;
import com.example.mymusic_final.Services.Music_player;
import com.example.mymusic_final.View.Music_details;
import com.example.mymusic_final.util.Constants;
import com.turingtechnologies.materialscrollbar.INameableAdapter;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;

public class adapter_music extends RecyclerView.Adapter<adapter_music.itemHolder> implements INameableAdapter {

    List<Music_item> listOfSongs;
    Context context;
    public static int position;


    public adapter_music setContext(Context context) {
        this.context = context;
        return this;
    }

    public adapter_music setListOfSongs(List<Music_item> listOfSongs) {
        this.listOfSongs = listOfSongs;
        return this;
    }

    @NonNull
    @Override
    public itemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music,parent,false);
        return new itemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemHolder holder, int position) {
        Music_item currentMusic=listOfSongs.get(position);
        holder.title.setText(currentMusic.getMusic_title());
        if (currentMusic.getArtist().equals("<unknown>")){
            holder.artist.setVisibility(View.GONE);
        }else{
            holder.artist.setText(currentMusic.getArtist());
        }

        holder.duration.setText(currentMusic.getDuration());
        Glide.with(context).load(currentMusic.getAlbumArt()).error(R.drawable.audio_track).placeholder(R.drawable.audio_track)
               .into(holder.poster);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MusicServiceIntent= new Intent(context, Music_player.class);
                context.startService(MusicServiceIntent);


                Intent intent= new Intent(context, Music_details.class);
                intent.putExtra(Constants.Music.MUSIC_POSITION,position);
                context.startActivity(intent);

                Music_details.position=position;
                Music_details.listOfSongs=listOfSongs;


            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfSongs.size();
    }

    @Override
    public Character getCharacterForElement(int element) {
        return Character.valueOf(listOfSongs.get(element).getMusic_title().charAt(0));
    }

    public class itemHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView artist;
        TextView duration;
        ImageView poster;
        public itemHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            artist=itemView.findViewById(R.id.artist);
            duration=itemView.findViewById(R.id.duration);
            poster=itemView.findViewById(R.id.poster);
            /*if (listener!=null){
                title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnMusicClicked(title,listener);
                        OnMusicClicked(artist,listener);
                        OnMusicClicked(duration,listener);
                        OnMusicClicked(poster,listener);
                        listener.onItemClick();
                    }
                });
            }*/
        }
    }

    /*public interface OnItemClickListener{
        void onItemClick();
    }

    public void setListener(OnItemClickListener listener)
    {
        this.listener=listener;
    }

    private void OnMusicClicked(View view,OnItemClickListener listener){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick();
            }
        });
    }*/
}
