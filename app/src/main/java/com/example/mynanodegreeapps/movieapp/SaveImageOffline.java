package com.example.mynanodegreeapps.movieapp;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by akhatri on 08/01/16.
 */
public class SaveImageOffline extends AsyncTask<String,Void,String> {

    private ArrayList<Movie> movies;
    private GridViewAdapter gridViewAdapter;

    public SaveImageOffline(ArrayList<Movie> movie,GridViewAdapter popularMoviesAdapter){
        this.movies = movie;
        this.gridViewAdapter = popularMoviesAdapter;
    }

    @Override
    protected void onPostExecute(String s) {
        for (Movie m : movies)
            gridViewAdapter.add(m);
    }

    @Override
    protected String doInBackground(String... params) {

        for(Movie m : movies)
            m.setImageByteArray(Global.getImageByteArray(m.getPosterImage()));
        Log.d("SaveImageOffline","Finished");
        return null;
    }
}