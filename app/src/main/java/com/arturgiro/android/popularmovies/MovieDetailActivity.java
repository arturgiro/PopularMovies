package com.arturgiro.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView mTvId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mTvId = (TextView) findViewById(R.id.tv_movie_id);
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_INDEX)) {
                int movieId = intentThatStartedThisActivity.getIntExtra(Intent.EXTRA_INDEX, 0);
                mTvId.setText(Integer.toString(movieId));
            }
        }
    }
}
