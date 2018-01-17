package team51.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Drew on 12/3/17.
 */

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tvOriginalTitle = (TextView) findViewById(R.id.textview_original_title);
        ImageView ivPoster = (ImageView) findViewById(R.id.imageview_poster);
        TextView tvOverView = (TextView) findViewById(R.id.textview_overview);
        TextView tvVoteAverage = (TextView) findViewById(R.id.textview_vote_average);
        TextView tvReleaseDate = (TextView) findViewById(R.id.textview_release_date);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("Parsel Movie");

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
    }
}
