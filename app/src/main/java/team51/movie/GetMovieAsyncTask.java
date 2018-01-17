package team51.movie;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Drew on 11/29/17.
 */

public class GetMovieAsyncTask extends AsyncTask<String,Void,Movie[]> {
    String mApiKey;
    private final OnTaskCompleted mListener;
    String sortMethod;


    public GetMovieAsyncTask(OnTaskCompleted listener, String apiKey, String sortMethod) {
        super();

        mListener = listener;
        mApiKey = apiKey;
        this.sortMethod = sortMethod;
    }


    @Override
    protected Movie[] doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Holds data returned from the API
        String moviesJsonStr = null;

        try {
            URL url = getUrl(params);

            // Start connecting to get JSON
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();

            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }


            moviesJsonStr = builder.toString();
        } catch (IOException e) {
            return null;
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (final IOException e) {

            }
        }
        if (urlConnection != null) {
            urlConnection.disconnect();
        }



        try {
            // Parse JSON
            return getMoviesDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
        }

        return null;
    }


    private Movie[] getMoviesDataFromJson(String moviesJsonStr) throws JSONException {
        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = moviesJson.getJSONArray("results");

        Movie[] movies = new Movie[resultsArray.length()];
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject movieInfo = resultsArray.getJSONObject(i);
            String title = movieInfo.getString("original_title");
            String releaseDate = movieInfo.getString("release_date");
            String path = movieInfo.getString("poster_path");
            String overview = movieInfo.getString("overview");
            double vote = movieInfo.getDouble("vote_average");

            movies[i] = new Movie(title,releaseDate,path,overview,vote);
        }

        return movies;
    }

    //https://stackoverflow.com/questions/19167954/use-uri-builder-in-android-or-create-url-with-variables
    private URL getUrl(String[] parameters) throws MalformedURLException {
        final String baseURL = "https://api.themoviedb.org/3/movie/";
        final String sortBy = sortMethod;
        final String APIKey = "api_key";

        Uri.Builder uri = Uri.parse(baseURL).buildUpon();

        uri.appendPath(sortBy);
        uri.appendQueryParameter(APIKey, mApiKey);
        uri.build();
        Log.d("URL MOVIE PULL:", uri.toString());
        return new URL(uri.toString());
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);

        // Notify UI
        mListener.onFetchMoviesTaskCompleted(movies);
    }
}
