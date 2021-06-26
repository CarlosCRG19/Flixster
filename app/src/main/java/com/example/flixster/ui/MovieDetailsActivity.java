package com.example.flixster.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.R;
import com.example.flixster.databinding.ActivityDetailBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.Objects;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    // Movie object to assign the movie to display
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Reduce view boilerplate with View Binding
        ActivityDetailBinding detailsBinding = ActivityDetailBinding.inflate(getLayoutInflater()); // activity_detail.xml -> ActivityDetailBinding
        // layout activity stored in special property root
        View view = detailsBinding.getRoot();
        setContentView(view);

        // Set new Toolbar
        Toolbar toolbar = (Toolbar) detailsBinding.toolbar;
        setSupportActionBar(toolbar);
        // Don't display title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Use parceler to unwrap the movie passed by the intent (with simple name as identifier)
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle())); // log message in case of success

        // Change the text on the TextViews
        detailsBinding.tvTitle.setText(movie.getTitle());
        detailsBinding.tvOverview.setText(movie.getOverview());
        // Change RatingBar value (rating scale is from 0 to 10, so we need to divide by 2)
        detailsBinding.rbVoteAverage.setRating((float) (movie.getVoteAverage() / 2));

        // CodePath's image loader (doc -> https://guides.codepath.org/android/Displaying-Images-with-the-Glide-Library)
        Glide.with(this)
                .load(movie.getPosterPath())
                .transform(new RoundedCornersTransformation(30,0))
                .placeholder(R.drawable.placeholder)
                .into(detailsBinding.ivPoster);

        Glide.with(this)
                .load(movie.getBackdropPath())
                .placeholder(R.drawable.placeholder)
                .into(detailsBinding.ivBackground);

        // If there is a movie video from YouTube, display a button to start the MovieTrailerActivity
        if(Objects.isNull(movie.getVideoId())){
            detailsBinding.btnPlay.setVisibility(View.INVISIBLE); // if there is no video, the button won't appear
        } else {
            detailsBinding.btnPlay.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        // Create intent to display MovieTrailerActivity
        Intent i = new Intent(this, MovieTrailerActivity.class);
        // Pass videoId to make the request with the YT API
        i.putExtra("VideoId", movie.getVideoId());
        // Show the activity
        this.startActivity(i);
    }
}