package com.arturgiro.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arturgiro.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.HashMap;
import java.util.Set;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{

    private final String POSTER_SIZE = "w342";

    //keep the ids of the movies
    private int[] mIds;
    //keep the poster's path of the movies
    private String[] mPosters;

    private final MoviesAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MoviesAdapterOnClickHandler {
        void onClick(int movieId);
    }

    /**
     * Creates a ForecastAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public MoviesAdapter(MoviesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * Cache of the children views for a movie list item.
     */
    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public int mMovieId;
        public final ImageView mPosterImageView;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            mMovieId = -1;
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
            //int adapterPosition = getAdapterPosition();
            //int movieId = mMoviesData[adapterPosition];
            mClickHandler.onClick(mMovieId);
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
        holder.mMovieId = mIds[position];
        String posterPath = mPosters[position];
        Context context = holder.mPosterImageView.getContext();
        URL posterUrl = NetworkUtils.buildPosterUrl(posterPath, POSTER_SIZE);
        Picasso.with(context).load(posterUrl.toString()).into(holder.mPosterImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mPosters) return 0;
        return mPosters.length;
    }

    /**
     * Add new movies info into the arrays of ids and posters.
     *
     * @param moviesData The new movies to be added to de adapter
     */
    public void setMoviesData(HashMap<Integer, String> moviesData) {

        int[] idAux = mIds;
        String[] postersAux = mPosters;

        int newSize = moviesData.size() + (mIds == null ? 0 : mIds.length);
        mIds = new int[newSize];
        mPosters = new String[newSize];

        int index = 0;
        if (idAux != null) {
            System.arraycopy(idAux, 0, mIds, 0, idAux.length);
            System.arraycopy(postersAux, 0, mPosters, 0, postersAux.length);
            index = idAux.length;
        }

        System.arraycopy(toIntArray(moviesData.keySet()), 0, mIds, index, moviesData.size());
        System.arraycopy(moviesData.values().toArray(), 0, mPosters, index, moviesData.size());

        notifyDataSetChanged();
    }

    //Converts an Integer set to an int array
    private int[] toIntArray(Set<Integer> set) {
        int[] ret = new int[set.size()];
        int i = 0;
        for (Integer e : set)
            ret[i++] = e.intValue();
        return ret;
    }

    public void resetMovieData() {
        // 1. First, clear the array of data
        mIds = null;
        mPosters = null;

        // 2. Notify the adapter of the update
        notifyDataSetChanged(); // or notifyItemRangeRemoved

    }

}
