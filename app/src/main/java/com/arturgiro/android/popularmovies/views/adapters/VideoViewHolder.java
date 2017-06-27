package com.arturgiro.android.popularmovies.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.arturgiro.android.popularmovies.R;
import com.arturgiro.android.popularmovies.data.Video;

public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public Video mVideo;
    public final ImageView mVideoImageView;

    public VideoViewHolder(View view) {
        super(view);
        mVideo = null;
        mVideoImageView = (ImageView) view.findViewById(R.id.iv_video_thumbnail);
        view.setOnClickListener(this);
    }

    /**
     * This gets called by the child views during a click.
     *
     * @param v The View that was clicked
     */
    @Override
    public void onClick(View v) {
        //mClickHandler.onClick(mMovie);
    }
}