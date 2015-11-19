package com.aziz.udacity.popflix.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

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


public class TestFlixDb extends AndroidTestCase {

    public void deteleDB() {
        mContext.deleteDatabase(FlixDbHelper.DB_NAME);
    }

    public void setUp() {
        deteleDB();
    }

    public void testCreateDB() throws Throwable{
        SQLiteDatabase db = getWritableDB();

        try {
            final HashSet<String> tableNameHashSet = new HashSet<String>();
            tableNameHashSet.add(FlickEntry.TABLE_NAME);
            tableNameHashSet.add(ReviewEntry.TABLE_NAME);
            tableNameHashSet.add(TrailerEntry.TABLE_NAME);

            assertEquals(true, db.isOpen());

            Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            assertTrue("Error: This means that the database has not been created correctly", c.moveToFirst());

            // verify that the tables have been created
            do {
                tableNameHashSet.remove(c.getString(0));
            } while (c.moveToNext());

            assertTrue("Error: Your database was created without the Flick, Review and Trailer entry tables",
                tableNameHashSet.isEmpty());
            
            checkFlickColumns(db);
            checkReviewColumns(db);
            checkTrailerColumns(db);

            c.close();
        } finally {
            db.close();
        }

    }

    private SQLiteDatabase getWritableDB() {
        return new FlixDbHelper(this.mContext).getWritableDatabase();
    }

    public void testFlickTable() {
        SQLiteDatabase db = getWritableDB();
        try {
            final ContentValues flickValues = TestHelper.createJpFlickValues();
            final long rowId = db.insert(FlickEntry.TABLE_NAME, null, flickValues);
            assertTrue(rowId != -1);

            Cursor cursor = db.query(
                FlickEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
            );
            // Move the cursor to a valid database row and check to see if we got any records back
            // from the query
            assertTrue("Error: No Records returned from flick query", cursor.moveToFirst());

            // Fifth Step: Validate data in resulting Cursor with the original ContentValues
            // (you can use the validateCurrentRecord function in TestUtilities to validate the
            // query if you like)
            TestHelper.validateCurrentRecord("Error: Flick Query Validation Failed", cursor, flickValues);

            // Move the cursor to demonstrate that there is only one record in the database
            assertFalse("Error: More than one record returned from flick query", cursor.moveToNext());
            cursor.close();
        } finally {
            db.close();
        }
    }

    public void testReviewTable() {
        SQLiteDatabase db = getWritableDB();
        try {
            final ContentValues reviewValues = TestHelper.createJpReviewValues1();
            final long rowId = db.insert(ReviewEntry.TABLE_NAME, null, reviewValues);
            assertTrue(rowId != -1);

            Cursor cursor = db.query(
                ReviewEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
            );
            // Move the cursor to a valid database row and check to see if we got any records back
            // from the query
            assertTrue("Error: No Records returned from review query", cursor.moveToFirst());

            // Fifth Step: Validate data in resulting Cursor with the original ContentValues
            // (you can use the validateCurrentRecord function in TestUtilities to validate the
            // query if you like)
            TestHelper.validateCurrentRecord("Error: Review Query Validation Failed", cursor, reviewValues);

            // Move the cursor to demonstrate that there is only one record in the database
            assertFalse("Error: More than one record returned from review query", cursor.moveToNext());
            cursor.close();
        } finally {
            db.close();
        }
    }
    
    public void testTrailerTable() {
        SQLiteDatabase db = getWritableDB();
        try {
            final ContentValues trailerValues = TestHelper.createJpTrailerValues1();
            final long rowId = db.insert(TrailerEntry.TABLE_NAME, null, trailerValues);
            assertTrue(rowId != -1);

            Cursor cursor = db.query(
                TrailerEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
            );
            // Move the cursor to a valid database row and check to see if we got any records back
            // from the query
            assertTrue("Error: No Records returned from trailer query", cursor.moveToFirst());

            // Fifth Step: Validate data in resulting Cursor with the original ContentValues
            // (you can use the validateCurrentRecord function in TestUtilities to validate the
            // query if you like)
            TestHelper.validateCurrentRecord("Error: Trailer Query Validation Failed", cursor, trailerValues);

            // Move the cursor to demonstrate that there is only one record in the database
            assertFalse("Error: More than one record returned from trailer query", cursor.moveToNext());
            cursor.close();
        } finally {
            db.close();
        }
    }

