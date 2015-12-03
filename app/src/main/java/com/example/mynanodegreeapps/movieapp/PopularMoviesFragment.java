package com.example.mynanodegreeapps.movieapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
    ArrayList<Movie> popularMovieList;
    GridView popularMoviesView;
    int index;
    public PopularMoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Global.isSortByChanged){
            popularMoviesAdapter.clear();
            FetchMoviesData fetchMoviesData = new FetchMoviesData();
            fetchMoviesData.execute();
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
        index = popularMoviesView.getFirstVisiblePosition();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_popular_movies, container, false);

        popularMoviesView = (GridView) rootView.findViewById(R.id.popularMovies_view);

        ArrayList<Movie> popularMoviesData = new ArrayList<>();
        popularMoviesAdapter = new GridViewAdapter(getActivity(),R.layout.movie_item,popularMoviesData);
        popularMoviesView.setAdapter(popularMoviesAdapter);

        popularMoviesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String toastMessge = popularMoviesAdapter.getItem(position).toString();
                //Toast.makeText(parent.getContext(), toastMessge, Toast.LENGTH_SHORT).show();

                Intent detailActivityIntent = new Intent(getActivity(), MovieDetailActivity.class).putExtra("movieData", popularMoviesAdapter.getItem(position));
                startActivity(detailActivityIntent);
            }
        });
        return rootView;
    }

    public class FetchMoviesData extends AsyncTask<String,Void,String> {

        @Override
        protected void onPostExecute(String s) {
            try {
                popularMovieList = Global.parseJSONToMOVIE(s);
                //popularMoviesAdapter.clear();
                for(Movie m : popularMovieList){
                    popularMoviesAdapter.add(m);
                }
                popularMoviesAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.e("PopularMoviesFragment", e.getMessage());
            }
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {

            String movieDataJson = "";
            try{
                //Load image configuration
                Global.parseJSONImageConfiguration(getString(R.string.tmdb_appkey));


                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( getActivity());

                String sortByParam = prefs.getString(getString(R.string.pref_sortby_key),getString(R.string.pref_sortby_key));

                URL fetchMovieDataURL = new URL(Global.getPopularMoviesURI(getString(R.string.tmdb_appkey),sortByParam));
                movieDataJson = Global.getJSONDataFromURL(fetchMovieDataURL);
            }
            catch (MalformedURLException ex){
                Log.e("PopularMoviesFragment",ex.getMessage());
            }
            return movieDataJson;
        }
    }



}
