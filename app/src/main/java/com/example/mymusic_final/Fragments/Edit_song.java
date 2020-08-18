package com.example.mymusic_final.Fragments;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mymusic_final.Pojo.Music_item;
import com.example.mymusic_final.R;
import com.example.mymusic_final.util.Stored_music;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Edit_song#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Edit_song extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Music_item musicItem;
    private OnCancel onCancel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Edit_song() {
        // Required empty public constructor
    }
    public void setMusicItem(Music_item musicItem){
        this.musicItem=musicItem;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Edit_song.
     */
    // TODO: Rename and change types and number of parameters
    public static Edit_song newInstance(String param1, String param2) {
        Edit_song fragment = new Edit_song();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_edit_song, container, false);
        TextView title=root.findViewById(R.id.title_editor);
        TextView artist=root.findViewById(R.id.artist_editor);
        TextView album=root.findViewById(R.id.album_editor);
        Button save=root.findViewById(R.id.saveEdit);
        Button cancel=root.findViewById(R.id.cancelEdit);

        title.setText(musicItem.getMusic_title());
        artist.setText(musicItem.getArtist());
        album.setText(musicItem.getAlbumName());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues content = new ContentValues();
                content.put(MediaStore.Audio.Media.TITLE,title.getText().toString());
                content.put(MediaStore.Audio.Media.ARTIST,artist.getText().toString());
                content.put(MediaStore.Audio.Media.ALBUM,album.getText().toString());
                Stored_music.updateAtId(getContext(),musicItem.get_ID(),content);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel.onCancelClicked();
            }
        });

        return root;
    }

    public interface OnCancel{
        void onCancelClicked();
    }

    public void setOnCancel(OnCancel onCancel) {
        this.onCancel = onCancel;
    }
}