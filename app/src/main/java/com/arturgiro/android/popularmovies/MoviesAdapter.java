package com.arturgiro.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{

    private String[] mMoviesData;

    /**
     * Cache of the children views for a movie list item.
     */
    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mPosterImageView;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            mPosterImageView = (ImageView) view.findViewById(R.id.iv_poster);
            //view.setOnClickListener(this);
        }
    }


    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        String posterPath = mMoviesData[position];

        Picasso.with(holder.mPosterImageView.getContext()).load("http://image.tmdb.org/t/p/w500/"+posterPath).into(holder.mPosterImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMoviesData) return 0;
        return mMoviesData.length;
    }

    public void setmMoviesData(String[] moviesData) {
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }

}
