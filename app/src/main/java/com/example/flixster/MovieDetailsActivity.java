package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.flixster.databinding.ActivityDetailBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    // Movie object to assign the movie to display
    Movie movie;

    // Layout views
    ImageView ivPoster;
    RatingBar rbVoteAverage;
    TextView tvTitle, tvOverview;

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
        ivPoster = detailsBinding.ivPoster;
        tvOverview = detailsBinding.tvOverview;
        rbVoteAverage = detailsBinding.rbVoteAverage;

        // Use parceler to unwrap the movie passed by the intent (with simple name as identifier)
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle())); // log message in case of success

        // Change the text on the TextViews
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        // Change RatingBar value (rating scale is from 0 to 10, so we need to divide by 2)
        rbVoteAverage.setRating((float) (movie.getVoteAverage() / 2));

//        // Load movie image depending on orientation
//        boolean orientation = this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT; // true if it is in portrait mode
//        String imageURL =  orientation ? movie.getPosterPath() : movie.getBackdropPath(); // link to image
//        int placeholder = orientation ? R.drawable.placeholder : R.drawable.placeholder_land; // image while waiting to load

        // CodePath's image loader (doc -> https://guides.codepath.org/android/Displaying-Images-with-the-Glide-Library)
        Glide.with(this)
                .load(movie.getPosterPath())
                .transform(new RoundedCornersTransformation(50,0))
                .placeholder(R.drawable.placeholder)
                .into(ivPoster);
    }
}