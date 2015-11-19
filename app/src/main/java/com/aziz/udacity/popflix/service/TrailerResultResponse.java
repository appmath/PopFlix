package com.aziz.udacity.popflix.service;

import com.aziz.udacity.popflix.model.Trailer;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulate the list of trailers that are fetched by retrofit.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public class TrailerResultResponse {

    @SerializedName("results")
    public ArrayList<Trailer> mTrailerList;

    @Override
    public String toString() {
        return "TrailerResultResponse{" +
            "mTrailerList=" + mTrailerList +
            '}';
    }
}
