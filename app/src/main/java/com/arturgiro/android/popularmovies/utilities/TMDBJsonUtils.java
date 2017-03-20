package com.arturgiro.android.popularmovies.utilities;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;

public class TMDBJsonUtils {

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * with the poster's path of the movies.
     *
     * @param moviesJsonStr JSON response from server
     *
     * @return Array of Strings containing poster's paths
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static String[] getMoviesFromJson(String moviesJsonStr)throws JSONException
    {
        final String OWM_MESSAGE_CODE = "cod";
        final String RESULTS_SECTION = "results";
        final String POSTER_PATH_FIELD = "poster_path";

        /* String array to hold each poster path String */
        String[] posters = null;

        JSONObject completeJson = new JSONObject(moviesJsonStr);

        /* Is there an error? */
        if (completeJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = completeJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        //results has the info about the movies
        JSONArray resultsArray = completeJson.getJSONArray(RESULTS_SECTION);

        posters = new String[resultsArray.length()];

        for (int i = 0; i < resultsArray.length(); i++) {

            /* Get the JSON object representing the infos about one movie */
            JSONObject movieInfo = resultsArray.getJSONObject(i);

            String posterPath = movieInfo.getString(POSTER_PATH_FIELD);
            posters[i] = posterPath;
        }

        return posters;
    }

}
