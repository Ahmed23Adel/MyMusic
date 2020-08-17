package com.example.mymusic_final.Observing;

import com.example.mymusic_final.Pojo.Music_item;
import com.example.mymusic_final.play_cloud.Observer;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public interface Observable_Stored_music {
    CopyOnWriteArraySet<Observer_Stored_music> listOfObservers= new CopyOnWriteArraySet<Observer_Stored_music>();

    static void subscribe(Observer_Stored_music observer) {
        listOfObservers.add(observer);

    }

    static void unsubscribe(Observer observer) {
        listOfObservers.remove(observer);

    }

    static void notifyObservers() {
        for (Observer_Stored_music o: listOfObservers){
            o.updated();
        }
    }
}
