package com.example.flixster.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    // Debugging tags
    public static final String TAG = "MainActivity";

    // TMDB API Requests
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=0dc187a82659e56d37255f3123371c8c"; // ENDPOINT

    // Fields
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Reduce view boilerplate with View Binding
        ActivityMainBinding mainBinding = ActivityMainBinding.inflate(getLayoutInflater()); // activity_detail.xml -> ActivityDetailBinding
        // layout activity stored in special property root
        View view = mainBinding.getRoot();
        setContentView(view);

        // Assign movies to an empty array (here we'll add all the movie objects)
        movies = new ArrayList<>();

        // Instantiate MovieAdapter to create a bridge between each row of the RV and the data
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        // Create ViewGroup / Set Adapter and the manager for the layout
        RecyclerView rvMovies = mainBinding.rvMovies;
        rvMovies.setAdapter(movieAdapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(this)); // arranges individual elements in a one-dimensional list

        // CodePath's Http client (doc -> https://guides.codepath.org/android/Using-Codepath-Async-Http-Client)
        AsyncHttpClient client = new AsyncHttpClient(); // handles threads for asynchronous requests, catches and handls errors, onSuccess and onFailure callbacks

        // GET Request
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                // Fill the movies ArrayList with the JSON response, send a success log message
                Log.i(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject; // Empty JSON Object

                try {
                    // results array contains the info for all the movies
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    // Use fromJsonArray function to create array of Movie objects
                    movies.addAll(Movie.fromJsonArray(results));
                    // Notify the adapter about changes
                    movieAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.i(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                // Failure on the request, send a debug log message
                Log.d(TAG, "onFailure");
            }
        });

    }
}