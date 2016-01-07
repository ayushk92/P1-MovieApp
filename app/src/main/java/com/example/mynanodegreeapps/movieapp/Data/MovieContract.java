package com.example.mynanodegreeapps.movieapp.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.mynanodegreeapps.movieapp.Trailer;
import com.example.mynanodegreeapps.movieapp.UserReview;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by akhatri on 06/01/16.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.mynanodegreeapps.movieapp";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_USERREVIEW = "userreview";

    public static final class TrailerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        // Table name
        public static final String TABLE_NAME = "trailer";

        public static final String COLUMN_TRAILER_NAME = "trailer_name";


        public static final String COLUMN_TRAILER_SITE = "trailer_site";

        public static final String COLUMN_TRAILER_KEY = "trailer_key";

        public static final String COLUMN_MOVIEID = "movie_id";


        public static Uri buildTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildTrailerWithMovieUri(String movieId){
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }
    }

    public static final class UserReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USERREVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERREVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERREVIEW;

        // Table name
        public static final String TABLE_NAME = "user_review";

        public static final String COLUMN_ID = "user_review_id";

        public static final String COLUMN_AUTHOR = "author";

        public static final String COLUMN_CONTENT = "content";

        public static final String COLUMN_MOVIEID = "movie_id";


        public static Uri buildUserReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildUserReviewWithMovieUri(String movieId){
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }
    }

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // Table name
        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTERIMAGE = "poster_image";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASEDATE = "release_date";
        public static final String COLUMN_RATING = "rating";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieDetailUri(String movieID){
            return CONTENT_URI.buildUpon().appendPath(movieID).build();
        }
    }


}
