package com.aziz.udacity.popflix.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Movie trailer that get retrieved by retrofit.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public class Trailer implements Parcelable {

    public String id;
    public String name;

    /**
     * YouTube key, used to launch the trailer.
     */
    public String key;
    public String site;
    public String type;

    public Trailer() {
    }

    protected Trailer(Parcel in) {
        id = in.readString();
        name = in.readString();
        key = in.readString();
        site = in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(key);
        dest.writeString(site);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public String toString() {
        return "Trailer{" +
            "id='" + id + '\'' +
            "name='" + name + '\'' +
            ", key='" + key + '\'' +
            ", site='" + site + '\'' +
            ", type='" + type + '\'' +
            '}';
    }
}
