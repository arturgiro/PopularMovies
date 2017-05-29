package com.arturgiro.android.popularmovies.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arturgiro.android.popularmovies.R;
import com.arturgiro.android.popularmovies.models.Review;


public class MovieReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView txtContent;
    TextView txtAuthor;

    Review mReview;

    private MovieReviewAdapter.MoviesReviewOnClickHandler mClickHandler;

    public MovieReviewViewHolder(View view, MovieReviewAdapter.MoviesReviewOnClickHandler clickHandler) {
        super(view);
        mReview = null;
        txtContent = (TextView) view.findViewById(R.id.txt_movie_review_content);
        txtAuthor = (TextView) view.findViewById(R.id.txt_movie_review_author);
        mClickHandler = clickHandler;
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mClickHandler != null)
            mClickHandler.onClick(mReview);
    }
}
