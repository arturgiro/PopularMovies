package com.arturgiro.android.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arturgiro.android.popularmovies.R;
import com.arturgiro.android.popularmovies.models.Movie;
import com.arturgiro.android.popularmovies.utilities.NetworkUtils;
import com.arturgiro.android.popularmovies.utilities.TMDBJsonUtils;
import com.arturgiro.android.popularmovies.views.EndlessRecyclerViewScrollListener;
import com.arturgiro.android.popularmovies.views.adapters.MoviesAdapter;

import java.net.URL;
import java.util.ArrayList;

import static com.arturgiro.android.popularmovies.models.Movie.MOVIE_IDENTIFIER;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private CoordinatorLayout coordinatorLayout;

    private MoviesAdapter mMoviestAdapter;

    private EndlessRecyclerViewScrollListener mScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);//TODO VErificar esse número
        mRecyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setHasFixedSize(true);

        mMoviestAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mMoviestAdapter);

        // Retain an instance so that you can call `resetState()` for fresh searches
       mScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadMoviesData(page);
            }
        };

        // Adds the scroll listener to RecyclerView
        mRecyclerView.addOnScrollListener(mScrollListener);

        loadMoviesData(1);
    }

    /**
     * This method will get the user's preferred sort criteria, and then tell some
     * background method to get the movies data in the background.
     */
    private void loadMoviesData(final int page) {

        showMoviesDataView();

        if(NetworkUtils.isNetworkConnected(this)) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            String sortMethod = sharedPref.getString(getString(R.string.pref_order_key), "");
            new FetchMoviesTask().execute(sortMethod, String.valueOf(page));
        }
        else {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.no_internet_connection), Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction(getString(R.string.retry), new View.OnClickListener() {
                //Ao clicar na snackbar, uma nova tentativa de atualizar a lista é efetuada :-)
                @Override
                public void onClick(View view) {
                    loadMoviesData(page);
                }
            });
            snackbar.show();
        }
    }

    private void showMoviesDataView() {
       //Hide the error message
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        //show the movies
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param movie The movie that was clicked
     */
    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(MOVIE_IDENTIFIER, movie);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.pref_order_key))) {
            // clear the array of data
            mMoviestAdapter.resetMovieData();
            // reset endless scroll listener when performing a new search
            mScrollListener.resetState();

            loadMoviesData(1);
        }
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            /* If there's no sort criteria, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String sortMethod = params[0];
            int pageNumber = Integer.parseInt(params[1]);
            URL moviesRequestUrl = NetworkUtils.buildUrl(sortMethod, pageNumber);

            ArrayList<Movie> movies;

            try {
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
                movies = TMDBJsonUtils.getMoviesFromJson(jsonMoviesResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMoviesDataView();
                mMoviestAdapter.setMoviesData(movieData);
            } else {
                showErrorMessage();
            }
        }
    }

    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }
}
