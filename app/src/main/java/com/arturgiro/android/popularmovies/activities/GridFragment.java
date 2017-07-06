package com.arturgiro.android.popularmovies.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arturgiro.android.popularmovies.R;
import com.arturgiro.android.popularmovies.data.Movie;
import com.arturgiro.android.popularmovies.data.MoviesContract;
import com.arturgiro.android.popularmovies.network.AsyncTaskDelegate;
import com.arturgiro.android.popularmovies.network.FetchMoviesTask;
import com.arturgiro.android.popularmovies.network.NetworkUtils;
import com.arturgiro.android.popularmovies.views.EndlessRecyclerViewScrollListener;
import com.arturgiro.android.popularmovies.views.adapters.MoviesAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnGridFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridFragment extends Fragment implements MoviesAdapter.MoviesAdapterOnClickHandler, AsyncTaskDelegate, SharedPreferences.OnSharedPreferenceChangeListener {

    // the fragment initialization parameters
    public static final String ARG_SORT_METHOD = "sort";

    private String mSortMethod;

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private MoviesAdapter mMoviestAdapter;

    private EndlessRecyclerViewScrollListener mScrollListener;

    private OnGridFragmentInteractionListener mListener;

    public GridFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sortMethod top rated, popular or favorites.
     * @return A new instance of fragment GridFragment.
     */
    public static GridFragment newInstance(String sortMethod) {
        GridFragment fragment = new GridFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SORT_METHOD, sortMethod);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSortMethod = getArguments().getString(ARG_SORT_METHOD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_grid, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_movies);
        mErrorMessageDisplay = (TextView) rootView.findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
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

        //loadMoviesData(1);

        return rootView;
    }

    /**
     * This method will get the user's preferred sort criteria, and then tell some
     * background method to get the movies data in the background.
     */
    private void loadMoviesData(final int page) {

        showMoviesDataView();

        if(mSortMethod.compareTo(getString(R.string.pref_sort_label_favorite)) == 0){
            if(page == 1)//não usa paginação para os filmes salvos no banco
                refreshMovies(getFavorites());
        }
        else
        if(NetworkUtils.isNetworkConnected(getContext())) {
            new FetchMoviesTask(this).execute(mSortMethod, String.valueOf(page));
        }
        else {
            Snackbar snackbar = Snackbar.make(mRecyclerView, getString(R.string.no_internet_connection), Snackbar.LENGTH_INDEFINITE);

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

    private void refreshMovies(ArrayList<Movie> movieData) {
        if (movieData != null) {
            showMoviesDataView();
            mMoviestAdapter.setMoviesData(movieData);
        } else {
            showErrorMessage();
        }
    }

    private ArrayList<Movie> getFavorites() {
        Cursor cursor = getContext().getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null);
        ArrayList<Movie> ret = new ArrayList<Movie>();
        try {
            while (cursor.moveToNext()) {
                Movie m = new Movie(cursor.getInt(cursor.getColumnIndex(MoviesContract.MovieEntry._ID)),
                        cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_PATH)),
                        cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_OVERVIEW)),
                        cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_USER_RATING)),
                        cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE))
                );
                ret.add(m);
            }
        } finally {
            cursor.close();
        }
        return ret;
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGridFragmentInteractionListener) {
            mListener = (OnGridFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGridFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This method is overridden in order to handle RecyclerView item
     * clicks.
     *
     * @param movie The movie that was clicked
     */
    @Override
    public void onClick(Movie movie) {
         mListener.onItemSelected(movie);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface OnGridFragmentInteractionListener {
        void onItemSelected(Movie movie);
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

    @Override
    public void processStart() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void processFinish(Object output) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        ArrayList<Movie> movieData = (ArrayList<Movie>)output;
        refreshMovies(movieData);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        PreferenceManager.setDefaultValues(getContext(), R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPref.registerOnSharedPreferenceChangeListener(this);
        mSortMethod = sharedPref.getString(getString(R.string.pref_order_key), "");

        loadMoviesData(1);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(isAdded()) {
            if (key.equals(getString(R.string.pref_order_key))) {
                // clear the array of data
                mMoviestAdapter.resetMovieData();
                // reset endless scroll listener when performing a new search
                mScrollListener.resetState();

                mSortMethod = sharedPreferences.getString(getString(R.string.pref_order_key), "");

                loadMoviesData(1);

                mListener.onItemSelected(null);


            }
        }
    }
}
