package com.example.flixster.models;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.R;
import com.google.android.youtube.player.internal.e;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Headers;

@Parcel // Annotation that indicates that class is Parcelable
public class Movie {

    // Field (public for parceler use)
    int movieId;
    Double voteAverage;
    String title, overview, posterPath, backdropPath, videoId;

    // Empty constructor (required for Parceler)
    public Movie() {}

    public Movie(JSONObject jsonObject) throws JSONException {
        // Extract the values from a JSON object (received by GET Request)
        movieId = jsonObject.getInt("id");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        voteAverage = jsonObject.getDouble("vote_average"); // rating on scale from 0 to 10

        initializeVideoId(movieId);

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

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }
    public String getOverview() {
        return overview;
    }
    public String getPosterPath() { return String.format( "https://image.tmdb.org/t/p/w342/%s", posterPath ); }
    public String getBackdropPath() { return String.format( "https://image.tmdb.org/t/p/w342/%s", backdropPath); }
    public Double getVoteAverage() { return voteAverage; }
    public String getVideoId() { return videoId; }

    private void initializeVideoId(int movieId) {
        // CodePath's Http client (doc -> https://guides.codepath.org/android/Using-Codepath-Async-Http-Client)
        AsyncHttpClient client = new AsyncHttpClient(); // handles threads for asynchronous requests, catches and handls errors, onSuccess and onFailure callbacks
        String getURL = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=%s", String.valueOf(movieId), "0dc187a82659e56d37255f3123371c8c");

        // GET Request
        client.get(getURL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                // Fill the movies ArrayList with the JSON response, send a success log message
                Log.i("YTVideos", "onSuccess");
                JSONObject jsonObject = json.jsonObject; // Empty JSON Object

                try {
                    // results array contains the info for all the movies
                    JSONArray results = jsonObject.getJSONArray("results");
                    // Use fromJsonArray function to create array of Movie objects
                    for (int j=0; j<results.length(); j++) {
                        JSONObject video = results.getJSONObject(j);
                        if(video.getString("site").equals("YouTube")){
                            videoId = video.getString("key");
                            break;
                        }
                    }
                } catch (JSONException e) {
                   Log.i("YTVIDEOS", "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable e) {
                Log.i("YTVIDEOS", "Failed to do request:" + getURL);
            }
        });
    }

}
