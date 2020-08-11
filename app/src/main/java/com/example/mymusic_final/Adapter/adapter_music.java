package com.example.mymusic_final.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic_final.Pojo.Music_item;
import com.example.mymusic_final.R;

import java.util.ArrayList;
import java.util.List;

public class adapter_music extends RecyclerView.Adapter<adapter_music.itemHolder> {

    List<Music_item> listOfSongs;

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
        holder.artist.setText(currentMusic.getArtist());
        holder.duration.setText(currentMusic.getDuration());
    }

    @Override
    public int getItemCount() {
        return listOfSongs.size();
    }

    public class itemHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView artist;
        TextView duration;
        ImageView fav;
        ImageView poster;
        public itemHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            artist=itemView.findViewById(R.id.artist);
            duration=itemView.findViewById(R.id.duration);
            fav=itemView.findViewById(R.id.fav);
            poster=itemView.findViewById(R.id.poster);
        }
    }
}
