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

public class GetMovieReviewAsyncTask extends AsyncTask<String,Void,MovieReview[]> {
    private String mApiKey;
    private final OnTaskCompleted mListener;
    private String movieID;
    DetailActivity activity;


    public GetMovieReviewAsyncTask(OnTaskCompleted listener, String apiKey, String movieID, DetailActivity activity) {
        super();

        mListener = listener;
        mApiKey = apiKey;
        this.movieID = movieID;
        this.activity = activity;
    }


    @Override
    protected MovieReview[] doInBackground(String... params) {
        HttpURLConnection urlConnection;
        BufferedReader reader;

        String moviesJsonStr;

        try {
            URL url = getUrl(params);

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


            moviesJsonStr = builder.toString();
        } catch (IOException e) {
            return null;
        }
            try {
                reader.close();
            } catch (final IOException e) {

            }
        try {
            // Parse JSON
            return getReviewDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
        }



        return null;
    }


    private MovieReview[] getReviewDataFromJson(String moviesJsonStr) throws JSONException {
        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = moviesJson.getJSONArray("results");

        MovieReview[] reviews = new MovieReview[resultsArray.length()];
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject movieInfo = resultsArray.getJSONObject(i);
            String author = movieInfo.getString("author");
            String content = movieInfo.getString("content");
            String url = movieInfo.getString("url");
            String id = movieInfo.getString("id");

            reviews[i] = new MovieReview(id,author,content,url);
        }

        return reviews;
    }

    //https://stackoverflow.com/questions/19167954/use-uri-builder-in-android-or-create-url-with-variables
    private URL getUrl(String[] parameters) throws MalformedURLException {
        final String baseURL = "https://api.themoviedb.org/3/movie/";
        final String APIKey = "api_key";

        Uri.Builder uri = Uri.parse(baseURL).buildUpon();

        uri.appendPath(movieID);
        uri.appendPath("reviews");
        uri.appendQueryParameter(APIKey, mApiKey);
        uri.build();
        Log.i("URL MOVIE REVIEW PULL:", uri.toString());
        return new URL(uri.toString());
    }



    @Override
    protected void onPostExecute(MovieReview[] reviews) {
        super.onPostExecute(reviews);
        activity.addReviews(reviews);

        // Notify UI
        mListener.onFetchReviewsTaskCompleted(reviews);
    }
}
