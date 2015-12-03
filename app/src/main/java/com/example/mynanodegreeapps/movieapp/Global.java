package com.example.mynanodegreeapps.movieapp;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by akhatri on 28/11/15.
 * All globally accessed function will be part of this class
 */
public class Global {

    final static String BASE_TMDBURI = "http://api.themoviedb.org/3/discover/movie";
    final static String BASE_CONFIGURATIONURI = "http://api.themoviedb.org/3/configuration";
    final static String APPID_PARAM = "api_key";
    final static String SORTBY_PARAM = "sort_by";
    final static String PAGENO_PARAM = "pageNo";
    static String imageBaseUrl;
    static String posterImageSize;
    static final DateFormat dfMovie = new SimpleDateFormat("yyyy-mm-dd");
    static final DateFormat dfMovieDetail = new SimpleDateFormat("yyyy");

    static Boolean isSortByChanged = true;

    public static String getPopularMoviesURI(String appkey,String sortByParam){

        Uri fetchMovieDataUri = Uri.parse(Global.BASE_TMDBURI).buildUpon()
                .appendQueryParameter(Global.SORTBY_PARAM, sortByParam)
                .appendQueryParameter(Global.APPID_PARAM, appkey)
                .build();
        return fetchMovieDataUri.toString();
    }
    public static String getImageConfigurationURI(String appkey){
        Uri fetchMovieDataUri = Uri.parse(Global.BASE_CONFIGURATIONURI).buildUpon()
                .appendQueryParameter(Global.APPID_PARAM, appkey)
                .build();
        return fetchMovieDataUri.toString();
    }


    public static ArrayList<Movie> parseJSONToMOVIE(String movieJSON)
    throws JSONException{

        ArrayList<Movie> moviesList = new ArrayList<Movie>();

        final String MOVIE_LIST = "results";
        final String ID = "id";
        final String TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String USER_RATING = "vote_average";
        final String RELEASE_DATE = "release_date";

        JSONObject forecastJson = new JSONObject(movieJSON);
        JSONArray movieArray = forecastJson.getJSONArray(MOVIE_LIST);

        for(int i =0 ; i < movieArray.length(); i++){

            JSONObject movieObject = movieArray.getJSONObject(i);
            Movie movie = new Movie(movieObject.getInt(ID),
                                    movieObject.getString(TITLE),
                                    imageBaseUrl + posterImageSize + movieObject.getString(POSTER_PATH),
                                    movieObject.getString(OVERVIEW),
                                    movieObject.getDouble(USER_RATING),
                                    movieObject.getString(RELEASE_DATE));
                    moviesList.add(movie);

        }
        return  moviesList;


    }

    public static String getJSONDataFromURL(URL url){
        HttpURLConnection urlConnection = null;
        String jsonDAta;
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        try {

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                // Nothing to do.
                jsonDAta = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }
        }
        catch (IOException ex){
            Log.e("PopularMoviesFragment", ex.getMessage());
        }
        finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }
        return  buffer.toString();

    }
    public static void parseJSONImageConfiguration(String appKey){
        final String IMAGES = "images";
        final String BASE_URL = "secure_base_url";
        try {
            URL fetchConfigurationDataURL = new URL(getImageConfigurationURI(appKey));
            String jsonConfig = getJSONDataFromURL(fetchConfigurationDataURL);

            JSONObject configJSON = new JSONObject(jsonConfig);
            JSONObject imageObject = configJSON.getJSONObject(IMAGES);

            imageBaseUrl = imageObject.getString(BASE_URL);
            //Image size can be changed in future based upon requirement
            posterImageSize = "w500";

        }
        catch (MalformedURLException ex){
            Log.e("ImageConfiguraton",ex.getMessage());
        }
        catch (JSONException ex){
            Log.e("ImageConfiguraton",ex.getMessage());
        }



    }
}
