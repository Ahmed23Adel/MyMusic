package com.example.mymusic_final.Fragments;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mymusic_final.Adapter.adapter_music;
import com.example.mymusic_final.Pojo.Music_item;
import com.example.mymusic_final.R;
import com.example.mymusic_final.util.Stored_music;
import com.example.mymusic_final.util.Util;
import com.example.mymusic_final.util.Util.Background;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_music#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_music extends Fragment {


    private final int requestCode_readExternalStorage=1;

    private RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_music() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_music.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_music newInstance(String param1, String param2) {
        fragment_music fragment = new fragment_music();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_music, container, false);
        recyclerView= root.findViewById(R.id.music_recycler_view);
        if (Stored_music.isExternalReadGranted(getContext())){
           showMusic();
        }else{
            requestPermissionForExternalStorage();
        }
        return root;
    }

    public void requestPermissionForExternalStorage(){
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode_readExternalStorage);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==requestCode_readExternalStorage){
            if (grantResults[0]==PERMISSION_GRANTED){
                showMusic();
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

    public void showMusic(){
        Stored_music.getListOfSongs(getContext()).observe(getActivity(),music_items ->{
            LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            adapter_music adapterMusic= new adapter_music().setListOfSongs(music_items);
            recyclerView.setAdapter(adapterMusic);
        });


    }
}