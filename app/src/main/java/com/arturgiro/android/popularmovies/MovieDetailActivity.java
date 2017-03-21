package com.arturgiro.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arturgiro.android.popularmovies.utilities.NetworkUtils;
import com.arturgiro.android.popularmovies.utilities.TMDBJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.HashMap;

import static android.view.View.VISIBLE;
import static com.arturgiro.android.popularmovies.utilities.TMDBJsonUtils.ORIGINAL_TITLE_FIELD;
import static com.arturgiro.android.popularmovies.utilities.TMDBJsonUtils.OVERVIEW_FIELD;
import static com.arturgiro.android.popularmovies.utilities.TMDBJsonUtils.POSTER_PATH_FIELD;
import static com.arturgiro.android.popularmovies.utilities.TMDBJsonUtils.RELEASE_DATE_FIELD;
import static com.arturgiro.android.popularmovies.utilities.TMDBJsonUtils.USER_RATE_FIELD;

public class MovieDetailActivity extends AppCompatActivity {

    private final String DETAIL_POSTER_SIZE = "w500";

    private int mMovieId;
    private ImageView mIvPosterDetail;
    private TextView mTvUserRate;
    private TextView mTvReleaseDate;
    private TextView mTvOriginalTitle;
    private TextView mTvOverview;

    private ProgressBar mLoadingIndicator;
    private TextView mErrorDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mIvPosterDetail = (ImageView) findViewById(R.id.iv_poster_detail);
        mTvUserRate = (TextView) findViewById(R.id.tv_detail_user_rate);
        mTvReleaseDate = (TextView) findViewById(R.id.tv_detail_release_date);
        mTvOriginalTitle = (TextView) findViewById(R.id.tv_detail_original_title);
        mTvOverview = (TextView) findViewById(R.id.tv_detail_overview);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_detail_indicator);
        mErrorDisplay = (TextView) findViewById(R.id.tv_detail_error_message_display);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_INDEX)) {
                mMovieId = intentThatStartedThisActivity.getIntExtra(Intent.EXTRA_INDEX, 0);
                loadMovie();
            }
        }
    }

    /**
     * This method will get the user's preferred sort criteria, and then tell some
     * background method to get the movies data in the background.
     */
    private void loadMovie() {
        mErrorDisplay.setVisibility(View.INVISIBLE);
        new FetchMovieDetailTask().execute(new Integer(mMovieId));
    }

    private void showMovie(HashMap<String, String> movieData) {

        String posterPath = movieData.get(POSTER_PATH_FIELD);
        URL posterUrl = NetworkUtils.buildPosterUrl(posterPath, DETAIL_POSTER_SIZE);
        Picasso.with(this).load(posterUrl.toString()).into(mIvPosterDetail);

        mTvUserRate.setText(movieData.get(USER_RATE_FIELD));
        mTvReleaseDate.setText(movieData.get(RELEASE_DATE_FIELD));
        mTvOriginalTitle.setText(movieData.get(ORIGINAL_TITLE_FIELD));
        mTvOverview.setText(movieData.get(OVERVIEW_FIELD));
    }

    public class FetchMovieDetailTask extends AsyncTask<Integer, Void, HashMap<String, String>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(VISIBLE);
        }

        @Override
        protected HashMap<String, String> doInBackground(Integer... params) {
                       /* If there's no sort criteria, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            int movieId = params[0].intValue();

            URL movieRequestUrl = NetworkUtils.buildMovieUrl(movieId);

            try {
                String jsonMovieDetail = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);

                HashMap<String, String> simpleJsonMoviesData = TMDBJsonUtils
                        .getMovieDetailFromJson(jsonMovieDetail);

                return simpleJsonMoviesData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(HashMap<String, String> movieDetails) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieDetails != null) {
                showMovie(movieDetails);
            }
            else {
                mErrorDisplay.setVisibility(VISIBLE);
            }
        }
    }
}
