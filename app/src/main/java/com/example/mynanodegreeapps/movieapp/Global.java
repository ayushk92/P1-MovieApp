package com.example.mynanodegreeapps.movieapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import com.example.mynanodegreeapps.movieapp.Data.MovieContract;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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
import java.util.Vector;

/**
 * Created by akhatri on 28/11/15.
 * All globally accessed function will be part of this class
 */
public class Global {

    final static String BASE_TMDBURI = "http://api.themoviedb.org/3/discover/movie";
    final static String BASE_CONFIGURATIONURI = "http://api.themoviedb.org/3/configuration";
    final static String TRAILERS_TMDBURI = "http://api.themoviedb.org/3/movie/%s/videos";
    final static String USERREVIEW_TMDBURI = "http://api.themoviedb.org/3/movie/%s/reviews";
    final static String APPID_PARAM = "api_key";
    final static String SORTBY_PARAM = "sort_by";
    final static String PAGENO_PARAM = "pageNo";
    final static String BASE_YOUTUBEURI = "https://www.youtube.com/watch";
    final static String YOUTUBE_PARAM = "v";
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
    public static String getImageConfigurationURI(String appkey) {
        Uri fetchMovieDataUri = Uri.parse(Global.BASE_CONFIGURATIONURI).buildUpon()
                .appendQueryParameter(Global.APPID_PARAM, appkey)
                .build();
        return fetchMovieDataUri.toString();
    }
    public static String getTrailersURI(int movieID,String appkey){
        String baseURI = String.format(TRAILERS_TMDBURI,movieID);
        Uri fetchTrailersDataUri = Uri.parse(baseURI).buildUpon()
                .appendQueryParameter(Global.APPID_PARAM, appkey)
                .build();
        return  fetchTrailersDataUri.toString();
    }
    public static String getUserReviewsURI(int movieID,String appkey){
        String baseURI = String.format(USERREVIEW_TMDBURI,movieID);
        Uri fetchUserReviewsDataUri = Uri.parse(baseURI).buildUpon()
                .appendQueryParameter(Global.APPID_PARAM,appkey)
                .build();
        return fetchUserReviewsDataUri.toString();
    }
    public static Uri getTrailerURI(Trailer t){
        Uri trailerURI;
        if(t.getTrailerSite().toLowerCase().equals("youtube")){
            trailerURI = Uri.parse(BASE_YOUTUBEURI).buildUpon()
                    .appendQueryParameter(YOUTUBE_PARAM,t.getTrailerKey())
                    .build();
            return trailerURI;
        }
        else
            return null;
    }

    public static ArrayList<Trailer> parseJSONToTrailer(String trailerJSON)
    throws JSONException{

        ArrayList<Trailer> trailerList = new ArrayList<Trailer>();

        final String TRAILER_LIST = "results";
        final String NAME = "name";
        final String TRAILER_SITE = "site";
        final String SITE_KEY = "key";

        JSONObject forecastJson = new JSONObject(trailerJSON);
        JSONArray movieArray = forecastJson.getJSONArray(TRAILER_LIST);

        for(int i =0 ; i < movieArray.length(); i++){

            JSONObject trailerObject = movieArray.getJSONObject(i);
            Trailer trailer = new Trailer(trailerObject.getString(NAME)
                                          ,trailerObject.getString(TRAILER_SITE)
                                          ,trailerObject.getString(SITE_KEY));
            trailerList.add(trailer);
        }
        return trailerList;
    }

