package com.example.mymusic_final.Fragments;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymusic_final.Adapter.adapter_music;
import com.example.mymusic_final.Observing.Observable_Stored_music;
import com.example.mymusic_final.Observing.Observer_Stored_music;
import com.example.mymusic_final.R;
import com.example.mymusic_final.util.Stored_music;
import com.google.android.material.snackbar.Snackbar;
import com.turingtechnologies.materialscrollbar.MaterialScrollBar;

import java.util.ArrayList;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_music#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_music extends Fragment implements Observer_Stored_music {


    private final int requestCode_readExternalStorage = 1;

    private RecyclerView recyclerView;
    private static adapter_music adapterMusic;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ACTIONS_ALBUM_ID = "abid";
    private int album_id = -1;

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    // TODO: Rename and change types of parameters
    //if action == null it will show all music list
    private String ACTION;
    private String mParam2;

    public fragment_music() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ACTION Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_music.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_music newInstance(String ACTION, String param2) {
        fragment_music fragment = new fragment_music();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, ACTION);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ACTION = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Observable_Stored_music.subscribe(this);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_music, container, false);
        recyclerView = root.findViewById(R.id.music_recycler_view);
        if (Stored_music.isExternalReadGranted(getContext())) {
            if (ACTION == null) {
                showMusic();
            } else if (ACTIONS_ALBUM_ID == ACTIONS_ALBUM_ID) {
                showMusic(album_id);
            }

        } else {
            requestPermissionForExternalStorage();
        }
        return root;
    }

    public void requestPermissionForExternalStorage() {
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode_readExternalStorage);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestCode_readExternalStorage) {
            if (grantResults[0] == PERMISSION_GRANTED) {
                showMusic();
            } else {
                Snackbar.make(getView(), "Permission not granted. We can't work without it", Snackbar.LENGTH_LONG).setAction("Grant it", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestPermissionForExternalStorage();
                    }
                }).show();
            }
        }
    }

    public void showMusic() {

        Stored_music.getListOfSongs(getContext()).observe(getActivity(), music_items -> {
            setRecyclerView((ArrayList) music_items);
           /* LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            adapterMusic= new adapter_music().setListOfSongs((ArrayList)music_items).setContext(getContext());
            recyclerView.setAdapter(adapterMusic);

            //for sidebar scroll alphabetically
            MaterialScrollBar materialScrollBar = new MaterialScrollBar(getContext(), recyclerView);
            materialScrollBar.addSectionIndicator(getContext());
            materialScrollBar.setAutoHide(true);
            materialScrollBar.setTextColour(R.color.black);*/

        });


    }

    public void showMusic(int id) {
        setRecyclerView(Stored_music.getMusicAtAlbumID(getContext(), id));
    }

    //for Stored_Music observer
    @Override
    public void updated() {
        recyclerView.setAdapter(null);
        showMusic();
    }

    void setRecyclerView(ArrayList music_items) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapterMusic = new adapter_music().setListOfSongs(music_items).setContext(getContext());
        recyclerView.setAdapter(adapterMusic);

        //for sidebar scroll alphabetically
        MaterialScrollBar materialScrollBar = new MaterialScrollBar(getContext(), recyclerView);
        materialScrollBar.addSectionIndicator(getContext());
        materialScrollBar.setAutoHide(true);
        materialScrollBar.setTextColour(R.color.black);
    }

    public static adapter_music getAdapter() {
        return adapterMusic;
    }
}