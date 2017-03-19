package com.arturgiro.android.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arturgiro.android.popularmovies.utilities.NetworkUtils;
import com.arturgiro.android.popularmovies.utilities.TMDBJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private MoviesAdapter mMoviestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMoviestAdapter = new MoviesAdapter();
        mRecyclerView.setAdapter(mMoviestAdapter);

        loadMoviesData();
    }

    /**
     * This method will get the user's preferred sort criteria, and then tell some
     * background method to get the movies data in the background.
     */
    private void loadMoviesData() {
        showMoviesDataView();
        //TODO - Implementar getPreferredSort
        String sort = "popular";//SunshinePreferences.getPreferredSort(this);

        new FetchMoviesTask().execute(sort);
    }

    private void showMoviesDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {

            /* If there's no sort criteria, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String sortMethod = params[0];

            URL moviesRequestUrl = NetworkUtils.buildUrl(sortMethod);

            try {
                String jsonMoviesResponse = NetworkUtils
                        .getResponseFromHttpUrl(moviesRequestUrl);

                String[] simpleJsonMoviesData = TMDBJsonUtils
                        .getMoviesFromJson(MainActivity.this, jsonMoviesResponse);

                return simpleJsonMoviesData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMoviesDataView();
                mMoviestAdapter.setmMoviesData(movieData);
            } else {
                showErrorMessage();
            }
        }
    }
}
