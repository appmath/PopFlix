package com.aziz.udacity.popflix.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * Movie that gets retrieved by retrofit.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public class Flick implements Parcelable{
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w185";
    private static final String LARGE_POSTER_SIZE = "w342";

    public int id;

    @SerializedName("original_title")
    public String originalTitle;

    @SerializedName("poster_path")
    public String posterPath;

    @SerializedName("vote_average")
    public float userRating;

    @SerializedName("backdrop_path")
    public String backdropPath;


    public Flick() {
    }

    protected Flick(Parcel in) {
        originalTitle = in.readString();
        posterPath = in.readString();
        userRating = in.readFloat();
        id = in.readInt();
        backdropPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeFloat(userRating);
        dest.writeInt(id);
        dest.writeString(backdropPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Flick> CREATOR = new Creator<Flick>() {
        @Override
        public Flick createFromParcel(Parcel in) {
            return new Flick(in);
        }

        @Override
        public Flick[] newArray(int size) {
            return new Flick[size];
        }
    };

    public String getPosterUrl( ) {
        return POSTER_BASE_URL + POSTER_SIZE + posterPath;
    }
    public String getLargePosterUrl( ) {
        return POSTER_BASE_URL + LARGE_POSTER_SIZE + posterPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flick flick = (Flick) o;

        if (id != flick.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Flick{" +
            "originalTitle='" + originalTitle + '\'' +
            ", flickPosterPath='" + posterPath + '\'' +
            ", userRating=" + userRating +
            ", id=" + id +
            ", backdropPath='" + backdropPath + '\'' +
            '}';
    }
}
