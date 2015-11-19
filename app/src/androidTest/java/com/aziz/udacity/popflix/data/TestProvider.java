package com.aziz.udacity.popflix.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import com.aziz.udacity.popflix.ui.FlickBundleWrapper;
import timber.log.Timber;

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
public class TestProvider extends AndroidTestCase {



    public void setUp() {
        Timber.d("...Deleting all the records...");
        TestHelper.deleteAllRecordsFromProvider(mContext);
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();
        ComponentName componentName = new ComponentName(mContext.getPackageName(), FlixProvider.class.getName());
        try {
            final ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            assertEquals("Error: FlixProvider registered with authority: " + providerInfo.authority +
                    " instead of authority: " + FlixContract.CONTENT_AUTHORITY,
                providerInfo.authority, FlixContract.CONTENT_AUTHORITY);

        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: FlixProvider not registered at " + mContext.getPackageName(), false);
        }
    }

    public void testGetType() {
        // content://com.aziz.udacity.popflix/flick
        String type = mContext.getContentResolver().getType(FlickEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.aziz.udacity.popflix/flick
        assertEquals("Error: the FlickEntry CONTENT_URI should return FlickEntry.CONTENT_TYPE",
            FlickEntry.CONTENT_TYPE, type);

        // content://com.aziz.udacity.popflix/flick/135397
        type = mContext.getContentResolver().getType(FlickEntry.buildFlickUri(TestHelper.JP_FLICK_ID));
        // vnd.android.cursor.item/com.aziz.udacity.popflix/flick
        assertEquals("Error: the FlickEntry CONTENT_URI should return FlickEntry.CONTENT_ITEM_TYPE",
            FlickEntry.CONTENT_ITEM_TYPE, type);

        // content://com.aziz.udacity.popflix/review
        type = mContext.getContentResolver().getType(ReviewEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.aziz.udacity.popflix/review
        assertEquals("Error: the ReviewEntry CONTENT_URI should return ReviewEntry.CONTENT_TYPE",
            ReviewEntry.CONTENT_TYPE, type);

        // content://com.aziz.udacity.popflix/trailer
        type = mContext.getContentResolver().getType(TrailerEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.aziz.udacity.popflix/trailer
        assertEquals("Error: the TrailerEntry CONTENT_URI should return TrailerEntry.CONTENT_TYPE",
            TrailerEntry.CONTENT_TYPE, type);
    }

    public void testInsertFlick() {
        final ContentValues flickValues = TestHelper.createJpFlickValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        final TestHelper.TestContentObserver observer = TestHelper.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(FlickEntry.CONTENT_URI, true, observer);
        Uri flickUri = mContext.getContentResolver().insert(FlickEntry.CONTENT_URI, flickValues);

        // Did our content observer get called? If this fails, your insert flick
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        observer.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(observer);

        final long flickId = ContentUris.parseId(flickUri);

        assertTrue(flickId != -1);

        final Cursor cursor = Repository.retrieveAllFlix(mContext);

        TestHelper.validateCursor("Error validating FlickEntry cursor", cursor, flickValues);
        Timber.d("Successfully inserted a movie using the contentProvider");
        cursor.close();
    }

    public void testInsertReview() {
        final ContentValues martianFlickValues = TestHelper.createMartianFlickValues();
        Uri martianFlickUri = mContext.getContentResolver().insert(FlickEntry.CONTENT_URI, martianFlickValues);

        final ContentValues martianReviewValues = TestHelper.createMartianReviewValues1();
        final Uri martianReviewUri = mContext.getContentResolver().insert(ReviewEntry.CONTENT_URI, martianReviewValues);

        final ContentValues martianReviewValues2 = TestHelper.createMartianReviewValues2();
        final Uri martianReviewUri2 = mContext.getContentResolver().insert(ReviewEntry.CONTENT_URI, martianReviewValues2);


        final ContentValues jpFlickValues = TestHelper.createJpFlickValues();
        final Uri jpFlickUri = mContext.getContentResolver().insert(FlickEntry.CONTENT_URI, jpFlickValues);

        final ContentValues jpReviewValues = TestHelper.createJpReviewValues1();
        final Uri jpReviewUri = mContext.getContentResolver().insert(ReviewEntry.CONTENT_URI, jpReviewValues);

        final Cursor cursor = Repository.retrieveReviewsByFlickId(mContext, TestHelper.MARTIAN_FLICK_ID_STR);

        TestHelper.validateCursor("Error validating ReviewEntry cursor", cursor, martianReviewValues);
        Timber.d("Successfully inserted a review using the contentProvider");
        cursor.close();
    }

    public void testInsertTrailer() {
        final ContentValues martianFlickValues = TestHelper.createMartianFlickValues();
        Uri martianFlickUri = mContext.getContentResolver().insert(FlickEntry.CONTENT_URI, martianFlickValues);

        final ContentValues martianTrailerValues = TestHelper.createMartianTrailerValues();
        final Uri martianTrailerUri = mContext.getContentResolver().insert(TrailerEntry.CONTENT_URI, martianTrailerValues);


        final ContentValues jpFlickValues = TestHelper.createJpFlickValues();
        final Uri jpFlickUri = mContext.getContentResolver().insert(FlickEntry.CONTENT_URI, jpFlickValues);

        final ContentValues jpTrailerValues1 = TestHelper.createJpTrailerValues1();
        final Uri jpTrailerUri1 = mContext.getContentResolver().insert(TrailerEntry.CONTENT_URI, jpTrailerValues1);


        final ContentValues jpTrailerValues2 = TestHelper.createJpTrailerValues2();
        final Uri jpTrailerUri2 = mContext.getContentResolver().insert(TrailerEntry.CONTENT_URI, jpTrailerValues2);


        final ContentValues jpTrailerValues3 = TestHelper.createJpTrailerValues3();
        final Uri jpTrailerUri3 = mContext.getContentResolver().insert(TrailerEntry.CONTENT_URI, jpTrailerValues3);

        final Cursor cursor = Repository.retrieveTrailersByFlickId(mContext, TestHelper.JP_FLICK_ID_STR);

        assertTrue("Error: No Records returned from trailer-flick inner join query", cursor.moveToFirst());
        TestHelper.validateCurrentRecord("Error: Review-Flick Join Query Validation Failed", cursor, jpTrailerValues1);

        assertTrue("Error: No Records returned from trailer-flick inner query", cursor.moveToNext());
        TestHelper.validateCurrentRecord("Error: Review-Flick Join Query Validation Failed", cursor, jpTrailerValues2);

        assertTrue("Error: No Records returned from trailer-flick inner query", cursor.moveToNext());
        TestHelper.validateCurrentRecord("Error: Review-Flick Join Query Validation Failed", cursor, jpTrailerValues3);
        cursor.close();
    }

    public void testBulkInsertTrailers() {

        final FlickBundleWrapper wrapper = TestHelper.createJpFlickBundleWrapper();

        final Uri jpFlickUri = mContext.getContentResolver().insert(FlickEntry.CONTENT_URI, wrapper.getFlickContentValues());

        final int insertCount = mContext.getContentResolver().bulkInsert(TrailerEntry.CONTENT_URI,
            wrapper.getTrailerContentValues());

        assertEquals(3, insertCount);

        final Cursor cursor = Repository.retrieveTrailersByFlickId(mContext, TestHelper.JP_FLICK_ID_STR);

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
    }

    public void testBulkInsertReviews() {

        final ContentValues jpFlickValues = TestHelper.createJpFlickValues();
        final Uri jpFlickUri = mContext.getContentResolver().insert(FlickEntry.CONTENT_URI, jpFlickValues);

        final FlickBundleWrapper wrapper = TestHelper.createJpFlickBundleWrapper();
        final int insertCount = Repository.persistReviews(mContext, wrapper.getReviewContentValues());
        assertEquals(2, insertCount);

        final Cursor cursor = Repository.retrieveReviewsByFlickId(mContext, TestHelper.JP_FLICK_ID_STR);
        assertTrue("Error: No Records returned from review-flick inner join query", cursor.moveToFirst());
        TestHelper.validateCurrentRecord("Error: Review-Flick Join Query Validation Failed", cursor,
            TestHelper.createJpReviewValues1());

        assertTrue("Error: No Records returned from review-flick inner query", cursor.moveToNext());
        TestHelper.validateCurrentRecord("Error: Review-Flick Join Query Validation Failed",
            cursor, TestHelper.createJpReviewValues2());

        cursor.close();
    }


    public void testBatchDeleteTrailers() {

        final ContentValues jpFlickValues = TestHelper.createJpFlickValues();
        final Uri jpFlickUri = Repository.persistFlick(mContext, jpFlickValues);
        System.out.println("jpFlickUri: " + jpFlickUri);


        final ContentValues martianFlickValues = TestHelper.createMartianFlickValues();
        final Uri martianFlickUri = mContext.getContentResolver().insert(FlickEntry.CONTENT_URI, martianFlickValues);

        final ContentValues martianTrailerValues = TestHelper.createMartianTrailerValues();
        final Uri martianTrailerUri = mContext.getContentResolver().insert(TrailerEntry.CONTENT_URI, martianTrailerValues);


        final FlickBundleWrapper wrapper = TestHelper.createJpFlickBundleWrapper();
        final int insertCount = Repository.persistTrailers(mContext, wrapper.getTrailerContentValues());
        assertEquals(3, insertCount);


        Cursor cursor = Repository.retrieveTrailersByFlickId(mContext, TestHelper.JP_FLICK_ID_STR);
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

        final String[] trailerIds = wrapper.getTrailerIds();
        final int numOfDeletedIds = Repository.deleteTrailers(mContext, trailerIds);
        assertTrue("Failed to delete the right number of id's", numOfDeletedIds == trailerIds.length);

        cursor = Repository.retrieveTrailersByFlickId(mContext, TestHelper.JP_FLICK_ID_STR);
        ;
        assertTrue(cursor.getCount() == 0);
        cursor.close();

        cursor = Repository.retrieveTrailersByFlickId(mContext, TestHelper.MARTIAN_FLICK_ID_STR);

        assertTrue(cursor.getCount() == 1);
        assertTrue("Error: No Records returned from trailer-flick inner join query", cursor.moveToFirst());
        TestHelper.validateCurrentRecord("Error: Martian-Trailer-Flick Join Query Validation Failed", cursor,
            TestHelper.createMartianTrailerValues());
        cursor.close();

    }

}
