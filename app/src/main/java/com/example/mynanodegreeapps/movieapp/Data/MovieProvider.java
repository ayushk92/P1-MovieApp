package com.example.mynanodegreeapps.movieapp.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;

import com.example.mynanodegreeapps.movieapp.Movie;

/**
 * Created by akhatri on 06/01/16.
 */
public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    final static int MOVIE = 100;
    final static int TRAILER_WITH_MOVIE = 101;
    final static int USERREVIEW_WITH_MOVIE = 102;
    final static int MOVIE_WITH_ID = 103;

    private static final SQLiteQueryBuilder sMovieQueryBuilder;

    static {
        sMovieQueryBuilder = new SQLiteQueryBuilder();
        sMovieQueryBuilder.setTables(MovieContract.MovieEntry.TABLE_NAME);
    }

    private static final SQLiteQueryBuilder sTrailerQueryBuilder;

    static {
        sTrailerQueryBuilder = new SQLiteQueryBuilder();
        sTrailerQueryBuilder.setTables(MovieContract.TrailerEntry.TABLE_NAME);
    }

    private static final SQLiteQueryBuilder sUserReviewQueryBuilder;

    static {
        sUserReviewQueryBuilder = new SQLiteQueryBuilder();
        sUserReviewQueryBuilder.setTables(MovieContract.UserReviewEntry.TABLE_NAME);
    }



    private Cursor getMovie(Uri uri, String[] projection,String selection,String[] selectionArguments, String sortOrder) {

        return sMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArguments,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getTrailer(Uri uri, String[] projection,String selection,String[] selectionArguments, String sortOrder) {

        return sTrailerQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArguments,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getUserReview(Uri uri, String[] projection,String selection,String[] selectionArguments, String sortOrder) {

        return sUserReviewQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArguments,
                null,
                null,
                sortOrder
        );
    }


    private static final String sMovieSelection =
            MovieContract.MovieEntry.TABLE_NAME+
                    "." + MovieContract.MovieEntry.COLUMN_ID + " = ? ";

    private static final String sTrailerSelection =
            MovieContract.TrailerEntry.TABLE_NAME+
                    "." + MovieContract.TrailerEntry.COLUMN_MOVIEID + " = ? ";

    private static final String sUserReviewSelection =
            MovieContract.UserReviewEntry.TABLE_NAME+
                    "." + MovieContract.UserReviewEntry.COLUMN_MOVIEID + " = ? ";


    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }
    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority,MovieContract.PATH_TRAILER + "/*",TRAILER_WITH_MOVIE);
        matcher.addURI(authority,MovieContract.PATH_USERREVIEW + "/*",USERREVIEW_WITH_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE,MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*",MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case TRAILER_WITH_MOVIE:
                return MovieContract.TrailerEntry.CONTENT_TYPE;
            case USERREVIEW_WITH_MOVIE:
                return MovieContract.UserReviewEntry.CONTENT_TYPE;
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "weather/*/*"
            case MOVIE:
            {
                retCursor = getMovie(uri, projection,null,null, sortOrder);
                break;
            }
            case MOVIE_WITH_ID:
            {
                retCursor = getMovie(uri,projection,sMovieSelection,selectionArgs,sortOrder);
                break;
            }
            case TRAILER_WITH_MOVIE:
            {
                retCursor = getTrailer(uri,projection,sTrailerSelection,selectionArgs,sortOrder);
                break;
            }
            case USERREVIEW_WITH_MOVIE:
            {
                retCursor = getUserReview(uri,projection,sUserReviewSelection,selectionArgs,sortOrder);
                break;
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILER_WITH_MOVIE : {
                long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.TrailerEntry.buildTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case  USERREVIEW_WITH_MOVIE : {
                long _id = db.insert(MovieContract.UserReviewEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.UserReviewEntry.buildUserReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount;
        switch (match) {
            case TRAILER_WITH_MOVIE:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case USERREVIEW_WITH_MOVIE:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.UserReviewEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
