package com.arturgiro.android.popularmovies.network;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.arturgiro.android.popularmovies.data.Review;
import com.arturgiro.android.popularmovies.utilities.TMDBJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class FetchReviewsTask implements LoaderManager.LoaderCallbacks<ArrayList<Review>>{

    private static final String MOVIE_ID_EXTRA = "id";

    private Context mContext = null;
    private LoaderReviewDelegate mDelegate = null;

    public FetchReviewsTask(Context mContext, LoaderReviewDelegate mDelegate) {
        this.mContext = mContext;
        this.mDelegate = mDelegate;
    }

    @Override
    public Loader<ArrayList<Review>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Review>>(mContext) {

            @Override
            protected void onStartLoading() {
                mDelegate.loadReviewStart();
                forceLoad();
            }

            @Override
            public ArrayList<Review> loadInBackground() {
                   /* Extract the search query from the args using our constant */
                int movieId = args.getInt(MOVIE_ID_EXTRA);
                ArrayList<Review> reviews = new ArrayList<Review>();
                /* Parse the URL from the passed in String and perform the search */
                try {
                    URL reviewsUrl = NetworkUtils.buildMovieReviewsUrl(movieId);
                    String json = NetworkUtils.getResponseFromHttpUrl(reviewsUrl);
                    try {
                        reviews = TMDBJsonUtils.getReviewsFromJson(json);
                        return reviews;
                    }
                    catch (JSONException e) {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                return reviews;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {

        if(data != null)
            mDelegate.loadReviewFinish(data);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Review>> loader) {

    }
}
