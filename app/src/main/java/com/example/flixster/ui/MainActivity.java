package com.example.flixster.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.R;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    // TMDB API Requests
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?"; // ENDPOINT

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

        // Assign new Toolbar
        Toolbar toolbar = (Toolbar) mainBinding.toolbar;
        setSupportActionBar(toolbar);
        // Don't display title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Assign movies to an empty array (here we'll add all the movie objects)
        movies = new ArrayList<>();

        // Instantiate MovieAdapter to create a bridge between each row of the RV and the data
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        // Create ViewGroup / Set Adapter and the manager for the layout
        mainBinding.rvMovies.setAdapter(movieAdapter);
        mainBinding.rvMovies.setLayoutManager(new LinearLayoutManager(this)); // arranges individual elements in a one-dimensional list
        mainBinding.rvMovies.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); // Adds a vertical divider between each row

        // CodePath's Http client (doc -> https://guides.codepath.org/android/Using-Codepath-Async-Http-Client)
        AsyncHttpClient client = new AsyncHttpClient(); // handles threads for asynchronous requests, catches and handls errors, onSuccess and onFailure callbacks

        // Get API key from resources
        RequestParams params = new RequestParams();
        params.put("api_key", getString(R.string.TMDB_KEY));

        // GET Request
        client.get(NOW_PLAYING_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                // Fill the movies ArrayList with the JSON response, send a success log message
                Log.i("TMDB Request", "onSuccess");
                JSONObject jsonObject = json.jsonObject; // Empty JSON Object

                try {
                    // results array contains the info for all the movies
                    JSONArray results = jsonObject.getJSONArray("results");
                    // Use fromJsonArray function to create array of Movie objects
                    movies.addAll(Movie.fromJsonArray(results, MainActivity.this));
                    // Notify the adapter about changes
                    movieAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.i("TMDB Request", "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                // Failure on the request, send a debug log message
                Log.d("TMDB Request", "onFailure", throwable);
            }
        });

    }
}