package com.arturgiro.android.popularmovies.utilities;

import com.arturgiro.android.popularmovies.data.Movie;
import com.arturgiro.android.popularmovies.data.Review;
import com.arturgiro.android.popularmovies.data.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class TMDBJsonUtils {

    //field names in movie XML
    static final String MOVIE_ID_FIELD = "id";
    static final String ORIGINAL_TITLE_FIELD = "original_title";
    static final String POSTER_PATH_FIELD = "poster_path";
    static final String OVERVIEW_FIELD = "overview";
    static final String RATING_FIELD = "vote_average";
    static final String RELEASE_DATE_FIELD = "release_date";

    //field names in review XML
    static final String REVIEW_ID_FIELD = "id";
    static final String REVIEW_AUTHOR_FIELD = "author";
    static final String REVIEW_CONTENT_FIELD = "content";
    static final String REVIEW_URL_FIELD = "url";

    //field names in video XML
    static final String VIDEO_ID_FIELD = "id";
    static final String VIDEO_ISO_639_FIELD = "iso_639_1";
    static final String VIDEO_ISO_3166_FIELD = "iso_3166_1";
    static final String VIDEO_KEY_FIELD = "key";
    static final String VIDEO_NAME_FIELD = "name";
    static final String VIDEO_SITE_FIELD = "site";
    static final String VIDEO_SIZE_FIELD = "size";
    static final String VIDEO_TYPE_FIELD = "type";

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

    public static ArrayList<Review> getReviewsFromJson(String reviewsJsonStr)throws JSONException
    {
        final String OWM_MESSAGE_CODE = "cod";
        final String RESULTS_SECTION = "results";

        JSONObject completeJson = new JSONObject(reviewsJsonStr);

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

        ArrayList<Review> reviews = new ArrayList<Review>();

        //results has the info about the movies
        JSONArray resultsArray = completeJson.getJSONArray(RESULTS_SECTION);
        for (int i = 0; i < resultsArray.length(); i++) {

            /* Get the JSON object representing the infos about one movie */
            JSONObject reviewInfo = resultsArray.getJSONObject(i);

            int reviewId = reviewInfo.optInt(REVIEW_ID_FIELD);
            String author = reviewInfo.optString(REVIEW_AUTHOR_FIELD);
            String content = reviewInfo.optString(REVIEW_CONTENT_FIELD);
            String url = reviewInfo.optString(REVIEW_URL_FIELD);

            Review review = new Review(reviewId, author, content, url);

            reviews.add(review);
        }

        return reviews;
    }

    public static ArrayList<Video> getVideosFromJson(String videoJsonStr)throws JSONException
    {
        final String OWM_MESSAGE_CODE = "cod";
        final String RESULTS_SECTION = "results";

        JSONObject completeJson = new JSONObject(videoJsonStr);

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

        ArrayList<Video> videos = new ArrayList<Video>();

        //results has the info about the movies
        JSONArray resultsArray = completeJson.getJSONArray(RESULTS_SECTION);
        for (int i = 0; i < resultsArray.length(); i++) {

            /* Get the JSON object representing the infos about one movie */
            JSONObject videoInfo = resultsArray.getJSONObject(i);

            int videoId = videoInfo.optInt(VIDEO_ID_FIELD);
            String iso_639_1 = videoInfo.optString(VIDEO_ISO_639_FIELD);
            String iso_3166_1 = videoInfo.optString(VIDEO_ISO_3166_FIELD);
            String key = videoInfo.optString(VIDEO_KEY_FIELD);
            String name = videoInfo.optString(VIDEO_NAME_FIELD);
            String site = videoInfo.optString(VIDEO_SITE_FIELD);
            int size = videoInfo.optInt(VIDEO_SIZE_FIELD);
            String type = videoInfo.optString(VIDEO_TYPE_FIELD);

            Video video = new Video(videoId, iso_639_1, iso_3166_1, key, name, site, size, type);

            videos.add(video);
        }

        return videos;
    }
}
