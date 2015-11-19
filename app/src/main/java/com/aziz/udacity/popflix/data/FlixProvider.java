package com.aziz.udacity.popflix.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import com.aziz.udacity.popflix.data.FlixContract.*;
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
public class FlixProvider extends ContentProvider {

    public static final SQLiteQueryBuilder sFlickReviewJoinQbuilder;
    public static final SQLiteQueryBuilder sFlickTrailerJoinQbuilder;
    static final int FLICK = 100;
    static final int FLICK_WITH_REVIEW_AND_TRAILER = 102;
    static final int REVIEW = 200;
    static final int TRAILER = 300;
    private static final String LOG_TAG = FlixProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    //movie._id = ?
    private static final String sFlickSelection = FlickEntry.TABLE_NAME + "." + FlickEntry._ID + " = ? ";

    static {
        sFlickReviewJoinQbuilder = new SQLiteQueryBuilder();
        sFlickReviewJoinQbuilder.setTables(
            ReviewEntry.TABLE_NAME
                + " INNER JOIN " + FlickEntry.TABLE_NAME
                + " ON "
                + ReviewEntry.TABLE_NAME + "." + ReviewEntry.COLUMN_FLICK_ID + " = "
                + FlickEntry.TABLE_NAME + "." + FlickEntry._ID
        );

        sFlickTrailerJoinQbuilder = new SQLiteQueryBuilder();
        sFlickTrailerJoinQbuilder.setTables(
            TrailerEntry.TABLE_NAME
                + " INNER JOIN " + FlickEntry.TABLE_NAME
                + " ON "
                + TrailerEntry.TABLE_NAME + "." + TrailerEntry.COLUMN_FLICK_ID + " = "
                + FlickEntry.TABLE_NAME + "." + FlickEntry._ID
        );
    }

    private FlixDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        matcher.addURI(authority, PATH_FLICK, FLICK);
        matcher.addURI(authority, PATH_FLICK + "/*", FLICK_WITH_REVIEW_AND_TRAILER);
        matcher.addURI(authority, PATH_TRAILER, TRAILER);
        matcher.addURI(authority, PATH_REVIEW, REVIEW);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new FlixDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case FLICK:
                cursor = mOpenHelper.getReadableDatabase().query(
                    FlickEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                );
                break;

            case REVIEW:

                cursor =
                    sFlickReviewJoinQbuilder.query(
                        mOpenHelper.getReadableDatabase(),
                        ReviewEntry.PROJECTION,
                        sFlickSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case TRAILER:
                cursor =
                    sFlickTrailerJoinQbuilder.query(
                        mOpenHelper.getReadableDatabase(),
                        projection,
                        sFlickSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
//                    mOpenHelper.getReadableDatabase().query(
//                    TrailerEntry.TABLE_NAME,
//                    projection,
//                    selection,
//                    selectionArgs,
//                    null,
//                    null,
//                    sortOrder
//                   );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case FLICK:
                return FlickEntry.CONTENT_TYPE;
            case FLICK_WITH_REVIEW_AND_TRAILER:
                return FlickEntry.CONTENT_ITEM_TYPE;
            case REVIEW:
                return ReviewEntry.CONTENT_TYPE;
            case TRAILER:
                return TrailerEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case FLICK: {
                final long id = db.insert(FlickEntry.TABLE_NAME, null, values);
                Timber.d("Insert id: " + id);
                if (id > 0) {
                    returnUri = FlickEntry.buildFlickUri(id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case REVIEW: {
                final long id = db.insert(ReviewEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ReviewEntry.buildReviewUri(id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case TRAILER: {
                final long id = db.insert(TrailerEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = TrailerEntry.buildTrailerUri(id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int insertCount = 0;
        switch (sUriMatcher.match(uri)) {
            case FLICK:
                ;
                break;
            case REVIEW:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        final long id = db.insert(ReviewEntry.TABLE_NAME, null, value);
                        if (id > 0) {
                            insertCount++;
                        } else {
                            throw new SQLException("Failed to insert Review row into " + uri);
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case TRAILER:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        final long id = db.insert(TrailerEntry.TABLE_NAME, null, value);
                        if (id > 0) {
                            insertCount++;
                        } else {
                            throw new SQLException("Failed to insert Trailer row into " + uri);
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                break;

        }

        return insertCount;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int deletedRowsTotal;
        switch (sUriMatcher.match(uri)) {
            case FLICK:
                deletedRowsTotal = db.delete(FlickEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEW:
                deletedRowsTotal = db.delete(ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TRAILER:
                deletedRowsTotal = db.delete(TrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return deletedRowsTotal;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
