package com.aziz.udacity.popflix.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

/**
 * Contract for flick persistence
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public final class FlixContract {


    public static final String CONTENT_AUTHORITY = "com.aziz.udacity.popflix";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FLICK = "flick";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_TRAILER = "trailer";

    public static final class FlickEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FLICK).build();

        public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FLICK;

        public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FLICK;

        public static final String TABLE_NAME = "flick";

        public static String ID_SELECTION = "_id=?";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_PLOT_SYNOPSIS = "plot_synopsis";
        public static final String COLUMN_RELEASE_YEAR = "release_year";
        public static final String COLUMN_LENGTH = "length";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_POSTER_BLOB = "poster_blob";

        public static Uri buildFlickUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public interface FlixBaseColumns extends BaseColumns {
        String COLUMN_FLICK_ID = "flick_id";
        String ID_SELECTION = "_id=?";

    }
    
    public static final class ReviewEntry implements FlixBaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static final String TABLE_NAME = "review";
        
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";

        public static final String[] PROJECTION = new String[]{
            TABLE_NAME + "." + _ID,
            TABLE_NAME + "." + COLUMN_FLICK_ID,
            TABLE_NAME + "." + COLUMN_AUTHOR,
            TABLE_NAME + "." + COLUMN_CONTENT
        };

        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class TrailerEntry implements FlixBaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        public static final String TABLE_NAME = "trailer";


        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";

        public static final String[] PROJECTION = new String[]{
            TABLE_NAME + "." + _ID,
            TABLE_NAME + "." + COLUMN_FLICK_ID,
            TABLE_NAME + "." + COLUMN_KEY,
            TABLE_NAME + "." + COLUMN_NAME,
            TABLE_NAME + "." + COLUMN_TYPE
        };

        public static Uri buildTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }

}
