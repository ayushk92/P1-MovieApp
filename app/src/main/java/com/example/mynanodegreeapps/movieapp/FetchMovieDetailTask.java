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

    Context context;
    Movie movie;
    TrailerViewAdapter trailerViewAdapter;
    UserReviewViewAdapter userReviewViewAdapter;
    private String appKey;
    boolean IsFavourite;
    ShareActionProvider shareActionProvider;

    private static final String[] TRAILER_COLUMNS = {MovieContract.TrailerEntry.COLUMN_TRAILER_NAME,
                                                     MovieContract.TrailerEntry.COLUMN_TRAILER_SITE,
                                                     MovieContract.TrailerEntry.COLUMN_TRAILER_KEY};

    private static final int COL_TRAILER_NAME = 0;
    private static final int COL_TRAILER_SITE = 1;
    private static final int COL_TRAILER_KEY = 2;

    private static final String[] USER_REVIEW_COLUMNS = {MovieContract.UserReviewEntry.COLUMN_ID,
                                                         MovieContract.UserReviewEntry.COLUMN_AUTHOR,
                                                         MovieContract.UserReviewEntry.COLUMN_CONTENT};

    private static final int COL_USER_REVIEW_ID = 0;
    private static final int COL_USER_REVIEW_AUTHOR = 1;
    private static final int COL_USER_REVIEW_CONTENT = 2;



    public FetchMovieDetailTask(Context context,
                                Movie movie,
                                boolean IsFavourite,
                                TrailerViewAdapter trailerViewAdapter,
                                UserReviewViewAdapter userReviewViewAdapter ,
                                ShareActionProvider shareActionProvider,
                                String appKey){
        super(context);
        this.context = context;
        this.movie = movie;
        this.IsFavourite = IsFavourite;
        this.appKey = appKey;
        this.trailerViewAdapter = trailerViewAdapter;
        this.userReviewViewAdapter = userReviewViewAdapter;
        this.shareActionProvider = shareActionProvider;
    }

//    @Override
//    protected void onPostExecute(String s) {
//
//        if(IsFavourite){
//            Cursor trailerCursor =  context.getContentResolver().query(
//                    MovieContract.TrailerEntry.buildTrailerWithMovieUri(String.valueOf(movie.getId())),
//                    TRAILER_COLUMNS,
//                    null,
//                    new String[]{String.valueOf(movie.getId())},
//                    null);
//            while (trailerCursor.moveToNext()){
//                trailerViewAdapter.add(new Trailer(trailerCursor.getString(COL_TRAILER_NAME)
//                                                  ,trailerCursor.getString(COL_TRAILER_SITE)
//                                                  ,trailerCursor.getString(COL_TRAILER_SITE)));
//            }
//
//            Cursor userReviewCursor =  context.getContentResolver().query(
//                    MovieContract.UserReviewEntry.buildUserReviewWithMovieUri(String.valueOf(movie.getId())),
//                    USER_REVIEW_COLUMNS,
//                    null,
//                    new String[]{String.valueOf(movie.getId())},
//                    null);
//            while (userReviewCursor.moveToNext()){
//                userReviewViewAdapter.add(new UserReview(userReviewCursor.getString(COL_USER_REVIEW_ID)
//                                                        ,userReviewCursor.getString(COL_USER_REVIEW_AUTHOR)
//                                                        ,userReviewCursor.getString(COL_USER_REVIEW_CONTENT)));
//            }
//
//        }
//        else {
//            if(movie.getTrailers() != null){
//                for(Trailer t : movie.getTrailers())
//                    trailerViewAdapter.add(t);
//            }
//            if(movie.getUserReviews() != null){
//                for(UserReview ur : movie.getUserReviews())
//                    userReviewViewAdapter.add(ur);
//            }
//        }
//        trailerViewAdapter.notifyDataSetChanged();
//        userReviewViewAdapter.notifyDataSetChanged();
//    }
//    @Override
//    protected String doInBackground(String... params) {
//
//        String trailersJSON = "";
//        String userreviewsJSON = "";
//        try{
//            if(!IsFavourite){
//                String trailersURI = Global.getTrailersURI(movie.getId(), appKey);
//
//                trailersJSON = Global.getJSONDataFromURL(new URL(trailersURI));
//
//                ArrayList<Trailer> trailers = Global.parseJSONToTrailer(trailersJSON);
//                movie.setTrailers(trailers);
//
//                String userReviewsURI = Global.getUserReviewsURI(movie.getId(), appKey);
//
//                userreviewsJSON = Global.getJSONDataFromURL(new URL((userReviewsURI)));
//
//                ArrayList<UserReview> userReviews = Global.parseJSONToUserReview(userreviewsJSON);
//                movie.setUserReviews(userReviews);
//            }
//
//        }
//        catch (MalformedURLException ex){
//            Log.e("FetchMovieDetailTask",ex.getMessage());
//        }
//        catch (JSONException e) {
//            Log.e("FetchMovieDetailTask", e.getMessage());
//        }
//        return trailersJSON;
//    }
}
