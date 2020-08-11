package com.example.mymusic_final.util;

import android.content.ComponentName;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.example.mymusic_final.R;

import java.util.Random;

public class Util {

    public static class Background{
        private static Random random= new Random();
        private static int max;
        private static int min;
        private static int rand;
        private static String uri;
        private static Integer imageResourceId;

        public static void setMax(int max) {
            Background.max = max;
        }

        public static void setMin(int min) {
            Background.min = min;
        }

        public static Integer  getRandomBackground(Context context){
            rand=random.nextInt((max-min)+1)+min;
            uri="@drawable/background_"+rand;
            imageResourceId=context.getResources().getIdentifier(uri,null,context.getPackageName());
            return imageResourceId;
        }
    }
}
