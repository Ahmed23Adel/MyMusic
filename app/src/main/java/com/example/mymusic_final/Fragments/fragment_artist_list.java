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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymusic_final.Adapter.adapter_albums;
import com.example.mymusic_final.Adapter.adapter_artists;
import com.example.mymusic_final.Pojo.Artist_item;
import com.example.mymusic_final.Pojo.Specific_folder;
import com.example.mymusic_final.R;
import com.example.mymusic_final.View.album_details;
import com.example.mymusic_final.util.Constants;
import com.example.mymusic_final.util.Stored_music;
import com.google.android.material.snackbar.Snackbar;
import com.turingtechnologies.materialscrollbar.MaterialScrollBar;

import java.util.ArrayList;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_artist_list#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_artist_list extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final int requestCode_readExternalStorage=1;
    RecyclerView recyclerView;
    private adapter_artists adapterArtists=new adapter_artists();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_artist_list() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment fragment_artist_list.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_artist_list newInstance(int columnCount) {
        fragment_artist_list fragment = new fragment_artist_list();
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
        // Inflate the layout for this fragment
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
                showArtists();
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
                showArtists();
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

    public void showArtists(){

        Stored_music.getArtists(getContext()).observe(getActivity(), music_items ->{
            setRecyclerView((ArrayList) music_items);
        });
    }

    void setRecyclerView(ArrayList<Specific_folder> music_items){
        recyclerView.setHasFixedSize(true);
        adapterArtists= new adapter_artists().setListOfSongs(music_items).setContext(getContext());

        adapterArtists.setListener(new adapter_albums.OnClickListener() {
            @Override
            public void onClick(int id) {
                Intent intent= new Intent(getContext(), album_details.class);
                intent.putExtra(Constants.Music.ID,id);
                intent.setAction(album_details.ACTION_ARTISTS);
                getContext().startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapterArtists);

        //for sidebar scroll alphabetically
        //MaterialScrollBar materialScrollBar = new MaterialScrollBar(getContext(), recyclerView);
        //materialScrollBar.addSectionIndicator(getContext());
       // materialScrollBar.setAutoHide(true);
        //materialScrollBar.setTextColour(R.color.black);
    }
}