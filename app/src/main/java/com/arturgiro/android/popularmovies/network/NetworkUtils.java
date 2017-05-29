package com.arturgiro.android.popularmovies.network;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.arturgiro.android.popularmovies.BuildConfig;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    static final String MOVIES_BASE_URL = "http://api.themoviedb.org";
    static final String POSTERS_BASE_URL = "http://image.tmdb.org/t/p";

    final static String PATH1_PARAM = "3";
    final static String PATH_MOVIE = "movie";
    final static String API_KEY_PARAM = "api_key";
    final static String PAGE_PARAM = "page";
    final static String PATH_VIDEOS = "videos";
    final static String PATH_REVIEWS = "reviews";


    /**
     * Builds the URL used to talk to the movie server.
     *
     * @param sortMethod The movies order that will be queried for.
     * @return The URL to use to query the movie server.
     */
    public static URL buildUrl(String sortMethod, int pageNumber) {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(PATH1_PARAM)
                .appendPath(PATH_MOVIE)
                .appendPath(sortMethod)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .appendQueryParameter(PAGE_PARAM,String.valueOf(pageNumber))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    /**
     * Builds the URL used to retrive a poster image to a movie.
     *
     * @param posterPath The path for the poster.
     * @param posterSize The size of the image to be requested
     *                   which will be one of the following: "w92", "w154", "w185", "w342", "w500",
     *                   "w780", or "original"
     * @return The URL to use to query the image.
     */
    public static URL buildPosterUrl(String posterPath, String posterSize) {
        Uri builtUri = Uri.parse(POSTERS_BASE_URL).buildUpon()
                .appendPath(posterSize)
                .appendEncodedPath(posterPath)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds the URL used to retrive the details of a movie.
     *
     * @param movieId Id of the movie we want to retrive.
     * @return The URL to use to query the movie.
     */
    public static URL buildMovieUrl(int movieId) {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(PATH1_PARAM)
                .appendPath(PATH_MOVIE)
                .appendEncodedPath(String.valueOf(movieId))
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds the URL used to retrive the movie videos.
     *
     * @param movieId Id of the movie we want to retrive the videos.
     * @return The URL to use to query the movie.
     */
    public static URL buildMovieVideosUrl(int movieId) {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(PATH1_PARAM)
                .appendPath(PATH_MOVIE)
                .appendEncodedPath(String.valueOf(movieId))
                .appendPath(PATH_VIDEOS)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds the URL used to retrive the movie reviews.
     *
     * @param movieId Id of the movie we want to retrive the reviews.
     * @return The URL to use to query the movie.
     */
    public static URL buildMovieReviewsUrl(int movieId) {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(PATH1_PARAM)
                .appendPath(PATH_MOVIE)
                .appendEncodedPath(String.valueOf(movieId))
                .appendPath(PATH_REVIEWS)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
