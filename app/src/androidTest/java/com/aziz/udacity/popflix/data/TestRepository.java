package com.aziz.udacity.popflix.data;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import com.aziz.udacity.popflix.ui.FlickBundleWrapper;
import timber.log.Timber;

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
public class TestRepository extends AndroidTestCase {
    private static final String LOG_TAG = TestRepository.class.getSimpleName();


    public void setUp() {
        Timber.d("...Deleting all the records...");
        TestHelper.deleteAllRecordsFromProvider(mContext);
    }

    public void testPersistFlickWithDetails() {
        final FlickBundleWrapper wrapper = TestHelper.createJpFlickBundleWrapper();

        Repository.persistFlickWithDetails(mContext, wrapper);

        validatePersistedJpFlickAndDetails(wrapper);
    }

    private void validatePersistedJpFlickAndDetails(FlickBundleWrapper wrapper) {
        Cursor cursor = Repository.retrieveTrailersByFlickId(mContext, wrapper.getFlickId());

        assertTrue("Error: No Records returned from trailer-flick inner join query", cursor.moveToFirst());
        TestHelper.validateCurrentRecord("Error: Review-Flick Join Query Validation Failed", cursor,
            TestHelper.createJpTrailerValues1());

        assertTrue("Error: No Records returned from trailer-flick inner query", cursor.moveToNext());
        TestHelper.validateCurrentRecord("Error: Review-Flick Join Query Validation Failed",
            cursor, TestHelper.createJpTrailerValues2());

        assertTrue("Error: No Records returned from trailer-flick inner query", cursor.moveToNext());
        TestHelper.validateCurrentRecord("Error: Review-Flick Join Query Validation Failed",
            cursor, TestHelper.createJpTrailerValues3());
        cursor.close();

        cursor = Repository.retrieveReviewsByFlickId(mContext, TestHelper.JP_FLICK_ID_STR);
        assertTrue("Error: No Records returned from review-flick inner join query", cursor.moveToFirst());
        TestHelper.validateCurrentRecord("Error: Review-Flick Join Query Validation Failed", cursor,
            TestHelper.createJpReviewValues1());

        assertTrue("Error: No Records returned from review-flick inner query", cursor.moveToNext());
        TestHelper.validateCurrentRecord("Error: Review-Flick Join Query Validation Failed",
            cursor, TestHelper.createJpReviewValues2());
        cursor.close();

        cursor = Repository.retrieveFlickById(mContext, wrapper.getFlickId());
        TestHelper.validateCursor("Error validating FlickEntry cursor", cursor, TestHelper.createJpFlickValues());
        cursor.close();
    }

    public void testRetrieveFlickById() {
        final FlickBundleWrapper wrapper = TestHelper.createJpFlickBundleWrapper();
        Uri uri = Repository.persistFlick(mContext, wrapper.getFlickContentValues());
        assertTrue(ContentUris.parseId(uri) != -1);

        Cursor cursor = Repository.retrieveFlickById(mContext, wrapper.getFlickId());
        TestHelper.validateCursor("Error validating FlickEntry cursor", cursor, wrapper.getFlickContentValues());
        cursor.close();

    }

    public void testDeleteFlickWithDetails() {
        final FlickBundleWrapper wrapper = TestHelper.createJpFlickBundleWrapper();

        Repository.persistFlickWithDetails(mContext, wrapper);
        validatePersistedJpFlickAndDetails(wrapper);

        Repository.deleteFlickWithDetails(mContext, wrapper);
        Cursor cursor = Repository.retrieveAllFlix(mContext);
        assertTrue("Failed to delete flick", emptyCursor(cursor));

        cursor = Repository.retrieveAllReviews(mContext);
        assertTrue("Failed to delete reviews", emptyCursor(cursor));

        cursor = Repository.retrieveAllTrailers(mContext);
        assertTrue("Failed to delete trailers", emptyCursor(cursor));
    }

    public void testIsPersistedFlick() {
        Repository.persistFlick(mContext, TestHelper.createJpFlickValues());
        Repository.persistFlick(mContext, TestHelper.createMartianFlickValues());

        assertTrue("Failed to find JP movie", Repository.isPersistedFlick(mContext, TestHelper.JP_FLICK_ID_STR));
    }


    private boolean emptyCursor(Cursor cursor) {
        return cursor.getCount() == 0;
    }
}
