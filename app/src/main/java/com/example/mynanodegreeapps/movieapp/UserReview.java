package com.example.mynanodegreeapps.movieapp;

/**
 * Created by akhatri on 06/01/16.
 */
public class UserReview {

    private String Id;
    private String author;
    private String content;

    public UserReview(String Id,String author,String content){
        this.Id = Id;
        this.author = author;
        this.content = content;
    }

    public String getId(){ return Id;}

    public String getAuthor(){ return author;}

    public String getContent(){ return content;}
}
