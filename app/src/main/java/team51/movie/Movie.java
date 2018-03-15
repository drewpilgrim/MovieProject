package team51.movie;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Drew on 11/29/17.
 */

public class Movie implements Parcelable{

    private final String base_url = "https://image.tmdb.org/t/p/w185";
    private String movieTitle,movieReleaseDate,moviePath,movieOverivew;
    private Double movieVote;

    private int id;

    //Default Constructor
    public Movie(){

    }


    //Constructor
    public Movie(String movieTitle, String movieReleaseDate, String moviePath, String movieOverivew, Double movieVote, int id) {
        this.movieTitle = movieTitle;
        this.movieReleaseDate = movieReleaseDate;
        this.moviePath = moviePath;
        this.movieOverivew = movieOverivew;
        this.movieVote = movieVote;
        this.id = id;
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

    public void setId(int id) {
        this.id = id;
    }

    //Getters
    public String getOriginalTitle() {
        return movieTitle;
    }

    //Append base url with size w185
    public String getPosterPath() {
        return base_url + moviePath;
    }

    public String getPath(){return moviePath;}

    public String getOverview() {
        return movieOverivew;
    }

    public Double getMovieVote() {
        return movieVote;
    }

    public String getReleaseDate() {
        return movieReleaseDate;
    }

    public String getVoteAverage() {
        return String.valueOf(getMovieVote()) + "/10";
    }

    public int getId() {
        return id;
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
        dest.writeInt(id);
        Log.d("Parsel_WID", id +"");
    }

    private Movie(Parcel in) {
        movieTitle = in.readString();
        movieReleaseDate = in.readString();
        moviePath = in.readString();
        movieOverivew = in.readString();
        movieVote = (Double) in.readValue(Double.class.getClassLoader());
        id = in.readInt();
        Log.d("Parsel_RID", id +"");

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
