package com.arturgiro.android.popularmovies.data;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

public class FavoritesService {

    private Context mContext;

    public FavoritesService(Context context) {
        this.mContext = context;
    }

    public void addToFavorites(Movie movie){
        mContext.getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, movie.toContentValues());
    }

    public void removeFromFavorites(Movie movie) {
        String stringId = Integer.toString(movie.getId());
        Uri uri = MoviesContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        mContext.getContentResolver().delete(uri, MoviesContract.MovieEntry._ID + " = " + movie.getId(), null);
    }

    public boolean isFavorite(Movie movie) {
        boolean favorite = false;
        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.MovieEntry.CONTENT_URI,
                null,
                MoviesContract.MovieEntry._ID + " = " + movie.getId(),
                null,
                null
        );
        if (cursor != null) {
            favorite = cursor.getCount() != 0;
            cursor.close();
        }
        return favorite;
    }

}
