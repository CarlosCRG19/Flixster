package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.databinding.ItemMovieBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    // Fields
    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the movie at the passed position
        Movie movie = movies.get(position);
        // Bind the movie data into the VH
        holder.bind(movie);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // Class can't be static
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Layout views
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            // Add itemView's click listener (use this since the class implements View.OnClickListener)
            itemView.setOnClickListener(this);
        }

        // Method to connect each ViewHolder with the data
        public void bind(Movie movie) {
            // Change values on tvs
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            // Load movie image depending on orientation
            boolean orientation = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT; // true if it is in portrait mode
            String imageURL =  orientation ? movie.getPosterPath() : movie.getBackdropPath(); // link to image
            int placeholder = orientation ? R.drawable.placeholder : R.drawable.placeholder_land; // image while waiting to load

            // CodePath's image loader (doc -> https://guides.codepath.org/android/Displaying-Images-with-the-Glide-Library)
            Glide.with(context)
                    .load(imageURL)
                    .transform(new RoundedCornersTransformation(50,0))
                    .placeholder(R.drawable.placeholder)
                    .into(ivPoster);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition(); // position of the adapter in the array

            // Validate the position (it actually exists in the view)
            if(position != RecyclerView.NO_POSITION) {
                // Get movie at that position (won't work if class is static)
                Movie movie = movies.get(position);
                // Create intent to display MovieDetailsActivity
                Intent i = new Intent(context, MovieDetailsActivity.class); // NOTE: Intents are messaging objects that are used to request action from another app component.
                // Wrap the movie and pass it using simple name as identifier
                i.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // Show the activity
                context.startActivity(i);
            }
        }

    }

}
