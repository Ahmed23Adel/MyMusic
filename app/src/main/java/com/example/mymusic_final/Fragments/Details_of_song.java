package com.example.mymusic_final.Fragments;

import android.content.ContentUris;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mymusic_final.Pojo.Details_music_item;
import com.example.mymusic_final.Pojo.Music_item;
import com.example.mymusic_final.R;
import com.example.mymusic_final.play_cloud.Music_player;
import com.example.mymusic_final.util.Stored_music;

import java.io.IOException;
import java.text.CharacterIterator;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Date;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Details_of_song#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Details_of_song extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Music_item musicItem;

    public Details_of_song() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Details_of_song.
     */
    // TODO: Rename and change types and number of parameters
    public static Details_of_song newInstance(String param1, String param2) {
        Details_of_song fragment = new Details_of_song();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void setMusicItem(Music_item musicItem){
        this.musicItem=musicItem;
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
        // Inflate the layout for this fragment]
        View root= inflater.inflate(R.layout.fragment_details_of_song, container, false);
        TextView title= root.findViewById(R.id.title_details);
        TextView artist= root.findViewById(R.id.artist_details);
        TextView composer= root.findViewById(R.id.composer_details);
        TextView album= root.findViewById(R.id.album_details);
        TextView duration= root.findViewById(R.id.duration_details);
        TextView filePath= root.findViewById(R.id.filePath_details);
        TextView fileName= root.findViewById(R.id.fileName_details);
        TextView size= root.findViewById(R.id.size_details);
        TextView format= root.findViewById(R.id.format_details);
        TextView bitrateView= root.findViewById(R.id.bitrate_details);
        TextView samplingRate= root.findViewById(R.id.samplingRate_details);
        TextView dataAdded= root.findViewById(R.id.date_added_details);
        TextView dateModified= root.findViewById(R.id.last_modified_details);
        TextView isAlarm= root.findViewById(R.id.isAlarm_details);
        TextView isRingtone= root.findViewById(R.id.isRingtone_details);
        TextView channelsCount= root.findViewById(R.id.channels_count_details);


        setTextTo(title,musicItem.getMusic_title(),R.string.title);
        setTextTo(artist,musicItem.getArtist(),R.string.artist);
        setTextTo(album,musicItem.getAlbumName(),R.string.album);
        setTextTo(duration,musicItem.getDuration(),R.string.duration);
        setTextTo(filePath,musicItem.getPath(),R.string.file_path);
        setTextTo(fileName, getLastSegmentSLASH(musicItem.getPath()),R.string.file_name);
        setTextTo(format, getLastSegmentDOT(musicItem.getPath()),R.string.format);

        Stored_music.getDetailsAtId(getContext(), musicItem.get_ID()).observe(getActivity(),o -> {
            Details_music_item detailsMusicItem= (Details_music_item)o;
            setTextTo(size,Details_music_item.humanReadableByteCountBin(Long.valueOf(detailsMusicItem.getSize())),R.string.size);
            setTextTo(composer,detailsMusicItem.getComposer(),R.string.composer);
            setTextTo(isAlarm, detailsMusicItem.isAlarm() ?"True":"False",R.string.is_alarm);
            setTextTo(isRingtone, detailsMusicItem.isRingtone() ?"True":"False",R.string.is_ringtone);
            setTextTo(dataAdded,detailsMusicItem.getDateAdded(),R.string.date_added);
            setTextTo(dateModified,detailsMusicItem.getLastModifiedDate(),R.string.last_modified);

        });


        MediaExtractor mex = new MediaExtractor();
        try {
            mex.setDataSource(Music_player.getListOfSongs().get(Music_player.getPosition()).getPath());// the adresss location of the sound on sdcard.
        } catch (IOException e) {
            e.printStackTrace();
        }

        MediaFormat mf = mex.getTrackFormat(0);

        int bitRate = mf.getInteger(MediaFormat.KEY_BIT_RATE);
        int sampleRate = mf.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        int channelCount = mf.getInteger(MediaFormat.KEY_CHANNEL_COUNT);

        setTextTo(bitrateView,humanReadableRate(Long.valueOf(bitRate),"b/s"),R.string.bitrate);
        setTextTo(samplingRate,humanReadableRate(Long.valueOf(sampleRate),"Hz"),R.string.sampling_rate);
        setTextTo(channelsCount,String.valueOf(channelCount),R.string.channels_count);
        return root;
    }

    public static String humanReadableRate(long rate,String SI) {
        if ( -1000 < rate && rate<1000){
            return rate+" "+SI;
        }
        CharacterIterator ci= new StringCharacterIterator("KMGTPE");
        while( rate <= -999_950 || rate >= 999_950){
            rate/=1000;
            ci.next();
        }
        return String.format("%.1f %c"+SI,rate/1000.0,ci.current());
    }

    public String getLastSegmentSLASH(String text){
        return text.substring(text.lastIndexOf("/")+1);
    }
    public String getLastSegmentDOT(String text){
        return text.substring(text.lastIndexOf(".")+1);
    }

    //it sets text to differnt sections like album, artist,..
    public void setTextTo(TextView textView, String text, int stringId){
        if (text!=null){
            if (!text.equals("<unknown>")&&!text.isEmpty()){
                textView.setText((HtmlCompat.fromHtml("<b>" + getString(stringId) + "</b>"  +
                        "<small>" + ": "+text + "</small>" + "<br />",HtmlCompat.FROM_HTML_MODE_LEGACY)));
            }else{
                textView.setVisibility(View.GONE);

            }
        }
       else{
            textView.setVisibility(View.GONE);
        }
    }

}