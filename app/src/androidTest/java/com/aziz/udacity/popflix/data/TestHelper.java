package com.aziz.udacity.popflix.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.test.AndroidTestCase;
import com.aziz.udacity.popflix.ui.FlickBundleWrapper;
import com.aziz.udacity.popflix.util.Utils;
import com.aziz.udacity.popflix.model.Review;
import com.aziz.udacity.popflix.model.Trailer;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static com.aziz.udacity.popflix.data.FlixContract.*;

/**
 * Description
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public final class TestHelper extends AndroidTestCase {

    public static final int JP_FLICK_ID = 135397;
    public static final String JP_FLICK_ID_STR = String.valueOf(JP_FLICK_ID);
    public static final int MARTIAN_FLICK_ID = 243408;
    public static final String MARTIAN_FLICK_ID_STR = String.valueOf(MARTIAN_FLICK_ID);

    public static byte[] getImageBytes() {
        return Utils.convertToBytes(Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888));
    }

    static ContentValues createJpFlickValues() {
        ContentValues flickValues = new ContentValues();
        flickValues.put(FlickEntry._ID, JP_FLICK_ID);
        flickValues.put(FlickEntry.COLUMN_TITLE, "Jurassic World");
        flickValues.put(FlickEntry.COLUMN_POSTER_PATH, "/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg");
        flickValues.put(FlickEntry.COLUMN_RELEASE_YEAR, "2015");
        flickValues.put(FlickEntry.COLUMN_LENGTH, "124");
        flickValues.put(FlickEntry.COLUMN_RATING, 6.9);
        flickValues.put(FlickEntry.COLUMN_PLOT_SYNOPSIS, getJpSynopsis());
        flickValues.put(FlickEntry.COLUMN_POSTER_BLOB, getImageBytes());

        return flickValues;
    }

    static ContentValues createJpReviewValues1() {
        ContentValues reviewValues = new ContentValues();
        reviewValues.put(ReviewEntry._ID, "55910381c3a36807f900065d");
        reviewValues.put(ReviewEntry.COLUMN_FLICK_ID, JP_FLICK_ID);
        reviewValues.put(ReviewEntry.COLUMN_AUTHOR, "/jp_reviewer_1");
        reviewValues.put(ReviewEntry.COLUMN_CONTENT, getJpContent1());
        return reviewValues;
    }

    static ContentValues createJpReviewValues2() {
        ContentValues reviewValues = new ContentValues();
        reviewValues.put(ReviewEntry._ID, "58998381c3a36807f234335e");
        reviewValues.put(ReviewEntry.COLUMN_FLICK_ID, JP_FLICK_ID);
        reviewValues.put(ReviewEntry.COLUMN_AUTHOR, "/jp_reviewer_2");
        reviewValues.put(ReviewEntry.COLUMN_CONTENT, getJpContent2());
        return reviewValues;
    }

    static ContentValues createJpTrailerValues1() {
        ContentValues trailerValues = new ContentValues();
        trailerValues.put(TrailerEntry._ID, "5576eac192514111e4001b03");
        trailerValues.put(TrailerEntry.COLUMN_FLICK_ID, JP_FLICK_ID);
        trailerValues.put(TrailerEntry.COLUMN_KEY, "lP-sUUUfamw");
        trailerValues.put(TrailerEntry.COLUMN_NAME, "Official Trailer 3");
        trailerValues.put(TrailerEntry.COLUMN_TYPE, "Trailer");

        return trailerValues;
    }

    static ContentValues createJpTrailerValues2() {
        ContentValues trailerValues = new ContentValues();
        trailerValues.put(TrailerEntry._ID, "54749bea9251414f41001b58");
        trailerValues.put(TrailerEntry.COLUMN_FLICK_ID, JP_FLICK_ID);
        trailerValues.put(TrailerEntry.COLUMN_KEY, "bvu-zlR5A8Q");
        trailerValues.put(TrailerEntry.COLUMN_NAME, "Teaser");
        trailerValues.put(TrailerEntry.COLUMN_TYPE, "Teaser");

        return trailerValues;
    }

    static ContentValues createJpTrailerValues3() {
        ContentValues trailerValues = new ContentValues();
        trailerValues.put(TrailerEntry._ID, "5474d2339251416e58002ae1");
        trailerValues.put(TrailerEntry.COLUMN_FLICK_ID, JP_FLICK_ID);
        trailerValues.put(TrailerEntry.COLUMN_KEY, "RFinNxS5KN4");
        trailerValues.put(TrailerEntry.COLUMN_NAME, "Official Trailer");
        trailerValues.put(TrailerEntry.COLUMN_TYPE, "Trailer");

        return trailerValues;
    }

    static ContentValues createMartianFlickValues() {
        ContentValues flickValues = new ContentValues();
        flickValues.put(FlickEntry._ID, MARTIAN_FLICK_ID);
        flickValues.put(FlickEntry.COLUMN_TITLE, "The Martian");
        flickValues.put(FlickEntry.COLUMN_POSTER_PATH, "/5aGhaIHYuQbqlHWvWYqMCnj40y2.jpg");
        flickValues.put(FlickEntry.COLUMN_RELEASE_YEAR, "2015");
        flickValues.put(FlickEntry.COLUMN_LENGTH, "141");
        flickValues.put(FlickEntry.COLUMN_RATING, 7.7);
        flickValues.put(FlickEntry.COLUMN_PLOT_SYNOPSIS, getMartianSynopsis());
        flickValues.put(FlickEntry.COLUMN_POSTER_BLOB, getImageBytes());

        return flickValues;
    }

    static ContentValues createMartianReviewValues1() {
        ContentValues reviewValues = new ContentValues();
        reviewValues.put(ReviewEntry._ID, "5619f70d9251415d3100129c");
        reviewValues.put(ReviewEntry.COLUMN_FLICK_ID, MARTIAN_FLICK_ID);
        reviewValues.put(ReviewEntry.COLUMN_AUTHOR, "/jonlikesmoviesthatdontsuck");
        reviewValues.put(ReviewEntry.COLUMN_CONTENT, getMartianContent1());

        return reviewValues;
    }

    static ContentValues createMartianReviewValues2() {
        ContentValues reviewValues = new ContentValues();
        reviewValues.put(ReviewEntry._ID, "7619f70d9251415d310234d");
        reviewValues.put(ReviewEntry.COLUMN_FLICK_ID, MARTIAN_FLICK_ID);
        reviewValues.put(ReviewEntry.COLUMN_AUTHOR, "/Justin Brock");
        reviewValues.put(ReviewEntry.COLUMN_CONTENT, getMartianContent2());

        return reviewValues;
    }

    static ContentValues createMartianTrailerValues() {
        ContentValues trailerValues = new ContentValues();
        trailerValues.put(TrailerEntry._ID, "544859d80e0a26748c00061a");
        trailerValues.put(TrailerEntry.COLUMN_FLICK_ID, MARTIAN_FLICK_ID);
        trailerValues.put(TrailerEntry.COLUMN_KEY, "tmeOjFno6Do");
        trailerValues.put(TrailerEntry.COLUMN_NAME, "Trailer 1");
        trailerValues.put(TrailerEntry.COLUMN_TYPE, "Trailer");

        return trailerValues;
    }

    static ArrayList<Trailer> createJpTrailerList() {
        ArrayList<Trailer> trailers = new ArrayList<>();
        trailers.add(createTrailer(createJpTrailerValues1()));
        trailers.add(createTrailer(createJpTrailerValues2()));
        trailers.add(createTrailer(createJpTrailerValues3()));
        return trailers;
    }

    static ArrayList<Trailer> createMatianTrailerContentValueList() {
        ArrayList<Trailer> trailers = new ArrayList<>();
        trailers.add(createTrailer(createMartianTrailerValues()));
        return trailers;
    }

    static ArrayList<Review> createJpReviewList() {
        ArrayList<Review> reviews = new ArrayList<>();
        reviews.add(createReview(createJpReviewValues1()));
        reviews.add(createReview(createJpReviewValues2()));
        return reviews;
    }

    @NonNull
    private static Trailer createTrailer(ContentValues cv) {
        Trailer trailer = new Trailer();
        trailer.id = cv.getAsString(TrailerEntry._ID);
        trailer.key = cv.getAsString(TrailerEntry.COLUMN_KEY);
        trailer.name = cv.getAsString(TrailerEntry.COLUMN_NAME);
        trailer.type = cv.getAsString(TrailerEntry.COLUMN_TYPE);
        return trailer;
    }

    @NonNull
    private static Review createReview(ContentValues cv) {
        Review review = new Review();
        review.id = cv.getAsString(ReviewEntry._ID);
        review.author = cv.getAsString(ReviewEntry.COLUMN_AUTHOR);
        review.content = cv.getAsString(ReviewEntry.COLUMN_CONTENT);
        return review;
    }

    static FlickBundleWrapper createJpFlickBundleWrapper() {
        final ContentValues cv = createJpFlickValues();
        final FlickBundleWrapper wrapper = new FlickBundleWrapper(new Bundle());
        wrapper.flickId(cv.getAsString(FlickEntry._ID))
            .title(cv.getAsString(FlickEntry.COLUMN_TITLE))
            .posterBytes(cv.getAsByteArray(FlickEntry.COLUMN_POSTER_BLOB))
            .posterUrl(cv.getAsString(FlickEntry.COLUMN_POSTER_PATH))
            .releaseDate(cv.getAsString(FlickEntry.COLUMN_RELEASE_YEAR))
            .flickLength(cv.getAsString(FlickEntry.COLUMN_LENGTH))
            .userRating(cv.getAsFloat(FlickEntry.COLUMN_RATING))
            .synopsis(cv.getAsString(FlickEntry.COLUMN_PLOT_SYNOPSIS))
            .reviews(createJpReviewList())
            .trailers(createJpTrailerList());
        return wrapper;
    }


    @NonNull
    private static String getJpContent1() {
        return "I was a huge fan of the original 3 movies, they were out when I was younger, and I grew up loving dinosaurs because of them. This movie was awesome, and I think it can stand as a testimonial piece towards the capabilities that Christopher Pratt has. He nailed it. The graphics were awesome, the supporting cast did great and the t rex saved the child in me. 10\5 stars, four thumbs up, and I hope that star wars episode VII doesn't disappoint,";
    }

    @NonNull
    private static String getJpContent2() {
        return "I'm a bit baffle at some of the reviews that praise this film. Yes, it's better than Jurassic Park 3 and its really dumb fun but some of the praise seem bit robotic, like they are going through talking points given by a press agent.";
    }

    @NonNull
    private static String getMartianContent1() {
        return "The Martian’ is definitely in the creative wheelhouse of filmmaker Ridley Scott whose Science Fiction sensibilities are grounded in colorful futuristic fantasies that tiptoe in grand whimsy.  The veteran auteur responsible for such pop cultural high-minded spectacles in ‘Alien’, ‘Blade Runner’ and even the mixed bag reception of ‘Prometheus’ certainly brings a sophisticated and thought-provoking vibe to the probing aura of ‘The Martian’";
    }

    @NonNull
    private static String getMartianContent2() {
        return "The Martian is a new Ridley Scott classic, featuring his best work in years, the best performance I've ever seen from Matt Damon, an outstanding supporting cast, a surprisingly funny screenplay from Daredevil creator Drew Goddard, and a great narrative that ties the film together beautifully.";
    }

    @NonNull
    private static String getJpSynopsis() {
        return "Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.";
    }

    @NonNull
    private static String getMartianSynopsis() {
        return "During a manned mission to Mars, Astronaut Mark Watney is presumed dead after a fierce storm and left behind by his crew. But Watney has survived and finds himself stranded and alone on the hostile planet. With only meager supplies, he must draw upon his ingenuity, wit and spirit to subsist and find a way to signal to Earth that he is alive.";
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String msg, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        Timber.d("--DB Values");
        for (int i = 0; i < valueCursor.getColumnCount(); i++) {
            final String columnName = valueCursor.getColumnName(i);
            final Object value;
            if (columnName.equals(FlickEntry.COLUMN_POSTER_BLOB)) {
                value = valueCursor.getBlob(i);
            } else {
                value = valueCursor.getString(i);
            }

            Timber.d("[" + columnName + ", " + value + "]");

        }
        Timber.d("--Content Values");

        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + msg, idx == -1);

            if (columnName.equals(FlickEntry.COLUMN_POSTER_BLOB)) {
                byte[] expectedValue = (byte[]) entry.getValue();
                Timber.d("[" + columnName + ", " + Arrays.toString(expectedValue) + "]");
                byte[] dbColValue = valueCursor.getBlob(idx);
                assertTrue(msg + ", values for column [" + columnName + "] did not match", Arrays.equals(expectedValue, dbColValue));
            } else {
                String expectedValue = entry.getValue().toString();
                Timber.d("[" + columnName + ", " + expectedValue + "]");
                String dbColValue = valueCursor.getString(idx);
                if (!expectedValue.equals(dbColValue)) {
                    System.out.println();
                }
                assertEquals(msg + ", values for column [" + columnName + "] did not match", expectedValue, dbColValue);
            }
        }
    }

    public static void deleteAllRecordsFromProvider(Context context) {
        context.getContentResolver().delete(
            FlickEntry.CONTENT_URI,
            null,
            null
        );

        context.getContentResolver().delete(
            ReviewEntry.CONTENT_URI,
            null,
            null
        );

        context.getContentResolver().delete(
            TrailerEntry.CONTENT_URI,
            null,
            null
        );

        Cursor cursor = Repository.retrieveAllFlix(context);

        assertEquals("Error: Records not deleted from Flick table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = Repository.retrieveAllReviews(context);

        assertEquals("Error: Records not deleted from Review table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = Repository.retrieveAllTrailers(context);

        assertEquals("Error: Records not deleted from Trailer table during delete", 0, cursor.getCount());
        cursor.close();
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }

    /*
        Students: The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.

        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }
}
