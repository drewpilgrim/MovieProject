package team51.movie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Drew on 11/29/17.
 */

public class Movie implements Parcelable{

    private final String base_url = "https://image.tmdb.org/t/p/w185";
    private String movieTitle,movieReleaseDate,moviePath,movieOverivew;
    private Double movieVote;

    //Default Constructor
    public Movie(){

    }


    //Constructor
    public Movie(String movieTitle, String movieReleaseDate, String moviePath, String movieOverivew, Double movieVote) {
        this.movieTitle = movieTitle;
        this.movieReleaseDate = movieReleaseDate;
        this.moviePath = moviePath;
        this.movieOverivew = movieOverivew;
        this.movieVote = movieVote;
    }

    //Setters
    public void setTitle(String title) {
        movieTitle = title;
    }
    public void setPath(String path) {
        moviePath = path;
    }
    
    public void setOverview(String overview) {
        movieOverivew = overview;
    }

    public void setVote(Double vote) {
        movieVote = vote;
    }

    public void setReleaseDate(String releaseDate) {
        movieReleaseDate = releaseDate;
    }

    //Getters
    public String getOriginalTitle() {
        return movieTitle;
    }

    //Append base url with size w185
    public String getPosterPath() {
        return base_url + moviePath;
    }

    public String getOverview() {
        return movieOverivew;
    }


    private Double getMovieVote() {
        return movieVote;
    }

    public String getReleaseDate() {
        return movieReleaseDate;
    }

    public String getVoteAverage() {
        return String.valueOf(getMovieVote()) + "/10";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieTitle);
        dest.writeString(movieReleaseDate);
        dest.writeString(moviePath);
        dest.writeString(movieOverivew);
        dest.writeValue(movieVote);
    }

    private Movie(Parcel in) {
        movieTitle = in.readString();
        movieReleaseDate = in.readString();
        moviePath = in.readString();
        movieOverivew = in.readString();
        movieVote = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
