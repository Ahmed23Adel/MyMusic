package com.example.mymusic_final.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymusic_final.Adapter.adapter_music;
import com.example.mymusic_final.R;
import com.example.mymusic_final.Adapter.adapter_albums;
import com.example.mymusic_final.View.album_details;
import com.example.mymusic_final.dummy.DummyContent;
import com.example.mymusic_final.util.Constants;
import com.example.mymusic_final.util.Stored_music;
import com.google.android.material.snackbar.Snackbar;
import com.turingtechnologies.materialscrollbar.MaterialScrollBar;

import java.util.ArrayList;
import java.util.logging.Logger;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * A fragment representing a list of Items.
 */
public class fragmentalbums extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private final int requestCode_readExternalStorage=1;
    RecyclerView recyclerView;
    private adapter_albums adapter_albums=new adapter_albums();
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public fragmentalbums() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static fragmentalbums newInstance(int columnCount) {
        fragmentalbums fragment = new fragmentalbums();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {

                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            if (Stored_music.isExternalReadGranted(getContext())){
                showAlbums();
            }else{
                requestPermissionForExternalStorage();
            }
            //recyclerView.setAdapter(new adapter_albums(DummyContent.ITEMS));
        }
        return view;
    }

    public void requestPermissionForExternalStorage(){
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode_readExternalStorage);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==requestCode_readExternalStorage){
            if (grantResults[0]==PERMISSION_GRANTED){
                showAlbums();
            }else{
                Snackbar.make(getView(),"Permission not granted. We can't work without it",Snackbar.LENGTH_LONG).setAction("Grant it", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestPermissionForExternalStorage();
                    }
                }).show();
            }
        }
    }

    public void showAlbums(){
        Stored_music.getAlbums(getContext()).observe(getActivity(), music_items ->{
            setRecyclerView((ArrayList) music_items);
        });
    }

    void setRecyclerView(ArrayList music_items){
        recyclerView.setHasFixedSize(true);
        adapter_albums= new adapter_albums().setListOfSongs(music_items).setContext(getContext());

        adapter_albums.setListener(new adapter_albums.OnClickListener() {
            @Override
            public void onClick(int id) {
                Intent intent= new Intent(getContext(), album_details.class);
                Log.v("main","kkkk"+id);
                intent.putExtra(Constants.Music.ID,id);
                intent.setAction(album_details.ACTION_ALBUMS);
                //intent.putExtra(Constants.Music.DETAILS_ACTIONS,album_details.ACTIONS_ALBUMS);
                getContext().startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter_albums);

        //for sidebar scroll alphabetically
        MaterialScrollBar materialScrollBar = new MaterialScrollBar(getContext(), recyclerView);
        materialScrollBar.addSectionIndicator(getContext());
        materialScrollBar.setAutoHide(true);
        materialScrollBar.setTextColour(R.color.black);
    }


}