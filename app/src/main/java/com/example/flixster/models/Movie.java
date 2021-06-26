package com.example.flixster.models;

import android.content.Context;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.R;
import com.google.android.youtube.player.internal.e;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

@Parcel // Annotation that indicates that class is Parcelable
public class Movie {

    // Field (public for parceler use)
    int movieId;
    Double voteAverage;
    String title, overview, posterPath, backdropPath, videoId, tmdbKey;

    // Empty constructor (required for Parceler)
    public Movie() {}

    public Movie(JSONObject jsonObject, String apiKey) throws JSONException {

        tmdbKey = apiKey; // TMDB API Key

        // Extract the values from a JSON object (received by GET Request)
        movieId = jsonObject.getInt("id");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        voteAverage = jsonObject.getDouble("vote_average"); // rating on scale from 0 to 10

        initializeVideoId(movieId); // sets videoId with a private method that uses the movieId to make a GET request to the TMDB API

    }

    /*
    IMPORTANT NOTE : this method is dangerous, since the request is Asynchronous, which means that movies can be declared
    even before a response has been received so videoId will be null until the request has been completed
     */
    private void initializeVideoId(int movieId) {
        // CodePath's Http client (doc -> https://guides.codepath.org/android/Using-Codepath-Async-Http-Client)
        AsyncHttpClient client = new AsyncHttpClient(); // handles threads for asynchronous requests, catches and handls errors, onSuccess and onFailure callbacks
        String requestURL = String.format("https://api.themoviedb.org/3/movie/%s/videos?", String.valueOf(movieId));

        // Set TMDB API Key as a param
        RequestParams params = new RequestParams();
        params.put("api_key", tmdbKey);

        // GET Request
        client.get(requestURL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                // Fill the movies ArrayList with the JSON response, send a success log message
                Log.i("YTVideos", "onSuccess");
                JSONObject jsonObject = json.jsonObject; // Empty JSON Object

                try {
                    // results array contains the info for all the videos
                    JSONArray results = jsonObject.getJSONArray("results");

                    // Filter videos and take the first one whose site is YouTube
                    for (int j=0; j<results.length(); j++) {
                        JSONObject video = results.getJSONObject(j);
                        if(video.getString("site").equals("YouTube")){
                            videoId = video.getString("key"); // key name has the value for the video id
                            break;
                        }
                    }
                } catch (JSONException e) {
                    Log.i("YTVIDEOS", "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable e) {
                Log.i("YTVIDEOS", "Failed to do request:" + requestURL);
            }
        });
    }

    // This method returns an array with all the movies gotten with the request
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray, Context context) throws JSONException {

        List<Movie> movies = new ArrayList<>();

        // Fill empty ArrayList with every movie object
        for (int i=0; i<movieJsonArray.length(); i++){
            movies.add(new Movie(movieJsonArray.getJSONObject(i), context.getString(R.string.TMDB_KEY))); // Passes the API Key from resources
        }
        return movies;
    }


    // Getters for each field
    public String getTitle() { return title; }
    public int getMovieId() { return movieId; }
    public String getVideoId() { return videoId; }
    public String getOverview() { return overview; }
    public Double getVoteAverage() { return voteAverage; }
    public String getPosterPath() { return String.format( "https://image.tmdb.org/t/p/w342/%s", posterPath ); }
    public String getBackdropPath() { return String.format( "https://image.tmdb.org/t/p/w342/%s", backdropPath); }





}
