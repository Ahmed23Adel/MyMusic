package com.example.mymusic_final.Adapter;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
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

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;

public class adapter_music extends RecyclerView.Adapter<adapter_music.itemHolder> {

    List<Music_item> listOfSongs;
    Context context;

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
        holder.artist.setText(currentMusic.getArtist());
        holder.duration.setText(currentMusic.getDuration());
        Glide.with(context).load(currentMusic.getAlbumArt()).error(R.drawable.test)
               .into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return listOfSongs.size();
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
        }
    }

    public static Bitmap getAlbumart(Context context, Long album_id){
        final Bitmap[] bm = {null};
        Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {

                BitmapFactory.Options options = new BitmapFactory.Options();
                try{
                    final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                    Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
                    ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                    if (pfd != null){
                        FileDescriptor fd = pfd.getFileDescriptor();
                        bm[0] = BitmapFactory.decodeFileDescriptor(fd, null, options);
                        //context.notifyAll();
                        pfd = null;
                        fd = null;
                    }
                } catch(Error ee){}
                catch (Exception e) {}

            }
        });
        return bm[0];
    }
}
