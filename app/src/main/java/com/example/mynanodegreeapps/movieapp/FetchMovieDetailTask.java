package com.example.mynanodegreeapps.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;

import com.example.mynanodegreeapps.movieapp.Data.MovieContract;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by akhatri on 05/01/16.
 */
public class FetchMovieDetailTask  extends AsyncTaskLoader<String> {

    Context context;
    Movie movie;
    private String appKey;
    boolean IsFavourite;



    public FetchMovieDetailTask(Context context,
                                Movie movie,
                                boolean IsFavourite,
                                String appKey){
        super(context);
        this.context = context;
        this.movie = movie;
        this.IsFavourite = IsFavourite;
        this.appKey = appKey;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        String trailersJSON = "";
        String userreviewsJSON = "";
        try{
            if(!IsFavourite){
                String trailersURI = Global.getTrailersURI(movie.getId(), appKey);

                trailersJSON = Global.getJSONDataFromURL(new URL(trailersURI));

                ArrayList<Trailer> trailers = Global.parseJSONToTrailer(trailersJSON);
                movie.setTrailers(trailers);

                String userReviewsURI = Global.getUserReviewsURI(movie.getId(), appKey);

                userreviewsJSON = Global.getJSONDataFromURL(new URL((userReviewsURI)));

                ArrayList<UserReview> userReviews = Global.parseJSONToUserReview(userreviewsJSON);
                movie.setUserReviews(userReviews);
            }

        }
        catch (MalformedURLException ex){
            Log.e("FetchMovieDetailTask",ex.getMessage());
        }
        catch (JSONException e) {
            Log.e("FetchMovieDetailTask", e.getMessage());
        }
        return trailersJSON;
    }


}
