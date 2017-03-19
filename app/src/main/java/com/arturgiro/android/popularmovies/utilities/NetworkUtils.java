package com.arturgiro.android.popularmovies.utilities;


import android.net.Uri;
import com.arturgiro.android.popularmovies.BuildConfig;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    static final String MOVIES_BASE_URL = "http://api.themoviedb.org";

    final static String PATH1_PARAM = "3";
    final static String PATH2_PARAM = "movie";
    final static String API_KEY_PARAM = "api_key";

    /**
     * Builds the URL used to talk to the movie server.
     *
     * @param sortMethod The movies order that will be queried for.
     * @return The URL to use to query the movie server.
     */
    public static URL buildUrl(String sortMethod) {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(PATH1_PARAM)
                .appendPath(PATH2_PARAM)
                .appendPath(sortMethod)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

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
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
