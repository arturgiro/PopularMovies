package com.arturgiro.android.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.arturgiro.android.popularmovies.R;

import static com.arturgiro.android.popularmovies.data.Movie.MOVIE_IDENTIFIER;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle arguments = new Bundle();
        arguments.putParcelable(MOVIE_IDENTIFIER, getIntent().getParcelableExtra(MOVIE_IDENTIFIER));

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(arguments);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, fragment)
                    .commit();
        }
    }


}
