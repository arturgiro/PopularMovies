package com.arturgiro.android.popularmovies.utilities;

import com.arturgiro.android.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class TMDBJsonUtils {

    //field names in XML
    static final String MOVIE_ID_FIELD = "id";
    static final String ORIGINAL_TITLE_FIELD = "original_title";
    static final String POSTER_PATH_FIELD = "poster_path";
    static final String OVERVIEW_FIELD = "overview";
    static final String RATING_FIELD = "vote_average";
    static final String RELEASE_DATE_FIELD = "release_date";


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
    public static ArrayList<Movie> getMoviesFromJson(String moviesJsonStr)throws JSONException
    {
        final String OWM_MESSAGE_CODE = "cod";
        final String RESULTS_SECTION = "results";

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

        ArrayList<Movie> movies = new ArrayList<Movie>();

        //results has the info about the movies
        JSONArray resultsArray = completeJson.getJSONArray(RESULTS_SECTION);
        for (int i = 0; i < resultsArray.length(); i++) {

            /* Get the JSON object representing the infos about one movie */
            JSONObject movieInfo = resultsArray.getJSONObject(i);

            int movieId = movieInfo.optInt(MOVIE_ID_FIELD);
            String originalTitle = movieInfo.optString(ORIGINAL_TITLE_FIELD);
            String posterPath = movieInfo.optString(POSTER_PATH_FIELD);
            String overview = movieInfo.optString(OVERVIEW_FIELD);
            String rating = movieInfo.optString(RATING_FIELD);
            String releaseDate = movieInfo.optString(RELEASE_DATE_FIELD);

            Movie movie = new Movie(movieId, originalTitle, posterPath, overview, rating, releaseDate);

            movies.add(movie);
        }

        return movies;
    }

}
