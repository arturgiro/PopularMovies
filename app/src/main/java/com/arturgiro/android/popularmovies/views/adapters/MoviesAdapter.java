package com.arturgiro.android.popularmovies.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arturgiro.android.popularmovies.models.Movie;
import com.arturgiro.android.popularmovies.R;
import com.arturgiro.android.popularmovies.network.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{

    private final String POSTER_SIZE = "w342";

    //keep the movies information
    private ArrayList<Movie> mMovies;

    private final MoviesAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MoviesAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    /**
     * Creates a ForecastAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public MoviesAdapter(MoviesAdapterOnClickHandler clickHandler) {

        mClickHandler = clickHandler;
        mMovies = new ArrayList<Movie>();
    }

    /**
     * Cache of the children views for a movie list item.
     */
    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public Movie mMovie;
        public final ImageView mPosterImageView;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            mMovie = null;
            mPosterImageView = (ImageView) view.findViewById(R.id.iv_poster);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            mClickHandler.onClick(mMovie);
        }
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        holder.mMovie = mMovies.get(position);
        String posterPath = holder.mMovie.getPosterPath();

        Context context = holder.mPosterImageView.getContext();
        URL posterUrl = NetworkUtils.buildPosterUrl(posterPath, POSTER_SIZE);
        Picasso.with(context).load(posterUrl.toString()).into(holder.mPosterImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMovies) return 0;
        return mMovies.size();
    }

    /**
     * Add new movies info into the arrays of ids and posters.
     *
     * @param moviesData The new movies to be added to de adapter
     */
    public void setMoviesData(ArrayList<Movie> moviesData) {
        mMovies.addAll(moviesData);
        notifyDataSetChanged();
    }

    public void resetMovieData() {
        // 1. First, clear the array of data
        mMovies.clear();

        // 2. Notify the adapter of the update
        notifyDataSetChanged();
    }

}
