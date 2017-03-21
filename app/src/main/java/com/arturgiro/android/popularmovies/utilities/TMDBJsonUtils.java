package com.arturgiro.android.popularmovies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.util.HashMap;

public class TMDBJsonUtils {

    public static final String POSTER_PATH_FIELD = "poster_path";
    public static final String USER_RATE_FIELD = "vote_average";
    public static final String RELEASE_DATE_FIELD = "release_date";
    public static final String ORIGINAL_TITLE_FIELD = "original_title";
    public static final String OVERVIEW_FIELD = "overview";

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
    public static HashMap<Integer, String> getMoviesFromJson(String moviesJsonStr)throws JSONException
    {
        final String OWM_MESSAGE_CODE = "cod";
        final String RESULTS_SECTION = "results";
        final String POSTER_PATH_FIELD = "poster_path";
        final String MOVIE_ID_FIELD = "id";

        /* String array to hold each poster path String */
        HashMap<Integer, String> posters = null;

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

        posters = new HashMap<Integer, String>();

        for (int i = 0; i < resultsArray.length(); i++) {

            /* Get the JSON object representing the infos about one movie */
            JSONObject movieInfo = resultsArray.getJSONObject(i);

            int movieId = movieInfo.getInt(MOVIE_ID_FIELD);
            String posterPath = movieInfo.getString(POSTER_PATH_FIELD);

            posters.put(new Integer(movieId), posterPath);
        }

        return posters;
    }

    /**
     * This method parses JSON from a web response and returns an object with a movie details
     *
     * @param moveId the id of the movie you want the details
     *
     * @return Array of Strings containing poster's paths
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static HashMap<String, String> getMovieDetailFromJson(String moviesJsonStr) throws JSONException {

        final String OWM_MESSAGE_CODE = "cod";

            /* String array to hold each poster path String */
        HashMap<String, String> details = null;

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

        details = new HashMap<String, String>();

        details.put(POSTER_PATH_FIELD, completeJson.getString(POSTER_PATH_FIELD));
        details.put(USER_RATE_FIELD, completeJson.getString(USER_RATE_FIELD));
        details.put(RELEASE_DATE_FIELD, completeJson.getString(RELEASE_DATE_FIELD));
        details.put(ORIGINAL_TITLE_FIELD, completeJson.getString(ORIGINAL_TITLE_FIELD));
        details.put(OVERVIEW_FIELD, completeJson.getString(OVERVIEW_FIELD));

        return details;
    }
}
