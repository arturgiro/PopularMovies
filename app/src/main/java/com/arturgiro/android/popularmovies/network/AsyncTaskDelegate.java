package com.arturgiro.android.popularmovies.network;

/**
 * Created by artur.moreno on 24/03/2017.
 */

public interface AsyncTaskDelegate {
    void processStart();
    void processFinish(Object output);
}
