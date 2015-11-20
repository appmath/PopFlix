package com.aziz.udacity.popflix.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import com.aziz.udacity.popflix.model.Review;
import com.aziz.udacity.popflix.model.Trailer;

import java.util.ArrayList;

import static com.aziz.udacity.popflix.data.FlixContract.FlickEntry;
import static com.aziz.udacity.popflix.data.FlixContract.ReviewEntry;
import static com.aziz.udacity.popflix.data.FlixContract.TrailerEntry;

/**
 * Captures all the info related to a movie
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public class FlickBundleWrapper {

    private static final String TITLE = "TITLE";
    private static final String POSTER_URL = "POSTER_PATH";
    private static final String RELEASE_DATE = "RELEASE_DATE";
    private static final String SYNOPSIS = "SYNOPSIS";
    private static final String FLICK_LENGTH = "MOVIE_LENGTH";
    private static final String POSTER_BYTES = "POSTER_BYTES";
    private static final String DUAL_PANE = "DUAL_PANE";

    private static final String USER_RATING = "USER_RATING";
    private static final String FLICK_ID = "FLICK_ID";
    private static final String IS_PERSISTED = "IS_PERSISTED";
    
    private static final String IS_FAVORITE_MODE = "IS_FAVORITE_MODE";

    private static final String REVIEWS = "REVIEWS";
    private static final String TRAILERS = "TRAILERS";
    public static final ContentValues[] CONTENT_VALUES = new ContentValues[0];
    public static final String[] ID_S = new String[0];
    public static final String IMAGE_WIDTH = "IMAGE_WIDTH";

    private final Bundle mBundle;

    public FlickBundleWrapper(Bundle bundle) {
        mBundle = bundle;
    }


    public Bundle getBundle() {
        return mBundle;
    }

    public String getTitle() {
        return mBundle.getString(TITLE);
    }

    public FlickBundleWrapper title(String title) {
        mBundle.putString(TITLE, title);
        return this;
    }

    public boolean getDualPane() {
        return mBundle.getBoolean(DUAL_PANE);
    }

    public FlickBundleWrapper dualPane(boolean dualPane) {
        mBundle.putBoolean(DUAL_PANE, dualPane);
        return this;
    }

    public boolean isFavoriteMode() {
        return mBundle.getBoolean(IS_FAVORITE_MODE);
    }

    public FlickBundleWrapper favoriteMode(boolean isFavoriteMode) {
        mBundle.putBoolean(IS_FAVORITE_MODE, isFavoriteMode);
        return this;
    }

    public String getPosterUrl() {
        return mBundle.getString(POSTER_URL);
    }

    public FlickBundleWrapper posterUrl(String posterUrl) {
        mBundle.putString(POSTER_URL, posterUrl);
        return this;
    }

    public byte[] getPosterBytes() {
        return mBundle.getByteArray(POSTER_BYTES);
    }

    public FlickBundleWrapper posterBytes(byte[] bytes) {
        mBundle.putByteArray(POSTER_BYTES, bytes);
        return this;
    }

    public String getReleaseDate() {
        return mBundle.getString(RELEASE_DATE);
    }

    public FlickBundleWrapper releaseDate(String releaseDate) {
        mBundle.putString(RELEASE_DATE, releaseDate);
        return this;
    }

    public String getSynopsis() {
        return mBundle.getString(SYNOPSIS);
    }

    public FlickBundleWrapper synopsis(String synopsis) {
        mBundle.putString(SYNOPSIS, synopsis);
        return this;
    }

    public boolean getIsPersisted() {
        return mBundle.getBoolean(IS_PERSISTED);
    }

    public FlickBundleWrapper isPersisted(boolean IsPersisted) {
        mBundle.putBoolean(IS_PERSISTED, IsPersisted);
        return this;
    }

    public int getImagewidth() {
        return mBundle.getInt(IMAGE_WIDTH);
    }

    public FlickBundleWrapper imageWidth(int imageWidth) {
        mBundle.putInt(IMAGE_WIDTH, imageWidth);
        return this;
    }

    public String getFlickLength() {
        return mBundle.getString(FLICK_LENGTH);
    }

    public FlickBundleWrapper flickLength(String flickLength) {
        mBundle.putString(FLICK_LENGTH, flickLength);
        return this;
    }

    public float getUserRating() {
        return mBundle.getFloat(USER_RATING);
    }

    public FlickBundleWrapper userRating(float userRating) {
        mBundle.putFloat(USER_RATING, userRating);
        return this;
    }

    public String getFlickId() {
        return mBundle.getString(FLICK_ID);
    }

    public FlickBundleWrapper flickId(String flickId) {
        mBundle.putString(FLICK_ID, flickId);
        return this;
    }

    public ArrayList<Review> getReviews() {
        return mBundle.getParcelableArrayList(REVIEWS);
    }

    public FlickBundleWrapper reviews(ArrayList<Review> reviews) {
        mBundle.putParcelableArrayList(REVIEWS, reviews);
        return this;
    }

    public ArrayList<Trailer> getTrailers() {
        return mBundle.getParcelableArrayList(TRAILERS);
    }

    public FlickBundleWrapper trailers(ArrayList<Trailer> trailers) {
        mBundle.putParcelableArrayList(TRAILERS, trailers);
        return this;
    }

    public ContentValues getFlickContentValues() {
        ContentValues cv = new ContentValues(mBundle.size());
        cv.put(FlickEntry._ID, getFlickId());
        cv.put(FlickEntry.COLUMN_TITLE, getTitle());
        cv.put(FlickEntry.COLUMN_POSTER_PATH, getPosterUrl());
        cv.put(FlickEntry.COLUMN_RELEASE_YEAR, getReleaseDate());
        cv.put(FlickEntry.COLUMN_LENGTH, getFlickLength());
        cv.put(FlickEntry.COLUMN_RATING, getUserRating());
        cv.put(FlickEntry.COLUMN_PLOT_SYNOPSIS, getSynopsis());
        cv.put(FlickEntry.COLUMN_POSTER_BLOB, getPosterBytes());
        return cv;
    }

    public ContentValues[] getReviewContentValues() {
        ContentValues[] cvs = CONTENT_VALUES;

        if (hasReviews()) {
            final ArrayList<Review> reviews = getReviews();
            final String flickId = getFlickId();
            cvs = new ContentValues[reviews.size()];
            for (int i = 0; i < reviews.size(); i++) {
                Review review = reviews.get(i);
                cvs[i] = new ContentValues();
                cvs[i].put(ReviewEntry._ID, review.id);
                cvs[i].put(ReviewEntry.COLUMN_FLICK_ID, flickId);
                cvs[i].put(ReviewEntry.COLUMN_AUTHOR, review.author);
                cvs[i].put(ReviewEntry.COLUMN_CONTENT, review.content);
            }
        }
        return cvs;
    }

    public boolean hasReviews() {
        final ArrayList<Review> reviews = getReviews();
        return reviews != null && reviews.size() > 0;
    }

    public boolean hasTrailers() {
        final ArrayList<Trailer> trailers = getTrailers();
        return trailers != null && trailers.size() > 0;
    }

    public ContentValues[] getTrailerContentValues() {
        ContentValues[] cvs = CONTENT_VALUES;

        if (hasTrailers()) {
            final ArrayList<Trailer> trailers = getTrailers();
            final String flickId = getFlickId();
            cvs = new ContentValues[trailers.size()];
            for (int i = 0; i < trailers.size(); i++) {
                Trailer trailer = trailers.get(i);
                cvs[i] = new ContentValues();
                cvs[i].put(TrailerEntry._ID, trailer.id);
                cvs[i].put(TrailerEntry.COLUMN_FLICK_ID, flickId);
                cvs[i].put(TrailerEntry.COLUMN_KEY, trailer.key);
                cvs[i].put(TrailerEntry.COLUMN_NAME, trailer.name);
                cvs[i].put(TrailerEntry.COLUMN_TYPE, trailer.type);
            }
        }
        return cvs;
    }

    public String[] getTrailerIds( ) {
        String[] ids = ID_S;
        if (hasTrailers()) {
            final ArrayList<Trailer> trailers = getTrailers();
            ids = new String[trailers.size()];
            for (int i = 0; i < trailers.size(); i++) {
                Trailer trailer = trailers.get(i);
                ids[i] = trailer.id;
            }
        }
        return ids;
    }

    public String[] getReviewIds( ) {
        String[] ids = ID_S;
        if (hasReviews()) {
            final ArrayList<Review> reviews = getReviews();
            ids = new String[reviews.size()];
            for (int i = 0; i < reviews.size(); i++) {
                Review review = reviews.get(i);
                ids[i] = review.id;
            }
        }
        return ids;
    }

    @Override
    public String toString() {
        return "FlickBundleWrapper{" +
            "mBundle=" + mBundle +
            '}';
    }
}
