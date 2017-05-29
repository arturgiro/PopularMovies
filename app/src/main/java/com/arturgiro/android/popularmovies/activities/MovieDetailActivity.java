package com.arturgiro.android.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.arturgiro.android.popularmovies.models.Movie;
import com.arturgiro.android.popularmovies.R;
import com.arturgiro.android.popularmovies.models.Review;
import com.arturgiro.android.popularmovies.network.NetworkUtils;
import com.arturgiro.android.popularmovies.utilities.TMDBJsonUtils;
import com.arturgiro.android.popularmovies.views.adapters.MovieReviewAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.arturgiro.android.popularmovies.models.Movie.MOVIE_IDENTIFIER;

public class MovieDetailActivity extends AppCompatActivity implements MovieReviewAdapter.MoviesReviewOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList<Review>>{

    private final String DETAIL_POSTER_SIZE = "w500";
    private static final int REVIEW_LOADER = 101;
    private static final int VIDEO_LOADER = 102;
    private static final String MOVIE_ID_EXTRA = "id";

    private Movie mMovie;

    private ImageView mIvPosterDetail;
    private TextView mTvUserRate;
    private TextView mTvReleaseDate;
    private TextView mTvOriginalTitle;
    private TextView mTvOverview;

    private RecyclerView mMovieReviews;
    private MovieReviewAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mIvPosterDetail = (ImageView) findViewById(R.id.iv_poster_detail);
        mTvUserRate = (TextView) findViewById(R.id.tv_detail_user_rate);
        mTvReleaseDate = (TextView) findViewById(R.id.tv_detail_release_date);
        mTvOriginalTitle = (TextView) findViewById(R.id.tv_detail_original_title);
        mTvOverview = (TextView) findViewById(R.id.tv_detail_overview);
        mMovieReviews = (RecyclerView) findViewById(R.id.movie_reviews);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMovieReviews.setLayoutManager(layoutManager);
        //ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        //mMovieReviews.addItemDecoration(itemDecoration);

        mReviewAdapter = new MovieReviewAdapter(this);
        mMovieReviews.setAdapter(mReviewAdapter);

        Bundle queryBundle = new Bundle();
        queryBundle.putInt(MOVIE_ID_EXTRA, mMovie.getId());

        getSupportLoaderManager().initLoader(REVIEW_LOADER, queryBundle, this);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(MOVIE_IDENTIFIER)) {
                mMovie = (Movie)intentThatStartedThisActivity.getParcelableExtra(MOVIE_IDENTIFIER);
                showMovie();
            }
        }
    }

    private void showMovie() {

        URL posterUrl = NetworkUtils.buildPosterUrl(mMovie.getPosterPath(), DETAIL_POSTER_SIZE);
        Picasso.with(this).load(posterUrl.toString()).into(mIvPosterDetail);

        mTvUserRate.setText(mMovie.getRating());
        mTvReleaseDate.setText(mMovie.getReleaseDate());
        mTvOriginalTitle.setText(mMovie.getOriginalTitle());
        mTvOverview.setText(mMovie.getOverview());

    }

    private void loadReviews() {

        if (mMovie == null) {
            return;
        }

        Bundle queryBundle = new Bundle();
        queryBundle.putInt(MOVIE_ID_EXTRA, mMovie.getId());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<ArrayList<Review>> reviewLoader = loaderManager.getLoader(REVIEW_LOADER);
        if (reviewLoader == null) {
            loaderManager.initLoader(REVIEW_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(REVIEW_LOADER, queryBundle, this);
        }
    }


    @Override
    public void onClick(Review review) {

    }

    @Override
    public Loader<ArrayList<Review>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Review>>(this) {

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
            mReviewAdapter.setReviewsData(data);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Review>> loader) {

    }
}
