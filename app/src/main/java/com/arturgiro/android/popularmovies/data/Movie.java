package com.arturgiro.android.popularmovies.data;


import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    public static final String MOVIE_IDENTIFIER = "MOVIE";

    private int id;
    private String originalTitle;
    private String posterPath;
    private String overview;
    private String rating;
    private String releaseDate;


    public Movie(int id, String originalTitle, String posterPath, String overview, String rating, String releaseDate) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

        /*
     * Parcelable particular
     */

    public static final String PARCELABLE_KEY = "movie";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(rating);
        dest.writeString(releaseDate);
    }

    private Movie(Parcel in) {
        id = in.readInt();
        originalTitle = in.readString();
        posterPath  = in.readString();
        overview = in.readString();
        rating = in.readString();
        releaseDate = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.MovieEntry._ID, id);
        values.put(MoviesContract.MovieEntry.COLUMN_TITLE, originalTitle);
        values.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, overview);
        values.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        values.put(MoviesContract.MovieEntry.COLUMN_POSTER_PATH, posterPath);
        values.put(MoviesContract.MovieEntry.COLUMN_USER_RATING, rating);
        return values;
    }
}
