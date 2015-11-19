package com.aziz.udacity.popflix.model;

import java.util.ArrayList;

/**
 * Wrapper to capture the details associated with a movie.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public class DetailWrapper {

    private ArrayList<Review> mReviewList = new ArrayList<>();
    private ArrayList<Trailer> mTrailerList = new ArrayList<>();

    private String mTitle;
    private String mReleaseYear;
    private String mLength;
    private String mSynopsis;
    private float mUserRating;


    public void addReview(Review review) {
        mReviewList.add(review);
    }

    public void addTrailer(Trailer trailer) {
        mTrailerList.add(trailer);
    }

    public ArrayList<Review> getReviewList() {
        return mReviewList;
    }

    public ArrayList<Trailer> getTrailerList() {
        return mTrailerList;
    }

    public String getReleaseYear() {
        return mReleaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        mReleaseYear = releaseYear;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getLength() {
        return mLength;
    }

    public void setLength(String length) {
        mLength = length;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public void setSynopsis(String synopsis) {
        mSynopsis = synopsis;
    }

    public float getUserRating() {
        return mUserRating;
    }

    public void setUserRating(String userRating) {
        mUserRating = Float.valueOf(userRating).floatValue();
    }
}
