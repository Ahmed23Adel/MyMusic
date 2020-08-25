package com.example.mymusic_final.Adapter;

import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mymusic_final.Pojo.Album_item;
import com.example.mymusic_final.Pojo.Music_item;
import com.example.mymusic_final.Pojo.Specific_folder;
import com.example.mymusic_final.R;
import com.example.mymusic_final.dummy.DummyContent.DummyItem;
import com.example.mymusic_final.play_cloud.Music_player;
import com.example.mymusic_final.util.Util;
import com.turingtechnologies.materialscrollbar.INameableAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class adapter_albums extends RecyclerView.Adapter<adapter_albums.ViewHolder> implements INameableAdapter {

    ArrayList<Specific_folder> listOfAlbums;
    Context context;
    public static int position;
    public OnClickListener listener;
    public adapter_albums setContext(Context context) {
        this.context = context;
        return this;
    }

    public adapter_albums setListOfSongs(ArrayList<Specific_folder> listOfAlbums) {
        this.listOfAlbums = listOfAlbums;
        return this;
    }

    @Override
    public Character getCharacterForElement(int element) {
        return Character.valueOf(listOfAlbums.get(element).getName().charAt(0));
    }
    public adapter_albums() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Specific_folder currentAlbumItem=listOfAlbums.get(position);

        holder.title.setText(currentAlbumItem.getName());
        Glide.with(context).load(currentAlbumItem.getPic_uri()).error(R.drawable.audio_track).placeholder(R.drawable.audio_track)
                .into(holder.albumPic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("main","k"+currentAlbumItem.getId());;
                listener.onClick(currentAlbumItem.getId());
            }
        });

        /*ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), currentAlbumItem.getAlbumUri());
        try {
            Bitmap bitmap = ImageDecoder.decodeBitmap(source);
            Palette palette=Util.platte.createPaletteSync(bitmap);
            int colors=palette.getDominantColor(000);
            holder.title.setBackgroundColor(colors);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

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
    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }
}