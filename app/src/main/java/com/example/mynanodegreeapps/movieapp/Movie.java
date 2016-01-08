package com.example.mynanodegreeapps.movieapp;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by akhatri on 28/11/15.
 */
public class Movie  implements Parcelable{
    private  int Id;
    private String title;
    private String posterImage;
    private String overview;
    private Date release_date;
    private ArrayList<Trailer> trailers;
    private ArrayList<UserReview> userReviews;
    private double rating;
    private byte[] imageByteArray;

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

    public Movie(int Id,String title,byte[] imageByteArray,String overview,double rating,String release_Date) {
        try{
            this.Id = Id;
            this.title = title;
            this.imageByteArray = imageByteArray;
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

    public ArrayList<Trailer> getTrailers(){ return trailers; }

    public ArrayList<UserReview> getUserReviews(){ return userReviews;}

    public void setTrailers(ArrayList<Trailer> trailers){
        this.trailers = trailers;
    }

    public void setUserReviews(ArrayList<UserReview> userReviews){ this.userReviews = userReviews; }

    public byte[] getImageByteArray(){ return  imageByteArray; }

    public void setImageByteArray(byte[] imageByteArray){
        this.imageByteArray = imageByteArray;
    }



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
        if(release_date != null)
            dest.writeString(Global.dfMovie.format(release_date));
        if(imageByteArray != null){
        dest.writeInt(imageByteArray.length);
        dest.writeByteArray(imageByteArray);
        }
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
            String temp = in.readString();
            if(temp != null)
                release_date = Global.dfMovie.parse(temp);
            int length = in.readInt();
            if(length != 0){
            imageByteArray = new byte[length];
            in.readByteArray(imageByteArray);
            }
        }
        catch (java.text.ParseException ex){
            Log.d("Movie",ex.getMessage());
        }
    }



}
