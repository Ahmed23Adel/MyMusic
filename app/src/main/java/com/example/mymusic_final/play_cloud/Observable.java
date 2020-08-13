package com.example.mymusic_final.play_cloud;

import android.app.Activity;

import com.example.mymusic_final.Pojo.Music_item;
import com.example.mymusic_final.View.Music_details;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public interface Observable {

     CopyOnWriteArraySet<Observer> listOfObservers= new CopyOnWriteArraySet<Observer>();

     static void subscribe(Observer observer) {
          listOfObservers.add(observer);

     }

     static void unsubscribe(Observer observer) {
          listOfObservers.remove(observer);

     }

     static void notifyObservers(ArrayList<Music_item> listOfSongs, int position) {
          for (Observer o: listOfObservers){
               o.updated(listOfSongs,position);
          }
     }


}
