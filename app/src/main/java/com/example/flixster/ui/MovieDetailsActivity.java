package com.example.flixster.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.flixster.R;
import com.example.flixster.databinding.ActivityDetailBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    // Movie object to assign the movie to display
    Movie movie;

    // Layout views
    RatingBar rbVoteAverage;
    TextView tvTitle, tvOverview;
    ImageView ivPoster, ivBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Reduce view boilerplate with View Binding
        ActivityDetailBinding detailsBinding = ActivityDetailBinding.inflate(getLayoutInflater()); // activity_detail.xml -> ActivityDetailBinding
        // layout activity stored in special property root
        View view = detailsBinding.getRoot();
        setContentView(view);

        // Assign View objects
        tvTitle = detailsBinding.tvTitle;
        tvOverview = detailsBinding.tvOverview;
        rbVoteAverage = detailsBinding.rbVoteAverage;
        ivPoster = detailsBinding.ivPoster;
        ivBackground = detailsBinding.ivBackground;

        // Use parceler to unwrap the movie passed by the intent (with simple name as identifier)
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle())); // log message in case of success

        // Change the text on the TextViews
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        // Change RatingBar value (rating scale is from 0 to 10, so we need to divide by 2)
        rbVoteAverage.setRating((float) (movie.getVoteAverage() / 2));

        // CodePath's image loader (doc -> https://guides.codepath.org/android/Displaying-Images-with-the-Glide-Library)
        Glide.with(this)
                .load(movie.getPosterPath())
                .transform(new RoundedCornersTransformation(50,0))
                .placeholder(R.drawable.placeholder)
                .into(ivPoster);

        Glide.with(this)
                .load(movie.getBackdropPath())
                .placeholder(R.drawable.placeholder)
                .into(ivBackground);

        ivBackground.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // Create intent to display MovieDetailsActivity
        Intent i = new Intent(this, MovieTrailerActivity.class); // NOTE: Intents are messaging objects that are used to request action from another app component.
        // Wrap the movie and pass it using simple name as identifier
        i.putExtra("Extra", movie.getVideoId());
        // Show the activity
        this.startActivity(i);
    }
}