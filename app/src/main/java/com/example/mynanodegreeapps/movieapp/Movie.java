package com.example.mynanodegreeapps.movieapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by akhatri on 28/11/15.
 */
public class Movie  implements Parcelable{
    int Id;
    String title;
    String posterImage;
    String overview;
    Date release_date;
    double rating;

    public Movie(){

    }

    public Movie(int Id,String title,String posterImage,String overview,double rating,String release_Date) {
        try{
            this.Id = Id;
            this.title = title;
            this.posterImage = posterImage;
            this.overview = overview;
            this.rating = rating;
            this.release_date = Global.dfMovie.parse(release_Date);
        }
        catch (java.text.ParseException ex){
            Log.d("Movie",ex.getMessage());
        }

    }

    public int getId() {
        return Id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public String getOverview(){ return overview;  }

    public double getRating(){ return rating; }

    public Date getRelease_date(){ return release_date; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(title);
        dest.writeString(posterImage);
        dest.writeString(overview);
        dest.writeDouble(rating);
        dest.writeString(Global.dfMovie.format(release_date));
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    // "De-parcel object
    public Movie(Parcel in) {
        try {
            Id = in.readInt();
            title = in.readString();
            posterImage = in.readString();
            overview = in.readString();
            rating = in.readDouble();
            release_date = Global.dfMovie.parse(in.readString());
        }
        catch (java.text.ParseException ex){
            Log.d("Movie",ex.getMessage());
        }
    }

}
