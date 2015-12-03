package com.example.mynanodegreeapps.movieapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends ActionBarActivity {

    Movie movieDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        TextView movieTitle = (TextView)findViewById(R.id.movieTitle);
        TextView movieOverview = (TextView)findViewById(R.id.movieOverview);
        TextView movieRating = (TextView)findViewById(R.id.movieRating);
        TextView movieReleaseDate = (TextView)findViewById(R.id.movieReleaseDate);
        ImageView moviePoster = (ImageView) findViewById(R.id.moviePosterDetail);
        if (savedInstanceState == null) {
            movieDetail = getIntent().getExtras().getParcelable("movieData");
            if(movieDetail != null) {
                movieTitle.setText(movieDetail.getTitle());
                movieOverview.setText(movieDetail.getOverview());
                movieRating.setText(Double.toString(movieDetail.getRating()) + "/10");
                movieReleaseDate.setText(Global.dfMovieDetail.format(movieDetail.getRelease_date()));
                Picasso.with(this).load(movieDetail.getPosterImage()).into(moviePoster);
            }
        } else {
            movieTitle.setText(movieDetail.getTitle());
            Picasso.with(this).load(movieDetail.getPosterImage()).into(moviePoster);
        }
    }
}
