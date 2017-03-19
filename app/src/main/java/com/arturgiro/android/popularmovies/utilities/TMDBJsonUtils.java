package com.arturgiro.android.popularmovies.utilities;

import android.content.Context;

import org.json.JSONException;

public class TMDBJsonUtils {

    private static final String[] fakeValues = {"um", "dois", "três", "quatro", "cinco", "seis"};

    public static String[] getMoviesFromJson(Context context, String forecastJsonStr)throws JSONException
    {
        return fakeValues;
    }

}
