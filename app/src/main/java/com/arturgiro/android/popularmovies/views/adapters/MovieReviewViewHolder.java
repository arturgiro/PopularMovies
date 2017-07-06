package com.arturgiro.android.popularmovies.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arturgiro.android.popularmovies.R;
import com.arturgiro.android.popularmovies.data.Review;


public class MovieReviewViewHolder extends RecyclerView.ViewHolder {

    TextView txtContent;
    TextView txtAuthor;

    Review mReview;


    public MovieReviewViewHolder(View view) {
        super(view);
        mReview = null;
        txtContent = (TextView) view.findViewById(R.id.txt_movie_review_content);
        txtAuthor = (TextView) view.findViewById(R.id.txt_movie_review_author);
    }

}
