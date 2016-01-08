package com.example.mynanodegreeapps.movieapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.mynanodegreeapps.movieapp.Data.MovieContract;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;


public class PopularMoviesFragment extends Fragment {

    GridViewAdapter popularMoviesAdapter;
    ArrayList<Movie> popularMovieList = new ArrayList<Movie>();
    GridView popularMoviesView;
    int index;
    final String MOVIE_KEY = "savedMovies";
    private int mPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";
    public PopularMoviesFragment() {
        // Required empty public constructor
    }

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.COLUMN_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASEDATE,
            MovieContract.MovieEntry.COLUMN_POSTERIMAGE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RATING
    };

    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_TITLE = 1;
    static final int COL_MOVIE_RELEASEDATE = 2;
    static final int COL_MOVIE_POSTERIMAGE = 3;
    static final int COL_MOVIE_OVERVIEW= 4;
    static final int COL_MOVIE_RATING = 5;


    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = getActivity().getPreferences(0);
        mPosition = pref.getInt(SELECTED_KEY,-1);
        if(Global.isSortByChanged || mPosition != ListView.INVALID_POSITION){
            popularMoviesAdapter.clear();
            FetchMoviesData fetchMoviesData = new FetchMoviesData(getActivity());
            fetchMoviesData.execute();
            ((Callback) getActivity())
                    .onSortByChanged();

            Global.isSortByChanged = false;
        }
        else
        {
            popularMoviesView.setSelection(index);
        }



    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences pref = getActivity().getPreferences(0);
        SharedPreferences.Editor edt = pref.edit();
        edt.putInt(SELECTED_KEY, popularMoviesView.getFirstVisiblePosition());
        edt.commit();

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(savedInstanceState != null && savedInstanceState.containsKey(MOVIE_KEY)){
            popularMovieList = savedInstanceState.getParcelableArrayList(MOVIE_KEY);
        }
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Movie movie);
        public void onSortByChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

//        if (mPosition != ListView.INVALID_POSITION) {
//            outState.putInt(SELECTED_KEY, mPosition);
//        }
//        outState.putParcelableArrayList(MOVIE_KEY, popularMovieList);
        super.onSaveInstanceState(outState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_popular_movies, container, false);


        popularMoviesView = (GridView) rootView.findViewById(R.id.popularMovies_view);

//        if (popularMovieList == null)
//            popularMovieList = new ArrayList<Movie>();
        popularMoviesAdapter = new GridViewAdapter(getActivity(), R.layout.movie_item, popularMovieList);
        popularMoviesView.setAdapter(popularMoviesAdapter);

        popularMoviesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ((Callback) getActivity())
                        .onItemSelected(popularMoviesAdapter.getItem(position)
                        );

//                Intent detailActivityIntent = new Intent(getActivity(), MovieDetailActivity.class).putExtra("movieData", popularMoviesAdapter.getItem(position));
//                startActivity(detailActivityIntent);
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            popularMovieList = savedInstanceState.getParcelableArrayList(MOVIE_KEY);
            popularMoviesAdapter = new GridViewAdapter(getActivity(), R.layout.movie_item, popularMovieList);
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        return rootView;
    }

    public class FetchMoviesData extends AsyncTask<String,Void,String> {

        Context mContext;
        Cursor movieCursor;
        public FetchMoviesData(Context context){
            this.mContext = context;
            movieCursor = null;
        }



        @Override
        protected void onPostExecute(String s) {
            try {
                if(movieCursor != null){
                    //movieCursor.moveToFirst();
                    while (movieCursor.moveToNext()){
                        Movie item = new Movie(movieCursor.getInt(COL_MOVIE_ID),
                                                           movieCursor.getString(COL_MOVIE_TITLE),
                                                           movieCursor.getBlob(COL_MOVIE_POSTERIMAGE),
                                                           movieCursor.getString(COL_MOVIE_OVERVIEW),
                                                           movieCursor.getDouble(COL_MOVIE_RATING),
                                                           movieCursor.getString(COL_MOVIE_RELEASEDATE));
                        popularMoviesAdapter.add(item);
                    }
                }
                else {
                    popularMovieList = Global.parseJSONToMOVIE(s);
                    //popularMoviesAdapter.clear();
                    SaveImageOffline saveImageOffline;
                        saveImageOffline = new SaveImageOffline(popularMovieList,popularMoviesAdapter);
                        saveImageOffline.execute();
                }
                //popularMoviesAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.e("PopularMoviesFragment", e.getMessage());
            }
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {

            String movieDataJson = "";
            try{
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

                String sortByParam = prefs.getString(getString(R.string.pref_sortby_key),getString(R.string.pref_sortby_key));

                if(sortByParam.equals("favourite")){
                    movieCursor = mContext.getContentResolver().query(
                            MovieContract.MovieEntry.CONTENT_URI,
                            MOVIE_COLUMNS,
                            null,
                            null,
                            null);
                }
                else {
                    //Load image configuration
                    Global.parseJSONImageConfiguration(getString(R.string.tmdb_appkey));
                    URL fetchMovieDataURL = new URL(Global.getPopularMoviesURI(getString(R.string.tmdb_appkey),sortByParam));
                    movieDataJson = Global.getJSONDataFromURL(fetchMovieDataURL);

                }

            }
            catch (MalformedURLException ex){
                Log.e("PopularMoviesFragment",ex.getMessage());
            }
            return movieDataJson;
        }
    }



}
