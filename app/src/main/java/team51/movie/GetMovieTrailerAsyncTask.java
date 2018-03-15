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

public class GetMovieTrailerAsyncTask extends AsyncTask<String,Void,MovieTrailer[]> {
    private String ApiKey;
    private final OnTaskCompleted Listener;
    private String movieID;
    private DetailActivity activity;


    public GetMovieTrailerAsyncTask(OnTaskCompleted listener, String apiKey, String movieID, DetailActivity activity) {
        super();

        this.Listener = listener;
        this.ApiKey = apiKey;
        this.movieID = movieID;
        this.activity = activity;
    }


    @Override
    protected MovieTrailer[] doInBackground(String... params) {
        HttpURLConnection urlConnection;
        BufferedReader reader;
        String trailersJSONstr;

        try {
            URL url = getUrl();

            // Connect to get JSON
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


            trailersJSONstr = builder.toString();
        } catch (IOException e) {
            return null;
        }
            try {
                reader.close();
            } catch (final IOException e) {

            }
        try {
            // Parse JSON
            return getMovieTrailerDataFromJson(trailersJSONstr);
        } catch (JSONException e) {
        }

        return null;
    }


    private MovieTrailer[] getMovieTrailerDataFromJson(String moviesJsonStr) throws JSONException {
        JSONObject trailersJSON = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = trailersJSON.getJSONArray("results");

        MovieTrailer[] trailers = new MovieTrailer[resultsArray.length()];
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject movieInfo = resultsArray.getJSONObject(i);
            String id = movieInfo.getString("id");
            String iso_639_1 = movieInfo.getString("iso_639_1");
            String iso_3166_1 = movieInfo.getString("iso_3166_1");
            String key = movieInfo.getString("key");
            String name = movieInfo.getString("name");
            String site = movieInfo.getString("site");
            String size = movieInfo.getString("size");
            String type = movieInfo.getString("type");

            trailers[i] = new MovieTrailer(id, iso_639_1, iso_3166_1, key, name, site, size, type);
        }

        return trailers;
    }

    //https://stackoverflow.com/questions/19167954/use-uri-builder-in-android-or-create-url-with-variables
    private URL getUrl() throws MalformedURLException {
        final String baseURL = "https://api.themoviedb.org/3/movie/" + movieID;
        final String APIKey = "api_key";

        Uri.Builder uri = Uri.parse(baseURL).buildUpon();
        uri.appendPath("videos");
        uri.appendQueryParameter(APIKey, ApiKey);
        uri.build();
        Log.d("URL MOVIE TRAILER PULL:", uri.toString());
        return new URL(uri.toString());
        //http://api.themoviedb.org/3/movie/198663/videos?api_key=ccc65938377c518796bde2e9995d7dbc
    }

    @Override
    protected void onPostExecute(MovieTrailer[] trailers) {
        super.onPostExecute(trailers);
        activity.addTrailers(trailers);

        // Notify UI
        Listener.onFetchTrailersTaskCompleted(trailers);
    }
}
