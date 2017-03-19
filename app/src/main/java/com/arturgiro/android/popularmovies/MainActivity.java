package com.arturgiro.android.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arturgiro.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadMoviesData();
    }

    /**
     * This method will get the user's preferred sort criteria, and then tell some
     * background method to get the movies data in the background.
     */
    private void loadMoviesData() {
        //TODO - Implementar showMoviesDataView()
        // showMoviesDataView();

        //TODO - Implementar getPreferredSort
         String sort = "popular";//SunshinePreferences.getPreferredSort(this);

        new FetchMoviesTask().execute(sort);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO mLoadingIndicator.setVisibility(View.VISIBLE);
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

                //// TODO: 18/03/2017
                //String[] simpleJsonMoviesData = OpenWeatherJsonUtils
                  //      .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);

                //return simpleJsonWeatherData;
                return null;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] movieData) {
//            mLoadingIndicator.setVisibility(View.INVISIBLE);
//            if (weatherData != null) {
//                showWeatherDataView();
//                mForecastAdapter.setWeatherData(weatherData);
//            } else {
//                showErrorMessage();
//            }
        }
    }
}
