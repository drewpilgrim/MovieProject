package team51.movie;

/**
 * Created by Drew on 11/30/17.
 */

interface OnTaskCompleted {
    void onFetchMoviesTaskCompleted(Movie[] movies);
    void onFetchTrailersTaskCompleted(MovieTrailer[] movies);
    void onFetchReviewsTaskCompleted(MovieReview[] movies);


}
