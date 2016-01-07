package com.example.mynanodegreeapps.movieapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mynanodegreeapps.movieapp.Data.MovieContract.TrailerEntry;
import com.example.mynanodegreeapps.movieapp.Data.MovieContract.MovieEntry;
import com.example.mynanodegreeapps.movieapp.Data.MovieContract.UserReviewEntry;
import com.example.mynanodegreeapps.movieapp.Movie;
import com.example.mynanodegreeapps.movieapp.Trailer;

/**
 * Created by akhatri on 06/01/16.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntry.COLUMN_ID + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POSTERIMAGE + " BLOB NOT NULL, " +
                MovieEntry.COLUMN_RELEASEDATE + " TEXT NOT NULL," +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                MovieEntry.COLUMN_RATING + " TEXT NOT NULL" +
                ");";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + TrailerEntry.TABLE_NAME + " (" +
                TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TrailerEntry.COLUMN_TRAILER_NAME + " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_TRAILER_SITE + " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_TRAILER_KEY + " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_MOVIEID + " REAL NOT NULL," +
                " FOREIGN KEY (" + TrailerEntry.COLUMN_MOVIEID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_ID + ") " +
                ");";

        final String SQL_CREATE_USERREVIEW_TABLE = "CREATE TABLE " + UserReviewEntry.TABLE_NAME + " (" +
                UserReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                UserReviewEntry.COLUMN_ID + " TEXT NOT NULL," +
                UserReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                UserReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                UserReviewEntry.COLUMN_MOVIEID + " REAL NOT NULL, " +
                " FOREIGN KEY (" + UserReviewEntry.COLUMN_MOVIEID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_ID + ") " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USERREVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserReviewEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
