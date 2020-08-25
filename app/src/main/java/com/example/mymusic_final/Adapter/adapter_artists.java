package com.example.mymusic_final.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymusic_final.Pojo.Album_item;
import com.example.mymusic_final.Pojo.Artist_item;
import com.example.mymusic_final.Pojo.Specific_folder;
import com.example.mymusic_final.R;
import com.turingtechnologies.materialscrollbar.INameableAdapter;

import java.util.ArrayList;

public class adapter_artists extends RecyclerView.Adapter<adapter_artists.ViewHolder> implements INameableAdapter {

    ArrayList<Specific_folder> listOfAlbums;
    Context context;
    public static int position;
    public adapter_albums.OnClickListener listener;

    public adapter_artists setContext(Context context) {
        this.context = context;
        return this;
    }

    public adapter_artists setListOfSongs(ArrayList<Specific_folder> listOfAlbums) {
        this.listOfAlbums = listOfAlbums;
        return this;
    }

    @Override
    public Character getCharacterForElement(int element) {
        return Character.valueOf(listOfAlbums.get(element).getName().charAt(0));
    }


    @NonNull
    @Override
    public adapter_artists.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Specific_folder currentArtistItem=listOfAlbums.get(position);

        holder.title.setText(currentArtistItem.getName());
        Glide.with(context).load(currentArtistItem.getPic_uri()).error(R.drawable.audio_track).placeholder(R.drawable.audio_track)
                .into(holder.albumPic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(currentArtistItem.getId());
            }
        });

    }


    @Override
    public int getItemCount() {
        return listOfAlbums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final ImageView albumPic;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title_album);
            albumPic = view.findViewById(R.id.album_pic);
        }
    }

    public interface OnClickListener{
        void onClick(int id);
    }

    //it's set by albums fragment which when clicked the section adapter goes to album_details
    public void setListener(adapter_albums.OnClickListener listener) {
        this.listener = listener;
    }
}