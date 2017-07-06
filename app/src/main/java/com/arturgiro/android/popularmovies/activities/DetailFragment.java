package com.arturgiro.android.popularmovies.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arturgiro.android.popularmovies.R;
import com.arturgiro.android.popularmovies.data.FavoritesService;
import com.arturgiro.android.popularmovies.data.Movie;
import com.arturgiro.android.popularmovies.data.Review;
import com.arturgiro.android.popularmovies.data.Video;
import com.arturgiro.android.popularmovies.network.FetchReviewsTask;
import com.arturgiro.android.popularmovies.network.FetchVideosTask;
import com.arturgiro.android.popularmovies.network.LoaderReviewDelegate;
import com.arturgiro.android.popularmovies.network.LoaderVideoDelegate;
import com.arturgiro.android.popularmovies.network.NetworkUtils;
import com.arturgiro.android.popularmovies.views.adapters.MovieReviewAdapter;
import com.arturgiro.android.popularmovies.views.adapters.VideoAdapter;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import static com.arturgiro.android.popularmovies.data.Movie.MOVIE_IDENTIFIER;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment implements VideoAdapter.VideoAdapterOnClickHandler, LoaderReviewDelegate, LoaderVideoDelegate{

    private static final int REVIEW_LOADER = 101;
    private static final int VIDEO_LOADER = 102;

    private final String DETAIL_POSTER_SIZE = "w500";
    private static final String MOVIE_ID_EXTRA = "id";

    private Movie mMovie;

    private RecyclerView mMovieReviews;
    private MovieReviewAdapter mReviewAdapter;

    private RecyclerView mMovieVideos;
    private VideoAdapter mVideoAdapter;

    private ImageView mIvPosterDetail;
    private FloatingActionButton mFab;
    private RelativeLayout mRelativeLayout;
    private TextView mTvUserRate;
    private TextView mTvReleaseDate;
    private TextView mTvOriginalTitle;
    private TextView mTvOverview;

    public DetailFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movie Movie to be displyed.
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(Movie movie) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_IDENTIFIER, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(MOVIE_IDENTIFIER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mIvPosterDetail = (ImageView) rootView.findViewById(R.id.iv_poster_detail);
        mTvUserRate = (TextView) rootView.findViewById(R.id.tv_detail_user_rate);
        mTvReleaseDate = (TextView) rootView.findViewById(R.id.tv_detail_release_date);
        mTvOriginalTitle = (TextView) rootView.findViewById(R.id.tv_detail_original_title);
        mTvOverview = (TextView) rootView.findViewById(R.id.tv_detail_overview);

        mRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_layout_detail);
        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabClick();
            }
        });

        if (mMovie != null) {

            showMovie();

            mMovieReviews = (RecyclerView) rootView.findViewById(R.id.movie_reviews);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mMovieReviews.setLayoutManager(layoutManager);
            mReviewAdapter = new MovieReviewAdapter();
            mMovieReviews.setAdapter(mReviewAdapter);

            mMovieVideos = (RecyclerView) rootView.findViewById(R.id.movie_videos);
            LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            mMovieVideos.setLayoutManager(layoutManager2);
            mVideoAdapter = new VideoAdapter(this);
            mMovieVideos.setAdapter(mVideoAdapter);

            FavoritesService favoritesService = new FavoritesService(getContext());
            if (favoritesService.isFavorite(mMovie))
                mFab.setImageResource(R.drawable.ic_star_black_24dp);
            else
                mFab.setImageResource(R.drawable.ic_star_border_black_24dp);
        }

        return rootView;
    }

    private void showMovie() {

        URL posterUrl = NetworkUtils.buildPosterUrl(mMovie.getPosterPath(), DETAIL_POSTER_SIZE);
        Picasso.with(getContext()).load(posterUrl.toString()).into(mIvPosterDetail);

        mTvUserRate.setText(mMovie.getRating());
        mTvReleaseDate.setText(mMovie.getReleaseDate());
        mTvOriginalTitle.setText(mMovie.getOriginalTitle());
        mTvOverview.setText(mMovie.getOverview());

    }

    @Override
    public void loadReviewStart() {
       // mLoadingReviewIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadReviewFinish(Object output) {
        //mLoadingReviewIndicator.setVisibility(View.INVISIBLE);
        ArrayList<Review> reviews = (ArrayList<Review>)output;
        mReviewAdapter.setReviewsData(reviews);
    }

    @Override
    public void loadVideoStart() {
        //mLoadingVideoIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadVideoFinish(Object output) {
       // mLoadingVideoIndicator.setVisibility(View.INVISIBLE);
        ArrayList<Video> videos = (ArrayList<Video>)output;
        mVideoAdapter.setVideosData(videos);
    }

    private void onFabClick() {

        FavoritesService favoritesService = new FavoritesService(getContext());

        if (favoritesService.isFavorite(mMovie)) {
            favoritesService.removeFromFavorites(mMovie);
            Snackbar.make(mRelativeLayout, R.string.remove_favorite, Snackbar.LENGTH_LONG).show();
            mFab.setImageResource(R.drawable.ic_star_border_black_24dp);
        } else {
            favoritesService.addToFavorites(mMovie);
            Snackbar.make(mRelativeLayout, R.string.add_favorite, Snackbar.LENGTH_LONG).show();
            mFab.setImageResource(R.drawable.ic_star_black_24dp);
        }
    }

    @Override
    public void onClick(Video video) {
        if (video != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
            startActivity(intent);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        if(mMovie != null) {
            Bundle movieBundle = new Bundle();
            movieBundle.putInt(MOVIE_ID_EXTRA, mMovie.getId());

            getLoaderManager().initLoader(REVIEW_LOADER, movieBundle, new FetchReviewsTask(getContext(), this));
            getLoaderManager().initLoader(VIDEO_LOADER, movieBundle, new FetchVideosTask(getContext(), this));
        }

        super.onActivityCreated(savedInstanceState);
    }
}