    public void testReviewJoin() {
        SQLiteDatabase db = getWritableDB();
        try {
            final ContentValues martianFlickValues = TestHelper.createMartianFlickValues();
            long rowId = db.insert(FlickEntry.TABLE_NAME, null, martianFlickValues);
            assertTrue(rowId != -1);

            final ContentValues martianReviewValues1 = TestHelper.createMartianReviewValues1();
            rowId = db.insert(ReviewEntry.TABLE_NAME, null, martianReviewValues1);
            assertTrue(rowId != -1);

            final ContentValues martianReviewValues2 = TestHelper.createMartianReviewValues2();
            rowId = db.insert(ReviewEntry.TABLE_NAME, null, martianReviewValues2);
            assertTrue(rowId != -1);


            final ContentValues jpFlickValues = TestHelper.createJpFlickValues();
            rowId = db.insert(FlickEntry.TABLE_NAME, null, jpFlickValues);
            assertTrue(rowId != -1);

            final ContentValues jpReviewValues = TestHelper.createJpReviewValues1();
            rowId = db.insert(ReviewEntry.TABLE_NAME, null, jpReviewValues);
            assertTrue(rowId != -1);

            String query = "SELECT "
                + ReviewEntry.TABLE_NAME + "." + ReviewEntry._ID + ", "
                + ReviewEntry.TABLE_NAME + "." + ReviewEntry.COLUMN_FLICK_ID + ", "
                + ReviewEntry.TABLE_NAME + "." + ReviewEntry.COLUMN_AUTHOR + ", "
                + ReviewEntry.TABLE_NAME + "." + ReviewEntry.COLUMN_CONTENT
                + " FROM " + ReviewEntry.TABLE_NAME
                + " INNER JOIN " + FlickEntry.TABLE_NAME
                + " ON "
                + ReviewEntry.TABLE_NAME + "." +  ReviewEntry.COLUMN_FLICK_ID + " = "
                + FlickEntry.TABLE_NAME + "." +  FlickEntry._ID
                + " WHERE "
                + FlickEntry.TABLE_NAME + "." + FlickEntry._ID + "=?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(TestHelper.MARTIAN_FLICK_ID)});

            // Move the cursor to a valid database row and check to see if we got any records back
            // from the query
            assertTrue("Error: No Records returned from flick query", cursor.moveToFirst());

            TestHelper.validateCurrentRecord("Validating martianReviewValues1 ", cursor, martianReviewValues1);

            cursor.moveToNext();
            TestHelper.validateCurrentRecord("Validating martianReviewValues2 ", cursor, martianReviewValues2);

            // Move the cursor to demonstrate that there is only one record in the database
            assertFalse("Error: More than one record returned from flick query", cursor.moveToNext());
            cursor.close();
        } finally {
            db.close();
        }

    }

    public void testTrailerJoin() {
        SQLiteDatabase db = getWritableDB();
        try {
            final ContentValues martianFlickValues = TestHelper.createMartianFlickValues();
            long rowId = db.insert(FlickEntry.TABLE_NAME, null, martianFlickValues);
            assertTrue(rowId != -1);

            final ContentValues martianReviewValues = TestHelper.createMartianReviewValues1();
            rowId = db.insert(ReviewEntry.TABLE_NAME, null, martianReviewValues);
            assertTrue(rowId != -1);

            final ContentValues martianReviewValues2 = TestHelper.createMartianReviewValues2();
            rowId = db.insert(ReviewEntry.TABLE_NAME, null, martianReviewValues2);
            assertTrue(rowId != -1);

            final ContentValues martianTrailerValues = TestHelper.createMartianTrailerValues();
            rowId = db.insert(TrailerEntry.TABLE_NAME, null, martianTrailerValues);
            assertTrue(rowId != -1);

            final ContentValues jpFlickValues = TestHelper.createJpFlickValues();
            rowId = db.insert(FlickEntry.TABLE_NAME, null, jpFlickValues);
            assertTrue(rowId != -1);

            final ContentValues jpReviewValues = TestHelper.createJpReviewValues1();
            rowId = db.insert(ReviewEntry.TABLE_NAME, null, jpReviewValues);
            assertTrue(rowId != -1);

            final ContentValues jpTrailerValues1 = TestHelper.createJpTrailerValues1();
            rowId = db.insert(TrailerEntry.TABLE_NAME, null, jpTrailerValues1);
            assertTrue(rowId != -1);

            final ContentValues jpTrailerValues2 = TestHelper.createJpTrailerValues2();
            rowId = db.insert(TrailerEntry.TABLE_NAME, null, jpTrailerValues2);
            assertTrue(rowId != -1);

            final ContentValues jpTrailerValues3 = TestHelper.createJpTrailerValues3();
            rowId = db.insert(TrailerEntry.TABLE_NAME, null, jpTrailerValues3);
            assertTrue(rowId != -1);

            String query = "SELECT "
                + TrailerEntry.TABLE_NAME + "." + TrailerEntry._ID + ", "
                + TrailerEntry.TABLE_NAME + "." + TrailerEntry.COLUMN_FLICK_ID + ", "
                + TrailerEntry.TABLE_NAME + "." + TrailerEntry.COLUMN_KEY + ", "
                + TrailerEntry.TABLE_NAME + "." + TrailerEntry.COLUMN_NAME + ", "
                + TrailerEntry.TABLE_NAME + "." + TrailerEntry.COLUMN_TYPE
                + " FROM " + TrailerEntry.TABLE_NAME
                + " INNER JOIN " + FlickEntry.TABLE_NAME
                + " ON "
                + TrailerEntry.TABLE_NAME + "." +  TrailerEntry.COLUMN_FLICK_ID + " = "
                + FlickEntry.TABLE_NAME + "." +  FlickEntry._ID
                + " WHERE "
                + FlickEntry.TABLE_NAME + "." + FlickEntry._ID + "=?";
            Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(TestHelper.JP_FLICK_ID)});


