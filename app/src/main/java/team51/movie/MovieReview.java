package team51.movie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Drew on 2/11/18.
 */

public class MovieReview implements Parcelable {
    private final String base_url = "https://image.tmdb.org/t/p/w185";


    private String id;
    private String author;



    private String content;
    private String url;

    public MovieReview(String id, String author, String content, String url){
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }



    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }




        //Default Constructor
        public MovieReview(){

        }

        //Constructor

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(id);
            dest.writeString(author);
            dest.writeString(content);
            dest.writeString(url);

        }

        private MovieReview(Parcel in) {
            id = in.readString();
            author = in.readString();
            content = in.readString();
            url = in.readString();
        }

        public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
            public MovieReview createFromParcel(Parcel source) {
                return new MovieReview(source);
            }

            public MovieReview[] newArray(int size) {
                return new MovieReview[size];
            }
        };

}
