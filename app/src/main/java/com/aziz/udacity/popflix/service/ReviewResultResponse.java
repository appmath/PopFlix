package com.aziz.udacity.popflix.service;

import com.aziz.udacity.popflix.model.Review;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulate the list of reviews that are fetched by retrofit.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public class ReviewResultResponse {

    @SerializedName("results")
    public ArrayList<Review> mReviewList;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (mReviewList != null && !mReviewList.isEmpty()) {
            for (int i = 0; i < mReviewList.size(); i++) {
                builder.append(" Review " + i + ": ");
                builder.append(mReviewList.get(i));
                builder.append("\n");
            }
            return builder.toString();
        }
        return "No reviews!";

    }
}
