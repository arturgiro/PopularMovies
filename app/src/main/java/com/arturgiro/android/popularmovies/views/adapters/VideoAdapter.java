package com.arturgiro.android.popularmovies.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arturgiro.android.popularmovies.R;
import com.arturgiro.android.popularmovies.models.Video;
import com.arturgiro.android.popularmovies.network.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder>{


    //keep the videos information
    private ArrayList<Video> mVideos;

    private final VideoAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface VideoAdapterOnClickHandler {
        void onClick(Video video);
    }

    /**
     * Creates.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public VideoAdapter(VideoAdapterOnClickHandler clickHandler) {

        mClickHandler = clickHandler;
        mVideos = new ArrayList<Video>();
    }


    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.video_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.mVideo = mVideos.get(position);
        String videoKey = holder.mVideo.getKey();

        Context context = holder.mVideoImageView.getContext();
        URL imageUrl = NetworkUtils.buildVideoImageUrl(videoKey);
        Picasso.with(context).load(imageUrl.toString()).into(holder.mVideoImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mVideos) return 0;
        return mVideos.size();
    }

    /**
     * Add new movies info into the arrays of ids and posters.
     *
     * @param videosData The new movies to be added to de adapter
     */
    public void setVideosData(ArrayList<Video> videosData) {
        mVideos.addAll(videosData);
        notifyDataSetChanged();
    }

    public void resetVideoData() {
        // 1. First, clear the array of data
        mVideos.clear();

        // 2. Notify the adapter of the update
        notifyDataSetChanged();
    }

}