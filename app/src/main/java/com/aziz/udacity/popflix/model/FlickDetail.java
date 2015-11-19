package com.aziz.udacity.popflix.model;

import com.google.gson.annotations.SerializedName;

/**
 * Movie details that get retrieved by retrofit.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public class FlickDetail {

    public String id;

    @SerializedName("original_title")
    public String originalTitle;

    @SerializedName("overview")
    public String plotSynopsis;

    @SerializedName("release_date")
    public String releaseDate;

    @SerializedName("runtime")
    public String movieLength;

    public String getYear( ) {
        if (releaseDate != null && releaseDate.length() > 0 && releaseDate.contains("-")) {
            return releaseDate.substring(0, releaseDate.indexOf("-"));
        }
        return releaseDate;
    }


    @Override
    public String toString() {
        return "FlickDetail{" +
            "id='" + id + '\'' +
            "title='" + originalTitle + '\'' +
            ", plotSynopsis='" + plotSynopsis + '\'' +
            ", releaseDate='" + getYear() + '\'' +
            ", movieLength='" + movieLength + '\'' +
            '}';
    }
}
