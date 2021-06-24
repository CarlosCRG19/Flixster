package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel // Annotation that indicates that class is Parcelable
public class Movie {

    // Field (public for parceler use)
    Double voteAverage;
    String title, overview, posterPath, backdropPath;

    // Empty constructor (required for Parceler)
    public Movie() {}

    public Movie(JSONObject jsonObject) throws JSONException {
        // Extract the values from a JSON object (received by GET Request)
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        voteAverage = jsonObject.getDouble("vote_average"); // rating on scale from 0 to 10
    }

    // This method returns an array with all the movies gotten with the request
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {

        List<Movie> movies = new ArrayList<>();

        // Fill empty ArrayList with every movie object
        for (int i=0; i<movieJsonArray.length(); i++){
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    // Getters for each field
    public String getTitle() {
        return title;
    }
    public String getOverview() {
        return overview;
    }
    public String getPosterPath() { return String.format( "https://image.tmdb.org/t/p/w342/%s", posterPath ); }
    public String getBackdropPath() { return String.format( "https://image.tmdb.org/t/p/w342/%s", backdropPath); }
    public Double getVoteAverage() {
        return voteAverage;
    }
}