            assertTrue("Error: No Records returned from trailer-flick inner join query", cursor.moveToFirst());
            TestHelper.validateCurrentRecord("Error: Trailer-Flick Join Query Validation Failed", cursor, jpTrailerValues1);

            assertTrue("Error: No Records returned from trailer-flick inner query", cursor.moveToNext());
            TestHelper.validateCurrentRecord("Error: Trailer-Flick Join Query Validation Failed", cursor, jpTrailerValues2);

            assertTrue("Error: No Records returned from trailer-flick inner query", cursor.moveToNext());
            TestHelper.validateCurrentRecord("Error: Trailer-Flick Join Query Validation Failed", cursor, jpTrailerValues3);

            // Move the cursor to demonstrate that there is only one record in the database
            assertFalse("Error: More than one record returned from flick query", cursor.moveToNext());
            cursor.close();
        } finally {
            db.close();
        }
    }


    //////////////////////////////////////////////////////////////////////////////////
    // Helper methods
    //////////////////////////////////////////////////////////////////////////////////

    private void checkFlickColumns(SQLiteDatabase db) {
        Cursor c;// now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + FlickEntry.TABLE_NAME + ")", null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
            c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> flickColumnHashSet = new HashSet<String>();
        flickColumnHashSet.add(FlickEntry._ID);
        flickColumnHashSet.add(FlickEntry.COLUMN_TITLE);
        flickColumnHashSet.add(FlickEntry.COLUMN_POSTER_PATH);
        flickColumnHashSet.add(FlickEntry.COLUMN_RELEASE_YEAR);
        flickColumnHashSet.add(FlickEntry.COLUMN_LENGTH);
        flickColumnHashSet.add(FlickEntry.COLUMN_RATING);
        flickColumnHashSet.add(FlickEntry.COLUMN_PLOT_SYNOPSIS);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            flickColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required flick
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required flick entry columns",
            flickColumnHashSet.isEmpty());
    }
    
    private void checkReviewColumns(SQLiteDatabase db) {
        Cursor c;// now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + ReviewEntry.TABLE_NAME + ")", null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
            c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> flickColumnHashSet = new HashSet<String>();
        flickColumnHashSet.add(ReviewEntry._ID);
        flickColumnHashSet.add(ReviewEntry.COLUMN_FLICK_ID);
        flickColumnHashSet.add(ReviewEntry.COLUMN_AUTHOR);
        flickColumnHashSet.add(ReviewEntry.COLUMN_CONTENT);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            flickColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required review
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required review entry columns",
            flickColumnHashSet.isEmpty());
    }
   
    private void checkTrailerColumns(SQLiteDatabase db) {
        Cursor c;// now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + TrailerEntry.TABLE_NAME + ")", null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
            c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> flickColumnHashSet = new HashSet<String>();
        flickColumnHashSet.add(TrailerEntry._ID);
        flickColumnHashSet.add(TrailerEntry.COLUMN_FLICK_ID);
        flickColumnHashSet.add(TrailerEntry.COLUMN_KEY);
        flickColumnHashSet.add(TrailerEntry.COLUMN_NAME);
        flickColumnHashSet.add(TrailerEntry.COLUMN_TYPE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            flickColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required trailer
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required trailer entry columns",
            flickColumnHashSet.isEmpty());
    }





}
