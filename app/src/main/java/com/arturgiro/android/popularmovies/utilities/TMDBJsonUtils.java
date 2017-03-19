package com.arturgiro.android.popularmovies.utilities;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class TMDBJsonUtils {

    public static String[] getMoviesFromJson(Context context, String moviesJsonStr)throws JSONException
    {
        final String OWM_MESSAGE_CODE = "cod";

        /* String array to hold each poster path String */
        String[] posters = null;

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        /* Is there an error? */
        if (moviesJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = moviesJson.getInt(OWM_MESSAGE_CODE);

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

        JSONArray resultsArray = moviesJson.getJSONArray("results");

        posters = new String[resultsArray.length()];

        for (int i = 0; i < resultsArray.length(); i++) {

            /* Get the JSON object representing the day */
            JSONObject movie = resultsArray.getJSONObject(i);


            String caminho = movie.getString("poster_path");
            posters[i] = caminho;
        }

        return posters;
    }

}
