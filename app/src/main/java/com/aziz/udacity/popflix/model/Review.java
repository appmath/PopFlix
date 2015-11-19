package com.aziz.udacity.popflix.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * Movie review that gets retrieved by retrofit.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public class Review implements Parcelable{

    public String id;
    public String author;
    public String content;

    public Review() {
    }

    protected Review(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (content != null) {
            final int maxLength = 300;
            if (content.length() > maxLength) {
                builder.append(content.substring(0, maxLength - 50));
                builder.append("...");
                builder.append(content.substring(content.length() - 50, content.length()));
                builder.append("- End of movie review -");

            } else {
                builder.append(content);
            }
        }

        return "Review{" +
            "id='" + id + '\'' +
            "author='" + author + '\'' +
            ", content='" + builder.toString() + '\'' +
            '}';
    }
}