    public static ArrayList<UserReview> parseJSONToUserReview(String trailerJSON)
            throws JSONException{

        ArrayList<UserReview> userReviewArrayList = new ArrayList<UserReview>();

        final String USERREVIEW_LIST = "results";
        final String ID = "id";
        final String AUTHOR = "author";
        final String CONTENT = "content";

        JSONObject forecastJson = new JSONObject(trailerJSON);
        JSONArray movieArray = forecastJson.getJSONArray(USERREVIEW_LIST);

        for(int i =0 ; i < movieArray.length(); i++){

            JSONObject userReviewObject = movieArray.getJSONObject(i);
            UserReview userReview = new UserReview(userReviewObject.getString(ID)
                                                  ,userReviewObject.getString(AUTHOR)
                                                  ,userReviewObject.getString(CONTENT));
            userReviewArrayList.add(userReview);
        }
        return userReviewArrayList;
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
        final String VIDEO = "video";

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
            Log.e("JSONDataException", ex.getMessage());
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
    public static byte[] getImageByteArray(String imageURL){

        try{
            DefaultHttpClient mHttpClient = new DefaultHttpClient();
            HttpGet mHttpGet = new HttpGet(imageURL);
            HttpResponse mHttpResponse = mHttpClient.execute(mHttpGet);
            if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = mHttpResponse.getEntity();
                return EntityUtils.toByteArray(entity);
            }
        }
        catch (ClientProtocolException ex){
            Log.d("ImageByteArray", ex.getMessage());
        }
        catch (IOException ex){
            Log.d("ImageByteArray", ex.getMessage());
        }
        Log.d("ImageByteArray","NULL");
        return null;

    }
    public static void markMovieAsFavourite(Context context,Movie movie){


        int inserted = 0;
        ContentValues movieContent = new ContentValues();

        movieContent.put(MovieContract.MovieEntry.COLUMN_ID, movie.getId());
        movieContent.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        movieContent.put(MovieContract.MovieEntry.COLUMN_POSTERIMAGE, movie.getImageByteArray());
        movieContent.put(MovieContract.MovieEntry.COLUMN_RATING, movie.getRating());
        movieContent.put(MovieContract.MovieEntry.COLUMN_RELEASEDATE, Global.dfMovie.format(movie.getRelease_date()));
        movieContent.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());

        Uri insertedUri = context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,movieContent);

        if(insertedUri != null){
            int movieId = movie.getId();
            if(movie.getTrailers() != null) {
                Vector<ContentValues> cVVector = new Vector<ContentValues>(movie.getTrailers().size());
                for(Trailer t : movie.getTrailers()){
                    ContentValues trailerContent = new ContentValues();
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_TRAILER_KEY,t.getTrailerKey());
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME,t.getTrailerName());
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_TRAILER_SITE,t.getTrailerSite());
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_MOVIEID,movieId);

                    cVVector.add(trailerContent);
                }
                inserted = 0;
                // add to database
                if ( cVVector.size() > 0 ) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    inserted = context.getContentResolver().bulkInsert(MovieContract.TrailerEntry.buildTrailerWithMovieUri(String.valueOf(movieId)), cvArray);
                }
                if(inserted > 0)
                    Log.d("Trailer","Inserted");
            }
            if(movie.getUserReviews() != null) {
                Vector<ContentValues> cVVector = new Vector<ContentValues>(movie.getUserReviews().size());
                for(UserReview ur : movie.getUserReviews()){
                    ContentValues userReviewContent = new ContentValues();
                    userReviewContent.put(MovieContract.UserReviewEntry.COLUMN_ID,ur.getId());
                    userReviewContent.put(MovieContract.UserReviewEntry.COLUMN_AUTHOR,ur.getAuthor());
                    userReviewContent.put(MovieContract.UserReviewEntry.COLUMN_CONTENT,ur.getContent());
                    userReviewContent.put(MovieContract.UserReviewEntry.COLUMN_MOVIEID,movieId);

                    cVVector.add(userReviewContent);
                }
                inserted = 0;
                // add to database
                if ( cVVector.size() > 0 ) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    inserted = context.getContentResolver().bulkInsert(MovieContract.UserReviewEntry.buildUserReviewWithMovieUri(String.valueOf(movieId)), cvArray);
                }
                if(inserted > 0)
                    Log.d("Trailer","Inserted");
            }
        }






    }
    public static void SetImage(Context context, ImageView poster,Movie movie) {
        if(movie.getPosterImage() != null){
            Picasso.with(context).
                    load(movie.getPosterImage()).
                    into(poster);
        }
        else {
            byte[] bb = movie.getImageByteArray();
            if(bb != null)
                poster.setImageBitmap(BitmapFactory.decodeByteArray(bb, 0, bb.length));
        }
    }
}
