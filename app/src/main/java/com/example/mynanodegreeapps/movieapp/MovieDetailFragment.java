package com.example.mynanodegreeapps.movieapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    Movie movieDetail;
    TrailerViewAdapter trailerViewAdapter;
    UserReviewViewAdapter userReviewViewAdapter;
    boolean mIsfavourite;
    static final String MOVIE = "movie";

    private static final int DETAIL_LOADER = 0;

    FetchMovieDetailTask fetchMovieDetailTask;
    private ShareActionProvider mShareActionProvider;
    private MenuItem menuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView;
        //setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().invalidateOptionsMenu();
        inflater.inflate(R.menu.movie_detail_share, menu);

        // Retrieve the share menu item
        menuItem = menu.findItem(R.id.action_share);


        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if(movieDetail != null && movieDetail.getTrailers() != null && movieDetail.getTrailers().size() > 0)
            mShareActionProvider.setShareIntent(Global.createShareTrailerIntent(movieDetail));


    }



    private void getMovieDetailData(){
//        fetchMovieDetailTask = new FetchMovieDetailTask(getActivity(),
//                                                        movieDetail,
//                                                        mIsfavourite,
//                                                        trailerViewAdapter,
//                                                        userReviewViewAdapter,
//                                                        mShareActionProvider,
//                                                        getString(R.string.tmdb_appkey));
//        fetchMovieDetailTask.execute();
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        if(movieDetail != null){
            fetchMovieDetailTask = new FetchMovieDetailTask(getActivity(),
                    movieDetail,
                    mIsfavourite,
                    getString(R.string.tmdb_appkey));
            return fetchMovieDetailTask;
        }
        else
            return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Context context = getActivity();
        if(mIsfavourite){
            Cursor trailerCursor =  context.getContentResolver().query(
                    MovieContract.TrailerEntry.buildTrailerWithMovieUri(String.valueOf(movieDetail.getId())),
                    TRAILER_COLUMNS,
                    null,
                    new String[]{String.valueOf(movieDetail.getId())},
                    null);
            ArrayList<Trailer> tempTrailers = new ArrayList<Trailer>();
            while (trailerCursor.moveToNext()){
                tempTrailers.add(new Trailer(trailerCursor.getString(COL_TRAILER_NAME)
                        ,trailerCursor.getString(COL_TRAILER_SITE)
                        ,trailerCursor.getString(COL_TRAILER_KEY)));
            }
            movieDetail.setTrailers(tempTrailers);
            for(Trailer t : tempTrailers)
                trailerViewAdapter.add(t);

            Cursor userReviewCursor =  context.getContentResolver().query(
                    MovieContract.UserReviewEntry.buildUserReviewWithMovieUri(String.valueOf(movieDetail.getId())),
                    USER_REVIEW_COLUMNS,
                    null,
                    new String[]{String.valueOf(movieDetail.getId())},
                    null);
            ArrayList<UserReview> tempUserReview = new ArrayList<UserReview>();
            while (userReviewCursor.moveToNext()){
                tempUserReview.add(new UserReview(userReviewCursor.getString(COL_USER_REVIEW_ID)
                        ,userReviewCursor.getString(COL_USER_REVIEW_AUTHOR)
                        ,userReviewCursor.getString(COL_USER_REVIEW_CONTENT)));
            }
            movieDetail.setUserReviews(tempUserReview);
            for(UserReview ur : tempUserReview)
                userReviewViewAdapter.add(ur);

        }
        else {
            if(movieDetail.getTrailers() != null){
                for(Trailer t : movieDetail.getTrailers())
                    trailerViewAdapter.add(t);
            }
            if(movieDetail.getUserReviews() != null){
                for(UserReview ur : movieDetail.getUserReviews())
                    userReviewViewAdapter.add(ur);
            }
        }
        trailerViewAdapter.notifyDataSetChanged();
        userReviewViewAdapter.notifyDataSetChanged();
        if(movieDetail != null && movieDetail.getTrailers() != null && movieDetail.getTrailers().size() > 0)
            setHasOptionsMenu(true);

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }


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


}

