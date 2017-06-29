package com.arturgiro.android.popularmovies.views.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arturgiro.android.popularmovies.R;
import com.arturgiro.android.popularmovies.data.Review;

import java.util.ArrayList;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewViewHolder>{

    private ArrayList<Review> mReviews;

    public MovieReviewAdapter() {
        this.mReviews = new ArrayList<Review>();
    }

    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
        holder.mReview = mReviews.get(position);
        holder.txtAuthor.setText(holder.mReview.getAuthor());
        holder.txtContent.setText(holder.mReview.getContent());
    }

    @Override
    public int getItemCount() {
        if (null == mReviews) return 0;
        return mReviews.size();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface MoviesReviewOnClickHandler {
        void onClick(Review review);
    }

    public void setReviewsData(ArrayList<Review> reviews) {
        mReviews.addAll(reviews);
        notifyDataSetChanged();
    }

    public void resetReviews() {
        // 1. First, clear the array of data
        mReviews.clear();

        // 2. Notify the adapter of the update
        notifyDataSetChanged();
    }
}
