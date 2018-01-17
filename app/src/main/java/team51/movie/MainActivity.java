package team51.movie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    public String sortM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setOnItemClickListener(moviePosterClickListener);
        setUpSharedPreferences();
        sortM =  getString(R.string.sort_rating_url);
        if (savedInstanceState != null) {

        }else{
            getMovies();
        }
    }

    public void setUpSharedPreferences(){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu,menu);
        return true;
    }

    @Override public boolean onPrepareOptionsMenu(Menu menu) {
        switch (sortM) {
            case "popular":
                menu.findItem(R.id.sort_popularity).setChecked(true);
                break;
            case "top_rated":
                menu.findItem(R.id.sort_rating).setChecked(true);
                break;
        }
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_popularity:
                item.setChecked(!item.isChecked());
                sortM = getString(R.string.sort_popularity_url);
                getMovies();
                break;

            case R.id.sort_rating:
                item.setChecked(!item.isChecked());
                sortM = getString(R.string.sort_rating_url);
                getMovies();
                break;


        }
        return super.onOptionsItemSelected(item);
    }





    private void getMovies() {
        if (isNetworkAvailable()) {
            String apiKey = getString(R.string.movie_api_key);

            // Listener for UI update
            OnTaskCompleted taskCompleted = new OnTaskCompleted() {
                @Override
                public void onFetchMoviesTaskCompleted(Movie[] movies) {
                    gridView.setAdapter(new ImageBaseAdapter(getApplicationContext(), movies));
                }
            };
            SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
            String sortMethod = sortM;
            Log.d("GetMovies", sortMethod);
//            sortMethod = "popular";
            GetMovieAsyncTask movieTask = new GetMovieAsyncTask(taskCompleted, apiKey, sortMethod);
            movieTask.execute(sortMethod);
        } else {

        }
    }



    // From: https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

    private final GridView.OnItemClickListener moviePosterClickListener = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Movie movie = (Movie) parent.getItemAtPosition(position);

            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra("Parsel Movie", movie);
            Log.d("MainDebug", movie.toString());
            startActivity(intent);
        }
    };




}
