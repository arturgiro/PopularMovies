package com.arturgiro.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.arturgiro.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

import static com.arturgiro.android.popularmovies.Movie.MOVIE_IDENTIFIER;

public class MovieDetailActivity extends AppCompatActivity {

    private final String DETAIL_POSTER_SIZE = "w500";

    private Movie mMovie;

    private ImageView mIvPosterDetail;
    private TextView mTvUserRate;
    private TextView mTvReleaseDate;
    private TextView mTvOriginalTitle;
    private TextView mTvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mIvPosterDetail = (ImageView) findViewById(R.id.iv_poster_detail);
        mTvUserRate = (TextView) findViewById(R.id.tv_detail_user_rate);
        mTvReleaseDate = (TextView) findViewById(R.id.tv_detail_release_date);
        mTvOriginalTitle = (TextView) findViewById(R.id.tv_detail_original_title);
        mTvOverview = (TextView) findViewById(R.id.tv_detail_overview);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(MOVIE_IDENTIFIER)) {
                mMovie = (Movie)intentThatStartedThisActivity.getSerializableExtra(MOVIE_IDENTIFIER);
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

}
