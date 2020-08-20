package com.example.mymusic_final.util;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.palette.graphics.Palette;

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


    public static  class platte{
        public static Palette createPaletteSync(Bitmap bitmap) {
            Palette p = Palette.from(bitmap).generate();
            return p;
        }

        public void createPaletteAsync(Bitmap bitmap) {
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette p) {
                    // Use generated instance
                }
            });
        }
    }
}
