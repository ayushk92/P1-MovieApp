package com.example.mynanodegreeapps.movieapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mynanodegreeapps.movieapp.Data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akhatri on 05/01/16.
 */
public class MovieDetailFragment extends Fragment {

    Movie movieDetail;
    TrailerViewAdapter trailerViewAdapter;
    UserReviewViewAdapter userReviewViewAdapter;
    boolean mIsfavourite;
    static final String MOVIE = "movie";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView;
        Bundle arguments = getArguments();
        if (arguments != null) {
            movieDetail = arguments.getParcelable(MovieDetailFragment.MOVIE);
        }

        if(movieDetail != null){
            rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
            final Button favouriteBtn = (Button) rootView.findViewById(R.id.favourite_btn);

            mIsfavourite = checkMarkedMovie(getActivity(),movieDetail.getId());
            if(mIsfavourite)
                favouriteBtn.setText(getString(R.string.marked_favourite_btn));

            favouriteBtn.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    // Perform action on click
                    if(movieDetail != null && favouriteBtn.getText().equals(getString(R.string.favourite_btn))){
                        Global.markMovieAsFavourite(getActivity(),movieDetail);
                        favouriteBtn.setText(getString(R.string.marked_favourite_btn));
                    }
                }
            });

            TextView movieTitle = (TextView)rootView.findViewById(R.id.movieTitle);
            TextView movieOverview = (TextView)rootView.findViewById(R.id.movieOverview);
            TextView movieRating = (TextView)rootView.findViewById(R.id.movieRating);
            TextView movieReleaseDate = (TextView)rootView.findViewById(R.id.movieReleaseDate);
            ImageView moviePoster = (ImageView) rootView.findViewById(R.id.moviePosterDetail);
            TextView trailerLabel = (TextView) rootView.findViewById(R.id.trailer_label);
            ListView movieTrailers = (ListView) rootView.findViewById(R.id.movieTrailers);
            ListView userReviewListView = (ListView) rootView.findViewById(R.id.userReviews);

            movieTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Trailer trailer = trailerViewAdapter.getItem(position);

                    Uri trailerURI = Global.getTrailerURI(trailer);

                    if(trailerURI != null){
                    Intent detailActivityIntent = new Intent(Intent.ACTION_VIEW,trailerURI);
                    startActivity(detailActivityIntent);
                    }
                }
            });

            trailerViewAdapter = new TrailerViewAdapter(getActivity(), R.layout.trailer_item, new ArrayList<Trailer>());
            movieTrailers.setAdapter(trailerViewAdapter);

            userReviewViewAdapter = new UserReviewViewAdapter(getActivity(),R.layout.userreview_item,new ArrayList<UserReview>());
            userReviewListView.setAdapter(userReviewViewAdapter);

            if (savedInstanceState == null) {
                if(movieDetail != null) {
                    movieTitle.setText(movieDetail.getTitle());
                    movieOverview.setText(movieDetail.getOverview());
                    movieRating.setText(Double.toString(movieDetail.getRating()) + "/10");
                    movieReleaseDate.setText(Global.dfMovieDetail.format(movieDetail.getRelease_date()));
                    Global.SetImage(getActivity(),moviePoster,movieDetail);
                    getMovieDetailData();
                }

            } else {
                movieTitle.setText(movieDetail.getTitle());
                Global.SetImage(getActivity(), moviePoster, movieDetail);
            }
        }
        else {
            rootView =  super.onCreateView(inflater,container,savedInstanceState);
        }

        return rootView ;
    }

    private void getMovieDetailData(){
        FetchMovieDetailTask fetchMovieDetailTask = new FetchMovieDetailTask(getActivity(),movieDetail,mIsfavourite,trailerViewAdapter,userReviewViewAdapter,getString(R.string.tmdb_appkey));
        fetchMovieDetailTask.execute();
    }
    private boolean checkMarkedMovie(Context context,int movieId){
        Cursor movieWithId = context.getContentResolver().query(MovieContract.MovieEntry.buildMovieDetailUri(String.valueOf(movieId))
                                                                    ,new String[]{"COUNT(*)"}
                                                                    ,null
                                                                    ,new String[]{String.valueOf(movieId)}
                                                                    ,null);
        movieWithId.moveToFirst();

        if(movieWithId != null){
            int count = movieWithId.getInt(0);
            movieWithId.close();
            return count == 1;
        }
        movieWithId.close();
        return false;

    }
}
