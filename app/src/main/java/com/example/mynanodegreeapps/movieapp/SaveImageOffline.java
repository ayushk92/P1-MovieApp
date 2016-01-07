package com.example.mynanodegreeapps.movieapp;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by akhatri on 08/01/16.
 */
public class SaveImageOffline extends AsyncTask<String,Void,String> {

    private Movie movie;

    public SaveImageOffline(Movie movie){
        this.movie = movie;
    }

    @Override
    protected String doInBackground(String... params) {

        movie.setImageByteArray(Global.getImageByteArray(movie.getPosterImage()));
        Log.d("ImageSavedOffline",movie.getTitle());
        return null;
    }
}