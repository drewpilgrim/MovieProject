package team51.movie;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;


/**
 * Created by Drew on 12/3/17.
 */

public class DetailActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{
    private YouTubePlayerView trailerView;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    Movie movie;
    String apiKey;
    String movieID;
    MovieReview[] reviews;
    String[] trailerKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        apiKey = getString(R.string.movie_api_key);
        TextView tvOriginalTitle = (TextView) findViewById(R.id.textview_original_title);
        ImageView ivPoster = (ImageView) findViewById(R.id.imageview_poster);
        TextView tvOverView = (TextView) findViewById(R.id.textview_overview);
        TextView tvVoteAverage = (TextView) findViewById(R.id.textview_vote_average);
        TextView tvReleaseDate = (TextView) findViewById(R.id.textview_release_date);

        Intent intent = getIntent();
         movie = intent.getParcelableExtra("Parsel Movie");

        tvOriginalTitle.setText(movie.getOriginalTitle());

        Picasso.with(this)
                .load(movie.getPosterPath())
                .resize(185,273)
                .into(ivPoster);

        String overView = movie.getOverview();

        if (overView != null) {
            tvOverView.setText(overView);
        }else{
            tvOverView.setText(getResources().getString(R.string.no_overview));
        }

        if(movie.getVoteAverage() != null) {
            tvVoteAverage.setText(movie.getVoteAverage());
        }else{
            tvVoteAverage.setText(R.string.no_vote);
        }

        String releaseDate = movie.getReleaseDate();
        if(releaseDate != null) {
            tvReleaseDate.setText(releaseDate);
        }else{
            tvReleaseDate.setText(R.string.no_release);
        }

        //Get Trailers

//        trailerView = (YouTubePlayerView) findViewById(R.id.trailer_view);

        // https://www.androidhive.info/2014/12/how-to-play-youtube-video-in-android-app/
//        trailerView.initialize(youtubeAPI, this);


        OnTaskCompleted taskCompleted = new OnTaskCompleted() {
            @Override
            public void onFetchMoviesTaskCompleted(Movie[] movies) {

            }

            @Override
            public void onFetchTrailersTaskCompleted(MovieTrailer[] trailers) {

            }

            @Override
            public void onFetchReviewsTaskCompleted(MovieReview[] reviews) {
                Log.d("REVIEW_LENGTH","" + reviews.length);
            }
        };
        movieID = Integer.toString(movie.getId());
        Log.i("Movie_ID_INT", "" + movie.getId());
        Log.i("Movie_ID_STR", movieID);
        //get reviews
        GetMovieReviewAsyncTask reviewTask = new GetMovieReviewAsyncTask(taskCompleted, apiKey,movieID,this);
        reviewTask.execute();

        GetMovieTrailerAsyncTask trailerTask = new GetMovieTrailerAsyncTask(taskCompleted, apiKey,movieID,this);
        trailerTask.execute();

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            for (int i = 0; i< trailerKeys.length; i++) {
                player.cueVideo(trailerKeys[i]);
                Log.d("Video Cued:", trailerKeys[i]);
            }

            // Hiding player controls
//            player.setPlayerStyle(PlayerStyle.CHROMELESS);
        }
    }


    public void addReviews(MovieReview[] reviews){
        if (reviews != null) {
            for (int i = 0; i < reviews.length; i++) {
                TextView review = new TextView(this);
                review.setText(reviews[i].getContent());
                TextView author = new TextView(this);
                author.setText("     - " + reviews[i].getAuthor());
                author.setTextSize(20);
                LinearLayout linear = (LinearLayout) findViewById(R.id.ReviewsLinearLayout);
                linear.addView(review);
                linear.addView(author);
                Log.d("Review Created", reviews[i].getContent());
            }
        }
    }

    public void addTrailers(MovieTrailer[] trailers){
        if (trailers != null) {
            YouTubePlayerView video = new YouTubePlayerView(this);
            video.initialize(Integer.toString(R.string.youtube_api_key),this);
            LinearLayout linear = (LinearLayout) findViewById(R.id.TrailersLinearLayout);
            linear.addView(video);
            trailerKeys = new String[trailers.length];

            for (int i = 0; i < trailers.length; i++) {
                trailerKeys[i] = trailers[i].getKey();
                Log.d("Trailer Created", "id: "+ trailerKeys[i]);
            }
        }
    }



    public void onClickFavorite(View view){
        ContentValues values = new ContentValues();
        values.put(MovieProvider.MOVIE_TITLE, (movie.getOriginalTitle()));
        values.put(MovieProvider.MOVIE_OVERVIEW, (movie.getOverview()));
        values.put(MovieProvider.MOVIE_POSTER, (movie.getPath()));
        values.put(MovieProvider.MOVIE_RELEASE, (movie.getReleaseDate()));
        values.put(MovieProvider.MOVIE_VOTE, (movie.getMovieVote()));
        values.put(MovieProvider.MOVIE_API_ID, (movie.getId()));


        Uri uri = getContentResolver().insert(
                MovieProvider.CONTENT_URI, values);

        Toast.makeText(getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();


    }

}




