package com.example.mymusic_final.Pojo;

import android.util.Log;

import java.text.CharacterIterator;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Date;
import java.util.TimeZone;

public class Details_music_item {

    private String size;
    private String composer;
    private String date_added;
    private String last_modified;
    private boolean isAlarm;
    private boolean isRingtone;

    public Details_music_item(String size, String composer, String date_added, String last_modified, boolean isAlarm, boolean isRingtone) {
        this.size = size;
        this.composer = composer;
        this.date_added = date_added;
        this.last_modified = last_modified;
        this.isAlarm = isAlarm;
        this.isRingtone = isRingtone;
    }

    public Details_music_item() {
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(String last_modified) {
        this.last_modified = last_modified;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }

    public boolean isRingtone() {
        return isRingtone;
    }

    public void setRingtone(boolean ringtone) {
        isRingtone = ringtone;
    }

    public String getDateAdded() {
        Date date = new Date(Long.parseLong(getDate_added()) * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYY-MM-dd ");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(date);

    }

    public String getLastModifiedDate() {
        Date date = new Date(Long.parseLong(getLast_modified()) * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYY-MM-dd ");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(date);

    }


    /**
     * in while--> we check for it it's more than million, if it is, then it's in Megabyte, until it reaches less than one million, then we divide it by 1000 in format lastly by 1000
     * but if not then we divide it just once by 1000
     * be
     * */
    public static String humanReadableByteCountBin(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        Log.v("main",String.valueOf(bytes));

        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        Log.v("main",ci.getIndex()+" "+String.valueOf(ci.current()));
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }

}
