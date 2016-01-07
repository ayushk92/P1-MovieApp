package com.example.mynanodegreeapps.movieapp;

/**
 * Created by akhatri on 05/01/16.
 */
public class Trailer {
    private String trailerName;
    private String trailerSite;
    private String trailerKey;

    public Trailer(String trailerName,String trailerSite,String trailerKey){
        this.trailerName = trailerName;
        this.trailerSite = trailerSite;
        this.trailerKey = trailerKey;
    }


    public String getTrailerName(){ return  trailerName; }

    public String getTrailerSite(){ return  trailerSite; }

    public String getTrailerKey(){ return  trailerKey; }


}
