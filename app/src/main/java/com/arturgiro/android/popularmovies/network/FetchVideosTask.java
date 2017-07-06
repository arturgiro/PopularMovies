package com.arturgiro.android.popularmovies.network;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.arturgiro.android.popularmovies.data.Video;
import com.arturgiro.android.popularmovies.utilities.TMDBJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class FetchVideosTask implements LoaderManager.LoaderCallbacks<ArrayList<Video>>{

    private static final String MOVIE_ID_EXTRA = "id";

    private Context mContext = null;
    private LoaderVideoDelegate mDelegate = null;

    public FetchVideosTask(Context mContext, LoaderVideoDelegate mDelegate) {
        this.mContext = mContext;
        this.mDelegate = mDelegate;
    }

    @Override
    public Loader<ArrayList<Video>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Video>>(mContext) {

            @Override
            protected void onStartLoading() {
                mDelegate.loadVideoStart();
                forceLoad();
            }

            @Override
            public ArrayList<Video> loadInBackground() {
                   /* Extract the search query from the args using our constant */
                int movieId = args.getInt(MOVIE_ID_EXTRA);
                ArrayList<Video> videos = new ArrayList<Video>();
                /* Parse the URL from the passed in String and perform the search */
                try {
                    URL videosUrl = NetworkUtils.buildMovieVideosUrl(movieId);
                    String json = NetworkUtils.getResponseFromHttpUrl(videosUrl);
                    try {
                        videos = TMDBJsonUtils.getVideosFromJson(json);
                        return videos;
                    }
                    catch (JSONException e) {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                return videos;
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<ArrayList<Video>> loader, ArrayList<Video> data) {

        if(data != null)
            mDelegate.loadVideoFinish(data);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Video>> loader) {

    }
}
