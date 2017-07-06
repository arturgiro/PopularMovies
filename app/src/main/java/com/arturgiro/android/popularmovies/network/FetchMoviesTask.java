package com.arturgiro.android.popularmovies.network;

import android.os.AsyncTask;

import com.arturgiro.android.popularmovies.data.Movie;
import com.arturgiro.android.popularmovies.utilities.TMDBJsonUtils;

import java.net.URL;
import java.util.ArrayList;

public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private AsyncTaskDelegate mDelegate = null;

    public FetchMoviesTask(AsyncTaskDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDelegate.processStart();
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

            /* If there's no sort criteria, there's nothing to look up. */
        if (params.length == 0) {
            return null;
        }

        String sortMethod = params[0];
        int pageNumber = Integer.parseInt(params[1]);
        URL moviesRequestUrl = NetworkUtils.buildUrl(sortMethod, pageNumber);

        ArrayList<Movie> movies;

        try {
            String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
            movies = TMDBJsonUtils.getMoviesFromJson(jsonMoviesResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return movies;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movieData) {
        mDelegate.processFinish(movieData);
    }
}